# 📚 **CLASSES DO FRAMEWORK - COMENTÁRIOS DETALHADOS**

## 🎯 **VISÃO GERAL DO FRAMEWORK**

Este documento detalha as **classes principais** do framework de automação de testes API, incluindo **controllers**, **managers**, **models**, **sheets**, **steps**, **utils** e **runners**. É o coração do sistema de automação que integra todas as funcionalidades.

---

## 🏃 **RUNNERS - Pontos de Entrada**

### **Main.java - Preparação de Ambiente**

**📍 Localização**: `src/main/java/org/br/com/Main.java`

```java
package org.br.com;

import br.com.geradormassa.service.GeradorService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ============================================================================
 * CLASSE PRINCIPAL: Main
 * ============================================================================
 * 
 * PROPÓSITO:
 * Ponto de entrada para executar tarefas manuais de setup e preparação
 * do ambiente de testes. Responsável por gerar massa de dados e
 * configurar recursos necessários antes da execução dos testes.
 * 
 * QUANDO USAR:
 * - Configuração inicial do projeto
 * - Geração de massa de dados para testes
 * - Setup de ambiente antes da execução
 * - Preparação de recursos necessários
 * 
 * COMO EXECUTAR:
 * java -cp target/classes org.br.com.Main
 * ou
 * mvn exec:java -Dexec.mainClass="org.br.com.Main"
 * 
 * MOTIVOS DE USO:
 * 1. Automação: Setup automático do ambiente
 * 2. Preparação: Gera dados necessários para testes
 * 3. Isolamento: Separa setup de execução de testes
 * 4. Conveniência: Uma única classe para preparação
 * ============================================================================
 */
public class Main {

    /**
     * MÉTODO: main(String[] args)
     * 
     * PROPÓSITO:
     * Ponto de entrada principal da aplicação. Coordena a execução
     * de todas as tarefas de preparação com tratamento de erros robusto.
     * 
     * PARÂMETROS:
     * @param args - Argumentos da linha de comando (não utilizados)
     * 
     * QUANDO CHAMAR:
     * - Antes de executar suítes de teste
     * - Para recriar massa de dados
     * - Em pipelines de CI/CD para setup
     * - Manualmente quando necessário
     * 
     * FLUXO DE EXECUÇÃO:
     * 1. Exibe mensagem de início
     * 2. Chama prepararAmbienteDeTeste()
     * 3. Trata exceções de forma elegante
     * 4. Exibe resultado final (sucesso/erro)
     * 
     * TRATAMENTO DE ERROS:
     * - Try-catch abrangente
     * - Mensagens claras de erro
     * - Stack trace para debug
     * - Finalização controlada
     * 
     * SAÍDA ESPERADA:
     * "--- INICIANDO SCRIPT DE PREPARAÇÃO DE AMBIENTE ---"
     * "Iniciando a geração de massa para o teste..."
     * "Arquivo de saída: /caminho/completo/massaDeTeste.csv"
     * "Massa de dados pronta para ser usada!"
     * "--- SCRIPT FINALIZADO COM SUCESSO! ---"
     */
    public static void main(String[] args) {
        System.out.println("--- INICIANDO SCRIPT DE PREPARAÇÃO DE AMBIENTE ---");
        try {
            prepararAmbienteDeTeste();
            System.out.println("\n--- SCRIPT FINALIZADO COM SUCESSO! ---");
        } catch (Exception e) {
            System.err.println("\n--- OCORREU UM ERRO DURANTE A EXECUÇÃO DO SCRIPT ---");
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO: prepararAmbienteDeTeste()
     * 
     * PROPÓSITO:
     * Executa todas as tarefas necessárias para preparar o ambiente
     * de testes, incluindo geração de massa de dados e criação de
     * estruturas de diretórios.
     * 
     * EXCEÇÕES:
     * @throws IOException - Em caso de erro de I/O
     * 
     * TAREFAS EXECUTADAS:
     * 1. Instancia GeradorService
     * 2. Define parâmetros de geração
     * 3. Cria estrutura de diretórios
     * 4. Gera arquivo CSV com dados de teste
     * 
     * CONFIGURAÇÕES:
     * - Quantidade de usuários: 15
     * - Diretório de saída: "output"
     * - Nome do arquivo: "massaDeTeste.csv"
     * 
     * BOAS PRÁTICAS IMPLEMENTADAS:
     * - Método static para chamada direta
     * - API Path para manipulação segura de arquivos
     * - Criação automática de diretórios
     * - Verificação de pré-condições
     * 
     * INTEGRAÇÃO:
     * Utiliza biblioteca externa 'gerador-massa-unificado'
     * para criação de dados realistas e variados.
     */
    public static void prepararAmbienteDeTeste() throws IOException {
        GeradorService gerador = new GeradorService();

        int quantidadeDeUsuarios = 15;
        String diretorioSaida = "output";
        String nomeArquivo = "massaDeTeste.csv";

        Path caminhoDoArquivo = Paths.get(diretorioSaida, nomeArquivo);
        Files.createDirectories(caminhoDoArquivo.getParent());

        System.out.println("Iniciando a geração de massa para o teste...");
        System.out.println("Arquivo de saída: " + caminhoDoArquivo.toAbsolutePath());

        gerador.gerarMassaUnificada(quantidadeDeUsuarios, caminhoDoArquivo.toString());

        System.out.println("Massa de dados pronta para ser usada!");
    }
}
```

### **RunnerTestApi.java - Executor Principal de Testes**

**📍 Localização**: `src/main/java/org/br/com/RunnerTestApi.java`

