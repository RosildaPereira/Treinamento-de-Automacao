package org.br.com.test.utils.evidence;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
public class GeradorDocxApi {

    private static final String MODELO_API_PATH = "src/main/resources/Evidencia Modelo API.docx";
    private static final String EVIDENCE_DIR = "target/evidence/api";

    private static String horaInicio, horaTermino, requestMethod, requestUri, headers, requestBody, statusCode, responseBody, tag, scenarioName, featureName, failureLog;

    public static void setApiData(String inicio, String termino, String method, String uri, String headersData, String body, String code, String response, String tagName, String scenario, String feature, String log) {
        horaInicio = inicio; horaTermino = termino; requestMethod = method; requestUri = uri; headers = headersData;
        requestBody = body; statusCode = code; responseBody = response; tag = tagName; scenarioName = scenario; featureName = feature;
        failureLog = log;
    }

    public static void gerarEvidenciaApi(String aScenarioName, String scenarioId, boolean passed, String uuidFeature, String featureFileName) {
        String cleanScenarioName = aScenarioName
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
        String dataHora = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"));
        String fileName = tag + "_" + cleanScenarioName + " - " + dataHora + ".docx";
        String dirDocx = java.nio.file.Paths.get("target/evidence", featureFileName, uuidFeature, fileName).toString();
        new java.io.File(dirDocx).getParentFile().mkdirs();
        try {
            criarDocxApi(dirDocx, uuidFeature, passed);
//            GeradorDocxApi.log.info("Evidencia DOCX gerada com sucesso: " + dirDocx);
        } catch (IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            GeradorDocxApi.log.error("Erro ao gerar evidencia DOCX: ", e);
        }
    }

    private static void criarDocxApi(String docxFilePath, String idExecucao, boolean passed) throws IOException, InvalidFormatException {
        try (FileInputStream fis = new FileInputStream(MODELO_API_PATH);
             XWPFDocument templateDoc = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(docxFilePath)) {

            replacePlaceholders(templateDoc, idExecucao, passed);

            if (failureLog != null && !failureLog.isEmpty()) {
                appendFailureLog(templateDoc, failureLog);
            }

            templateDoc.write(fos);
        }
    }

    private static void replacePlaceholders(XWPFDocument doc, String idExecucao, boolean passed) {
        String statusValue = passed ? "PASSED" : "FAILED";
        String statusColor = passed ? "00B050" : "FF0000";
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        findAndReplace(doc, "{{projeto}}", "Projeto de test de API");
        findAndReplace(doc, "{{data}}", "Data: " + dataAtual);
        findAndReplace(doc, "{{feature}}", featureName);
        String scenarioField = tag + ": " + scenarioName;
        findAndReplace(doc, "{{scenario}}", scenarioField);
        findAndReplace(doc, "{{inicio}}", horaInicio);
        findAndReplace(doc, "{{fim}}", horaTermino);
        replaceStatusPlaceholder(doc, statusValue, statusColor);
        findAndReplace(doc, "{{requestMethod}}", requestMethod, getColorForMethod(requestMethod));
        findAndReplace(doc, "{{requestUri}}", requestUri);
        findAndReplace(doc, "{{headers}}", headers);
        findAndReplace(doc, "{{requestBody}}", requestBody);

        if (!passed) {
            findAndReplace(doc, "{{statusCode}}", "Falha na validacao. Status real: " + statusCode, "FF0000");
        } else {
            findAndReplace(doc, "{{statusCode}}", statusCode, getColorForStatusCode(statusCode));
        }

        findAndReplace(doc, "{{responseBody}}", responseBody);

        // >>>>>>>> ALTERAÇÃO FINAL DO ALINHAMENTO AQUI <<<<<<<<<<
        // O quarto parâmetro (alinhamento) foi alterado de CENTER para RIGHT.
        findAndReplace(doc, "{{idExecucao}}", "ID Execucao: " + idExecucao, null, ParagraphAlignment.RIGHT, 6);
    }

    private static void findAndReplace(XWPFDocument doc, String placeholder, String replacement) { findAndReplace(doc, placeholder, replacement, null, null, null); }
    private static void findAndReplace(XWPFDocument doc, String placeholder, String replacement, String hexColor) { findAndReplace(doc, placeholder, replacement, hexColor, null, null); }

