# 🎯 **TUTORIAL COMPLETO - PARTE 2: CLASSES CORE E DATA**
## **📋 RECEITA PARA RECRIAR TODAS AS CLASSES FUNDAMENTAIS**

---

## 📋 **ÍNDICE DA PARTE 2**

### **🔐 1. PACOTE CORE** - Classes fundamentais do framework
- **encryption/** - Criptografia AES
- **exceptions/** - Exceções personalizadas
- **filter/** - Filtros de captura HTTP
- **processor/** - Processamento de tags
- **support/** - Classes de suporte
- **token/** - Gerenciamento de tokens

### **📊 2. PACOTE DATA** - Manipulação de dados
- **datasheet/** - Modelos de dados
- **reader/** - Leitores de arquivos
- **writer/** - Escritores de CSV

---

# 🔐 **1. PACOTE CORE/ENCRYPTION**

## **📄 Encryption.java**
**📍 Local**: `src/main/java/org/br/com/core/encryption/Encryption.java`

```java
package org.br.com.core.encryption;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * ============================================================================
 * CLASSE UTILITÁRIA: Encryption
 * ============================================================================
 * 
 * PROPÓSITO:
 * Fornece métodos estáticos para criptografia e descriptografia de dados
 * usando o algoritmo AES (Advanced Encryption Standard). Utilizada para
 * proteger informações sensíveis como senhas e tokens nos testes.
 * 
 * QUANDO USAR:
 * - Criptografar senhas antes de armazenar em planilhas
 * - Proteger tokens de acesso em arquivos de configuração
 * - Mascarar dados sensíveis em logs
 * - Segurança em dados de teste compartilhados
 * 
 * COMO INSTANCIAR:
 * Não é instanciada - todos os métodos são estáticos:
 * String dadosCriptografados = Encryption.criptografar("senha123", "minhaChave");
 * String dadosOriginais = Encryption.descriptografar(dadosCriptografados, "minhaChave");
 */
public class Encryption {

    /**
     * MÉTODO: criptografar(String texto, String chaveSecreta)
     * 
     * PROPÓSITO:
     * Criptografa um texto usando algoritmo AES e retorna o resultado
     * codificado em Base64. Transforma dados legíveis em formato seguro.
     * 
     * PARÂMETROS:
     * @param texto - Texto a ser criptografado (ex: "senha123")
     * @param chaveSecreta - Chave para criptografia (deve ter tamanho adequado)
     * 
     * RETORNO:
     * @return String - Texto criptografado em Base64
     * 
     * EXCEÇÕES:
     * @throws Exception - Em caso de erro na criptografia
     */
    public static String criptografar(String texto, String chaveSecreta) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }

    /**
     * MÉTODO: descriptografar(String textoCriptografado, String chaveSecreta)
     * 
     * PROPÓSITO:
     * Descriptografa um texto que foi criptografado anteriormente,
     * retornando o conteúdo original. Processo reverso da criptografia.
     * 
     * PARÂMETROS:
     * @param textoCriptografado - Texto em Base64 que foi criptografado
     * @param chaveSecreta - Mesma chave usada na criptografia
     * 
     * RETORNO:
     * @return String - Texto original descriptografado
     * 
     * EXCEÇÕES:
     * @throws Exception - Em caso de erro na descriptografia
     */
    public static String descriptografar(String textoCriptografado, String chaveSecreta) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] textoDecodificado = Base64.getDecoder().decode(textoCriptografado);
        byte[] textoDescriptografado = cipher.doFinal(textoDecodificado);
        return new String(textoDescriptografado);
    }
}
```

---

# ⚠️ **2. PACOTE CORE/EXCEPTIONS**

## **📄 DataException.java**
**📍 Local**: `src/main/java/org/br/com/core/exceptions/DataException.java`

```java
package org.br.com.core.exceptions;

/**
 * ============================================================================
 * EXCEÇÃO PERSONALIZADA: DataException
 * ============================================================================
 * 
 * PROPÓSITO:
 * Exceção RuntimeException personalizada para tratar especificamente
 * erros relacionados ao acesso e manipulação de dados no framework.
 * Facilita o diagnóstico de problemas com planilhas, queries e conexões.
 * 
 * QUANDO USAR:
 * - Erros ao conectar com arquivos Excel
 * - Falhas na execução de queries SQL nas planilhas
 * - Problemas de formato de dados
 * - Campos não encontrados ou inválidos
 */
public class DataException extends RuntimeException {

    /**
     * ATRIBUTO: serialVersionUID
     * Identificador único para serialização da classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * CONSTRUTOR: DataException(String message)
     * 
     * PROPÓSITO:
     * Cria uma exceção com apenas uma mensagem descritiva.
     * Usado quando o erro é conhecido e não há exceção subjacente.
     * 
     * PARÂMETROS:
     * @param message - Mensagem descritiva do erro
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * CONSTRUTOR: DataException(Throwable cause)
     * 
     * PROPÓSITO:
     * Cria uma exceção encapsulando outra exceção como causa.
     * Usado quando se quer transformar uma exceção técnica
     * em uma exceção de domínio mais específica.
     * 
     * PARÂMETROS:
     * @param cause - Exceção original que causou o problema
     */
    public DataException(Throwable cause) {
        super(cause);
    }

    /**
     * CONSTRUTOR: DataException(String message, Throwable e)
     * 
     * PROPÓSITO:
     * Cria uma exceção com mensagem personalizada E a exceção original.
     * Combina contexto específico com preservação do erro técnico.
     * Mais informativo e útil para debug.
     * 
     * PARÂMETROS:
     * @param message - Mensagem contextual sobre o erro
     * @param e - Exceção original que causou o problema
     */
    public DataException(String message, Throwable e) {
        super(message, e);
    }
}
```

---

# 🎬 **3. PACOTE CORE/FILTER**

## **📄 EvidenceFilter.java**
**📍 Local**: `src/main/java/org/br/com/core/filter/EvidenceFilter.java`

```java
package org.br.com.core.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.br.com.test.utils.hooks.HooksEvidenciasApi;