```java
package org.br.com;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.br.com.core.support.Context;
import org.br.com.test.utils.DataUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * ============================================================================
 * RUNNER PRINCIPAL: RunnerTestApi
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe principal que executa todos os testes automatizados da API.
 * Configura Cucumber, gerencia logs, relatórios e ciclo de vida
 * da execução de testes.
 * 
 * CONFIGURAÇÃO CUCUMBER:
 * - Features: src/main/resources/features
 * - Glue: org.br.com.test.steps, org.br.com.test.utils
 * - Plugins: JSON e HTML reports
 * - Tags: @api (executa apenas testes marcados)
 * 
 * RESPONSABILIDADES:
 * - Configurar ambiente antes dos testes
 * - Executar suíte completa de testes
 * - Gerar relatórios automaticamente
 * - Gerenciar arquivos de log
 * - Limpeza e finalização
 * ============================================================================
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/main/resources/features",
    glue = {"org.br.com.test.steps", "org.br.com.test.utils", "org.br.com.test.utils.hooks"},
    plugin = {
        "json:target/reports/reports.json",
        "html:target/reports/cucumber-html-report.html"
    },
    tags = "@api",
    monochrome = false,
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    stepNotifications = false
)
public class RunnerTestApi {

    private static Instant startTime;
    private static String currentDate;

    /**
     * MÉTODO: beforeClass()
     * 
     * PROPÓSITO:
     * Executado UMA VEZ antes de todos os testes.
     * Configura ambiente, cria estruturas necessárias e
     * inicializa contadores e logs.
     * 
     * AÇÕES EXECUTADAS:
     * 1. Marca tempo de início da execução
     * 2. Obtém data atual para nomeação de arquivos
     * 3. Cria arquivos de log com timestamp
     * 4. Reseta contadores do Context
     * 
     * ESTRUTURA DE LOGS CRIADA:
     * - target/log/execution-YYYY-MM-DD.log
     * - target/log/automation-YYYY-MM-DD.log
     * 
     * VANTAGENS:
     * - Logs organizados por data
     * - Não sobrescreve execuções anteriores
     * - Fácil localização de logs específicos
     */
    @BeforeClass
    public static void beforeClass() {
        startTime = Instant.now();
        currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        createLogFilesWithDate();
        Context.resetCounters();
    }

    /**
     * MÉTODO: afterClass()
     * 
     * PROPÓSITO:
     * Executado UMA VEZ após todos os testes.
     * Finaliza execução, gera relatórios finais e
     * organiza arquivos de saída.
     * 
     * AÇÕES EXECUTADAS:
     * 1. Calcula duração total da execução
     * 2. Finaliza contexto com estatísticas
     * 3. Renomeia relatórios com timestamp
     * 4. Organiza arquivos de log
     * 
     * RELATÓRIOS GERADOS:
     * - target/reports/YYYYMMDD_HHMMSS.json
     * - target/reports/cucumber-html-report.html
     * 
     * BENEFÍCIOS:
     * - Relatórios únicos por execução
     * - Histórico preservado
     * - Estatísticas de performance
     */
    @AfterClass
    public static void afterClass() {
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        
        Context.finishedContext(duration);
        
        // Renomear relatórios com timestamp
        File reportFile = new File("target/reports/reports.json");
        if (reportFile.exists()) {
            String timestamp = DataUtils.getDataHoraAtual().replace(":", "").replace("-", "").replace(" ", "_");
            String newFileName = "target/reports/" + timestamp + ".json";
            File newFile = new File(newFileName);
            
            if (reportFile.renameTo(newFile)) {
                // Relatório renomeado com sucesso
            }
        }
        
        renameLogFilesWithDate();
    }

    /**
     * MÉTODO: createLogFilesWithDate()
     * 
     * PROPÓSITO:
     * Cria estrutura de arquivos de log com data atual.
     * Garante que logs são organizados cronologicamente.
     * 
     * ESTRUTURA CRIADA:
     * target/log/
     * ├── execution-YYYY-MM-DD.log
     * └── automation-YYYY-MM-DD.log
     * 
     * TRATAMENTO DE ERROS:
     * - Cria diretórios se não existirem
     * - Não sobrescreve arquivos existentes
     * - Trata exceções de I/O graciosamente
     */
    private static void createLogFilesWithDate() {
        try {
            Path logDir = Paths.get("target/log");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            String executionLogFile = "target/log/execution-" + currentDate + ".log";
            String automationLogFile = "target/log/automation-" + currentDate + ".log";
            
            Path executionPath = Paths.get(executionLogFile);
            Path automationPath = Paths.get(automationLogFile);
            
            if (!Files.exists(executionPath)) {
                Files.createFile(executionPath);
            }
            if (!Files.exists(automationPath)) {
                Files.createFile(automationPath);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivos de log: " + e.getMessage());
        }
    }

    /**
     * MÉTODO: renameLogFilesWithDate()
     * 
     * PROPÓSITO:
     * Organiza arquivos de log finais com nomenclatura por data.
     * Consolida logs temporários em arquivos definitivos.
     * 
     * LÓGICA:
     * 1. Verifica se existem logs temporários
     * 2. Se arquivo com data já existe, faz append
     * 3. Senão, renomeia arquivo temporário
     * 4. Remove arquivos temporários
     * 
     * BENEFÍCIOS:
     * - Preserva logs de múltiplas execuções no mesmo dia
     * - Organização cronológica automática
     * - Não perde informações
     */
    private static void renameLogFilesWithDate() {
        try {
            File executionLog = new File("target/log/execution.log");
            File automationLog = new File("target/log/automation.log");
            
            if (executionLog.exists()) {
                File newExecutionLog = new File("target/log/execution-" + currentDate + ".log");
                if (newExecutionLog.exists()) {
                    Files.write(newExecutionLog.toPath(), 
                               Files.readAllBytes(executionLog.toPath()), 
                               java.nio.file.StandardOpenOption.APPEND);
                    executionLog.delete();
                } else {
                    executionLog.renameTo(newExecutionLog);
                }
            }
            
            if (automationLog.exists()) {
                File newAutomationLog = new File("target/log/automation-" + currentDate + ".log");
                if (newAutomationLog.exists()) {
                    Files.write(newAutomationLog.toPath(), 
                               Files.readAllBytes(automationLog.toPath()), 
                               java.nio.file.StandardOpenOption.APPEND);
                    automationLog.delete();
                } else {
                    automationLog.renameTo(newAutomationLog);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao renomear arquivos de log: " + e.getMessage());
        }
    }
}
```