    private static void replaceInParagraphs(List<XWPFParagraph> paragraphs, String placeholder, String replacement, String hexColor, ParagraphAlignment alignment, Integer fontSize) {
        if (replacement == null) {
            replacement = "";
        }
        for (XWPFParagraph p : paragraphs) {
            if (p.getText() != null && p.getText().contains(placeholder)) {
                String fontFamily = "Arial";
                int size = 10;
                if (!p.getRuns().isEmpty()) {
                    XWPFRun originalRun = p.getRuns().get(0);
                    if (originalRun.getFontFamily() != null) fontFamily = originalRun.getFontFamily();
                    if (originalRun.getFontSize() > 0) size = originalRun.getFontSize();
                }
                for (int i = p.getRuns().size() - 1; i >= 0; i--) {
                    p.removeRun(i);
                }

                if (placeholder.equals("{{requestBody}}") || placeholder.equals("{{responseBody}}") || placeholder.equals("{{headers}}")) {
                    String[] lines = replacement.split("\\r?\\n");
                    for (int i = 0; i < lines.length; i++) {
                        XWPFRun run = p.createRun();
                        run.setText(lines[i].replace("\t", "    "));
                        run.setFontFamily(fontFamily);
                        run.setFontSize(size);
                        if (i < lines.length - 1) {
                            run.addBreak();
                        }
                    }
                } else if (placeholder.equals("{{data}}") && replacement.startsWith("Data: ")) {
                    String prefix = "Data: ";
                    String value = replacement.substring(prefix.length());
                    XWPFRun runPrefix = p.createRun();
                    runPrefix.setText(prefix);
                    runPrefix.setFontFamily(fontFamily);
                    runPrefix.setFontSize(size);
                    runPrefix.setBold(true);

                    XWPFRun runValue = p.createRun();
                    runValue.setText(value);
                    runValue.setFontFamily(fontFamily);
                    runValue.setFontSize(size);
                } else if (placeholder.equals("{{requestMethod}}") || placeholder.equals("{{statusCode}}")) {
                    XWPFRun run = p.createRun();
                    run.setText(replacement);
                    run.setFontFamily(fontFamily);
                    run.setFontSize(size);
                    run.setBold(true);
                    if (hexColor != null) {
                        run.setColor(hexColor);
                    }
                } else {
                    XWPFRun run = p.createRun();
                    run.setText(replacement);
                    run.setFontFamily(fontFamily);
                    run.setFontSize(size);
                    if (hexColor != null) {
                        run.setColor(hexColor);
                    }
                }

                if (fontSize != null) {
                    for(XWPFRun r : p.getRuns()) {
                        r.setFontSize(fontSize);
                    }
                }
                if (alignment != null) {
                    p.setAlignment(alignment);
                }
            }
        }
    }


    private static void findAndReplace(XWPFDocument doc, String placeholder, String replacement, String hexColor, ParagraphAlignment alignment, Integer fontSize) {
        if (replacement == null) replacement = "";
        replaceInParagraphs(doc.getParagraphs(), placeholder, replacement, hexColor, alignment, fontSize);

        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    replaceInParagraphs(cell.getParagraphs(), placeholder, replacement, hexColor, alignment, fontSize);
                }
            }
        }
    }

    private static void replaceStatusPlaceholder(XWPFDocument doc, String statusValue, String statusColor) {
        String placeholder = "{{status}}";
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    if (cell.getText() != null && cell.getText().contains(placeholder)) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            String fontFamily = "Arial";
                            int size = 10;
                            if (!p.getRuns().isEmpty()) {
                                XWPFRun originalRun = p.getRuns().get(0);
                                if (originalRun.getFontFamily() != null) fontFamily = originalRun.getFontFamily();
                                if (originalRun.getFontSize() > 0) size = originalRun.getFontSize();
                            }
                            for (int i = p.getRuns().size() - 1; i >= 0; i--) {
                                p.removeRun(i);
                            }
                            XWPFRun runLabel = p.createRun();
                            runLabel.setText("Status: ");
                            runLabel.setFontFamily(fontFamily);
                            runLabel.setFontSize(size);
                            runLabel.setBold(true);

                            XWPFRun runValue = p.createRun();
                            runValue.setText(statusValue);
                            runValue.setColor(statusColor);
                            runValue.setBold(true);
                            runValue.setFontFamily(fontFamily);
                            runValue.setFontSize(size);
                        }
                        return;
                    }
                }
            }
        }
    }

    private static void appendFailureLog(XWPFDocument doc, String logText) {
        doc.createParagraph().createRun().addBreak();
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Log de Falha:");
        titleRun.setBold(true);
        titleRun.setUnderline(UnderlinePatterns.SINGLE);

        XWPFParagraph logParagraph = doc.createParagraph();
        logParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun logRun = logParagraph.createRun();
        logRun.setFontFamily("Courier New");
        logRun.setFontSize(8);

        String[] lines = logText.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            logRun.setText(lines[i]);
            if (i < lines.length - 1) logRun.addBreak();
        }
    }

    private static String getColorForMethod(String method) {
        if (method == null) return null;
        switch (method.toUpperCase()) {
            case "GET": return "28A745"; case "POST": return "FD7E14";
            case "PUT": return "0D6EFD"; case "DELETE": return "DC3545";
            case "PATCH": return "7F3CFF"; default: return null;
        }
    }

    private static String getColorForStatusCode(String statusCodeStr) {
        if (statusCodeStr == null || statusCodeStr.isEmpty()) return null;
        try {
            int code = Integer.parseInt(statusCodeStr);
            if (code >= 200 && code < 300) return "28A745";
            if (code >= 300 && code < 400) return "0D6EFD";
            if (code >= 400 && code < 500) return "FD7E14";
            if (code >= 500 && code < 600) return "DC3545";
            return null;
        } catch (NumberFormatException e) { return null; }
    }
}