/**
 * ============================================================================
 * FILTRO DE EVIDÊNCIAS: EvidenceFilter
 * ============================================================================
 * 
 * PROPÓSITO:
 * Filtro REST Assured que intercepta automaticamente todas as requisições
 * HTTP e captura dados para geração de evidências de teste.
 * 
 * QUANDO USAR:
 * - Capturar dados de requisições automaticamente
 * - Gerar evidências de teste sem intervenção manual
 * - Documentar comunicação HTTP para auditoria
 */
public class EvidenceFilter implements Filter {

    /**
     * MÉTODO: filter - Interceptação de Requisições
     * 
     * PROPÓSITO:
     * Intercepta cada requisição HTTP, executa a chamada e captura
     * dados para evidências através do HooksEvidenciasApi.
     * 
     * PARÂMETROS:
     * @param requestSpec - Especificação da requisição
     * @param responseSpec - Especificação da resposta
     * @param ctx - Contexto de execução do filtro
     * 
     * RETORNO:
     * @return Response - Resposta da requisição HTTP
     */
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                          FilterableResponseSpecification responseSpec,
                          FilterContext ctx) {
        
        // Executa a requisição
        Response response = ctx.next(requestSpec, responseSpec);
        
        // Extrai dados da requisição
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        String headers = requestSpec.getHeaders().toString();
        String body = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "";
        
        // Captura dados da requisição
        HooksEvidenciasApi.capturarDadosRequisicao(method, uri, headers, body);
        
        // Extrai dados da resposta
        String statusCode = String.valueOf(response.getStatusCode());
        String responseBody = response.getBody().asString();
        
        // Captura dados da resposta
        HooksEvidenciasApi.capturarDadosResposta(statusCode, responseBody);
        
        return response;
    }
}
```

## **📄 PDFLoggerFilter.java**
**📍 Local**: `src/main/java/org/br/com/core/filter/PDFLoggerFilter.java`

```java
package org.br.com.core.filter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.MultiPartSpecification;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.support.logger.LogFormatter;

/**
 * ============================================================================
 * FILTRO AVANÇADO: PDFLoggerFilter
 * ============================================================================
 * 
 * PROPÓSITO:
 * Filtro REST Assured especializado em capturar TODAS as informações de
 * requisições HTTP e gerar evidências completas em formato PDF.
 * 
 * QUANDO USAR:
 * - Testes que requerem evidências formais
 * - Auditoria técnica de requisições
 * - Documentação detalhada de APIs
 * - Análise de problemas de integração
 */
@Log4j2
public class PDFLoggerFilter implements Filter {

    // ========================================================================
    // ATRIBUTOS DA CLASSE
    // ========================================================================
    
    private final String scenarioName;
    private final String scenarioId;
    private final List<Table> tables;

    // ========================================================================
    // CONSTRUTOR
    // ========================================================================
    
    /**
     * CONSTRUTOR: PDFLoggerFilter(String scenarioName, String scenarioId)
     * 
     * PROPÓSITO:
     * Inicializa o filtro com informações do cenário de teste.
     * Prepara estruturas internas para capturar dados das requisições.
     * 
     * PARÂMETROS:
     * @param scenarioName - Nome descritivo do cenário
     * @param scenarioId - Identificador único (tag) do cenário
     */
    public PDFLoggerFilter(String scenarioName, String scenarioId) {
        this.scenarioName = scenarioName;
        this.scenarioId = scenarioId;
        this.tables = new ArrayList<Table>();
    }

    // ========================================================================
    // MÉTODO DO INTERFACE FILTER
    // ========================================================================
    