---

## 🧠 **MANAGERS - Gerenciamento de Estado**

### **TokenManager.java - Gerenciador de Tokens**

**📍 Localização**: `src/main/java/org/br/com/test/manager/TokenManager.java`

```java
package org.br.com.test.manager;

/**
 * ============================================================================
 * GERENCIADOR: TokenManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia tokens de autenticação e IDs de usuários de forma thread-safe.
 * Permite compartilhamento seguro de credenciais entre diferentes steps
 * e cenários de teste.
 * 
 * THREAD-SAFETY:
 * Utiliza ThreadLocal para garantir isolamento entre threads paralelas.
 * Cada thread mantém sua própria cópia dos tokens.
 * 
 * QUANDO USAR:
 * - Armazenar tokens após login
 * - Compartilhar tokens entre steps
 * - Manter user ID para operações específicas
 * - Limpeza entre cenários
 * 
 * MOTIVOS DE USO:
 * 1. Centralização: Ponto único para tokens
 * 2. Thread-Safety: Isolamento entre execuções
 * 3. Conveniência: Acesso fácil em qualquer lugar
 * 4. Limpeza: Remoção controlada de dados
 * ============================================================================
 */
public class TokenManager {

    /**
     * ATRIBUTO: token
     * 
     * PROPÓSITO: Armazena token de autenticação (JWT, Bearer, etc.)
     * TIPO: ThreadLocal<String>
     * THREAD-SAFETY: Isolado por thread
     * USO: Autenticação em requisições HTTP
     */
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    /**
     * ATRIBUTO: userId
     * 
     * PROPÓSITO: Armazena ID do usuário autenticado
     * TIPO: ThreadLocal<String>
     * THREAD-SAFETY: Isolado por thread
     * USO: Operações específicas do usuário
     */
    private static final ThreadLocal<String> userId = new ThreadLocal<>();

    /**
     * MÉTODO: getToken()
     * 
     * PROPÓSITO:
     * Retorna o token de autenticação atual da thread.
     * Usado para incluir em headers de requisições HTTP.
     * 
     * RETORNO:
     * @return String - Token atual ou null se não definido
     * 
     * QUANDO USAR:
     * - Incluir em headers Authorization
     * - Validar se usuário está autenticado
     * - Logs de debug (mascarado)
     * 
     * EXEMPLO:
     * String currentToken = TokenManager.getToken();
     * if (currentToken != null) {
     *     given().header("Authorization", "Bearer " + currentToken);
     * }
     */
    public static String getToken() {
        return token.get();
    }

    /**
     * MÉTODO: setToken(String tk)
     * 
     * PROPÓSITO:
     * Armazena novo token de autenticação para a thread atual.
     * Chamado após login bem-sucedido.
     * 
     * PARÂMETROS:
     * @param tk - Token de autenticação a ser armazenado
     * 
     * QUANDO USAR:
     * - Após login bem-sucedido
     * - Renovação de tokens
     * - Mudança de usuário no mesmo cenário
     * 
     * EXEMPLO:
     * Response loginResponse = given().body(loginRequest).post("/login");
     * String token = loginResponse.jsonPath().getString("token");
     * TokenManager.setToken(token);
     */
    public static void setToken(String tk) {
        token.set(tk);
    }

    /**
     * MÉTODO: getUserId()
     * 
     * PROPÓSITO:
     * Retorna ID do usuário autenticado atual.
     * Útil para operações que requerem user ID específico.
     * 
     * RETORNO:
     * @return String - ID do usuário ou null se não definido
     * 
     * QUANDO USAR:
     * - Operações CRUD específicas do usuário
     * - Validações de ownership
     * - Logs e auditoria
     * 
     * EXEMPLO:
     * String currentUserId = TokenManager.getUserId();
     * given().pathParam("userId", currentUserId).get("/users/{userId}");
     */
    public static String getUserId() {
        return userId.get();
    }

    /**
     * MÉTODO: setUserId(String id)
     * 
     * PROPÓSITO:
     * Armazena ID do usuário autenticado.
     * Geralmente chamado junto com setToken().
     * 
     * PARÂMETROS:
     * @param id - ID do usuário a ser armazenado
     * 
     * QUANDO USAR:
     * - Após login bem-sucedido
     * - Criação de novo usuário
     * - Mudança de contexto de usuário
     * 
     * EXEMPLO:
     * Response loginResponse = given().body(loginRequest).post("/login");
     * String token = loginResponse.jsonPath().getString("token");
     * String userId = loginResponse.jsonPath().getString("user.id");
     * TokenManager.setToken(token);
     * TokenManager.setUserId(userId);
     */
    public static void setUserId(String id) {
        userId.set(id);
    }

    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove todos os dados armazenados na thread atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
     * 
     * QUANDO USAR:
     * - Final de cada cenário (@After hook)
     * - Limpeza entre testes
     * - Logout de usuário
     * - Reset de estado
     * 
     * IMPORTÂNCIA:
     * Método ESSENCIAL para manter isolamento entre testes.
     * Falha em chamar pode causar interferência entre cenários.
     * 
     * EXEMPLO:
     * @After
     * public void limpezaAposCenario() {
     *     TokenManager.remove();
     *     UsuarioManager.remove();
     *     // outras limpezas...
     * }
     */
    public static void remove() {
        token.remove();
        userId.remove();
    }
}
```

### **UsuarioManager.java - Gerenciador de Dados de Usuário**

**📍 Localização**: `src/main/java/org/br/com/test/manager/UsuarioManager.java`

