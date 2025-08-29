package org.br.com.test.utils.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.support.Context;
import org.br.com.test.utils.DataUtils;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.utils.FormatUtils;
import org.br.com.test.utils.evidence.GeradorDocxApi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class HooksEvidenciasApi {

    @Getter
    private static Scenario scenario;

    private static String requestMethod, requestUri, headers, requestBody, statusCode, responseBody;
    private static String tag, nomeCenario, idEvidencia, dataHoraInicio, dataHoraTermino, featureName;
    private static String uuidFeature;

    private static String lastFeatureName = null;
    private static String lastFeatureUUID = null;

    private static void limparDados() {
        requestMethod = requestUri = headers = requestBody = statusCode = responseBody = "";
        tag = nomeCenario = idEvidencia = dataHoraInicio = dataHoraTermino = featureName = "";
    }

    public static void capturarDadosRequisicao(String method, String uri, String headersData, String body) {
        requestMethod = method; requestUri = uri; headers = headersData; requestBody = body;
    }

    public static void capturarDadosResposta(String code, String response) {
        statusCode = code; responseBody = response;
    }

    @Before
    public void antesDoTeste(Scenario scenario) {
        limparDados();
        HooksEvidenciasApi.scenario = scenario;

        featureName = getFeatureTitleFromScenario(scenario);
        nomeCenario = scenario.getName();

        // Sempre imprime o cabeçalho, mesmo se a feature não mudou
        uuidFeature = Context.getOrCreateFeatureUUID(featureName);
        lastFeatureName = featureName;
        lastFeatureUUID = uuidFeature;

        LogFormatter.logExecutionHeader(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        LogFormatter.logFeature(featureName);
        LogFormatter.logScenarioId(uuidFeature);

        Context.startContext(featureName, scenario.getName());
        idEvidencia = uuidFeature;
        Context.setScenarioId(idEvidencia);
        LogFormatter.logScenarioStart(scenario.getName());

        dataHoraInicio = DataUtils.getHoraAtualComMilis();

        tag = scenario.getSourceTagNames().stream()
                .filter(t -> t.startsWith("@CT-"))
                .map(t -> t.substring(1))
                .findFirst()
                .orElse("CT_NAO_DEFINIDO");
    }

    @After
    public void depoisDoTeste(Scenario scenario) {
        dataHoraTermino = DataUtils.getHoraAtualComMilis();

        String failureLog = null;
        boolean failed = scenario.isFailed();
        if (failed) {
            Context.incrementFailed();
            failureLog = "O teste falhou durante a fase de validacao (step 'Then').\n" +
                    "O Status Code retornado pela API foi '" + statusCode + "', mas a validacao esperava um valor diferente.\n" +
                    "Consulte o log de execucao no console para ver o stack trace completo da assercao.";
        } else {
            Context.incrementPassed();
        }
        Context.clearContext();

        String featureFileName = getFeatureFileNameFromScenario(scenario);
        GeradorDocxApi.setApiData(
                dataHoraInicio,
                dataHoraTermino,
                requestMethod,
                requestUri,
                org.br.com.test.utils.FormatUtils.formatHeaders(headers),
                FormatUtils.prettyJson(requestBody),
                statusCode,
                org.br.com.test.utils.FormatUtils.maskSensitiveDataInJson(FormatUtils.prettyJson(responseBody)),
                tag,
                nomeCenario,
                featureName,
                failureLog
        );

        GeradorDocxApi.gerarEvidenciaApi(nomeCenario, idEvidencia, !failed, uuidFeature, featureFileName);

        // NOVO: Loga o status final do cenário
        LogFormatter.logScenarioEnd(failed ? "FAILED" : "PASSED");

        String cleanScenarioName = nomeCenario.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "_").toLowerCase();
        String fileName = tag + "_" + cleanScenarioName + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS")) + ".docx";
        java.nio.file.Path dirDocx = java.nio.file.Paths.get("target", "evidence", featureFileName, uuidFeature, fileName);

        if (java.nio.file.Files.exists(dirDocx)) {
            try {
                byte[] docx = java.nio.file.Files.readAllBytes(dirDocx);
                scenario.attach(docx, "application/vnd.openxmlformats-officedocument-wordprocessingml-document", "Evidencia - " + nomeCenario);
            } catch (IOException e) {
                HooksEvidenciasApi.log.error("Erro ao anexar evidencia DOCX ao relatorio: ", e);
            }
        }
    }

    private String getFeatureTitleFromScenario(Scenario scenario) {
        String uri = scenario.getUri().toString();
        java.nio.file.Path featurePath = java.nio.file.Paths.get(java.net.URI.create(uri));
        try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(featurePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Feature:")) {
                    return line.replace("Feature:", "").trim();
                }
            }
        } catch (Exception e) {
            log.warn("Nao foi possivel ler o titulo da Feature do arquivo. Usando nome do arquivo como fallback.", e);
            return getFeatureFileNameFromScenario(scenario);
        }
        return getFeatureFileNameFromScenario(scenario);
    }

    private String getFeatureFileNameFromScenario(Scenario scenario) {
        String uri = scenario.getUri().toString();
        String fileName = uri.substring(uri.lastIndexOf('/') + 1);
        if (fileName.endsWith(".feature")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }
}