    /**
     * MÉTODO: filter() - Interceptação de Requisições
     * 
     * PROPÓSITO:
     * Método principal do interface Filter. Intercepta cada requisição
     * HTTP, executa a chamada e captura todos os dados para o PDF.
     */
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec,
                          FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        logRequest(requestSpec, response);
        return response;
    }

    // ========================================================================
    // MÉTODO DE PROCESSAMENTO DE DADOS
    // ========================================================================
    
    /**
     * MÉTODO: logRequest() - Processamento de Dados da Requisição
     * 
     * PROPÓSITO:
     * Extrai e organiza TODAS as informações técnicas da requisição
     * e resposta, formatando-as em uma tabela PDF estruturada.
     */
    private void logRequest(FilterableRequestSpecification requestSpec, Response response) {
        
        // EXTRAÇÃO DE DADOS DA REQUISIÇÃO
        String method = requestSpec.getMethod();
        String url = requestSpec.getURI();
        String proxy = "";
        if (requestSpec.getProxySpecification() != null) {
            proxy = requestSpec.getProxySpecification().getHost() + ":" + 
                   requestSpec.getProxySpecification().getPort();
        }

        // PROCESSAMENTO DE PARÂMETROS
        StringBuilder requestParamsSb = new StringBuilder();
        for (Entry<String, String> entry : requestSpec.getRequestParams().entrySet()) {
            requestParamsSb.append(entry.getKey()).append(": ")
                          .append(entry.getValue()).append("\n");
        }

        StringBuilder queryParamsSb = new StringBuilder();
        for (Entry<String, String> entry : requestSpec.getQueryParams().entrySet()) {
            queryParamsSb.append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
        }

        StringBuilder formParamsSb = new StringBuilder();
        for (Entry<String, String> entry : requestSpec.getFormParams().entrySet()) {
            formParamsSb.append(entry.getKey()).append(": ")
                       .append(entry.getValue()).append("\n");
        }

        StringBuilder pathParamsSb = new StringBuilder();
        for (Entry<String, String> entry : requestSpec.getPathParams().entrySet()) {
            pathParamsSb.append(entry.getKey()).append(": ")
                       .append(entry.getValue()).append("\n");
        }

        // PROCESSAMENTO DE HEADERS
        StringBuilder headers = new StringBuilder();
        for (Header header : requestSpec.getHeaders().asList()) {
            headers.append(header.getName()).append("=")
                  .append(header.getValue()).append("\n");
        }

        // PROCESSAMENTO DE COOKIES
        StringBuilder cookies = new StringBuilder();
        for (Cookie cookie : requestSpec.getCookies().asList()) {
            cookies.append(cookie.getName()).append("=")
                  .append(cookie.getValue()).append("\n");
        }

        // PROCESSAMENTO DE MULTIPART
        StringBuilder multipartParams = new StringBuilder();
        for (MultiPartSpecification m : requestSpec.getMultiPartParams()) {
            multipartParams.append(m.getFileName()).append("\n");
            multipartParams.append(m.getMimeType()).append("\n");
            multipartParams.append(m.getContent()).append("\n");
        }

        String requestBody = requestSpec.getBody() != null ? 
                           requestSpec.getBody().toString() : "";

        // EXTRAÇÃO DE DADOS DA RESPOSTA
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        // CRIAÇÃO DA TABELA PDF
        Table table = new Table(UnitValue.createPercentArray(new float[] { 20, 80 }));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setFixedLayout();

        // ADIÇÃO DE DADOS À TABELA (apenas campos não vazios)
        table.addCell("Request method");
        table.addCell(method);
        table.addCell("Request URI");
        table.addCell(url);

        if (!proxy.isEmpty()) {
            table.addCell("Proxy");
            table.addCell(proxy);
        }
        if (!requestParamsSb.toString().isEmpty()) {
            table.addCell("Request params");
            table.addCell(requestParamsSb.toString());
        }
        if (!queryParamsSb.toString().isEmpty()) {
            table.addCell("Query params");
            table.addCell(queryParamsSb.toString());
        }
        if (!formParamsSb.toString().isEmpty()) {
            table.addCell("Form params");
            table.addCell(formParamsSb.toString());
        }
        if (!pathParamsSb.toString().isEmpty()) {
            table.addCell("Path params");
            table.addCell(pathParamsSb.toString());
        }

        table.addCell("Headers");
        table.addCell(headers.toString());

        if (!cookies.toString().isEmpty()) {
            table.addCell("Cookies");
            table.addCell(cookies.toString());
        }
        if (!multipartParams.toString().isEmpty()) {
            table.addCell("Multiparts");
            table.addCell(multipartParams.toString());
        }

        table.addCell("Request Body");
        table.addCell(requestBody);
        table.addCell("Status Code");
        table.addCell(Integer.toString(statusCode));
        table.addCell("Response Body");
        table.addCell(responseBody);

        tables.add(table);
    }

    // ========================================================================
    // MÉTODO DE FINALIZAÇÃO
    // ========================================================================
    
    /**
     * MÉTODO: closeDocument() - Geração Final do PDF
     * 
     * PROPÓSITO:
     * Finaliza a captura de dados e gera o arquivo PDF completo
     * com todas as requisições do cenário. Inclui formatação
     * profissional e informações de status.
     * 
     * PARÂMETROS:
     * @param passed - Indica se o cenário passou (true) ou falhou (false)
     * 
     * RETORNO:
     * @return String - Caminho completo do arquivo PDF gerado
     */
    public String closeDocument(Boolean passed) {
        log.info("gerando a evidencia");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss_SSS");
        String date = sdf.format(d);
        String scenarioNameRemovedChar = this.scenarioName.replaceAll("[\\\\/:*?\"<>|]", "");
        String pdfPath = "evidence/" + this.scenarioId + "_" + scenarioNameRemovedChar + "_" + date + ".pdf";
        File evidenceDir = new File("evidence");
        if (!evidenceDir.exists()) {
            evidenceDir.mkdirs();
        }

        try (PdfWriter writer = new PdfWriter(pdfPath)) {
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // TÍTULO DO DOCUMENTO
            Paragraph titulo = new Paragraph(scenarioId + ": " + scenarioName).setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            // STATUS COM COR
            String passedStr = passed ? "PASSOU" : "FALHOU";
            Color statusColor = passed ? new DeviceRgb(0, 128, 0) : new DeviceRgb(255, 0, 0);
            Paragraph status = new Paragraph("Status: ").add(new Text(passedStr).setFontColor(statusColor))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(status);

            // ADIÇÃO DAS REQUISIÇÕES
            int count = 1;
            for (Table table : tables) {
                Paragraph tableCount = new Paragraph("Requisição: " + count).setBold()
                        .setTextAlignment(TextAlignment.CENTER);
                document.add(tableCount);
                document.add(table);

                if (count < tables.size()) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
                count++;
            }

            document.close();
        } catch (Exception e) {
            LogFormatter.logError("falha ao gerar a evidencia: " + e.getMessage());
        }

        return pdfPath;
    }
}
```

---

# ⚙️ **4. PACOTE CORE/PROCESSOR**

## **📄 TagProcessor.java**
**📍 Local**: `src/main/java/org/br/com/core/processor/TagProcessor.java`

```java
package org.br.com.core.processor;

import org.br.com.test.model.builder.TagBuilder;
import org.br.com.test.utils.TagConcatenada;

/**
 * ============================================================================
 * PROCESSADOR: TagProcessor
 * ============================================================================
 * 
 * PROPÓSITO:
 * Processa e concatena tags de teste baseadas em dados de planilhas Excel.
 * Facilita a execução seletiva de testes através de tags dinâmicas
 * geradas a partir de critérios definidos por analistas.
 * 
 * QUANDO USAR:
 * - Executar testes baseados em critérios de planilhas
 * - Gerar tags dinamicamente para execução seletiva
 * - Integrar dados de analistas com execução de testes
 * - Automatizar seleção de cenários baseada em Excel
 */
public class TagProcessor {

