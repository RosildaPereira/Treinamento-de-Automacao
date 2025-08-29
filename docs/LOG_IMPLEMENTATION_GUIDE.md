# Guia de Implementação - Sistema de Logs para Automação

## 📋 Visão Geral

Este guia demonstra como implementar um sistema de logs robusto e estruturado para projetos de automação Java com Cucumber, similar ao que foi implementado no projeto AUTOMACAO-API-WEB-MOBILE.

## 🎯 Objetivos

- Criar logs estruturados e legíveis
- Implementar identificação única de cenários
- Gerar relatórios detalhados de execução
- Facilitar debugging e troubleshooting
- Integrar com sistemas de relatórios (Xray, Jira)

## 🏗️ Arquitetura do Sistema

```
src/
├── main/
│   └── java/
│       └── org/yourcompany/
│           ├── core/
│           │   ├── Context.java
│           │   └── LogFormatter.java
│           ├── utils/
│           │   ├── DataUtils.java
│           │   └── UUIDGenerator.java
│           └── config/
│               └── EnvironmentConfig.java
└── test/
    ├── java/
    │   └── org/yourcompany/
    │       ├── RunnerTestApi.java
    │       └── hooks/
    │           └── TestHooks.java
    └── resources/
        ├── config.properties
        └── log4j2.xml
```

## 📦 Dependências Necessárias

### Maven (pom.xml)
```xml
<dependencies>
    <!-- Cucumber -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Logging -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.23.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.23.1</version>
    </dependency>
    
    <!-- JUnit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Apache Commons -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.12.0</version>
    </dependency>
</dependencies>
```

## 🔧 Implementação Passo a Passo

### Passo 1: Configuração do Log4j2

**Arquivo: `src/test/resources/log4j2.xml`**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss} %-5level - %msg%n"/>
        </Console>
        
        <!-- File Appender -->
        <File name="FileAppender" fileName="log/execution.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n"/>
        </File>
        
        <!-- Rolling File Appender -->
        <RollingFile name="RollingFile" 
                     fileName="log/automation.log"
                     filePattern="log/automation-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="10 MB"/>
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

### Passo 2: Classe Context (Gerenciamento de Estado)

**Arquivo: `src/main/java/org/yourcompany/core/Context.java`**
```java
package org.yourcompany.core;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Context {
    private static final Map<String, Object> contextData = new HashMap<>();
    private static Instant startTime;
    private static String scenarioId;
    private static String featureName;
    private static String scenarioName;
    
    public static void startContext(String feature, String scenario) {
        startTime = Instant.now();
        scenarioId = UUID.randomUUID().toString();
        featureName = feature;
        scenarioName = scenario;
        
        LogFormatter.logStep("==================================== Execucao: " + 
                           DataUtils.getDataHoraAtual() + " ====================================");
        LogFormatter.logStep("Feature: " + feature);
        LogFormatter.logStep(scenarioId);
        LogFormatter.logStep("");
    }
    
    public static void finishedContext(long totalDuration) {
        long durationSeconds = totalDuration / 1000;
        LogFormatter.logStep("========================================== Resumo da Execucao ==========================================");
        LogFormatter.logStep("Duration: " + durationSeconds + "s");
        LogFormatter.logStep("");
    }
    
    public static void setData(String key, Object value) {
        contextData.put(key, value);
    }
    
    public static Object getData(String key) {
        return contextData.get(key);
    }
    
    public static String getScenarioId() {
        return scenarioId;
    }
    
    public static String getFeatureName() {
        return featureName;
    }
    
    public static String getScenarioName() {
        return scenarioName;
    }
    
    public static void clearContext() {
        contextData.clear();
        startTime = null;
        scenarioId = null;
        featureName = null;
        scenarioName = null;
    }
}
```

### Passo 3: Classe LogFormatter (Formatação de Logs)

**Arquivo: `src/main/java/org/yourcompany/core/LogFormatter.java`**
```java
package org.yourcompany.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogFormatter {
    private static final Logger logger = LogManager.getLogger(LogFormatter.class);
    
    public static void logStep(String message) {
        String formattedMessage = formatStepMessage(message);
        logger.info(formattedMessage);
        System.out.println(formattedMessage);
    }
    
    public static void logError(String message) {
        String formattedMessage = formatStepMessage("❌ ERRO: " + message);
        logger.error(formattedMessage);
        System.err.println(formattedMessage);
    }
    
    public static void logWarning(String message) {
        String formattedMessage = formatStepMessage("⚠️ AVISO: " + message);
        logger.warn(formattedMessage);
        System.out.println(formattedMessage);
    }
    
    public static void logSuccess(String message) {
        String formattedMessage = formatStepMessage("✅ " + message);
        logger.info(formattedMessage);
        System.out.println(formattedMessage);
    }
    
    public static void logRequest(String method, String url, String body) {
        logStep("Enviando requisição " + method + " para: " + url);
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
    
    private static String formatStepMessage(String message) {
        return "[INFO] " + DataUtils.getHoraAtual() + " | '-- " + message;
    }
    
    public static void logScenarioStart(String scenarioName) {
        logStep("+-- " + scenarioName);
    }
    
    public static void logScenarioEnd(String result) {
        logStep("'-- " + result);
        logStep("");
    }
}
```

