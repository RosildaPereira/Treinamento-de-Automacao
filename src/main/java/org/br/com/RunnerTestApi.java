package org.br.com;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.br.com.core.support.Context;
import org.br.com.test.utils.DataUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe principal para execução dos testes de API utilizando Cucumber e JUnit.
 * Configurações para features, glue, plugins de relatório e tags são definidas aqui.
 */
@RunWith(Cucumber.class) // TODO: Instancia a classe Cucumber para rodar os testes. // TODO: A classe Cucumber é instanciada para permitir a execução dos testes BDD.
@CucumberOptions(
    features = "src/main/resources/features", // TODO: Define o caminho para os arquivos de feature do Cucumber. // TODO: Os arquivos .feature contêm as descrições dos cenários de teste em Gherkin.
    glue = {"org.br.com.test.steps", "org.br.com.test.utils", "org.br.com.test.utils.hooks"}, // TODO: Define os pacotes onde o Cucumber deve procurar por definições de passos (steps), utilitários e hooks. // TODO: Os pacotes 'steps' contêm as implementações dos passos Gherkin, 'utils' para utilitários e 'hooks' para ganchos de execução.
    plugin = {
        "json:target/reports/reports.json", // TODO: Gera um relatório JSON da execução dos testes. // TODO: O relatório JSON é útil para integração com outras ferramentas de relatório.
        "html:target/reports/cucumber-html-report.html" // TODO: Gera um relatório HTML da execução dos testes.
    },
    tags = "@api",
    monochrome = false,
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    stepNotifications = false
)
public class RunnerTestApi {

    // Variável estática para armazenar o tempo de início da execução dos testes.
    private static Instant startTime; // TODO: Armazena o instante de início da execução dos testes. // TODO: Usado para calcular a duração total da execução dos testes.
    // Variável estática para armazenar a data atual formatada.
    private static String currentDate; // TODO: Armazena a data atual formatada para uso em nomes de arquivos de log. // TODO: Garante que os arquivos de log sejam únicos para cada dia de execução.
    
    /**
     * Método executado uma vez antes de todas as classes de teste.
     * Inicializa o tempo de início da execução, a data atual e cria os arquivos de log com a data.
     * Também reseta os contadores de contexto.
     */
    @BeforeClass // TODO: Anotação do JUnit que indica que este método será executado uma vez antes de todos os testes da classe.
    public static void beforeClass() {
        // Captura o tempo de início da execução. // TODO: Captura o tempo exato em que a execução dos testes começa. // TODO: Instant.now() é usado para obter o ponto atual no tempo.
        startTime = Instant.now();
        currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // TODO: Formata a data atual para 'yyyy-MM-dd'.
        
        // Criar arquivos de log com data // TODO: Chama o método para criar os arquivos de log com a data atual.
        createLogFilesWithDate();
        
        Context.resetCounters(); // TODO: Reseta os contadores de contexto da classe Context.
//        LogFormatter.logStep("Iniciando execucao dos test de API");
    }
    
    /**
     * Método executado uma vez após todas as classes de teste.
     * Calcula a duração total da execução, finaliza o contexto, renomeia o arquivo de relatório JSON
     * com um timestamp e renomeia os arquivos de log com a data.
     */
    @AfterClass // TODO: Anotação do JUnit que indica que este método será executado uma vez após todos os testes da classe.
    public static void afterClass() {
        // Captura o tempo de término da execução.
        Instant endTime = Instant.now(); // TODO: Captura o tempo exato em que a execução dos testes termina. // TODO: Usado em conjunto com 'startTime' para calcular a duração.
        // Calcula a duração total da execução em milissegundos.
        long duration = Duration.between(startTime, endTime).toMillis(); // TODO: Calcula a duração total da execução dos testes. // TODO: Duration.between() calcula a diferença entre dois Instant.
        
        // Finaliza o contexto, passando a duração total.
        Context.finishedContext(duration); // TODO: Chama o método para finalizar o contexto, passando a duração total. // TODO: O método 'finishedContext' da classe 'Context' é responsável por processar os dados finais da execução.
        
        // Renomear arquivo de relatório com timestamp
        File reportFile = new File("target/reports/reports.json"); // TODO: Cria um objeto File para o relatório JSON. // TODO: Representa o arquivo de relatório JSON gerado pelo Cucumber.
        if (reportFile.exists()) {
            // Gera um timestamp formatado para o nome do arquivo. // TODO: DataUtils.getDataHoraAtual() é uma classe utilitária para manipulação de datas e horas.
            String timestamp = DataUtils.getDataHoraAtual().replace(":", "").replace("-", "").replace(" ", "_"); // TODO: Gera um timestamp formatado.
            String newFileName = "target/reports/" + timestamp + ".json"; // TODO: Define o novo nome do arquivo de relatório com o timestamp.
            File newFile = new File(newFileName); // TODO: Cria um novo objeto File com o novo nome.
            
            if (reportFile.renameTo(newFile)) {
//                LogFormatter.logStep("Arquivo renomeado com sucesso: target/reports/reports.json -> " + newFileName);
//                LogFormatter.logSuccess("Relatorio gerado: " + newFileName);
            }
        }
        
        // Renomear arquivos de log com data // TODO: Chama o método para renomear os arquivos de log com a data atual.
        renameLogFilesWithDate();
    }
    