    /**
     * MÉTODO: start(TagBuilder tag)
     * 
     * PROPÓSITO:
     * Inicia o processamento de tags baseado nos critérios fornecidos.
     * Delega para TagConcatenada que faz o trabalho de leitura da planilha
     * e geração das tags de execução.
     * 
     * PARÂMETROS:
     * @param tag - Objeto TagBuilder com critérios de processamento
     *              - abaAnalista: nome da aba na planilha Excel
     *              - execution: critério de execução (ex: "S" para sim)
     */
    public void start(TagBuilder tag) {
        TagConcatenada tagConcatenada = new TagConcatenada();
        tagConcatenada.tagsExcel(tag.getAbaAnalista(), tag.getExecution());
    }
}
```

---

# 🛠️ **5. PACOTE CORE/SUPPORT**

## **📄 Context.java**
**📍 Local**: `src/main/java/org/br/com/core/support/Context.java`

```java
package org.br.com.core.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================================
 * GERENCIADOR DE CONTEXTO: Context
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados compartilhados entre diferentes partes do framework de teste
 * de forma thread-safe. Centraliza informações do cenário atual e estatísticas
 * globais de execução.
 * 
 * CARACTERÍSTICAS:
 * - Thread-safe para execução paralela
 * - Armazenamento de dados por cenário
 * - Contadores globais de testes
 * - UUIDs únicos por feature
 */
public class Context {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE
    // ========================================================================
    
    private static final ThreadLocal<Map<String, Object>> scenarioContext = 
        ThreadLocal.withInitial(HashMap::new);
    
    private static final Map<String, String> featureUUIDs = new ConcurrentHashMap<>();
    
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);

    // ========================================================================
    // MÉTODOS DE GERENCIAMENTO DE CONTEXTO
    // ========================================================================
    
    /**
     * MÉTODO: startContext(String featureName, String scenarioName)
     * 
     * PROPÓSITO:
     * Inicializa o contexto para um novo cenário de teste.
     * Limpa dados anteriores e configura informações básicas.
     * 
     * PARÂMETROS:
     * @param featureName - Nome da feature sendo executada
     * @param scenarioName - Nome do cenário sendo executado
     */
    public static void startContext(String featureName, String scenarioName) {
        clearContext();
        put("featureName", featureName);
        put("scenarioName", scenarioName);
        put("featureUUID", getOrCreateFeatureUUID(featureName));
    }

    /**
     * MÉTODO: clearContext()
     * 
     * PROPÓSITO:
     * Limpa todos os dados do contexto atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
     */
    public static void clearContext() {
        scenarioContext.get().clear();
    }

    /**
     * MÉTODO: finishedContext(long duration)
     * 
     * PROPÓSITO:
     * Finaliza o contexto atual registrando duração da execução.
     * 
     * PARÂMETROS:
     * @param duration - Duração da execução em milissegundos
     */
    public static void finishedContext(long duration) {
        put("executionDuration", duration);
    }

    // ========================================================================
    // MÉTODOS DE CONTADORES
    // ========================================================================
    
    /**
     * MÉTODO: resetCounters()
     * 
     * PROPÓSITO:
     * Reseta contadores globais de testes para nova execução.
     */
    public static void resetCounters() {
        passedTests.set(0);
        failedTests.set(0);
    }

    /**
     * MÉTODO: incrementPassed()
     * 
     * PROPÓSITO:
     * Incrementa contador de testes que passaram.
     */
    public static void incrementPassed() {
        passedTests.incrementAndGet();
    }

    /**
     * MÉTODO: incrementFailed()
     * 
     * PROPÓSITO:
     * Incrementa contador de testes que falharam.
     */
    public static void incrementFailed() {
        failedTests.incrementAndGet();
    }

    /**
     * MÉTODO: getPassedTests()
     * 
     * PROPÓSITO:
     * Retorna número total de testes que passaram.
     * 
     * RETORNO:
     * @return int - Número de testes que passaram
     */
    public static int getPassedTests() {
        return passedTests.get();
    }

    /**
     * MÉTODO: getFailedTests()
     * 
     * PROPÓSITO:
     * Retorna número total de testes que falharam.
     * 
     * RETORNO:
     * @return int - Número de testes que falharam
     */
    public static int getFailedTests() {
        return failedTests.get();
    }

    // ========================================================================
    // MÉTODOS DE UUID
    // ========================================================================
    
    /**
     * MÉTODO: getOrCreateFeatureUUID(String featureName)
     * 
     * PROPÓSITO:
     * Obtém UUID existente para feature ou cria novo se não existir.
     * Garante que cada feature tenha um identificador único consistente.
     * 
     * PARÂMETROS:
     * @param featureName - Nome da feature
     * 
     * RETORNO:
     * @return String - UUID da feature
     */
    public static String getOrCreateFeatureUUID(String featureName) {
        return featureUUIDs.computeIfAbsent(featureName, k -> UUID.randomUUID().toString());
    }

    // ========================================================================
    // MÉTODOS DE ARMAZENAMENTO GENÉRICO
    // ========================================================================
    
    /**
     * MÉTODO: put(String key, Object value)
     * 
     * PROPÓSITO:
     * Armazena um valor no contexto atual usando uma chave.
     * 
     * PARÂMETROS:
     * @param key - Chave para identificar o valor
     * @param value - Valor a ser armazenado
     */
    public static void put(String key, Object value) {
        scenarioContext.get().put(key, value);
    }

    /**
     * MÉTODO: get(String key)
     * 
     * PROPÓSITO:
     * Recupera um valor do contexto usando a chave.
     * 
     * PARÂMETROS:
     * @param key - Chave do valor desejado
     * 
     * RETORNO:
     * @return Object - Valor armazenado ou null se não encontrado
     */
    public static Object get(String key) {
        return scenarioContext.get().get(key);
    }

    // ========================================================================
    // MÉTODOS DE CONVENIÊNCIA
    // ========================================================================
    
    /**
     * MÉTODO: setData(Object data)
     * 
     * PROPÓSITO:
     * Armazena dados do cenário atual (geralmente LoginModel).
     * 
     * PARÂMETROS:
     * @param data - Dados do cenário
     */
    public static void setData(Object data) {
        put("data", data);
    }

    /**
     * MÉTODO: getData()
     * 
     * PROPÓSITO:
     * Recupera dados do cenário atual.
     * 
     * RETORNO:
     * @return Object - Dados do cenário
     */
    public static Object getData() {
        return get("data");
    }

    /**
     * MÉTODO: setIdUsuario(String idUsuario)
     * 
     * PROPÓSITO:
     * Armazena ID do usuário do cenário atual.
     * 
     * PARÂMETROS:
     * @param idUsuario - ID do usuário
     */
    public static void setIdUsuario(String idUsuario) {
        put("idUsuario", idUsuario);
    }

    /**
     * MÉTODO: getIdUsuario()
     * 
     * PROPÓSITO:
     * Recupera ID do usuário do cenário atual.
     * 
     * RETORNO:
     * @return String - ID do usuário
     */
    public static String getIdUsuario() {
        Object id = get("idUsuario");
        return id != null ? id.toString() : null;
    }

    /**
     * MÉTODO: getFeatureName()
     * 
     * PROPÓSITO:
     * Recupera nome da feature atual.
     * 
     * RETORNO:
     * @return String - Nome da feature
     */
    public static String getFeatureName() {
        Object name = get("featureName");
        return name != null ? name.toString() : null;
    }

    /**
     * MÉTODO: getScenarioName()
     * 
     * PROPÓSITO:
     * Recupera nome do cenário atual.
     * 
     * RETORNO:
     * @return String - Nome do cenário
     */
    public static String getScenarioName() {
        Object name = get("scenarioName");
        return name != null ? name.toString() : null;
    }
}
```

## **📄 ResourceUtils.java**
**📍 Local**: `src/main/java/org/br/com/core/support/resource/ResourceUtils.java`

```java
package org.br.com.core.support.resource;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * ============================================================================
 * UTILITÁRIO: ResourceUtils
 * ============================================================================
 * 
 * PROPÓSITO:
 * Localiza arquivos de recursos (planilhas, configurações) em diferentes
 * ambientes de execução. Torna o framework robusto e independente de
 * configurações específicas de IDE ou servidor de CI/CD.
 * 
 * QUANDO USAR:
 * - Carregar planilhas Excel de dados de teste
 * - Acessar arquivos de configuração
 * - Localizar recursos independente do ambiente
 * - Evitar erros de "arquivo não encontrado"
 */
