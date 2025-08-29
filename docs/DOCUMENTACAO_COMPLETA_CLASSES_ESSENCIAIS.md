# 📚 **DOCUMENTAÇÃO COMPLETA: CLASSES ESSENCIAIS DO PROJETO**

## 🎯 **ÍNDICE DAS CLASSES ESSENCIAIS**
1. [Classes do Core (Fundamentais)](#classes-core)
2. [Classes Utils (Utilitários)](#classes-utils)
3. [Configurações Essenciais](#configurações)
4. [Guia Passo a Passo: Do Zero ao Primeiro Teste](#guia-passo-a-passo)

---

## 🏗️ **CLASSES DO CORE (FUNDAMENTAIS)** {#classes-core}

### **1. Context.java - Gerenciador de Contexto Global**

**📍 Localização**: `src/main/java/org/br/com/core/support/Context.java`

**🎯 Função**: Gerencia dados compartilhados entre cenários e threads de teste de forma segura.

```java
package org.br.com.core.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CLASSE ESSENCIAL: Context
 * 
 * Responsabilidade:
 * - Gerenciar dados compartilhados entre cenários
 * - Manter isolamento entre threads (ThreadLocal)
 * - Controlar contadores de testes (passed/failed)
 * - Gerar UUIDs únicos para features
 */
public class Context {

    // Para dados específicos do cenário (isolado por thread)
    private static final ThreadLocal<Map<String, Object>> scenarioContext = 
        ThreadLocal.withInitial(HashMap::new);

    // Para dados compartilhados entre threads
    private static final Map<String, String> featureUUIDs = new ConcurrentHashMap<>();
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);

    // === MÉTODOS DE CICLO DE VIDA ===
    
    public static void startContext(String featureName, String scenarioName) {
        // Inicialização do contexto para novo cenário
    }

    public static void clearContext() {
        // CRÍTICO: Remove dados do ThreadLocal para evitar vazamento
        scenarioContext.remove();
    }

    public static void finishedContext(long duration) {
        // Exibe relatório final da execução
        System.out.println("==========================================");
        System.out.println("Test Run Finished.");
        System.out.println("Duration: " + duration + " ms");
        System.out.println("Passed: " + passedTests.get());
        System.out.println("Failed: " + failedTests.get());
        System.out.println("==========================================");
    }

    // === CONTADORES ===
    
    public static void resetCounters() {
        passedTests.set(0);
        failedTests.set(0);
        featureUUIDs.clear();
    }

    public static void incrementPassed() { passedTests.incrementAndGet(); }
    public static void incrementFailed() { failedTests.incrementAndGet(); }

    // === GERENCIAMENTO DE UUID ===
    
    public static String getOrCreateFeatureUUID(String featureName) {
        return featureUUIDs.computeIfAbsent(featureName, k -> UUID.randomUUID().toString());
    }

    // === GERENCIAMENTO DE DADOS ===
    
    public static void put(String key, Object value) {
        scenarioContext.get().put(key, value);
    }

    public static Object get(String key) {
        return scenarioContext.get().get(key);
    }

    // === MÉTODOS DE CONVENIÊNCIA ===
    
    public static void setData(Object data) { put("data", data); }
    public static Object getData() { return get("data"); }
    
    public static void setIdUsuario(String id) { put("idUsuario", id); }
    public static String getIdUsuario() { return (String) get("idUsuario"); }
}
```

**📋 Como Usar**:
```java
// No início do teste
Context.startContext("Feature Usuario", "Criar usuario");

// Armazenar dados
Context.setIdUsuario("123456");
Context.put("email", "teste@exemplo.com");

// Recuperar dados
String userId = Context.getIdUsuario();
String email = (String) Context.get("email");

// SEMPRE limpar no final (Hook @After)
Context.clearContext();
```

---

### **2. LogFormatter.java - Sistema de Logs Padronizado**

**📍 Localização**: `src/main/java/org/br/com/core/support/logger/LogFormatter.java`

**🎯 Função**: Padroniza e formata todas as saídas de log do projeto.

```java
package org.br.com.core.support.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.br.com.test.utils.DataUtils;

/**
 * CLASSE ESSENCIAL: LogFormatter
 * 
 * Responsabilidade:
 * - Padronizar formato de logs
 * - Categorizar tipos de log (step, erro, sucesso)
 * - Formatear mensagens com timestamp
 * - Integrar com Log4j2
 */
public class LogFormatter {
    private static final Logger logger = LogManager.getLogger(LogFormatter.class);
    
    // === LOGS PRINCIPAIS ===
    
    public static void logStep(String message) {
        String formattedMessage = formatStepMessage(message);
        logger.info(formattedMessage);
    }
    
    public static void logError(String message) {
        String formattedMessage = formatStepMessage("❌ ERRO: " + message);
        logger.error(formattedMessage);
    }
    
    public static void logWarning(String message) {
        String formattedMessage = formatStepMessage("⚠️ AVISO: " + message);
        logger.warn(formattedMessage);
    }
    
    public static void logSuccess(String message) {
        String formattedMessage = formatStepMessage("✅ " + message);
        logger.info(formattedMessage);
    }
    
    // === LOGS ESPECÍFICOS PARA API ===
    
    public static void logRequest(String method, String url, String body) {
        logStep("Enviando solicitacao de " + method + " para: " + url);
        if (body != null && !body.trim().isEmpty()) {
            logStep("Body: " + body);
        }
    }
    
    public static void logResponse(int statusCode, String responseBody) {
        logStep("Status Code: " + statusCode);
        if (responseBody != null && !responseBody.trim().isEmpty()) {
            logStep("Response: " + responseBody);
        }
    }
    
    // === FORMATAÇÃO ===
    
    private static String formatStepMessage(String message) {
        return "[INFO] " + DataUtils.getHoraAtual() + " | '-- " + message;
    }
    
    // === LOGS DE CENÁRIO ===
    
    public static void logScenarioStart(String scenarioName) {
        logStep("+-- " + scenarioName);
    }
    
    public static void logScenarioEnd(String result) {
        String icon = "PASSED".equalsIgnoreCase(result) ? "✅" : "❌";
        String formatted = formatStepMessage(icon + result);
        logger.info(formatted);
        logger.info(""); // quebra de linha
    }
    
    // === LOGS DE EXECUÇÃO ===
    
    public static void logExecutionHeader(String timestamp) {
        logger.info("==================================== Execucao: " + timestamp + " ====================================");
    }
    
    public static void logFeature(String featureName) {
        logger.info("Feature: " + featureName);
    }
    
    public static void logValidation(String message) {
        logStep("Validando " + message);
    }
}
```

**📋 Como Usar**:
```java
// Logs básicos
LogFormatter.logStep("Iniciando teste de usuario");
LogFormatter.logSuccess("Usuario criado com sucesso");
LogFormatter.logError("Falha na validacao do email");
LogFormatter.logWarning("Token expirando em 5 minutos");

// Logs específicos de API
LogFormatter.logRequest("POST", "/usuarios", "{\"nome\":\"João\"}");
LogFormatter.logResponse(201, "{\"id\":\"123\", \"nome\":\"João\"}");

// Logs de validação
LogFormatter.logValidation("status code 201");
LogFormatter.logValidation("dados do usuario na resposta");
```

---

### **3. EvidenceFilter.java - Captura Automática de Evidências**

**📍 Localização**: `src/main/java/org/br/com/core/filter/EvidenceFilter.java`

**🎯 Função**: Intercepta todas as requisições HTTP para capturar evidências automaticamente.

```java
package org.br.com.core.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.br.com.test.utils.hooks.HooksEvidenciasApi;

/**
 * CLASSE ESSENCIAL: EvidenceFilter
 * 
 * Responsabilidade:
 * - Interceptar requisições REST Assured
 * - Capturar dados de request (method, URI, headers, body)
 * - Capturar dados de response (status, body)
 * - Enviar dados para geração de evidências
 */
public class EvidenceFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // Executa a requisição
        Response response = ctx.next(requestSpec, responseSpec);

        // Captura dados da requisição
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        String headers = requestSpec.getHeaders().toString();
        String body = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "";

        // Envia para captura de evidências
        HooksEvidenciasApi.capturarDadosRequisicao(method, uri, headers, body);

        // Captura dados da resposta
        String statusCode = String.valueOf(response.getStatusCode());
        String responseBody = response.getBody().asString();

        // Envia para captura de evidências
        HooksEvidenciasApi.capturarDadosResposta(statusCode, responseBody);

        return response;
    }
}
```

**📋 Como Usar**:
```java
// Em todos os controladores, adicione o filter
response = given()
    .filter(new EvidenceFilter())  // ← AUTOMÁTICO: Captura evidências
    .header("Content-Type", "application/json")
    .body(usuarioRequest)
    .when()
    .post("/usuarios");

// O filter funciona automaticamente, capturando:
// - Method: POST
// - URI: http://localhost:3000/usuarios
// - Headers: Content-Type=application/json
// - Body: {"nome":"João","email":"joao@teste.com"}
// - Status Code: 201
// - Response: {"id":"123","nome":"João"}
```

---

### **4. DataReader.java - Leitor de Dados Excel**

**📍 Localização**: `src/main/java/org/br/com/core/data/DataReader.java`

**🎯 Função**: Lê dados de planilhas Excel usando a biblioteca Fillo.

```java
package org.br.com.core.data;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.exceptions.DataException;

/**
 * CLASSE ESSENCIAL: DataReader
 * 
 * Responsabilidade:
 * - Conectar com arquivos Excel (.xlsx)
 * - Executar queries SQL no Excel
 * - Gerenciar conexões de forma segura
 * - Tratar exceções de dados
 */
@Log4j2
public class DataReader implements AutoCloseable {

    private final Fillo fillo;
    private Connection connection;

    public DataReader(String excelFilePath) {
        synchronized (fillo = new Fillo()) {
            setConnection(excelFilePath);
        }
    }

    private void setConnection(String excelFilePath) {
        try {
            connection = fillo.getConnection(excelFilePath);
            log.info("Conexão estabelecida com: " + excelFilePath);
        } catch (FilloException e) {
            String message = "Error in get connection with file: " + excelFilePath;
            log.error(message, e);
            throw new DataException(message, e);
        }
    }

    public Recordset executeQuery(String query) {
        try {
            log.info("Executando query: " + query);
            return connection.executeQuery(query);
        } catch (FilloException e) {
            String message = "Error in execute with query: " + query;
            log.error(message, e);
            throw new DataException(message, e);
        }
    }

    public int updateQuery(String query) {
        try {
            log.info("Executando update: " + query);
            return connection.executeUpdate(query);
        } catch (FilloException e) {
            String message = "Error in update query with: " + query;
            log.error(message, e);
            throw new DataException(message, e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
            log.info("Conexão fechada com sucesso");
        }
    }
}
```

**📋 Como Usar**:
```java
// Lendo dados de uma planilha Excel
try (DataReader dataReader = new DataReader("src/main/resources/data/MassaDados.xlsx")) {
    
    // Query SQL no Excel (aba = tabela)
    Recordset recordset = dataReader.executeQuery("SELECT * FROM TBL_CADASTRO WHERE ativo = 'S'");
    
    while (recordset.next()) {
        String nome = recordset.getField("nome");
        String email = recordset.getField("email");
        String senha = recordset.getField("senha");
        
        System.out.println("Nome: " + nome + ", Email: " + email);
    }
    
    recordset.close();
} catch (Exception e) {
    System.err.println("Erro ao ler dados: " + e.getMessage());
}
```

---

## 🛠️ **CLASSES UTILS (UTILITÁRIOS)** {#classes-utils}

### **5. LogConfig.java - Configuração de Logs**

**📍 Localização**: `src/main/java/org/br/com/test/utils/LogConfig.java`

**🎯 Função**: Gerencia configurações de mascaramento de dados sensíveis nos logs.

```java
package org.br.com.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * CLASSE ESSENCIAL: LogConfig
 * 
 * Responsabilidade:
 * - Carregar configurações do arquivo test.properties
 * - Definir regras de mascaramento de dados sensíveis
 * - Fornecer valores padrão se arquivo não existir
 * - Validar configurações carregadas
 */
public class LogConfig {
    
    private static final Properties properties = new Properties();
    private static boolean initialized = false;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try (InputStream input = LogConfig.class.getClassLoader()
                .getResourceAsStream("test.properties")) {
            if (input != null) {
                properties.load(input);
                initialized = true;
                System.out.println("✅ Configurações carregadas de test.properties");
            } else {
                System.out.println("⚠️ Arquivo test.properties não encontrado, usando configurações padrão");
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.out.println("⚠️ Erro ao carregar test.properties: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("log.mascarar.dados.sensiveis", "true");
        properties.setProperty("log.mascarar.senha", "true");
        properties.setProperty("log.mascarar.id", "true");
        properties.setProperty("log.mascarar.token", "true");
        properties.setProperty("log.mascarar.email", "false");
        properties.setProperty("log.caracteres.visiveis", "3");
        initialized = true;
    }
    
    // === MÉTODOS DE CONFIGURAÇÃO ===
    
    public static boolean isMascararDadosSensiveis() {
        return Boolean.parseBoolean(properties.getProperty("log.mascarar.dados.sensiveis", "true"));
    }
    
    public static boolean isMascararSenha() {
        return Boolean.parseBoolean(properties.getProperty("log.mascarar.senha", "true"));
    }
    
    public static boolean isMascararId() {
        return Boolean.parseBoolean(properties.getProperty("log.mascarar.id", "true"));
    }
    
    public static boolean isMascararToken() {
        return Boolean.parseBoolean(properties.getProperty("log.mascarar.token", "true"));
    }
    
    public static boolean isMascararEmail() {
        return Boolean.parseBoolean(properties.getProperty("log.mascarar.email", "false"));
    }
    
    public static int getCaracteresVisiveis() {
        try {
            String valor = properties.getProperty("log.caracteres.visiveis", "3");
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Erro ao parsear log.caracteres.visiveis, usando valor padrão: 3");
            return 3;
        }
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
}
```

**📋 Como Usar**:
```java
// Verificar se deve mascarar dados
if (LogConfig.isMascararSenha()) {
    senhaLog = mascararSenha(senha);
} else {
    senhaLog = senha;
}

if (LogConfig.isMascararToken()) {
    tokenLog = mascararToken(token);
}

// Obter número de caracteres visíveis
int caracteres = LogConfig.getCaracteresVisiveis(); // retorna 3
String senhaMascarada = senha.substring(0, caracteres) + "***" + 
                       senha.substring(senha.length() - caracteres);
```

---

### **6. DataUtils.java - Utilitários de Data e Hora**

**📍 Localização**: `src/main/java/org/br/com/test/utils/DataUtils.java`

**🎯 Função**: Fornece métodos para formatação de data e hora nos logs e relatórios.

```java
package org.br.com.test.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CLASSE ESSENCIAL: DataUtils
 * 
 * Responsabilidade:
 * - Formatar data e hora para logs
 * - Gerar timestamps únicos
 * - Padronizar formatos de data
 * - Suportar diferentes precisões (com/sem milissegundos)
 */
public class DataUtils {
    private static final DateTimeFormatter HORA_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATA_HORA_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter HORA_COM_MILIS_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    // === MÉTODOS PRINCIPAIS ===

    public static String getHoraAtual() {
        return LocalDateTime.now().format(HORA_FORMATTER);
    }

    public static String getHoraAtualComMilis() {
        return LocalDateTime.now().format(HORA_COM_MILIS_FORMATTER);
    }

    public static String getDataHoraAtual() {
        return LocalDateTime.now().format(DATA_HORA_FORMATTER);
    }

    public static String getTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    public static String getDataHoraParaArquivo() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}
```

**📋 Como Usar**:
```java
// Nos logs
LogFormatter.logStep("Teste iniciado às " + DataUtils.getHoraAtual());

// Para timestamps únicos
String arquivo = "teste_" + DataUtils.getTimestamp() + ".log";

// Para evidências com milissegundos
String inicioTeste = DataUtils.getHoraAtualComMilis();

// Para relatórios
String relatorio = "Execução: " + DataUtils.getDataHoraAtual();
```

---

### **7. JavaFaker.java - Gerador de Dados de Teste**

**📍 Localização**: `src/main/java/org/br/com/test/utils/JavaFaker.java`

**🎯 Função**: Gera dados falsos realistas para testes usando JavaFaker.

```java
package org.br.com.test.utils;

import com.github.javafaker.Faker;
import org.br.com.test.model.request.UsuarioRequest;
import org.br.com.test.manager.UsuarioManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * CLASSE ESSENCIAL: JavaFaker
 * 
 * Responsabilidade:
 * - Gerar dados de teste realistas
 * - Criar usuários, categorias e artigos fake
 * - Garantir senhas válidas conforme regras da API
 * - Evitar conflitos com dados únicos (timestamp)
 */
public class JavaFaker {
    private static final Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));

    // === GERAÇÃO DE USUÁRIOS ===

    public static UsuarioRequest gerarUsuarioRequestSimples() {
        return UsuarioRequest.builder()
                .nomeCompleto(faker.name().fullName())
                .nomeUsuario(faker.name().username())
                .email(faker.internet().emailAddress())
                .senha(gerarSenhaValida())
                .build();
    }

    public static String gerarSenhaValida() {
        // Padrões que atendem aos requisitos da API
        String[] padroesSenha = {
            "@Pass123",
            "Pass321",
        };

        Random random = new Random();
        String senhaBase = padroesSenha[random.nextInt(padroesSenha.length)];
        
        // Adicionar variação com números aleatórios
        String numerosAleatorios = String.valueOf(100 + random.nextInt(900));
        return senhaBase.replace("123", numerosAleatorios);
    }

    // === GERAÇÃO DE CATEGORIAS ===

    public static Map<String, String> categoriaJavaFake() {
        Map<String, String> categoria = new HashMap<>();

        // Nome único para evitar conflitos
        String categoriaBase = faker.commerce().department();
        String nomeUnico = categoriaBase + "_" + System.currentTimeMillis();
        String descricao = "Artigos sobre " + categoriaBase.toLowerCase();

        categoria.put("nome", nomeUnico);
        categoria.put("descricao", descricao);

        return categoria;
    }

    // === GERAÇÃO DE ARTIGOS ===

    public static Map<String, String> artigosTesteFixo(String nomeAutor, String nomeCategoria) {
        if (nomeAutor == null || nomeAutor.isEmpty()) {
            nomeAutor = UsuarioManager.getNomeCompletoUsuario();
        }
        
        Map<String, String> artigo = new HashMap<>();
        artigo.put("titulo", "Introdução aos test Automatizados");
        artigo.put("conteudo", "Exemplos de ferramentas de test automatizados...");
        artigo.put("nomeAutor", nomeAutor);
        artigo.put("nomeCategoria", nomeCategoria);
        artigo.put("dataPublicacao", gerarDataPublicacao());
        
        return artigo;
    }

    private static String gerarDataPublicacao() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return agora.format(formatter);
    }

    // === DADOS PARA ATUALIZAÇÃO ===

    public static Map<String, String> DadosAtualizacaoJavaFakeMap() {
        Map<String, String> dadosAtualizacao = new HashMap<>();
        dadosAtualizacao.put("nomeUsuario", faker.name().firstName());
        dadosAtualizacao.put("senha", gerarSenhaValida());
        return dadosAtualizacao;
    }
}
```

**📋 Como Usar**:
```java
// Gerar usuário completo
UsuarioRequest usuario = JavaFaker.gerarUsuarioRequestSimples();

// Gerar categoria única
Map<String, String> categoria = JavaFaker.categoriaJavaFake();
String nomeCategoria = categoria.get("nome");
String descricaoCategoria = categoria.get("descricao");

// Gerar artigo
Map<String, String> artigo = JavaFaker.artigosTesteFixo("João Silva", "Tecnologia");

// Gerar dados para atualização
Map<String, String> dadosUpdate = JavaFaker.DadosAtualizacaoJavaFakeMap();
```

---

### **8. HooksEvidenciasApi.java - Hooks de Evidências**

**📍 Localização**: `src/main/java/org/br/com/test/utils/hooks/HooksEvidenciasApi.java`

**🎯 Função**: Gerencia capturas de evidências antes e depois de cada cenário.

```java
package org.br.com.test.utils.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;
import org.br.com.core.support.Context;
import org.br.com.test.utils.DataUtils;
import org.br.com.core.support.logger.LogFormatter;

/**
 * CLASSE ESSENCIAL: HooksEvidenciasApi
 * 
 * Responsabilidade:
 * - Executar ações antes/depois de cada cenário
 * - Capturar dados de request/response
 * - Gerar evidências em DOCX
 * - Gerenciar logs de execução
 * - Controlar contadores de teste
 */
public class HooksEvidenciasApi {

    @Getter
    private static Scenario scenario;

    private static String requestMethod, requestUri, headers, requestBody;
    private static String statusCode, responseBody;
    private static String tag, nomeCenario, idEvidencia, dataHoraInicio, dataHoraTermino;
    private static String featureName, uuidFeature;

    // === CAPTURA DE DADOS ===

    public static void capturarDadosRequisicao(String method, String uri, String headersData, String body) {
        requestMethod = method;
        requestUri = uri;
        headers = headersData;
        requestBody = body;
    }

    public static void capturarDadosResposta(String code, String response) {
        statusCode = code;
        responseBody = response;
    }

    // === HOOK ANTES DO TESTE ===

    @Before
    public void antesDoTeste(Scenario scenario) {
        limparDados();
        HooksEvidenciasApi.scenario = scenario;

        // Extrair informações do cenário
        featureName = getFeatureTitleFromScenario(scenario);
        nomeCenario = scenario.getName();

        // Gerar UUID único para a feature
        uuidFeature = Context.getOrCreateFeatureUUID(featureName);

        // Logs de início
        LogFormatter.logExecutionHeader(DataUtils.getDataHoraAtual());
        LogFormatter.logFeature(featureName);
        LogFormatter.logScenarioId(uuidFeature);

        // Configurar contexto
        Context.startContext(featureName, scenario.getName());
        idEvidencia = uuidFeature;
        Context.setScenarioId(idEvidencia);
        
        LogFormatter.logScenarioStart(scenario.getName());
        dataHoraInicio = DataUtils.getHoraAtualComMilis();

        // Extrair tag do cenário
        tag = scenario.getSourceTagNames().stream()
                .filter(t -> t.startsWith("@CT-"))
                .map(t -> t.substring(1))
                .findFirst()
                .orElse("CT_NAO_DEFINIDO");
    }

    // === HOOK DEPOIS DO TESTE ===

    @After
    public void depoisDoTeste(Scenario scenario) {
        dataHoraTermino = DataUtils.getHoraAtualComMilis();

        // Verificar se o teste falhou
        boolean failed = scenario.isFailed();
        String failureLog = null;
        
        if (failed) {
            Context.incrementFailed();
            failureLog = "O teste falhou durante a fase de validacao (step 'Then').\n" +
                    "O Status Code retornado pela API foi '" + statusCode + 
                    "', mas a validacao esperava um valor diferente.\n" +
                    "Consulte o log de execucao no console para ver o stack trace completo.";
        } else {
            Context.incrementPassed();
        }

        // Limpar contexto
        Context.clearContext();

        // Gerar evidência em DOCX
        String featureFileName = getFeatureFileNameFromScenario(scenario);
        GeradorDocxApi.setApiData(
                dataHoraInicio, dataHoraTermino,
                requestMethod, requestUri,
                FormatUtils.formatHeaders(headers),
                FormatUtils.prettyJson(requestBody),
                statusCode,
                FormatUtils.maskSensitiveDataInJson(FormatUtils.prettyJson(responseBody)),
                tag, nomeCenario, featureName, failureLog
        );

        GeradorDocxApi.gerarEvidenciaApi(nomeCenario, idEvidencia, !failed, uuidFeature, featureFileName);

        // Log final do cenário
        LogFormatter.logScenarioEnd(failed ? "FAILED" : "PASSED");

        // Anexar evidência DOCX ao relatório Cucumber
        anexarEvidenciaDocx(scenario, featureFileName);
    }

    // === MÉTODOS AUXILIARES ===

    private static void limparDados() {
        requestMethod = requestUri = headers = requestBody = "";
        statusCode = responseBody = "";
        tag = nomeCenario = idEvidencia = dataHoraInicio = dataHoraTermino = "";
        featureName = "";
    }

    private String getFeatureTitleFromScenario(Scenario scenario) {
        // Extrai o título da feature do arquivo .feature
        // Implementação completa no código original
    }

    private String getFeatureFileNameFromScenario(Scenario scenario) {
        // Extrai o nome do arquivo da feature
        // Implementação completa no código original
    }
}
```

**📋 Como Usar**:
```java
// Os hooks funcionam automaticamente!

// Antes de cada @Scenario:
// 1. Limpa dados anteriores
// 2. Configura contexto
// 3. Inicia captura de tempo
// 4. Loga início do cenário

// Durante o teste:
// - EvidenceFilter captura request/response automaticamente

// Depois de cada @Scenario:
// 1. Verifica se passou/falhou
// 2. Gera evidência DOCX
// 3. Atualiza contadores
// 4. Limpa contexto
// 5. Loga resultado final
```

---

## ⚙️ **CONFIGURAÇÕES ESSENCIAIS** {#configurações}

### **9. test.properties - Configurações do Projeto**

**📍 Localização**: `src/main/resources/test.properties`

```properties
# Configurações de Log para Testes
# ========================================

# Controla se dados sensíveis são mascarados nos logs
# true = dados mascarados (recomendado para produção)
# false = dados visíveis (útil para debug)
log.mascarar.dados.sensiveis=true

# Controla se senhas são mascaradas
log.mascarar.senha=true

# Controla se IDs são mascarados
log.mascarar.id=false

# Controla se tokens são mascarados
log.mascarar.token=true

# Controla se emails são mascarados
log.mascarar.email=false

# Número de caracteres visíveis no início e fim de dados mascarados
log.caracteres.visiveis=3
```

### **10. log4j2.xml - Configuração de Logs**

**📍 Localização**: `src/main/resources/log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%msg%n"/>
        </Console>
        
        <!-- File Appender -->
        <File name="FileAppender" fileName="target/log/execution.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n"/>
        </File>
        
        <!-- Rolling File Appender -->
        <RollingFile name="RollingFile"
                     fileName="target/log/automation-tmp.log"
                     filePattern="target/log/automation-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```

---

## 🚀 **GUIA PASSO A PASSO: DO ZERO AO PRIMEIRO TESTE** {#guia-passo-a-passo}

### **PASSO 1: Preparação do Ambiente**

```bash
# 1. Verificar pré-requisitos
java -version          # Java 21+
mvn -version          # Maven 3.8+
node -version         # Node.js 18+

# 2. Clonar/baixar o projeto
cd /caminho/do/projeto

# 3. Iniciar a API target
cd cms-for-qas-api/
npm install
npm start
# API rodando em http://localhost:3000

# 4. Voltar para raiz e compilar projeto Java
cd ..
mvn clean compile
```

### **PASSO 2: Estrutura Mínima de Arquivos**

**Ordem de criação obrigatória**:

```
1. src/main/resources/test.properties
2. src/main/resources/log4j2.xml
3. src/main/java/org/br/com/core/support/Context.java
4. src/main/java/org/br/com/test/utils/DataUtils.java
5. src/main/java/org/br/com/test/utils/LogConfig.java
6. src/main/java/org/br/com/core/support/logger/LogFormatter.java
7. src/main/java/org/br/com/core/filter/EvidenceFilter.java
8. src/main/java/org/br/com/test/utils/hooks/HooksEvidenciasApi.java
```

### **PASSO 3: Criar o Primeiro Modelo**

```java
// src/main/java/org/br/com/test/model/request/UsuarioRequest.java
package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    private String nomeCompleto;
    private String nomeUsuario;
    private String email;
    private String senha;
}
```

### **PASSO 4: Criar o Primeiro Manager**

```java
// src/main/java/org/br/com/test/manager/UsuarioManager.java
package org.br.com.test.manager;

public class UsuarioManager {
    private static final ThreadLocal<String> emailUsuario = new ThreadLocal<>();
    private static final ThreadLocal<String> senhaUsuario = new ThreadLocal<>();
    private static final ThreadLocal<String> idUsuario = new ThreadLocal<>();
    
    public static String getEmailUsuario() { return emailUsuario.get(); }
    public static void setEmailUsuario(String email) { emailUsuario.set(email); }
    
    public static String getSenhaUsuario() { return senhaUsuario.get(); }
    public static void setSenhaUsuario(String senha) { senhaUsuario.set(senha); }
    
    public static String getIdUsuario() { return idUsuario.get(); }
    public static void setIdUsuario(String id) { idUsuario.set(id); }
    
    public static void limparDados() {
        emailUsuario.remove();
        senhaUsuario.remove();
        idUsuario.remove();
    }
}
```

### **PASSO 5: Criar o Primeiro Controller**

```java
// src/main/java/org/br/com/test/controllers/usuario/UsuarioController.java
package org.br.com.test.controllers.usuario;

import io.restassured.response.Response;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.request.UsuarioRequest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class UsuarioController {
    
    private Response response;
    private static final String BASE_URL = "http://localhost:3000";
    private static final String ENDPOINT_USUARIOS = "/usuarios";
    
    public void criarUsuarioSimples() {
        // Dados únicos para evitar conflitos
        long timestamp = System.currentTimeMillis();
        String nome = "Usuario Teste " + timestamp;
        String usuario = "user" + timestamp;
        String email = "teste" + timestamp + "@exemplo.com";
        String senha = "senha123";
        
        UsuarioRequest usuarioRequest = UsuarioRequest.builder()
                .nomeCompleto(nome)
                .nomeUsuario(usuario)
                .email(email)
                .senha(senha)
                .build();
        
        LogFormatter.logStep("Criando usuario: " + email);
        
        response = given()
                .filter(new EvidenceFilter())  // ← AUTOMÁTICO: captura evidências
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(usuarioRequest)
                .when()
                .post(ENDPOINT_USUARIOS);
        
        // Salvar dados no manager
        if (response.getStatusCode() == 201) {
            String userId = response.jsonPath().getString("id");
            UsuarioManager.setEmailUsuario(email);
            UsuarioManager.setSenhaUsuario(senha);
            UsuarioManager.setIdUsuario(userId);
            
            LogFormatter.logSuccess("Usuario criado com ID: " + userId);
        }
    }
    
    public void validarStatusCode(int statusEsperado) {
        int statusAtual = response.getStatusCode();
        assertEquals("Status code não confere", statusEsperado, statusAtual);
        LogFormatter.logSuccess("Status code " + statusAtual + " validado");
    }
    
    public void validarDadosUsuario() {
        String emailEsperado = UsuarioManager.getEmailUsuario();
        String emailResposta = response.jsonPath().getString("email");
        
        assertEquals("Email não confere", emailEsperado, emailResposta);
        LogFormatter.logSuccess("Dados do usuario validados");
    }
}
```

### **PASSO 6: Criar o Primeiro Step**

```java
// src/main/java/org/br/com/test/steps/UsuarioSteps.java
package org.br.com.test.steps;

import io.cucumber.java.en.*;
import org.br.com.test.controllers.usuario.UsuarioController;

public class UsuarioSteps {
    
    private final UsuarioController usuarioController = new UsuarioController();
    
    @Given("que crio um novo usuario")
    public void queCrioUmNovoUsuario() {
        usuarioController.criarUsuarioSimples();
    }
    
    @Then("devo receber status code {int}")
    public void devoReceberStatusCode(int statusCode) {
        usuarioController.validarStatusCode(statusCode);
    }
    
    @And("os dados do usuario devem estar corretos")
    public void osDadosDoUsuarioDevemEstarCorretos() {
        usuarioController.validarDadosUsuario();
    }
}
```

### **PASSO 7: Criar a Primeira Feature**

```gherkin
# src/main/resources/features/primeiro-teste.feature
@api
Feature: Primeiro Teste de Usuario
  Como um desenvolvedor
  Eu quero criar meu primeiro teste
  Para validar que o framework funciona

  @primeiro-teste
  Scenario: Criar usuario com sucesso
    Given que crio um novo usuario
    Then devo receber status code 201
    And os dados do usuario devem estar corretos
```

### **PASSO 8: Criar o Runner**

```java
// src/main/java/org/br/com/PrimeiroTesteRunner.java
package org.br.com;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/main/resources/features/primeiro-teste.feature",
    glue = {
        "org.br.com.test.steps", 
        "org.br.com.test.utils.hooks"
    },
    plugin = {
        "pretty",
        "html:target/reports/primeiro-teste.html"
    },
    tags = "@primeiro-teste"
)
public class PrimeiroTesteRunner {
    // Classe vazia - configuração está nas anotações
}
```

### **PASSO 9: Executar o Primeiro Teste**

```bash
# Método 1: Via Maven
mvn test -Dtest=PrimeiroTesteRunner

# Método 2: Via tag específica
mvn test -Dcucumber.filter.tags="@primeiro-teste"

# Método 3: Via IDE (IntelliJ/Eclipse)
# Clique direito no Runner > Run 'PrimeiroTesteRunner'
```

### **PASSO 10: Verificar Resultados**

**Console deve exibir**:
```
==================================== Execucao: 2024-03-20 10:30:15 ====================================
Feature: Primeiro Teste de Usuario
[uuid-da-feature]

[INFO] 10:30:15 | '-- +-- Criar usuario com sucesso
[INFO] 10:30:15 | '-- Criando usuario: teste1710937815123@exemplo.com
[INFO] 10:30:16 | '-- ✅ Usuario criado com ID: 550e8400-e29b-41d4-a716-446655440000
[INFO] 10:30:16 | '-- ✅ Status code 201 validado
[INFO] 10:30:16 | '-- ✅ Dados do usuario validados
[INFO] 10:30:16 | '-- ✅PASSED

==========================================
Test Run Finished.
Duration: 1247 ms
Passed: 1
Failed: 0
==========================================
```

**Arquivos gerados**:
```
target/
├── reports/
│   └── primeiro-teste.html     # Relatório HTML
├── evidence/
│   └── primeiro-teste/
│       └── [uuid]/
│           └── evidencia.docx  # Evidência DOCX
└── log/
    ├── execution.log           # Log completo
    └── automation-tmp.log      # Log detalhado
```

### **PASSO 11: Validação de Sucesso**

✅ **Checklist de validação**:
- [ ] Console exibe logs formatados
- [ ] Status code 201 validado
- [ ] Dados do usuário conferem
- [ ] Relatório HTML gerado
- [ ] Evidência DOCX criada
- [ ] Logs salvos em arquivos
- [ ] Contadores mostram 1 passou, 0 falhou

### **PASSO 12: Próximos Passos**

1. **Adicionar mais cenários** na mesma feature
2. **Criar testes de login** usando o usuário criado
3. **Implementar CRUD completo** (buscar, atualizar, excluir)
4. **Adicionar validações de erro** (dados inválidos)
5. **Integrar com planilhas Excel** para massa de dados
6. **Criar features para categorias e artigos**

---

## 🎓 **RESUMO FINAL**

### **✅ Classes Essenciais Documentadas:**

**CORE (Fundamentais)**:
1. **Context.java** - Gerenciamento de dados entre cenários
2. **LogFormatter.java** - Sistema de logs padronizado
3. **EvidenceFilter.java** - Captura automática de evidências
4. **DataReader.java** - Leitor de dados Excel

**UTILS (Utilitários)**:
5. **LogConfig.java** - Configuração de mascaramento
6. **DataUtils.java** - Formatação de data/hora
7. **JavaFaker.java** - Geração de dados de teste
8. **HooksEvidenciasApi.java** - Hooks de evidências

**CONFIGURAÇÕES**:
9. **test.properties** - Configurações do projeto
10. **log4j2.xml** - Configuração de logs

### **✅ Fluxo Completo de Execução:**

```
1. Runner carrega configurações
2. Hook @Before inicia contexto e logs
3. Steps executam Controller
4. Controller faz requisição com EvidenceFilter
5. EvidenceFilter captura dados automaticamente
6. Controller valida resposta
7. Hook @After gera evidências e relatórios
8. Context finaliza com contadores
```

### **✅ Resultado Final:**
- **Framework completo** e funcional
- **Evidências automáticas** em DOCX
- **Logs estruturados** e mascarados
- **Relatórios HTML** do Cucumber
- **Dados isolados** por thread
- **Configuração flexível** via properties
- **Pronto para escalabilidade** com novos módulos

Este projeto está pronto para uso profissional e pode ser expandido conforme necessário!