### Passo 4: Classe DataUtils (Utilitários de Data/Hora)

**Arquivo: `src/main/java/org/yourcompany/utils/DataUtils.java`**
```java
package org.yourcompany.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {
    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    public static String getHoraAtual() {
        return LocalDateTime.now().format(HORA_FORMATTER);
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

### Passo 5: Classe EnvironmentConfig (Configuração)

**Arquivo: `src/main/java/org/yourcompany/config/EnvironmentConfig.java`**
```java
package org.yourcompany.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentConfig {
    private static final Logger logger = LogManager.getLogger(EnvironmentConfig.class);
    private static final String CONFIG_FILE = "src/test/resources/config.properties";
    
    public static void loadConfig() {
        try {
            Properties props = new Properties();
            FileInputStream file = new FileInputStream(CONFIG_FILE);
            props.load(file);
            file.close();
            
            // Carregar configurações específicas do seu projeto
            String baseUrl = props.getProperty("api.base.url");
            String timeout = props.getProperty("request.timeout", "30");
            
            // Validar configurações obrigatórias
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new RuntimeException("URL base da API não encontrada no arquivo de configuração");
            }
            
            // Configurar propriedades do sistema
            System.setProperty("api.base.url", baseUrl);
            System.setProperty("request.timeout", timeout);
            
            logger.info("Configuração carregada com sucesso");
            
        } catch (IOException e) {
            String errorMessage = "Erro ao carregar arquivo de configuração: " + e.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
```

### Passo 6: Hooks de Teste

**Arquivo: `src/test/java/org/yourcompany/hooks/TestHooks.java`**
```java
package org.yourcompany.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.yourcompany.core.Context;
import org.yourcompany.core.LogFormatter;

public class TestHooks {
    
    @Before
    public void beforeScenario(Scenario scenario) {
        String featureName = scenario.getUri().getPath().contains("/") ? 
            scenario.getUri().getPath().substring(scenario.getUri().getPath().lastIndexOf("/") + 1) : 
            scenario.getUri().getPath();
        
        Context.startContext(featureName, scenario.getName());
        LogFormatter.logScenarioStart(scenario.getName());
    }
    
    @After
    public void afterScenario(Scenario scenario) {
        String result = scenario.isFailed() ? "FAILED" : "PASSED";
        LogFormatter.logScenarioEnd(result);
        Context.clearContext();
    }
}
```

### Passo 7: Runner Principal

**Arquivo: `src/test/java/org/yourcompany/RunnerTestApi.java`**
```java
package org.yourcompany;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.yourcompany.config.EnvironmentConfig;
import org.yourcompany.core.Context;
import org.yourcompany.core.LogFormatter;
import org.yourcompany.utils.DataUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"org.yourcompany"},
    tags = "@API",
    plugin = {
        "json:reports/reports.json",
        "html:reports/cucumber-html-report.html"
    },
    publish = false,
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    monochrome = true
)
public class RunnerTestApi {
    private static String data;
    private static Instant startTime = Instant.now();
    
    @BeforeClass
    public static void setup() {
        EnvironmentConfig.loadConfig();
        data = DataUtils.getDataHoraParaArquivo();
        LogFormatter.logStep("Iniciando execução dos testes de API");
    }
    
    @AfterClass
    public static void after() {
        long totalDuration = Duration.between(startTime, Instant.now()).toMillis();
        Context.finishedContext(totalDuration);
        
        try {
            // Adicionar delay para evitar conflitos de file lock
            Thread.sleep(1000);
            
            // Validar se data não é null antes de renomear
            if (data != null && !data.trim().isEmpty()) {
                renameFile("reports/reports.json", "reports/" + data + ".json");
                LogFormatter.logSuccess("Relatório gerado: reports/" + data + ".json");
            } else {
                LogFormatter.logWarning("Nome do arquivo de relatório não foi definido, mantendo reports.json");
            }
        } catch (Exception e) {
            LogFormatter.logError("Erro ao processar relatórios: " + e.getMessage());
        }
    }
    
    private static void renameFile(String oldPath, String newPath) throws Exception {
        Path source = Paths.get(oldPath);
        Path target = Paths.get(newPath);
        
        // Verificar se o arquivo fonte existe
        if (!Files.exists(source)) {
            LogFormatter.logWarning("Arquivo de relatório não encontrado: " + oldPath);
            return;
        }
        
        // Implementar lógica de retry para lidar com file locks
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                LogFormatter.logStep("Arquivo renomeado com sucesso: " + oldPath + " -> " + newPath);
                return;
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw e;
                }
                LogFormatter.logStep("Tentativa " + (i + 1) + " falhou, aguardando...");
                Thread.sleep(2000);
            }
        }
    }
}
```

### Passo 8: Arquivo de Configuração

**Arquivo: `src/test/resources/config.properties`**
```properties
# ==========================================
# CONFIGURAÇÃO DO PROJETO DE AUTOMAÇÃO
# ==========================================