```java
package org.br.com.test.manager;

import org.br.com.test.model.request.UsuarioRequest;

/**
 * ============================================================================
 * GERENCIADOR: UsuarioManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados completos de usuários de forma thread-safe.
 * Armazena informações detalhadas para reutilização entre steps
 * e validações em cenários de teste.
 * 
 * DADOS GERENCIADOS:
 * - Email do usuário
 * - Senha para autenticação
 * - ID único do usuário
 * - Nome completo
 * - Nome de usuário/username
 * 
 * THREAD-SAFETY:
 * Cada campo usa ThreadLocal para isolamento completo.
 * Permite execução paralela sem interferência.
 * ============================================================================
 */
public class UsuarioManager {

    // ThreadLocal fields para isolamento por thread
    private static ThreadLocal<String> emailUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> senhaUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> idUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeCompletoUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeUsuario = new ThreadLocal<String>();

    /**
     * MÉTODO: getEmailUsuario()
     * 
     * PROPÓSITO:
     * Retorna email do usuário atual da thread.
     * Usado para login, validações e identificação.
     * 
     * RETORNO:
     * @return String - Email do usuário
     * 
     * USOS COMUNS:
     * - Login de usuário
     * - Validação de dados retornados
     * - Identificação em logs
     * - Verificação de ownership
     */
    public static String getEmailUsuario() {
        return emailUsuario.get();
    }

    /**
     * MÉTODO: setEmailUsuario(String tk)
     * 
     * PROPÓSITO:
     * Armazena email do usuário para uso posterior.
     * Chamado após criar ou carregar dados de usuário.
     * 
     * PARÂMETROS:
     * @param tk - Email do usuário (nome genérico mantido por compatibilidade)
     * 
     * QUANDO USAR:
     * - Após criar usuário
     * - Carregar dados de planilha
     * - Setup de cenário de teste
     */
    public static void setEmailUsuario(String tk) {
        emailUsuario.set(tk);
    }

    /**
     * MÉTODO: getSenhaUsuario()
     * 
     * PROPÓSITO:
     * Retorna senha do usuário atual.
     * ATENÇÃO: Considerar mascaramento em logs.
     * 
     * RETORNO:
     * @return String - Senha do usuário
     * 
     * SEGURANÇA:
     * - Nunca logar este valor
     * - Usar apenas para autenticação
     * - Considerar criptografia se necessário
     */
    public static String getSenhaUsuario() {
        return senhaUsuario.get();
    }

    /**
     * MÉTODO: setSenhaUsuario(String tk)
     * 
     * PROPÓSITO:
     * Armazena senha do usuário para autenticação.
     * 
     * PARÂMETROS:
     * @param tk - Senha do usuário
     * 
     * SEGURANÇA:
     * - Pode aceitar senhas criptografadas
     * - Validar força da senha se necessário
     * - Limpar da memória quando possível
     */
    public static void setSenhaUsuario(String tk) {
        senhaUsuario.set(tk);
    }

    /**
     * MÉTODO: getIdUsuario()
     * 
     * PROPÓSITO:
     * Retorna ID único do usuário no sistema.
     * Usado para operações CRUD específicas.
     * 
     * RETORNO:
     * @return String - ID único do usuário
     * 
     * USOS:
     * - Path parameters em URLs
     * - Validações de ownership
     * - Relacionamentos entre entidades
     * - Auditoria e logs
     */
    public static String getIdUsuario() {
        return idUsuario.get();
    }

    /**
     * MÉTODO: setIdUsuario(String tk)
     * 
     * PROPÓSITO:
     * Armazena ID único do usuário após criação ou login.
     * 
     * PARÂMETROS:
     * @param tk - ID único do usuário
     * 
     * MOMENTO TÍPICO:
     * Response response = post("/usuarios");
     * String id = response.jsonPath().getString("id");
     * UsuarioManager.setIdUsuario(id);
     */
    public static void setIdUsuario(String tk) {
        idUsuario.set(tk);
    }

    /**
     * MÉTODO: getNomeCompletoUsuario()
     * 
     * PROPÓSITO:
     * Retorna nome completo do usuário cadastrado/logado.
     * Usado para validações e preenchimento de formulários.
     * 
     * RETORNO:
     * @return String - Nome completo do usuário
     * 
     * EXEMPLOS DE USO:
     * - Validar dados em perfil
     * - Preencher campos de autor
     * - Exibir informações de usuário
     * - Validações de resposta
     */
    public static String getNomeCompletoUsuario() {
        return nomeCompletoUsuario.get();
    }

    /**
     * MÉTODO: setNomeCompletoUsuario(String nome)
     * 
     * PROPÓSITO:
     * Armazena nome completo do usuário.
     * 
     * PARÂMETROS:
     * @param nome - Nome completo do usuário
     * 
     * VALIDAÇÕES RECOMENDADAS:
     * - Verificar se não é null ou vazio
     * - Validar formato se necessário
     * - Considerar normalização (trim, case)
     */
    public static void setNomeCompletoUsuario(String nome) {
        nomeCompletoUsuario.set(nome);
    }

    /**
     * MÉTODO: getNomeUsuario()
     * 
     * PROPÓSITO:
     * Retorna nome de usuário/username.
     * Diferente do nome completo, é o identificador único textual.
     * 
     * RETORNO:
     * @return String - Nome de usuário (username)
     * 
     * DIFERENÇA:
     * - nomeCompleto: "João da Silva"
     * - nomeUsuario: "joao.silva" ou "jsilva"
     */
    public static String getNomeUsuario() {
        return nomeUsuario.get();
    }

    /**
     * MÉTODO: setNomeUsuario(String nome)
     * 
     * PROPÓSITO:
     * Armazena nome de usuário/username.
     * 
     * PARÂMETROS:
     * @param nome - Nome de usuário único
     * 
     * REGRAS TÍPICAS:
     * - Sem espaços
     * - Caracteres especiais limitados
     * - Case-sensitive ou não (depende do sistema)
     */
    public static void setNomeUsuario(String nome) {
        nomeUsuario.set(nome);
    }

    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove TODOS os dados de usuário da thread atual.
     * CRÍTICO para isolamento entre cenários.
     * 
     * CAMPOS LIMPOS:
     * - emailUsuario
     * - senhaUsuario  
     * - idUsuario
     * - nomeCompletoUsuario
     * - nomeUsuario
     * 
     * QUANDO CHAMAR:
     * - @After hooks
     * - Limpeza entre cenários
     * - Mudança de usuário
     * - Reset de teste
     * 
     * IMPORTÂNCIA:
     * Falha em chamar pode causar:
     * - Dados de usuário incorretos
     * - Interferência entre testes
     * - Falsos positivos/negativos
     * - Vazamento de dados sensíveis
     */
    public static void remove() {
        emailUsuario.remove();
        senhaUsuario.remove();
        idUsuario.remove();
        nomeCompletoUsuario.remove();
        nomeUsuario.remove();
    }

    /**
     * MÉTODO: getUsuarioAtual()
     * 
     * PROPÓSITO:
     * Retorna objeto UsuarioRequest com todos os dados atuais.
     * Conveniência para criar requests completos rapidamente.
     * 
     * RETORNO:
     * @return UsuarioRequest - Objeto com todos os dados atuais
     * 
     * QUANDO USAR:
     * - Criar requests de atualização
     * - Validar dados completos
     * - Duplicar usuário com modificações
     * - Debug e logging
     * 
     * EXEMPLO:
     * UsuarioRequest usuario = UsuarioManager.getUsuarioAtual();
     * usuario.setSenha("novaSenha123");
     * given().body(usuario).put("/usuarios/" + usuario.getId());
     * 
     * VANTAGEM:
     * Evita múltiplas chamadas get() individuais.
     */
    public static UsuarioRequest getUsuarioAtual() {
        return UsuarioRequest.builder()
            .email(getEmailUsuario())
            .senha(getSenhaUsuario())
            .nomeCompleto(getNomeCompletoUsuario())
            .nomeUsuario(getNomeUsuario())
            .build();
    }
}
```