public class ResourceUtils {

    /**
     * MÉTODO: getPath(String resourceName)
     * 
     * PROPÓSITO:
     * Encontra o caminho absoluto de um arquivo, procurando em vários 
     * locais padrão. Torna o framework resistente a diferentes 
     * configurações de ambiente e estruturas de projeto.
     * 
     * PARÂMETROS:
     * @param resourceName - Nome do arquivo a ser encontrado
     * 
     * RETORNO:
     * @return String - Caminho absoluto do arquivo encontrado
     * 
     * EXCEÇÕES:
     * @throws RuntimeException - Se arquivo não for encontrado
     */
    public static String getPath(String resourceName) {
        // 1. ESTRATÉGIA PRINCIPAL: ClassLoader (padrão Java)
        URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            return new File(resourceUrl.getPath()).getAbsolutePath();
        }

        // 2. ESTRATÉGIA ALTERNATIVA: Caminhos relativos comuns
        String[] possiblePaths = {
                "src/main/resources/",    // Maven/Gradle main resources
                "src/test/resources/",    // Maven/Gradle test resources  
                "data/",                  // Diretório comum para dados
                ""                        // Raiz do projeto (último recurso)
        };

        for (String path : possiblePaths) {
            File file = new File(path + resourceName);
            if (file.exists() && !file.isDirectory()) {
                System.out.println("INFO: Recurso '" + resourceName + "' encontrado em: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        }

        // 3. FALHA: Arquivo não encontrado em lugar nenhum
        throw new RuntimeException("FALHA CRÍTICA AO CARREGAR RECURSO: O arquivo '" + resourceName + "' não foi encontrado no classpath ou em diretórios comuns do projeto.");
    }
}
```

## **📄 Console.java**
**📍 Local**: `src/main/java/org/br/com/core/support/logger/Console.java`

```java
package org.br.com.core.support.logger;

/**
 * ============================================================================
 * CONSTANTES: Console
 * ============================================================================
 * 
 * PROPÓSITO:
 * Define constantes para formatação consistente de saídas no console.
 * Padroniza separadores visuais e mensagens de erro em todo o framework.
 * 
 * QUANDO USAR:
 * - Separar seções nos logs
 * - Indicar ausência de dados ou tags
 * - Formatar saídas de console consistentemente
 * - Melhorar legibilidade dos logs
 */
public class Console {

    /**
     * CONSTANTE: SEPARATE
     * 
     * PROPÓSITO: Separador principal para seções importantes
     * VALOR: Linha com sinais de igual (=)
     * USO: Delimitar seções principais de relatórios e logs
     */
    public static final String SEPARATE = "===========================================================================================";

    /**
     * CONSTANTE: SEPARATE_HYPHEN
     * 
     * PROPÓSITO: Separador secundário para subseções
     * VALOR: Linha com hífens (-)
     * USO: Delimitar subseções dentro de relatórios
     */
    public static final String SEPARATE_HYPHEN = "------------------------------------------------------------------------------------";

    /**
     * CONSTANTE: ID_TAG_NOT_FOUND
     * 
     * PROPÓSITO: Mensagem padrão quando tag ID não é encontrada
     * VALOR: "[ID tag not found]"
     * USO: Indicar ausência de identificador em cenários
     */
    public static final String ID_TAG_NOT_FOUND = "[ID tag not found]";

    /**
     * CONSTANTE: DATA_TAG_NOT_FOUND
     * 
     * PROPÓSITO: Mensagem padrão quando tag de dados não é encontrada  
     * VALOR: "[Data tag not found]"
     * USO: Indicar ausência de dados em cenários data-driven
     */
    public static final String DATA_TAG_NOT_FOUND = "[Data tag not found]";
}
```

## **📄 ContextTime.java**
**📍 Local**: `src/main/java/org/br/com/core/support/logger/ContextTime.java`

```java
package org.br.com.core.support.logger;

import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * UTILITÁRIO: ContextTime
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia medição de tempo de execução de testes individuais e suítes
 * completas. Usa ThreadLocal para isolamento entre threads paralelas.
 * Fornece relatórios de performance e duração.
 * 
 * QUANDO USAR:
 * - Medir tempo de execução de testes
 * - Gerar relatórios de performance
 * - Análise de tempo de suítes
 * - Debugging de testes lentos
 */
@Log4j2
public class ContextTime {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE DE TEMPOS
    // ========================================================================
    
    /**
     * ATRIBUTO: timeSuiteInit
     * 
     * PROPÓSITO: Armazena horário de início da suíte completa
     * TIPO: ThreadLocal<LocalDateTime>
     * THREAD-SAFETY: Isolado por thread para execução paralela
     * USO: Calcular tempo total da suíte de testes
     */
    public static final ThreadLocal<LocalDateTime> timeSuiteInit = new ThreadLocal<LocalDateTime>();

    /**
     * ATRIBUTO: timeTestInit
     * 
     * PROPÓSITO: Armazena horário de início do teste atual
     * TIPO: ThreadLocal<LocalDateTime>
     * THREAD-SAFETY: Isolado por thread para execução paralela
     * USO: Calcular tempo do teste individual
     */
    public static final ThreadLocal<LocalDateTime> timeTestInit = new ThreadLocal<LocalDateTime>();

    // ========================================================================
    // MÉTODOS DE MARCAÇÃO DE TEMPO
    // ========================================================================
    
    /**
     * MÉTODO: printTimeInitial()
     * 
     * PROPÓSITO:
     * Marca o início de um teste e exibe o horário no console.
     * Inicializa também o timer da suíte se for o primeiro teste.
     */
    public static void printTimeInitial() {
        timeTestInit.set(LocalDateTime.now());
        printHour("Initial hour of test...........", timeTestInit.get());

        if (timeSuiteInit.get() == null)
            timeSuiteInit.set(timeTestInit.get());
    }

    /**
     * MÉTODO: printTimeFinal()
     * 
     * PROPÓSITO:
     * Marca o final de um teste, calcula durações e exibe relatório
     * de tempo no console. Mostra tempo do teste e tempo total.
     */
    public static void printTimeFinal() {
        LocalDateTime timeTestFinal = LocalDateTime.now();
        printHour("Final hour of test.............", timeTestFinal);

        printTime("Time execution of test.........", returnDifferenceBetweenTimes(timeTestInit.get(), timeTestFinal));
        printTime("Time execution of tests so far.", returnDifferenceBetweenTimes(timeSuiteInit.get(), timeTestFinal));
    }

    // ========================================================================
    // MÉTODOS DE FORMATAÇÃO E CÁLCULO
    // ========================================================================
    
    /**
     * MÉTODO: printHour(String previous, LocalDateTime time)
     * 
     * PROPÓSITO:
     * Formata e exibe um horário específico com prefixo descritivo.
     * Usado internamente para exibir horários de início e fim.
     * 
     * PARÂMETROS:
     * @param previous - Texto descritivo (ex: "Initial hour of test")
     * @param time - Horário a ser formatado e exibido
     */
    public static void printHour(String previous, LocalDateTime time) {
        System.out.printf("%s: %d:%d:%d%n", previous, time.getHour(), time.getMinute(), time.getSecond());
    }

    /**
     * MÉTODO: printTime(String previous, LocalDateTime time)
     * 
     * PROPÓSITO:
     * Formata e exibe uma duração calculada com prefixo descritivo.
     * Usado para mostrar tempos decorridos de forma legível.
     * 
     * PARÂMETROS:
     * @param previous - Texto descritivo (ex: "Time execution of test")
     * @param time - Duração calculada a ser exibida
     */
    private static void printTime(String previous, LocalDateTime time) {
        System.out.printf(String.format("%s: %d Hour(s) %d minute(s) %d second(s)", previous,
                time.getHour(),
                time.getMinute(),
                time.getSecond()));
    }

    /**
     * MÉTODO: returnDifferenceBetweenTimes(LocalDateTime timeInit, LocalDateTime timeFinal)
     * 
     * PROPÓSITO:
     * Calcula a diferença entre dois horários e retorna como LocalDateTime.
     * Usado para determinar durações de testes e suítes.
     * 
     * PARÂMETROS:
     * @param timeInit - Horário de início
     * @param timeFinal - Horário de fim
     * 
     * RETORNO:
     * @return LocalDateTime - Diferença calculada (duração)
     */
    public static LocalDateTime returnDifferenceBetweenTimes(LocalDateTime timeInit, LocalDateTime timeFinal) {
        return timeFinal
                .minusHours(timeInit.getHour())
                .minusMinutes(timeInit.getMinute())
                .minusSeconds(timeInit.getSecond());
    }
}
```

## **📄 LogFormatter.java**
**📍 Local**: `src/main/java/org/br/com/core/support/logger/LogFormatter.java`

```java
package org.br.com.core.support.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.br.com.test.utils.DataUtils;

/**
 * ============================================================================
 * FORMATADOR: LogFormatter
 * ============================================================================
 * 
 * PROPÓSITO:
 * Centraliza e padroniza todos os logs do framework. Fornece métodos
 * específicos para diferentes tipos de mensagens (step, error, request, etc.)
 * com formatação consistente e integração com Log4j2.
 * 
 * QUANDO USAR:
 * - Logar passos de teste
 * - Registrar erros e exceções
 * - Documentar requisições HTTP
 * - Marcar início/fim de cenários
 */
public class LogFormatter {

    private static final Logger logger = LogManager.getLogger(LogFormatter.class);

    // ========================================================================
    // MÉTODOS DE LOG POR CATEGORIA
    // ========================================================================
    
    /**
     * MÉTODO: logStep(String message)
     * 
     * PROPÓSITO:
     * Loga um passo de teste com formatação padronizada.
     * Inclui timestamp automático e formatação específica.
     * 
     * PARÂMETROS:
     * @param message - Mensagem do passo de teste
     */
    public static void logStep(String message) {
        logger.info(formatStepMessage(message));
    }

    /**
     * MÉTODO: logError(String message)
     * 
     * PROPÓSITO:
     * Loga uma mensagem de erro com nível ERROR.
     * Usado para falhas críticas e exceções.
     * 
     * PARÂMETROS:
     * @param message - Mensagem de erro
     */
    public static void logError(String message) {
        logger.error(message);
    }

    /**
     * MÉTODO: logSuccess(String message)
     * 
     * PROPÓSITO:
     * Loga uma mensagem de sucesso com formatação especial.
     * Usado para confirmações de operações bem-sucedidas.
     * 
     * PARÂMETROS:
     * @param message - Mensagem de sucesso
     */
    public static void logSuccess(String message) {
        logger.info("✅ SUCCESS: " + message);
    }

    /**
     * MÉTODO: logWarning(String message)
     * 
     * PROPÓSITO:
     * Loga uma mensagem de aviso com nível WARN.
     * Usado para situações que merecem atenção mas não são erros.
     * 
     * PARÂMETROS:
     * @param message - Mensagem de aviso
     */
    public static void logWarning(String message) {
        logger.warn("⚠️ WARNING: " + message);
    }

    /**
     * MÉTODO: logInfo(String message)
     * 
     * PROPÓSITO:
     * Loga uma mensagem informativa com nível INFO.
     * Usado para informações gerais sobre execução.
     * 
     * PARÂMETROS:
     * @param message - Mensagem informativa
     */
    public static void logInfo(String message) {
        logger.info("ℹ️ INFO: " + message);
    }

    // ========================================================================
    // MÉTODOS ESPECÍFICOS PARA HTTP
    // ========================================================================
    
    /**
     * MÉTODO: logRequest(String method, String url, String body)
     * 
     * PROPÓSITO:
     * Loga informações de requisição HTTP de forma estruturada.
     * Inclui método, URL e body (se houver).
     * 
     * PARÂMETROS:
     * @param method - Método HTTP (GET, POST, etc.)
     * @param url - URL da requisição
     * @param body - Body da requisição (pode ser null)
     */
    public static void logRequest(String method, String url, String body) {
        logger.info("🔄 REQUEST: " + method + " " + url);
        if (body != null && !body.isEmpty()) {
            logger.info("📤 REQUEST BODY: " + body);
        }
    }

    /**
     * MÉTODO: logResponse(int statusCode, String responseBody)
     * 
     * PROPÓSITO:
     * Loga informações de resposta HTTP de forma estruturada.
     * Inclui status code e body da resposta.
     * 
     * PARÂMETROS:
     * @param statusCode - Código de status HTTP
     * @param responseBody - Body da resposta
     */
    public static void logResponse(int statusCode, String responseBody) {
        logger.info("📥 RESPONSE: Status " + statusCode);
        if (responseBody != null && !responseBody.isEmpty()) {
            logger.info("📥 RESPONSE BODY: " + responseBody);
        }
    }

    // ========================================================================
    // MÉTODOS DE FORMATAÇÃO
    // ========================================================================
    
    /**
     * MÉTODO: formatStepMessage(String message)
     * 
     * PROPÓSITO:
     * Formata mensagens de passos com timestamp e prefixo padrão.
     * Usado internamente pelo logStep().
     * 
     * PARÂMETROS:
     * @param message - Mensagem original
     * 
     * RETORNO:
     * @return String - Mensagem formatada
     */
    private static String formatStepMessage(String message) {
        return "🔹 STEP: " + message + " [" + DataUtils.getCurrentTimestamp() + "]";
    }

    // ========================================================================
    // MÉTODOS DE CONTROLE DE CENÁRIO
    // ========================================================================
    
    /**
     * MÉTODO: logScenarioStart(String scenarioName)
     * 
     * PROPÓSITO:
     * Marca o início de um cenário com separador visual.
     * Usado no hook @Before.
     * 
     * PARÂMETROS:
     * @param scenarioName - Nome do cenário iniciado
     */
    public static void logScenarioStart(String scenarioName) {
        logger.info(Console.SEPARATE);
        logger.info("🎬 SCENARIO START: " + scenarioName);
        logger.info(Console.SEPARATE_HYPHEN);
    }

    /**
     * MÉTODO: logScenarioEnd(String result)
     * 
     * PROPÓSITO:
     * Marca o fim de um cenário com resultado.
     * Usado no hook @After.
     * 
     * PARÂMETROS:
     * @param result - Resultado do cenário (PASSED/FAILED)
     */
    public static void logScenarioEnd(String result) {
        logger.info(Console.SEPARATE_HYPHEN);
        logger.info("🏁 SCENARIO END: " + result);
        logger.info(Console.SEPARATE);
    }

    /**
     * MÉTODO: logExecutionHeader(String timestamp)
     * 
     * PROPÓSITO:
     * Loga cabeçalho da execução de testes com timestamp.
     * Usado no início da suíte de testes.
     * 
     * PARÂMETROS:
     * @param timestamp - Timestamp do início da execução
     */
    public static void logExecutionHeader(String timestamp) {
        logger.info(Console.SEPARATE);
        logger.info("🚀 TEST EXECUTION STARTED: " + timestamp);
        logger.info(Console.SEPARATE);
    }

    /**
     * MÉTODO: logExecutionFooter(int passed, int failed, String duration)
     * 
     * PROPÓSITO:
     * Loga rodapé da execução com estatísticas finais.
     * Usado no final da suíte de testes.
     * 
     * PARÂMETROS:
     * @param passed - Número de testes que passaram
     * @param failed - Número de testes que falharam
     * @param duration - Duração total da execução
     */
    public static void logExecutionFooter(int passed, int failed, String duration) {
        logger.info(Console.SEPARATE);
        logger.info("📊 EXECUTION SUMMARY:");
        logger.info("   ✅ Passed: " + passed);
        logger.info("   ❌ Failed: " + failed);
        logger.info("   ⏱️ Duration: " + duration);
        logger.info(Console.SEPARATE);
    }
}
```

---

# 🔑 **6. PACOTE CORE/TOKEN**

## **📄 Token.java**
**📍 Local**: `src/main/java/org/br/com/core/token/Token.java`

```java
package org.br.com.core.token;

/**
 * ============================================================================
 * INTERFACE: Token
 * ============================================================================
 * 
 * PROPÓSITO:
 * Define o contrato padrão para diferentes tipos de tokens de autenticação
 * no framework. Garante que todas as implementações de token forneçam
 * informações essenciais de forma consistente.
 * 
 * QUANDO IMPLEMENTAR:
 * - Criar diferentes tipos de token (JWT, OAuth, API Key)
 * - Padronizar acesso a informações de token
 * - Facilitar polimorfismo com tokens
 * - Integrar com diferentes sistemas de autenticação
 */
public interface Token {

    /**
     * MÉTODO: getProductType()
     * 
     * PROPÓSITO:
     * Retorna o tipo ou categoria do produto/sistema que o token autentica.
     * Permite identificar para qual sistema o token é válido.
     * 
     * RETORNO:
     * @return String - Tipo do produto (ex: "API", "WEB", "MOBILE")
     */
    String getProductType();

    /**
     * MÉTODO: getAccessToken()
     * 
     * PROPÓSITO:
     * Retorna o valor atual do token de acesso para autenticação.
     * Fornece o token propriamente dito para uso em requisições.
     * 
     * RETORNO:
     * @return String - Token de acesso (ex: JWT, Bearer token, API key)
     */
    String getAccessToken();
}
```

## **📄 GerarTokenResquest.java**
**📍 Local**: `src/main/java/org/br/com/core/token/GerarTokenResquest.java`

```java
package org.br.com.core.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * MODELO: GerarTokenResquest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Modelo de dados para requisições de geração de token de autenticação.
 * Encapsula as credenciais necessárias para obter tokens de acesso
 * de forma padronizada e type-safe.
 * 
 * QUANDO USAR:
 * - Fazer login em APIs que retornam tokens
 * - Solicitar tokens de acesso OAuth
 * - Autenticar com serviços que usam email/senha
 * - Padronizar requisições de autenticação
 */
@Data              // Gera getters, setters, toString, equals, hashCode
@Builder           // Permite construção fluente com padrão Builder
@NoArgsConstructor // Construtor sem parâmetros (necessário para JSON)
@AllArgsConstructor // Construtor com todos os parâmetros
public class GerarTokenResquest {

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email do usuário para autenticação
     * TIPO: String
     * USO: Identificação do usuário no sistema
     */
    private String email;

    /**
     * CAMPO: password
     * 
     * PROPÓSITO: Senha do usuário para autenticação
     * TIPO: String
     * USO: Verificação de credenciais do usuário
     * 
     * SEGURANÇA:
     * - NUNCA logar este campo
     * - Considerar mascaramento em toString()
     * - Limpar da memória após uso se possível
     * - Usar HTTPS para transmissão
     */
    private String password;
}
```

## **📄 GerarToken.java**
**📍 Local**: `src/main/java/org/br/com/core/token/GerarToken.java`

```java
package org.br.com.core.token;

/**
 * ============================================================================
 * CLASSE LEGADA: GerarToken
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe originalmente destinada a gerar tokens automaticamente para
 * diferentes tipos de usuários. Atualmente está inativa (comentada)
 * mas mantida para referência histórica e possível reativação.
 * 
 * ESTADO ATUAL:
 * - Todos os métodos estão comentados
 * - Não está sendo utilizada no framework atual
 * - Mantida para referência de implementação
 * 
 * FUNCIONALIDADE ORIGINAL:
 * - Geração de tokens Bearer para administradores
 * - Tokens para usuários comuns (carrinhos)
 * - Integração com CadastrarUsuarioController
 * - Uso de FakerApiData para dados de teste
 */
public class GerarToken {
    // Classe mantida para referência histórica
    // Todos os métodos estão comentados
    // Funcionalidade migrada para controladores específicos
}
```

---

# 📊 **7. PACOTE DATA - Continua na próxima seção...**

## ✅ **CHECKLIST DA PARTE 2 - CLASSES CORE CONCLUÍDAS**

### **🔐 PACOTE CORE/ENCRYPTION:**
- [ ] ✅ **Encryption.java** - Criptografia AES completa

### **⚠️ PACOTE CORE/EXCEPTIONS:**
- [ ] ✅ **DataException.java** - Exceção personalizada

### **🎬 PACOTE CORE/FILTER:**
- [ ] ✅ **EvidenceFilter.java** - Captura básica de evidências
- [ ] ✅ **PDFLoggerFilter.java** - Captura avançada com PDF

### **⚙️ PACOTE CORE/PROCESSOR:**
- [ ] ✅ **TagProcessor.java** - Processamento de tags

### **🛠️ PACOTE CORE/SUPPORT:**
- [ ] ✅ **Context.java** - Gerenciamento de contexto thread-safe
- [ ] ✅ **ResourceUtils.java** - Localização de recursos
- [ ] ✅ **Console.java** - Constantes de formatação
- [ ] ✅ **ContextTime.java** - Medição de tempo
- [ ] ✅ **LogFormatter.java** - Formatação padronizada de logs

### **🔑 PACOTE CORE/TOKEN:**
- [ ] ✅ **Token.java** - Interface de token
- [ ] ✅ **GerarTokenResquest.java** - Modelo de requisição
- [ ] ✅ **GerarToken.java** - Classe legada

---

## 🎯 **PRÓXIMA ETAPA**

**📋 Continuar na PARTE 3** com:
- **📊 PACOTE DATA** completo (datasheet, reader, writer)
- **🏗️ FRAMEWORK** (Managers, Controllers, Models)
- **🎭 STEPS** e **🧪 UTILS**

**🚀 Classes CORE completas e prontas para uso!** 📚✨