# Configuração da API
api.base.url=https://api.example.com
request.timeout=30

# Configurações de Log
log.level=INFO
log.file.path=log/automation.log

# Configurações de Relatório
report.output.dir=reports
report.format=json,html

# Configurações de Ambiente
environment=qa
browser=chrome
headless=false

# ==========================================
# INSTRUÇÕES DE CONFIGURAÇÃO
# ==========================================
# 1. Configure a URL base da sua API
# 2. Ajuste timeouts conforme necessário
# 3. Configure o ambiente de execução
# 4. Ajuste configurações de browser se aplicável
```

## 🚀 Como Usar

### 1. Estrutura de Features
```gherkin
Feature: Exemplo de Feature

@API
Scenario: Teste de exemplo
  Given que eu tenho um endpoint configurado
  When eu faço uma requisição GET
  Then eu recebo status code 200
```

### 2. Step Definitions
```java
package org.yourcompany.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.yourcompany.core.LogFormatter;

public class ApiSteps {
    
    @Given("que eu tenho um endpoint configurado")
    public void queEuTenhoUmEndpointConfigurado() {
        LogFormatter.logStep("Configurando endpoint para teste");
        // Implementação do step
    }
    
    @When("eu faço uma requisição GET")
    public void euFacoUmaRequisicaoGET() {
        LogFormatter.logStep("Enviando requisição GET");
        // Implementação do step
    }
    
    @Then("eu recebo status code {int}")
    public void euReceboStatusCode(int statusCode) {
        LogFormatter.logStep("Validando status code: " + statusCode);
        // Implementação do step
        LogFormatter.logSuccess("PASSED");
    }
}
```

### 3. Execução
```bash
# Compilar o projeto
mvn clean compile

# Executar testes
mvn test -Dtest=RunnerTestApi

# Executar com tags específicas
mvn test -Dtest=RunnerTestApi -Dcucumber.filter.tags="@API"
```

## 📊 Exemplo de Saída

```
==================================== Execucao: 2025-07-20 13:26:09 ====================================
Feature: exemplo
eedbf1e2-dae8-4fed-959d-a4f622297a56

[INFO] 13:26:09 +-- Teste de exemplo
[INFO] 13:26:09 | '-- Configurando endpoint para teste
[INFO] 13:26:10 | '-- Enviando requisição GET
[INFO] 13:26:10 | '-- Validando status code: 200
[INFO] 13:26:10 | '-- ✅ PASSED

========================================== Resumo da Execucao ==========================================
Duration: 1s
```

## 🔧 Customizações Avançadas

### 1. Integração com Xray/Jira
```java
// Adicionar ao pom.xml
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20240303</version>
</dependency>

// Implementar upload de relatórios
public class XrayController {
    public void uploadReportToXray(String reportPath) {
        // Implementação do upload
    }
}
```

### 2. Screenshots para Testes Web
```java
public class ScreenshotUtils {
    public static void takeScreenshot(String name) {
        // Implementação de screenshot
        LogFormatter.logStep("Screenshot salvo: " + name);
    }
}
```

### 3. Logs de Performance
```java
public class PerformanceLogger {
    public static void logResponseTime(String operation, long timeMs) {
        LogFormatter.logStep("Tempo de resposta " + operation + ": " + timeMs + "ms");
    }
}
```

## 📝 Checklist de Implementação

- [ ] Configurar dependências no pom.xml
- [ ] Criar estrutura de diretórios
- [ ] Implementar classes core (Context, LogFormatter, DataUtils)
- [ ] Configurar log4j2.xml
- [ ] Criar EnvironmentConfig
- [ ] Implementar TestHooks
- [ ] Configurar Runner principal
- [ ] Criar arquivo config.properties
- [ ] Implementar step definitions com logs
- [ ] Testar execução completa
- [ ] Configurar integração com relatórios (opcional)
- [ ] Documentar uso para a equipe

## 🎯 Benefícios da Implementação

1. **Logs Estruturados**: Fácil leitura e análise
2. **Identificação Única**: Cada cenário tem um ID único
3. **Debugging Facilitado**: Logs detalhados de cada passo
4. **Relatórios Automáticos**: Geração automática de relatórios
5. **Integração**: Compatível com ferramentas de CI/CD
6. **Manutenibilidade**: Código organizado e reutilizável

Este sistema fornece uma base sólida para logs em projetos de automação, sendo facilmente adaptável para diferentes tipos de testes (API, Web, Mobile). 