### **ArtigosManager.java - Gerenciador de Artigos**

**📍 Localização**: `src/main/java/org/br/com/test/manager/ArtigosManager.java`

```java
package org.br.com.test.manager;

import io.restassured.response.Response;

/**
 * ============================================================================
 * GERENCIADOR: ArtigosManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados de artigos e suas relações com autores e categorias.
 * Facilita cenários complexos que envolvem múltiplas entidades
 * relacionadas no sistema de CMS.
 * 
 * ENTIDADES RELACIONADAS:
 * - Artigo (entidade principal)
 * - Autor (usuário que criou)
 * - Categoria (classificação do artigo)
 * - Response (última resposta HTTP)
 * 
 * CENÁRIOS TÍPICOS:
 * - Criar artigo com autor e categoria específicos
 * - Validar relacionamentos entre entidades
 * - Operações CRUD completas em artigos
 * - Testes de integridade de dados
 * ============================================================================
 */
public class ArtigosManager {

    // ThreadLocal fields para dados do artigo
    private static final ThreadLocal<String> artigoId = new ThreadLocal<String>();
    private static final ThreadLocal<String> autorId = new ThreadLocal<String>();
    private static final ThreadLocal<String> categoriaId = new ThreadLocal<String>();
    private static final ThreadLocal<String> nomeCategoria = new ThreadLocal<String>();
    private static final ThreadLocal<String> nomeAutor = new ThreadLocal<String>();
    private static final ThreadLocal<Response> response = new ThreadLocal<Response>();

    /**
     * MÉTODO: getArtigoId()
     * 
     * PROPÓSITO:
     * Retorna ID único do artigo atual.
     * Usado em operações CRUD específicas do artigo.
     * 
     * RETORNO:
     * @return String - ID do artigo
     * 
     * USOS:
     * - Path parameter em URLs (/artigos/{id})
     * - Validações de ownership
     * - Relacionamentos com outras entidades
     * - Operações de update/delete
     */
    public static String getArtigoId() {
        return artigoId.get();
    }

    /**
     * MÉTODO: getAutorId()
     * 
     * PROPÓSITO:
     * Retorna ID do autor (usuário) do artigo.
     * Permite validar relacionamentos autor-artigo.
     * 
     * RETORNO:
     * @return String - ID do autor
     * 
     * RELAÇÃO:
     * Geralmente corresponde ao ID de um usuário
     * armazenado no UsuarioManager.
     */
    public static String getAutorId() {
        return autorId.get();
    }

    /**
     * MÉTODO: getCategoriaId()
     * 
     * PROPÓSITO:
     * Retorna ID da categoria do artigo.
     * Usado para validar classificação correta.
     * 
     * RETORNO:
     * @return String - ID da categoria
     * 
     * RELAÇÃO:
     * Corresponde ao ID de uma categoria
     * armazenada no CategoriaManager.
     */
    public static String getCategoriaId() {
        return categoriaId.get();
    }

    /**
     * MÉTODO: getNomeCategoria()
     * 
     * PROPÓSITO:
     * Retorna nome textual da categoria.
     * Útil para validações e logs legíveis.
     * 
     * RETORNO:
     * @return String - Nome da categoria
     * 
     * EXEMPLO:
     * "Tecnologia", "Esportes", "Política"
     */
    public static String getNomeCategoria() {
        return nomeCategoria.get();
    }

    /**
     * MÉTODO: getNomeAutor()
     * 
     * PROPÓSITO:
     * Retorna nome do autor do artigo.
     * Usado em validações e exibições.
     * 
     * RETORNO:
     * @return String - Nome do autor
     * 
     * PODE SER:
     * - Nome completo: "João da Silva"
     * - Username: "joao.silva"
     * - Depende da implementação da API
     */
    public static String getNomeAutor() {
        return nomeAutor.get();
    }

    /**
     * MÉTODO: getResponse()
     * 
     * PROPÓSITO:
     * Retorna última Response HTTP relacionada a artigos.
     * Permite validações detalhadas de status e dados.
     * 
     * RETORNO:
     * @return Response - Objeto Response do REST Assured
     * 
     * USOS:
     * - Validar status codes específicos
     * - Extrair dados adicionais da resposta
     * - Validar headers HTTP
     * - Debug de problemas
     * 
     * EXEMPLO:
     * Response resp = ArtigosManager.getResponse();
     * resp.then().statusCode(201);
     * String titulo = resp.jsonPath().getString("titulo");
     */
    public static Response getResponse() {
        return response.get();
    }

    // MÉTODOS SETTERS

    /**
     * MÉTODO: setArtigoId(String id)
     * 
     * PROPÓSITO:
     * Armazena ID do artigo após criação ou busca.
     * 
     * MOMENTO TÍPICO:
     * Response resp = post("/artigos");
     * String id = resp.jsonPath().getString("id");
     * ArtigosManager.setArtigoId(id);
     */
    public static void setArtigoId(String id) {
        artigoId.set(id);
    }

    /**
     * MÉTODO: setAutorId(String id)
     * 
     * PROPÓSITO:
     * Define autor do artigo.
     * Pode vir do UsuarioManager ou ser específico.
     * 
     * CENÁRIO COMUM:
     * String autorId = UsuarioManager.getIdUsuario();
     * ArtigosManager.setAutorId(autorId);
     */
    public static void setAutorId(String id) {
        autorId.set(id);
    }

    /**
     * MÉTODO: setCategoriaId(String id)
     * 
     * PROPÓSITO:
     * Define categoria do artigo.
     * Relaciona artigo com categoria existente.
     * 
     * CENÁRIO COMUM:
     * String catId = CategoriaManager.getCategoriaId();
     * ArtigosManager.setCategoriaId(catId);
     */
    public static void setCategoriaId(String id) {
        categoriaId.set(id);
    }

    /**
     * MÉTODO: setNomeCategoria(String nome)
     * 
     * PROPÓSITO:
     * Armazena nome textual da categoria para referência.
     */
    public static void setNomeCategoria(String nome) {
        nomeCategoria.set(nome);
    }

    /**
     * MÉTODO: setNomeAutor(String nome)
     * 
     * PROPÓSITO:
     * Armazena nome do autor para validações e logs.
     */
    public static void setNomeAutor(String nome) {
        nomeAutor.set(nome);
    }

    /**
     * MÉTODO: setResponse(Response resp)
     * 
     * PROPÓSITO:
     * Armazena última resposta HTTP para validações posteriores.
     * 
     * USO COMUM:
     * Response response = given().body(artigo).post("/artigos");
     * ArtigosManager.setResponse(response);
     * // Validações posteriores podem usar getResponse()
     */
    public static void setResponse(Response resp) {
        response.set(resp);
    }

    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove TODOS os dados de artigos da thread atual.
     * Essencial para limpeza entre cenários.
     * 
     * CAMPOS LIMPOS:
     * - artigoId
     * - autorId
     * - categoriaId
     * - nomeCategoria
     * - nomeAutor
     * - response
     * 
     * QUANDO CHAMAR:
     * - @After hooks
     * - Limpeza entre cenários
     * - Reset de estado de artigos
     * 
     * INTEGRAÇÃO:
     * Geralmente chamado junto com:
     * UsuarioManager.remove();
     * CategoriaManager.remove();
     * TokenManager.remove();
     */
    public static void remove() {
        artigoId.remove();
        autorId.remove();
        categoriaId.remove();
        nomeCategoria.remove();
        nomeAutor.remove();
        response.remove();
    }
}
```