    /**
     * Cria diretórios e arquivos de log com a data atual, se ainda não existirem.
     * Os arquivos criados são 'execution-[data].log' e 'automation-[data].log' dentro de 'target/log'.
     */
    private static void createLogFilesWithDate() {
        try { // TODO: Bloco try-catch para tratamento de exceções de I/O.
            // Criar diretório de log se não existir
            Path logDir = Paths.get("target/log"); // TODO: Define o caminho para o diretório de logs. // TODO: Paths.get() cria um objeto Path a partir de uma string.
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir); // TODO: Cria o diretório de logs se ele não existir. // TODO: Files.createDirectories() cria todos os diretórios pai necessários.
            }
            
            // Criar arquivos de log com data
            String executionLogFile = "target/log/execution-" + currentDate + ".log"; // TODO: Define o nome do arquivo de log de execução com a data. // TODO: O nome do arquivo inclui a data para evitar sobrescrita em execuções diárias.
            String automationLogFile = "target/log/automation-" + currentDate + ".log"; // TODO: Define o nome do arquivo de log de automação com a data. // TODO: Separar logs de execução e automação pode facilitar a depuração.
            
            // Obtém os caminhos para os arquivos de log.
            Path executionPath = Paths.get(executionLogFile); // TODO: Obtém o caminho completo para o arquivo de log de execução. // TODO: Objeto Path para manipulação de arquivos.
            Path automationPath = Paths.get(automationLogFile); // TODO: Obtém o caminho completo para o arquivo de log de automação. // TODO: Objeto Path para manipulação de arquivos.
            
            // Cria os arquivos vazios se não existirem, evitando sobrescrever arquivos existentes // TODO: Verifica se o arquivo já existe antes de tentar criá-lo.
            // que podem ter sido criados por outras execuções ou processos.
            if (!Files.exists(executionPath)) {
                Files.createFile(executionPath); // TODO: Cria o arquivo de log de execução se ele não existir.
            }
            if (!Files.exists(automationPath)) {
                Files.createFile(automationPath); // TODO: Cria o arquivo de log de automação se ele não existir.
            }
            
//            System.out.println("Arquivos de log criados:");
//            System.out.println("- " + executionLogFile);
//            System.out.println("- " + automationLogFile);
            
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivos de log: " + e.getMessage());
        }
    }
    
    /**
     * Renomeia os arquivos de log padrão ('execution.log' e 'automation.log') para incluir a data atual.
     * Se um arquivo com a data já existir, o conteúdo do arquivo padrão é anexado ao arquivo datado,
     * e o arquivo padrão é então excluído. Caso contrário, o arquivo padrão é simplesmente renomeado.
     */
    private static void renameLogFilesWithDate() {
        try { // TODO: Bloco try-catch para tratamento de exceções de I/O.
            // Renomear arquivos de log padrão para arquivos com data
            File executionLog = new File("target/log/execution.log"); // TODO: Cria um objeto File para o log de execução padrão. // TODO: Representa o arquivo de log temporário gerado durante a execução.
            File automationLog = new File("target/log/automation.log"); // TODO: Cria um objeto File para o log de automação padrão. // TODO: Representa o arquivo de log temporário gerado durante a execução.
            
            // Processa o arquivo de log de execução.
            if (executionLog.exists()) {
                File newExecutionLog = new File("target/log/execution-" + currentDate + ".log"); // TODO: Define o novo nome do arquivo de log de execução com a data. // TODO: O novo nome inclui a data para arquivamento.
                if (newExecutionLog.exists()) {
                    // Se o arquivo com data já existe, anexa o conteúdo do log padrão.
                    Files.write(newExecutionLog.toPath(), 
                               Files.readAllBytes(executionLog.toPath()), 
                               java.nio.file.StandardOpenOption.APPEND); // TODO: Anexa o conteúdo do log padrão ao log datado. // TODO: StandardOpenOption.APPEND garante que o conteúdo seja adicionado ao final do arquivo.
                    // Deleta o arquivo de log padrão após anexar seu conteúdo.
                    executionLog.delete(); // TODO: Deleta o arquivo de log de execução padrão. // TODO: Remove o arquivo temporário após seu conteúdo ser transferido.
                } else {
                    // Se o arquivo com data não existe, simplesmente renomeia o log padrão.
                    executionLog.renameTo(newExecutionLog); // TODO: Renomeia o arquivo de log de execução padrão. // TODO: Move o arquivo para o novo nome.
                }
            }
            
            // Processa o arquivo de log de automação.
            if (automationLog.exists()) {
                File newAutomationLog = new File("target/log/automation-" + currentDate + ".log"); // TODO: Define o novo nome do arquivo de log de automação com a data. // TODO: O novo nome inclui a data para arquivamento.
                if (newAutomationLog.exists()) {
                    // Se o arquivo com data já existe, copiar o conteúdo
                    Files.write(newAutomationLog.toPath(), 
                               Files.readAllBytes(automationLog.toPath()), 
                               java.nio.file.StandardOpenOption.APPEND);
                    // Deleta o arquivo de log padrão após anexar seu conteúdo.
                    automationLog.delete();
                } else {
                    automationLog.renameTo(newAutomationLog);
                }
            } // TODO: Adicionar tratamento de exceção mais granular para diferentes tipos de IOException. // TODO: Considerar exceções específicas como FileAlreadyExistsException ou AccessDeniedException.
        } catch (IOException e) {
            System.err.println("Erro ao renomear arquivos de log: " + e.getMessage());
        }
    }
} 