### **CategoriaManager.java - Gerenciador de Categorias**

**📍 Localização**: `src/main/java/org/br/com/test/manager/CategoriaManager.java`

```java
package org.br.com.test.manager;

/**
 * ============================================================================
 * GERENCIADOR: CategoriaManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados de categorias do sistema CMS.
 * Facilita operações CRUD e relacionamentos entre
 * categorias e artigos em cenários de teste.
 * 
 * DADOS GERENCIADOS:
 * - ID único da categoria
 * - Nome da categoria
 * - Descrição detalhada
 * 
 * RELACIONAMENTOS:
 * Categorias são utilizadas por artigos, criando
 * hierarquia de classificação no sistema.
 * ============================================================================
 */
public class CategoriaManager {

    // ThreadLocal fields para dados da categoria
    private static ThreadLocal<String> categoriaId = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeCategoria = new ThreadLocal<String>();
    private static ThreadLocal<String> descricaoCategoria = new ThreadLocal<String>();

    /**
     * MÉTODO: getCategoriaId()
     * 
     * PROPÓSITO:
     * Retorna ID único da categoria atual.
     * Usado em operações CRUD e relacionamentos.
     * 
     * RETORNO:
     * @return String - ID da categoria
     * 
     * USOS COMUNS:
     * - Path parameters (/categorias/{id})
     * - Relacionar com artigos
     * - Operações de update/delete
     * - Validações de integridade
     */
    public static String getCategoriaId() {
        return categoriaId.get();
    }

    /**
     * MÉTODO: getNomeCategoria()
     * 
     * PROPÓSITO:
     * Retorna nome da categoria.
     * Usado para validações e exibições.
     * 
     * RETORNO:
     * @return String - Nome da categoria
     * 
     * EXEMPLOS:
     * - "Tecnologia"
     * - "Esportes"
     * - "Entretenimento"
     * - "Negócios"
     */
    public static String getNomeCategoria() {
        return nomeCategoria.get();
    }

    /**
     * MÉTODO: getDescricaoCategoria()
     * 
     * PROPÓSITO:
     * Retorna descrição detalhada da categoria.
     * Útil para validações de conteúdo completo.
     * 
     * RETORNO:
     * @return String - Descrição da categoria
     * 
     * EXEMPLOS:
     * - "Artigos sobre tecnologia, programação e inovação"
     * - "Notícias e análises do mundo esportivo"
     * - "Conteúdo de entretenimento e cultura pop"
     */
    public static String getDescricaoCategoria() {
        return descricaoCategoria.get();
    }

    // MÉTODOS SETTERS

    /**
     * MÉTODO: setCategoriaId(String id)
     * 
     * PROPÓSITO:
     * Armazena ID da categoria após criação ou busca.
     * 
     * MOMENTO TÍPICO:
     * Response response = post("/categorias");
     * String id = response.jsonPath().getString("id");
     * CategoriaManager.setCategoriaId(id);
     */
    public static void setCategoriaId(String id) {
        categoriaId.set(id);
    }

    /**
     * MÉTODO: setNomeCategoria(String nome)
     * 
     * PROPÓSITO:
     * Define nome da categoria para uso posterior.
     * 
     * VALIDAÇÕES RECOMENDADAS:
     * - Verificar se não é null ou vazio
     * - Validar unicidade se necessário
     * - Considerar normalização
     */
    public static void setNomeCategoria(String nome) {
        nomeCategoria.set(nome);
    }

    /**
     * MÉTODO: setDescricaoCategoria(String descricao)
     * 
     * PROPÓSITO:
     * Define descrição da categoria.
     * 
     * CARACTERÍSTICAS:
     * - Pode ser texto longo
     * - Opcional em algumas APIs
     * - Usado para contexto adicional
     */
    public static void setDescricaoCategoria(String descricao) {
        descricaoCategoria.set(descricao);
    }

    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove todos os dados de categoria da thread atual.
     * Necessário para isolamento entre cenários.
     * 
     * CAMPOS LIMPOS:
     * - categoriaId
     * - nomeCategoria
     * - descricaoCategoria
     * 
     * QUANDO CHAMAR:
     * - @After hooks
     * - Limpeza entre cenários
     * - Mudança de contexto de categoria
     * - Reset de teste
     * 
     * EXEMPLO DE USO EM HOOK:
     * @After
     * public void limpeza() {
     *     CategoriaManager.remove();
     *     ArtigosManager.remove();
     *     UsuarioManager.remove();
     *     TokenManager.remove();
     * }
     */
    public static void remove() {
        categoriaId.remove();
        nomeCategoria.remove();
        descricaoCategoria.remove();
    }
}
```

---

## 📊 **SHEETS - Leitura de Dados Excel**

### **ExcelDataReader.java - Leitor Avançado de Excel**

**📍 Localização**: `src/main/java/org/br/com/test/sheets/ExcelDataReader.java`

```java
package org.br.com.test.sheets;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ============================================================================
 * LEITOR AVANÇADO: ExcelDataReader
 * ============================================================================
 * 
 * PROPÓSITO:
 * Utilitário robusto para leitura de dados Excel usando Apache POI.
 * Implementa AutoCloseable para gerenciamento seguro de recursos
 * e oferece funcionalidades avançadas de busca e formatação.
 * 
 * VANTAGENS SOBRE FILLO:
 * - Melhor controle de tipos de dados
 * - Formatação inteligente de números
 * - Tratamento robusto de fórmulas
 * - Gerenciamento automático de recursos
 * - Mapeamento inteligente de colunas
 * 
 * RECURSOS:
 * - Busca por valor em qualquer coluna
 * - Mapeamento automático de headers
 * - Formatação inteligente de células
 * - Tratamento de datas e números
 * - Gestão segura de memória
 * ============================================================================
 */
public class ExcelDataReader implements AutoCloseable {

    /**
     * ATRIBUTO: workbook
     * 
     * PROPÓSITO: Representa arquivo Excel completo
     * TIPO: Workbook (Apache POI)
     * GESTÃO: Fechado automaticamente no close()
     */
    private final Workbook workbook;

    /**
     * ATRIBUTO: sheet
     * 
     * PROPÓSITO: Aba específica sendo processada
     * TIPO: Sheet (Apache POI)
     * USO: Contém dados da planilha atual
     */
    private final Sheet sheet;

    /**
     * ATRIBUTO: columnIndexes
     * 
     * PROPÓSITO: Mapa nome-da-coluna -> índice numérico
     * TIPO: Map<String, Integer>
     * VANTAGEM: Acesso por nome em vez de índice
     * EXEMPLO: {"nome" -> 0, "email" -> 1, "idade" -> 2}
     */
    private final Map<String, Integer> columnIndexes;

    /**
     * CONSTRUTOR: ExcelDataReader(String filePath, String sheetName)
     * 
     * PROPÓSITO:
     * Inicializa leitor para arquivo e aba específicos.
     * Valida parâmetros e cria mapeamento de colunas automaticamente.
     * 
     * PARÂMETROS:
     * @param filePath - Caminho completo para arquivo .xlsx
     * @param sheetName - Nome exato da aba a ser lida
     * 
     * EXCEÇÕES:
     * @throws IOException - Erro de leitura do arquivo
     * @throws IllegalArgumentException - Aba não encontrada
     * @throws NullPointerException - Parâmetros nulos
     * 
     * VALIDAÇÕES:
     * - Parâmetros não podem ser nulos
     * - Arquivo deve existir e ser acessível
     * - Aba deve existir no arquivo
     * - Recursos são liberados em caso de erro
     * 
     * EXEMPLO:
     * try (ExcelDataReader reader = new ExcelDataReader("dados/usuarios.xlsx", "TBL_USUARIOS")) {
     *     Map<String, String> dados = reader.getRowData("email", "teste@exemplo.com");
     * }
     */
    public ExcelDataReader(String filePath, String sheetName) throws IOException {
        Objects.requireNonNull(filePath, "O caminho do arquivo não pode ser nulo.");
        Objects.requireNonNull(sheetName, "O nome da aba não pode ser nulo.");

        FileInputStream fis = new FileInputStream(filePath);
        this.workbook = WorkbookFactory.create(fis);
        this.sheet = workbook.getSheet(sheetName);
        
        if (this.sheet == null) {
            workbook.close(); // Libera recursos em caso de erro
            throw new IllegalArgumentException("Aba '" + sheetName + "' não encontrada no arquivo: " + filePath);
        }
        
        this.columnIndexes = mapColumnHeaders();
    }

    /**
     * MÉTODO: mapColumnHeaders()
     * 
     * PROPÓSITO:
     * Cria mapeamento automático entre nomes de colunas e índices.
     * Permite acesso por nome em vez de posição numérica.
     * 
     * RETORNO:
     * @return Map<String, Integer> - Mapa nome -> índice
     * 
     * ALGORITMO:
     * 1. Lê primeira linha (row 0) como header
     * 2. Para cada célula com texto, mapeia nome -> índice
     * 3. Aplica trim() para remover espaços
     * 4. Ignora células vazias ou não-texto
     * 
     * VANTAGENS:
     * - Acesso intuitivo por nome
     * - Resistente a mudanças de ordem
     * - Facilita manutenção de planilhas
     * 
     * EXEMPLO RESULTADO:
     * {"nome" -> 0, "email" -> 1, "senha" -> 2, "ativo" -> 3}
     */
    private Map<String, Integer> mapColumnHeaders() {
        Map<String, Integer> indexes = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    indexes.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
                }
            }
        }
        
        return indexes;
    }

    /**
     * MÉTODO: getRowData(String lookupColumn, String lookupValue)
     * 
     * PROPÓSITO:
     * Busca linha que contém valor específico em coluna específica
     * e retorna TODOS os dados dessa linha como Map.
     * 
     * PARÂMETROS:
     * @param lookupColumn - Nome da coluna para busca
     * @param lookupValue - Valor a ser encontrado
     * 
     * RETORNO:
     * @return Map<String, String> - Todos dados da linha encontrada
     * 
     * ALGORITMO:
     * 1. Verifica se coluna de busca existe
     * 2. Itera por todas as linhas (exceto header)
     * 3. Compara valor da célula com valor procurado
     * 4. Se encontrar, extrai TODOS os dados da linha
     * 5. Retorna primeiro match encontrado
     * 
     * COMPORTAMENTO:
     * - Retorna Map vazio se não encontrar
     * - Para na primeira correspondência
     * - Ignora linhas vazias
     * - Trata células nulas como vazias
     * 
     * EXEMPLO:
     * // Buscar usuário por email
     * Map<String, String> usuario = reader.getRowData("email", "joao@teste.com");
     * String nome = usuario.get("nome");        // "João Silva"
     * String senha = usuario.get("senha");      // "senha123"
     * String ativo = usuario.get("ativo");      // "S"
     * 
     * CASOS DE USO:
     * - Buscar dados de teste específicos
     * - Localizar registros por identificador único
     * - Carregar configurações por ambiente
     * - Encontrar dados por critério personalizado
     */
    public Map<String, String> getRowData(String lookupColumn, String lookupValue) {
        Map<String, String> rowData = new HashMap<>();
        Integer lookupColumnIndex = columnIndexes.get(lookupColumn);

        if (lookupColumnIndex == null) {
            // Coluna não existe, retorna Map vazio
            return rowData;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(lookupColumnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String cellValue = getCellValueAsString(cell);

                if (lookupValue.equals(cellValue)) {
                    // Linha encontrada - extrair todos os dados
                    for (Map.Entry<String, Integer> header : columnIndexes.entrySet()) {
                        Cell dataCell = row.getCell(header.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        rowData.put(header.getKey(), getCellValueAsString(dataCell));
                    }
                    break; // Para após primeiro match
                }
            }
        }
        
        return rowData;
    }

    /**
     * MÉTODO: getCellValueAsString(Cell cell)
     * 
     * PROPÓSITO:
     * Converte qualquer tipo de célula Excel para String de forma inteligente.
     * Trata todos os tipos de dados possíveis com formatação adequada.
     * 
     * PARÂMETROS:
     * @param cell - Célula do Excel a ser convertida
     * 
     * RETORNO:
     * @return String - Valor formatado da célula
     * 
     * TIPOS SUPORTADOS:
     * - STRING: Retorna texto com trim()
     * - NUMERIC: Diferencia números de datas
     * - BOOLEAN: Converte para "true"/"false"
     * - FORMULA: Avalia resultado da fórmula
     * - BLANK: Retorna string vazia
     * - UNKNOWN: Retorna "TIPO_CELULA_DESCONHECIDO"
     * 
     * FORMATAÇÃO INTELIGENTE DE NÚMEROS:
     * - Inteiros: sem casas decimais (123, não 123.0)
     * - Decimais: até 10 casas, sem separador de milhares
     * - Datas: formato toString() padrão
     * - Evita notação científica
     * 
     * EXEMPLO CONVERSÕES:
     * - 123.0 -> "123"
     * - 123.456 -> "123.456"
     * - true -> "true"
     * - "  texto  " -> "texto"
     * - data -> "Tue Jan 01 00:00:00 BRT 2024"
     * - null -> ""
     * 
     * TRATAMENTO DE FÓRMULAS:
     * Avalia resultado da fórmula usando getCachedFormulaResultType()
     * em vez do tipo da fórmula em si.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            // Usa método moderno para avaliar fórmulas
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
                
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Formatação inteligente de números
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        // É um inteiro - retorna sem casas decimais
                        return String.valueOf((long) numericValue);
                    } else {
                        // É decimal - usa formato sem separador de milhares
                        return new DecimalFormat("#.##########").format(numericValue);
                    }
                }
                
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
                
            case BLANK:
                return "";
                
            default:
                return "TIPO_CELULA_DESCONHECIDO";
        }
    }

    /**
     * MÉTODO: close()
     * 
     * PROPÓSITO:
     * Implementação do AutoCloseable.
     * Libera recursos do arquivo Excel automaticamente.
     * 
     * EXCEÇÕES:
     * @throws IOException - Erro ao fechar arquivo
     * 
     * QUANDO É CHAMADO:
     * - Automaticamente no try-with-resources
     * - Manualmente se necessário
     * - Em caso de erro durante inicialização
     * 
     * IMPORTÂNCIA:
     * - Evita vazamento de memória
     * - Libera handles de arquivo
     * - Permite acesso ao arquivo por outros processos
     * 
     * USO RECOMENDADO:
     * try (ExcelDataReader reader = new ExcelDataReader(file, sheet)) {
     *     // usar reader
     * } // close() chamado automaticamente
     */
    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}
```

---

Continuando com mais seções... O arquivo está ficando muito extenso, então vou criar uma estrutura mais concisa mas ainda completa. Quer que eu continue com os **STEPS**, **MODELS**, **CONTROLLERS** e **UTILS** de forma mais resumida mas ainda detalhada?

