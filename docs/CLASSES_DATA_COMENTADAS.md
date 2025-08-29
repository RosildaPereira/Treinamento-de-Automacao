# 📚 **CLASSES DO PACOTE DATA/ - COMENTÁRIOS DETALHADOS**

## 🎯 **VISÃO GERAL DO PACOTE DATA/**

O pacote `org.br.com.core.data` é responsável por **gerenciar a leitura e manipulação de dados de planilhas Excel** no framework de automação. Utiliza a biblioteca **Fillo** para tratar arquivos Excel como bancos de dados, permitindo executar queries SQL diretamente nas planilhas.

---

## 📋 **1. DataModel.java - Interface Base para Modelos de Dados**

**📍 Localização**: `src/main/java/org/br/com/core/data/DataModel.java`

```java
package org.br.com.core.data;

/**
 * ============================================================================
 * INTERFACE ESSENCIAL: DataModel
 * ============================================================================
 * 
 * PROPÓSITO:
 * Interface marcadora que define o contrato para todos os modelos de dados
 * que serão utilizados no framework. Serve como um tipo base para garantir
 * que todas as classes de modelo implementem a mesma estrutura.
 * 
 * QUANDO USAR:
 * - Implementar em todas as classes que representam dados de planilhas Excel
 * - Garantir type safety ao trabalhar com diferentes tipos de dados
 * - Facilitar a criação de métodos genéricos que aceitem qualquer modelo
 * 
 * COMO INSTANCIAR:
 * Não é instanciada diretamente, mas implementada por outras classes:
 * 
 * public class UsuarioModel implements DataModel {
 *     private String nome;
 *     private String email;
 *     // ... outros campos
 * }
 * 
 * MOTIVOS DE USO:
 * 1. Padronização: Garante que todos os modelos sigam o mesmo padrão
 * 2. Type Safety: Permite usar generics de forma segura
 * 3. Polimorfismo: Facilita o tratamento genérico de diferentes modelos
 * 4. Manutenibilidade: Centraliza mudanças estruturais em um só lugar
 * ============================================================================
 */
public interface DataModel {
    // Interface marcadora - não possui métodos
    // Serve apenas para garantir que as classes implementem o contrato correto
}
```

---

## 📊 **2. DataSheet.java - Classe Base para Leitura de Dados Únicos**

**📍 Localização**: `src/main/java/org/br/com/core/data/DataSheet.java`

```java
package org.br.com.core.data;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import org.br.com.core.exceptions.DataException;
import org.br.com.core.support.logger.LogFormatter;

/**
 * ============================================================================
 * CLASSE ABSTRATA ESSENCIAL: DataSheet<T>
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe base abstrata para gerenciar leitura de dados de planilhas Excel
 * quando se espera trabalhar com UM ÚNICO REGISTRO por vez. Utiliza generics
 * para garantir type safety com o modelo de dados específico.
 * 
 * QUANDO USAR:
 * - Quando precisar ler dados de teste de uma planilha Excel
 * - Para cenários onde cada teste usa apenas UM registro de dados
 * - Implementar classes específicas que herdam desta base
 * 
 * COMO INSTANCIAR:
 * Não é instanciada diretamente. Deve ser herdada:
 * 
 * public class UsuarioDataSheet extends DataSheet<UsuarioModel> {
 *     public UsuarioDataSheet(String excelPath) {
 *         super(excelPath, "TBL_USUARIOS");
 *     }
 *     
 *     @Override
 *     public UsuarioModel getData() {
 *         setData(); // Carrega os dados
 *         return UsuarioModel.builder()
 *             .nome(getField("nome"))
 *             .email(getField("email"))
 *             .build();
 *     }
 * }
 * 
 * MOTIVOS DE USO:
 * 1. Reutilização: Evita duplicação de código entre diferentes modelos
 * 2. Padronização: Garante que todas as classes de dados sigam o mesmo padrão
 * 3. Manutenibilidade: Centraliza a lógica de acesso a dados
 * 4. Type Safety: Usa generics para garantir tipos corretos
 * ============================================================================
 */
public abstract class DataSheet<T> {

    // ========================================================================
    // ATRIBUTOS PRIVADOS
    // ========================================================================
    
    /**
     * ATRIBUTO: excelFilePath
     * 
     * PROPÓSITO: Armazena o caminho completo para o arquivo Excel
     * TIPO: String (final - não pode ser alterado após construção)
     * MOTIVO: Garante que a instância sempre trabalhe com o mesmo arquivo
     */
    private final String excelFilePath;
    
    /**
     * ATRIBUTO: defaultSheetName
     * 
     * PROPÓSITO: Nome padrão da aba/planilha a ser utilizada
     * TIPO: String
     * MOTIVO: Permite definir uma aba padrão para consultas simples
     */
    private String defaultSheetName;
    
    /**
     * ATRIBUTO: recordset
     * 
     * PROPÓSITO: Objeto que contém os dados retornados da query SQL
     * TIPO: Recordset (da biblioteca Fillo)
     * MOTIVO: Interface para navegar pelos registros retornados
     */
    private Recordset recordset;
    
    /**
     * ATRIBUTO: recordLineFound
     * 
     * PROPÓSITO: Flag que indica se já foi encontrado um registro válido
     * TIPO: boolean
     * MOTIVO: Otimiza a busca, evitando reprocessar dados já encontrados
     */
    boolean recordLineFound;

    // ========================================================================
    // CONSTRUTORES
    // ========================================================================

    /**
     * CONSTRUTOR SIMPLES
     * 
     * PROPÓSITO:
     * Inicializa a classe apenas com o caminho do arquivo Excel.
     * Usado quando a aba será especificada posteriormente.
     * 
     * PARÂMETROS:
     * @param excelFilePath - Caminho completo para o arquivo .xlsx
     * 
     * QUANDO USAR:
     * - Quando você vai trabalhar com múltiplas abas
     * - Quando a aba será definida dinamicamente
     * 
     * EXEMPLO DE USO:
     * DataSheet<UsuarioModel> sheet = new UsuarioDataSheet("dados/usuarios.xlsx");
     * 
     * MOTIVO:
     * Oferece flexibilidade para definir a aba posteriormente via métodos
     */
    public DataSheet(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    /**
     * CONSTRUTOR COMPLETO
     * 
     * PROPÓSITO:
     * Inicializa a classe com o arquivo Excel e define uma aba padrão.
     * Mais conveniente quando sempre trabalha com a mesma aba.
     * 
     * PARÂMETROS:
     * @param excelFilePath - Caminho completo para o arquivo .xlsx
     * @param defaultSheetName - Nome da aba padrão (ex: "TBL_USUARIOS")
     * 
     * QUANDO USAR:
     * - Quando sempre trabalha com a mesma aba
     * - Para simplificar o uso em classes filhas
     * 
     * EXEMPLO DE USO:
     * DataSheet<UsuarioModel> sheet = new UsuarioDataSheet("dados/usuarios.xlsx", "TBL_USUARIOS");
     * 
     * MOTIVO:
     * Reduz código repetitivo e torna o uso mais intuitivo
     */
    public DataSheet(String excelFilePath, String defaultSheetName) {
        this.excelFilePath = excelFilePath;
        this.defaultSheetName = defaultSheetName;
    }

    // ========================================================================
    // MÉTODOS DE CONFIGURAÇÃO DE DADOS
    // ========================================================================

    /**
     * MÉTODO: setData()
     * 
     * PROPÓSITO:
     * Carrega todos os dados da aba padrão especificada no construtor.
     * Executa automaticamente a query "SELECT * FROM [defaultSheetName]".
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - No método getData() das classes filhas
     * - Quando quiser carregar todos os dados da aba padrão
     * 
     * COMO USAR:
     * protected void carregarDados() {
     *     setData(); // Carrega dados da aba padrão
     *     String nome = getField("nome"); // Agora pode acessar os campos
     * }
     * 
     * MOTIVOS:
     * 1. Simplicidade: Um método para carregar dados da aba padrão
     * 2. Convenção: Padroniza a forma de carregar dados
     * 3. Menos código: Evita repetir o nome da aba constantemente
     * 
     * FLUXO INTERNO:
     * 1. Chama setDataFromSheetName(defaultSheetName)
     * 2. Monta query SQL automática
     * 3. Executa query e armazena resultado em recordset
     */
    protected void setData() {
        setDataFromSheetName(defaultSheetName);
    }

    /**
     * MÉTODO: setDataFromSheetName(String sheetName)
     * 
     * PROPÓSITO:
     * Carrega dados de uma aba específica, diferente da aba padrão.
     * Permite flexibilidade para trabalhar com múltiplas abas.
     * 
     * PARÂMETROS:
     * @param sheetName - Nome da aba/planilha a ser consultada
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Quando precisa acessar uma aba diferente da padrão
     * - Para implementar lógica que alterna entre abas
     * - Em cenários onde a aba é definida dinamicamente
     * 
     * COMO USAR:
     * protected void carregarDadosEspecificos() {
     *     setDataFromSheetName("TBL_DADOS_ESPECIAIS");
     *     String valor = getField("valor_especial");
     * }
     * 
     * MOTIVOS:
     * 1. Flexibilidade: Permite mudar de aba sem recriar a instância
     * 2. Reutilização: Uma mesma classe pode trabalhar com várias abas
     * 3. Dinamismo: Aba pode ser escolhida em tempo de execução
     * 
     * FLUXO INTERNO:
     * 1. Monta query "SELECT * FROM `[sheetName]`"
     * 2. Chama setDataFromQuery(query)
     * 3. Executa a query e carrega os dados
     */
    protected void setDataFromSheetName(String sheetName) {
        String query = String.format("select * from `%s`", sheetName);
        setDataFromQuery(query);
    }

    /**
     * MÉTODO: setDataFromQuery(String query)
     * 
     * PROPÓSITO:
     * Executa uma query SQL customizada na planilha Excel.
     * Oferece máxima flexibilidade para consultas complexas.
     * 
     * PARÂMETROS:
     * @param query - Query SQL a ser executada na planilha
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Consultas com WHERE, ORDER BY, etc.
     * - Filtros específicos nos dados
     * - Joins entre múltiplas abas
     * - Aggregações (COUNT, SUM, etc.)
     * 
     * COMO USAR:
     * protected void carregarUsuariosAtivos() {
     *     String query = "SELECT * FROM TBL_USUARIOS WHERE ativo = 'S' ORDER BY nome";
     *     setDataFromQuery(query);
     * }
     * 
     * MOTIVOS:
     * 1. Poder: Permite usar todo o poder do SQL
     * 2. Filtros: Carrega apenas os dados necessários
     * 3. Performance: Reduz a quantidade de dados carregados
     * 4. Flexibilidade: Suporta consultas complexas
     * 
     * FLUXO INTERNO:
     * 1. Cria uma instância de DataReader com o arquivo Excel
     * 2. Executa a query fornecida
     * 3. Armazena o resultado em recordset
     * 4. Marca recordLineFound como false (dados ainda não processados)
     * 5. Em caso de erro, loga e lança DataException
     * 
     * TRATAMENTO DE ERROS:
     * - Captura qualquer exceção durante a execução
     * - Loga o erro com LogFormatter.logError()
     * - Lança DataException com mensagem descritiva
     */
    protected void setDataFromQuery(String query) {
        try (DataReader dataReader = new DataReader(excelFilePath)) {
            recordset = dataReader.executeQuery(query);
            recordLineFound = false;
        } catch (Exception e) {
            String message = String.format("Error in execute query '%s'", query);
            LogFormatter.logError(message);
            throw new DataException(message, e);
        }
    }

    // ========================================================================
    // MÉTODOS DE ACESSO A DADOS
    // ========================================================================

    /**
     * MÉTODO: getField(String fieldName)
     * 
     * PROPÓSITO:
     * Obtém o valor de um campo específico do registro atual do recordset.
     * É o método principal para extrair dados das células da planilha.
     * 
     * PARÂMETROS:
     * @param fieldName - Nome da coluna/campo na planilha
     * 
     * RETORNO:
     * @return String - Valor do campo encontrado (pode ser null se vazio)
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Após chamar setData(), setDataFromSheetName() ou setDataFromQuery()
     * - Para extrair valores específicos das células
     * - No método getData() das classes filhas
     * 
     * COMO USAR:
     * @Override
     * public UsuarioModel getData() {
     *     setData();
     *     return UsuarioModel.builder()
     *         .nome(getField("nome"))           // Pega valor da coluna "nome"
     *         .email(getField("email"))         // Pega valor da coluna "email"
     *         .senha(getField("senha"))         // Pega valor da coluna "senha"
     *         .build();
     * }
     * 
     * MOTIVOS:
     * 1. Simplicidade: Interface simples para acessar dados
     * 2. Abstração: Esconde a complexidade do Recordset
     * 3. Segurança: Trata erros de forma consistente
     * 4. Otimização: Evita reprocessar dados já encontrados
     * 
     * FLUXO INTERNO:
     * 1. Verifica se recordLineFound é true (dados já processados)
     * 2. Se true: retorna campo do registro atual
     * 3. Se false: navega pelo recordset até encontrar dados
     * 4. Marca recordLineFound como true após encontrar dados
     * 5. Retorna o valor do campo solicitado
     * 
     * TRATAMENTO DE ERROS:
     * - NullPointerException: Quando setData() não foi chamado antes
     * - FilloException: Quando o campo não existe na planilha
     * - Ambos resultam em DataException com mensagem clara
     * 
     * OTIMIZAÇÃO:
     * - Uma vez que um registro é encontrado, outros getField() usam o mesmo registro
     * - Evita navegar pelo recordset múltiplas vezes
     */
    protected String getField(String fieldName) {
        String field = null;
        try {
            if (recordLineFound) {
                // Otimização: se já encontrou um registro, usa o mesmo
                field = recordset.getField(fieldName);
            } else {
                // Primeira busca: navega até encontrar dados
                while (recordset.next()) {
                    field = recordset.getField(fieldName);
                }
                if (field != null) {
                    recordLineFound = true; // Marca que encontrou dados
                }
            }
        } catch (NullPointerException e) {
            String message = String.format(
                    "Call method 'setData()' or 'setDataFromSheetName()' or 'setDataFromQuery()' before to set record");
            LogFormatter.logError(message);
            throw new DataException(message, e);
        } catch (FilloException e) {
            String message = String.format("Error in get field name '%s'", fieldName);
            LogFormatter.logError(message);
            throw new DataException(message, e);
        }
        return field;
    }

    /**
     * MÉTODO ABSTRATO: getData()
     * 
     * PROPÓSITO:
     * Método abstrato que deve ser implementado pelas classes filhas.
     * Define como os dados devem ser retornados como um objeto tipado.
     * 
     * RETORNO:
     * @return T - Objeto do tipo específico definido no generic
     * 
     * VISIBILIDADE: public abstract (deve ser implementado pelas filhas)
     * 
     * QUANDO IMPLEMENTAR:
     * - Em todas as classes que herdam de DataSheet
     * - Para definir como mapear dados da planilha para o modelo
     * 
     * COMO IMPLEMENTAR:
     * @Override
     * public UsuarioModel getData() {
     *     setData(); // Carrega os dados
     *     return UsuarioModel.builder()
     *         .nome(getField("nome"))
     *         .email(getField("email"))
     *         .senha(getField("senha"))
     *         .ativo(Boolean.parseBoolean(getField("ativo")))
     *         .build();
     * }
     * 
     * MOTIVOS:
     * 1. Contrato: Força as filhas a implementar a lógica específica
     * 2. Type Safety: Garante retorno do tipo correto
     * 3. Polimorfismo: Permite tratar diferentes tipos de forma uniforme
     * 4. Flexibilidade: Cada modelo define sua própria lógica de mapeamento
     */
    public abstract T getData();

    // ========================================================================
    // MÉTODOS DE ATUALIZAÇÃO DE DADOS
    // ========================================================================

    /**
     * MÉTODO: updateField(4 parâmetros) - Atualização na Aba Padrão
     * 
     * PROPÓSITO:
     * Atualiza um campo específico na aba padrão da planilha Excel.
     * Versão simplificada que usa a aba configurada no construtor.
     * 
     * PARÂMETROS:
     * @param fieldToUpdate - Nome do campo/coluna a ser atualizado
     * @param fieldToUpdateValue - Novo valor para o campo
     * @param registerField - Campo usado na condição WHERE
     * @param registerFieldValue - Valor da condição WHERE
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Atualizar dados na aba padrão
     * - Marcar registros como "usado" ou "processado"
     * - Atualizar status ou flags de controle
     * 
     * COMO USAR:
     * protected void marcarUsuarioComoUsado(String email) {
     *     updateField("usado", "S", "email", email);
     *     // SQL gerado: UPDATE TBL_USUARIOS SET usado = 'S' WHERE email = 'email'
     * }
     * 
     * MOTIVOS:
     * 1. Controle: Evita reutilização de dados de teste
     * 2. Rastreabilidade: Marca quais dados já foram utilizados
     * 3. Conveniência: Não precisa especificar a aba toda vez
     * 
     * FLUXO INTERNO:
     * 1. Chama o método updateField com 5 parâmetros
     * 2. Passa defaultSheetName como primeiro parâmetro
     * 3. Delega toda a lógica para o método mais completo
     */
    protected void updateField(String fieldToUpdate, String fieldToUpdateValue, String registerField,
                               String registerFieldValue) {
        updateField(defaultSheetName, fieldToUpdate, fieldToUpdateValue, registerField, registerFieldValue);
    }

    /**
     * MÉTODO: updateField(5 parâmetros) - Atualização em Aba Específica
     * 
     * PROPÓSITO:
     * Atualiza um campo específico em qualquer aba da planilha Excel.
     * Versão completa que oferece máxima flexibilidade.
     * 
     * PARÂMETROS:
     * @param sheetName - Nome da aba/planilha a ser atualizada
     * @param fieldToUpdate - Nome do campo/coluna a ser atualizado
     * @param fieldToUpdateValue - Novo valor para o campo
     * @param registerField - Campo usado na condição WHERE
     * @param registerFieldValue - Valor da condição WHERE
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Atualizar dados em abas específicas
     * - Trabalhar com múltiplas abas
     * - Cenários onde a aba é definida dinamicamente
     * 
     * COMO USAR:
     * protected void atualizarStatusNaAbaLog(String id, String status) {
     *     updateField("TBL_LOG", "status", status, "id", id);
     *     // SQL: UPDATE TBL_LOG SET status = 'PROCESSADO' WHERE id = '123'
     * }
     * 
     * MOTIVOS:
     * 1. Flexibilidade: Trabalha com qualquer aba
     * 2. Manutenção: Atualiza dados para próximas execuções
     * 3. Log: Registra o processamento de dados
     * 
     * FLUXO INTERNO:
     * 1. Monta query UPDATE usando String.format()
     * 2. Gera: "UPDATE [sheetName] SET [field] = '[value]' WHERE [where_field] = '[where_value]'"
     * 3. Chama updateOrInsert(query) para executar
     */
    protected void updateField(String sheetName, String fieldToUpdate, String fieldToUpdateValue, String registerField,
                               String registerFieldValue) {
        String query = String.format("update %s set %s = '%s' where %s = '%s'", sheetName, fieldToUpdate,
                fieldToUpdateValue, registerField, registerFieldValue);
        updateOrInsert(query);
    }

    /**
     * MÉTODO: updateOrInsert(String query)
     * 
     * PROPÓSITO:
     * Executa uma query de atualização (UPDATE) ou inserção (INSERT) 
     * na planilha Excel. Método de baixo nível para operações de escrita.
     * 
     * PARÂMETROS:
     * @param query - Query SQL de UPDATE ou INSERT a ser executada
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Executar UPDATEs customizados
     * - Inserir novos registros na planilha
     * - Operações de escrita que não se encaixam nos métodos padrão
     * 
     * COMO USAR:
     * protected void inserirNovoLog(String id, String acao) {
     *     String query = String.format(
     *         "INSERT INTO TBL_LOG (id, acao, data) VALUES ('%s', '%s', '%s')",
     *         id, acao, LocalDate.now().toString()
     *     );
     *     updateOrInsert(query);
     * }
     * 
     * MOTIVOS:
     * 1. Poder: Permite usar qualquer comando SQL de modificação
     * 2. Flexibilidade: Suporta INSERTs, UPDATEs complexos
     * 3. Controle: Acesso direto ao mecanismo de escrita
     * 
     * FLUXO INTERNO:
     * 1. Cria nova instância de DataReader
     * 2. Executa dataReader.updateQuery(query)
     * 3. Fecha automaticamente a conexão (try-with-resources)
     * 4. Em caso de erro, loga e lança DataException
     * 
     * TRATAMENTO DE ERROS:
     * - Captura qualquer exceção durante a execução
     * - Loga o erro com a query que falhou
     * - Lança DataException com contexto do erro
     * 
     * IMPORTANTE:
     * - Use com cuidado: pode modificar permanentemente a planilha
     * - Sempre teste em cópias antes de usar em dados importantes
     * - Considere fazer backup da planilha antes de modificações
     */
    protected void updateOrInsert(String query) {
        try (DataReader dataReader = new DataReader(excelFilePath)) {
            dataReader.updateQuery(query);
        } catch (Exception e) {
            String message = String.format("Error in update or insert with query '%s'", query);
            LogFormatter.logError(message);
            throw new DataException(message, e);
        }
    }
}
```

---

## 📊 **3. MultipleDataSheet.java - Classe Base para Leitura de Múltiplos Registros**

**📍 Localização**: `src/main/java/org/br/com/core/data/MultipleDataSheet.java`

```java
package org.br.com.core.data;

import com.codoid.products.fillo.Recordset;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.exceptions.DataException;

import java.util.List;

/**
 * ============================================================================
 * CLASSE ABSTRATA ESSENCIAL: MultipleDataSheet
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe base abstrata para gerenciar leitura de MÚLTIPLOS REGISTROS de 
 * planilhas Excel. Diferente da DataSheet, esta classe é otimizada para 
 * retornar listas de dados, ideal para testes data-driven.
 * 
 * QUANDO USAR:
 * - Testes que precisam executar com múltiplos conjuntos de dados
 * - Cenários de data-driven testing
 * - Quando uma planilha contém vários casos de teste
 * - Para carregar dados em lote para processamento
 * 
 * COMO INSTANCIAR:
 * Não é instanciada diretamente. Deve ser herdada:
 * 
 * public class UsuarioMultipleDataSheet extends MultipleDataSheet {
 *     public UsuarioMultipleDataSheet(String excelPath) {
 *         super(excelPath, "TBL_USUARIOS");
 *     }
 *     
 *     @Override
 *     public List<DataModel> getData() {
 *         setData();
 *         List<DataModel> usuarios = new ArrayList<>();
 *         
 *         try {
 *             while (recordset.next()) {
 *                 UsuarioModel usuario = UsuarioModel.builder()
 *                     .nome(recordset.getField("nome"))
 *                     .email(recordset.getField("email"))
 *                     .build();
 *                 usuarios.add(usuario);
 *             }
 *         } catch (FilloException e) {
 *             throw new DataException("Erro ao processar dados", e);
 *         }
 *         
 *         return usuarios;
 *     }
 * }
 * 
 * MOTIVOS DE USO:
 * 1. Escalabilidade: Processa grandes volumes de dados
 * 2. Data-Driven: Suporta testes com múltiplos cenários
 * 3. Eficiência: Carrega todos os dados de uma vez
 * 4. Flexibilidade: Permite filtros e ordenações complexas
 * ============================================================================
 */
@Log4j2
public abstract class MultipleDataSheet {

    // ========================================================================
    // ATRIBUTOS PRIVADOS E PROTECTED
    // ========================================================================
    
    /**
     * ATRIBUTO: excelFilePath
     * 
     * PROPÓSITO: Armazena o caminho completo para o arquivo Excel
     * TIPO: String (final - não pode ser alterado após construção)
     * MOTIVO: Garante que a instância sempre trabalhe com o mesmo arquivo
     * USO: Passado para o DataReader para estabelecer conexão
     */
    private final String excelFilePath;
    
    /**
     * ATRIBUTO: defaultSheetName
     * 
     * PROPÓSITO: Nome padrão da aba/planilha a ser utilizada
     * TIPO: String
     * MOTIVO: Permite definir uma aba padrão para consultas simples
     * USO: Usado no método setData() quando nenhuma aba específica é informada
     */
    private String defaultSheetName;
    
    /**
     * ATRIBUTO: recordset
     * 
     * PROPÓSITO: Objeto que contém TODOS os dados retornados da query SQL
     * TIPO: Recordset (da biblioteca Fillo)
     * VISIBILIDADE: protected (classes filhas precisam acessar)
     * MOTIVO: Classes filhas precisam navegar pelos registros
     * DIFERENÇA: Na DataSheet é private, aqui é protected para acesso direto
     */
    protected Recordset recordset;
    
    /**
     * ATRIBUTO: recordLineFound
     * 
     * PROPÓSITO: Flag que indica se já foi encontrado pelo menos um registro
     * TIPO: boolean
     * MOTIVO: Controle interno para otimização de consultas
     * USO: Evita reprocessar dados já encontrados
     */
    boolean recordLineFound;

    // ========================================================================
    // CONSTRUTORES
    // ========================================================================

    /**
     * CONSTRUTOR SIMPLES
     * 
     * PROPÓSITO:
     * Inicializa a classe apenas com o caminho do arquivo Excel.
     * Ideal quando você vai trabalhar com múltiplas abas ou definir 
     * a aba dinamicamente.
     * 
     * PARÂMETROS:
     * @param excelFilePath - Caminho completo para o arquivo .xlsx
     * 
     * QUANDO USAR:
     * - Aplicações que trabalham com várias abas
     * - Casos onde a aba é escolhida em tempo de execução
     * - Máxima flexibilidade na escolha de dados
     * 
     * EXEMPLO DE USO:
     * MultipleDataSheet sheet = new UsuarioMultipleDataSheet("dados/teste.xlsx");
     * // Depois especifica a aba:
     * sheet.setDataFromSheetName("TBL_USUARIOS_ADMIN");
     * 
     * MOTIVO:
     * Flexibilidade para trabalhar com diferentes abas sem recriar a instância
     */
    public MultipleDataSheet(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    /**
     * CONSTRUTOR COMPLETO
     * 
     * PROPÓSITO:
     * Inicializa a classe com o arquivo Excel e define uma aba padrão.
     * Mais conveniente quando sempre trabalha com a mesma aba.
     * 
     * PARÂMETROS:
     * @param excelFilePath - Caminho completo para o arquivo .xlsx
     * @param defaultSheetName - Nome da aba padrão (ex: "TBL_USUARIOS")
     * 
     * QUANDO USAR:
     * - Quando sempre trabalha com a mesma aba
     * - Para simplificar o código das classes filhas
     * - Em cenários de uso específico e focado
     * 
     * EXEMPLO DE USO:
     * MultipleDataSheet sheet = new UsuarioMultipleDataSheet("dados/usuarios.xlsx", "TBL_USUARIOS");
     * // Pode usar diretamente:
     * List<DataModel> usuarios = sheet.getData();
     * 
     * MOTIVO:
     * Conveniência e redução de código repetitivo
     */
    public MultipleDataSheet(String excelFilePath, String defaultSheetName) {
        this.excelFilePath = excelFilePath;
        this.defaultSheetName = defaultSheetName;
    }

    // ========================================================================
    // MÉTODOS DE CONFIGURAÇÃO DE DADOS
    // ========================================================================

    /**
     * MÉTODO: setData()
     * 
     * PROPÓSITO:
     * Carrega TODOS os dados da aba padrão especificada no construtor.
     * Executa automaticamente "SELECT * FROM [defaultSheetName]".
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - No método getData() das classes filhas
     * - Quando quiser carregar todos os registros da aba padrão
     * - Como primeiro passo antes de iterar pelos dados
     * 
     * COMO USAR:
     * @Override
     * public List<DataModel> getData() {
     *     setData(); // Carrega TODOS os dados da aba padrão
     *     List<DataModel> resultados = new ArrayList<>();
     *     
     *     try {
     *         while (recordset.next()) { // Itera por TODOS os registros
     *             // Processa cada registro
     *             resultados.add(criarModeloDoRegistro());
     *         }
     *     } catch (FilloException e) {
     *         throw new DataException("Erro ao processar dados", e);
     *     }
     *     
     *     return resultados;
     * }
     * 
     * MOTIVOS:
     * 1. Simplicidade: Um método para carregar todos os dados da aba padrão
     * 2. Convenção: Padroniza a forma de carregar dados
     * 3. Menos código: Evita repetir o nome da aba
     * 4. Preparação: Prepara o recordset para iteração completa
     * 
     * DIFERENÇA DA DataSheet:
     * - DataSheet: foca no PRIMEIRO registro encontrado
     * - MultipleDataSheet: prepara para processar TODOS os registros
     * 
     * FLUXO INTERNO:
     * 1. Chama setDataFromSheetName(defaultSheetName)
     * 2. Monta query "SELECT * FROM [defaultSheetName]"
     * 3. Executa query e armazena TODOS os resultados em recordset
     * 4. Prepara para iteração pelos registros
     */
    protected void setData() {
        setDataFromSheetName(defaultSheetName);
    }

    /**
     * MÉTODO: setDataFromSheetName(String sheetName)
     * 
     * PROPÓSITO:
     * Carrega TODOS os dados de uma aba específica, diferente da aba padrão.
     * Permite trabalhar com múltiplas abas na mesma instância.
     * 
     * PARÂMETROS:
     * @param sheetName - Nome da aba/planilha a ser consultada
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Carregar dados de abas específicas
     * - Alternar entre diferentes conjuntos de dados
     * - Implementar lógica que trabalha com múltiplas abas
     * - Cenários onde a aba é definida dinamicamente
     * 
     * COMO USAR:
     * protected List<DataModel> carregarDadosPorTipo(String tipo) {
     *     String nomeAba = "TBL_" + tipo.toUpperCase();
     *     setDataFromSheetName(nomeAba); // Carrega dados da aba específica
     *     
     *     return processarTodosOsRegistros();
     * }
     * 
     * MOTIVOS:
     * 1. Flexibilidade: Permite mudar de aba sem recriar a instância
     * 2. Reutilização: Uma classe pode trabalhar com várias abas
     * 3. Dinamismo: Aba pode ser escolhida em tempo de execução
     * 4. Organização: Dados diferentes em abas separadas
     * 
     * EXEMPLO PRÁTICO:
     * // Carregar usuários ativos
     * setDataFromSheetName("TBL_USUARIOS_ATIVOS");
     * List<DataModel> ativos = getData();
     * 
     * // Carregar usuários inativos
     * setDataFromSheetName("TBL_USUARIOS_INATIVOS");
     * List<DataModel> inativos = getData();
     * 
     * FLUXO INTERNO:
     * 1. Monta query "SELECT * FROM `[sheetName]`"
     * 2. Chama setDataFromQuery(query)
     * 3. Executa a query e carrega TODOS os registros
     */
    protected void setDataFromSheetName(String sheetName) {
        String query = String.format("select * from `%s`", sheetName);
        setDataFromQuery(query);
    }

    /**
     * MÉTODO: setDataFromQuery(String query)
     * 
     * PROPÓSITO:
     * Executa uma query SQL customizada na planilha Excel e carrega
     * TODOS os registros que atendem aos critérios da consulta.
     * Máxima flexibilidade para consultas complexas.
     * 
     * PARÂMETROS:
     * @param query - Query SQL a ser executada na planilha
     * 
     * VISIBILIDADE: protected (apenas classes filhas podem usar)
     * 
     * QUANDO USAR:
     * - Consultas com WHERE, ORDER BY, LIMIT
     * - Filtros específicos nos dados
     * - Joins entre múltiplas abas
     * - Aggregações e funções SQL
     * - Consultas otimizadas para performance
     * 
     * COMO USAR:
     * protected List<DataModel> carregarUsuariosPorStatus(String status) {
     *     String query = String.format(
     *         "SELECT * FROM TBL_USUARIOS WHERE status = '%s' ORDER BY nome ASC",
     *         status
     *     );
     *     setDataFromQuery(query);
     *     
     *     return processarRegistros();
     * }
     * 
     * protected List<DataModel> carregarTop10Usuarios() {
     *     String query = "SELECT TOP 10 * FROM TBL_USUARIOS ORDER BY score DESC";
     *     setDataFromQuery(query);
     *     
     *     return processarRegistros();
     * }
     * 
     * MOTIVOS:
     * 1. Poder: Usa toda a capacidade do SQL
     * 2. Performance: Carrega apenas dados necessários
     * 3. Filtros: Aplica critérios na consulta, não na aplicação
     * 4. Ordenação: Dados já vêm ordenados da planilha
     * 5. Flexibilidade: Suporta consultas complexas
     * 
     * EXEMPLOS DE QUERIES ÚTEIS:
     * 
     * // Filtrar por múltiplos critérios
     * "SELECT * FROM TBL_USUARIOS WHERE ativo = 'S' AND tipo = 'ADMIN'"
     * 
     * // Ordenar e limitar resultados
     * "SELECT * FROM TBL_USUARIOS ORDER BY data_criacao DESC LIMIT 5"
     * 
     * // Buscar por padrão
     * "SELECT * FROM TBL_USUARIOS WHERE email LIKE '%@teste.com'"
     * 
     * // Join entre abas (se suportado)
     * "SELECT u.*, p.nome as perfil_nome FROM TBL_USUARIOS u INNER JOIN TBL_PERFIS p ON u.perfil_id = p.id"
     * 
     * FLUXO INTERNO:
     * 1. Cria instância de DataReader com try-with-resources
     * 2. Executa dataReader.executeQuery(query)
     * 3. Armazena TODOS os resultados em recordset
     * 4. Marca recordLineFound como false (dados ainda não processados)
     * 5. Fecha automaticamente a conexão
     * 6. Em caso de erro, loga e lança DataException
     * 
     * TRATAMENTO DE ERROS:
     * - Captura qualquer exceção durante execução
     * - Usa log.error() (Log4j2) para registrar o erro
     * - Lança DataException com mensagem descritiva
     * - Inclui a query que falhou para facilitar debug
     * 
     * PERFORMANCE:
     * - Queries otimizadas reduzem o tempo de carregamento
     * - Filtros na query são mais eficientes que na aplicação
     * - Ordering na query evita processamento adicional
     */
    protected void setDataFromQuery(String query) {
        try (DataReader dataReader = new DataReader(excelFilePath)) {
            recordset = dataReader.executeQuery(query);
            recordLineFound = false;
        } catch (Exception e) {
            String message = String.format("Error in execute query '%s'", query);
            log.error(message, e);
            throw new DataException(message, e);
        }
    }

    // ========================================================================
    // MÉTODO ABSTRATO
    // ========================================================================

    /**
     * MÉTODO ABSTRATO: getData()
     * 
     * PROPÓSITO:
     * Método abstrato que deve ser implementado pelas classes filhas.
     * Define como todos os dados devem ser processados e retornados
     * como uma lista de objetos DataModel.
     * 
     * RETORNO:
     * @return List<DataModel> - Lista de todos os registros processados
     * 
     * VISIBILIDADE: public abstract (deve ser implementado pelas filhas)
     * 
     * QUANDO IMPLEMENTAR:
     * - Em todas as classes que herdam de MultipleDataSheet
     * - Para definir como mapear TODOS os dados da planilha para modelos
     * - Quando precisa processar múltiplos registros
     * 
     * COMO IMPLEMENTAR:
     * @Override
     * public List<DataModel> getData() {
     *     // 1. Carregar dados
     *     setData(); // ou setDataFromSheetName() ou setDataFromQuery()
     *     
     *     // 2. Preparar lista de resultados
     *     List<DataModel> usuarios = new ArrayList<>();
     *     
     *     // 3. Iterar por TODOS os registros
     *     try {
     *         while (recordset.next()) {
     *             // 4. Mapear cada registro para um modelo
     *             UsuarioModel usuario = UsuarioModel.builder()
     *                 .nome(recordset.getField("nome"))
     *                 .email(recordset.getField("email"))
     *                 .senha(recordset.getField("senha"))
     *                 .ativo(Boolean.parseBoolean(recordset.getField("ativo")))
     *                 .build();
     *             
     *             // 5. Adicionar à lista
     *             usuarios.add(usuario);
     *         }
     *     } catch (FilloException e) {
     *         String message = "Erro ao processar registros da planilha";
     *         log.error(message, e);
     *         throw new DataException(message, e);
     *     }
     *     
     *     // 6. Retornar todos os dados processados
     *     return usuarios;
     * }
     * 
     * MOTIVOS:
     * 1. Contrato: Força as filhas a implementar processamento específico
     * 2. Type Safety: Garante retorno de lista tipada
     * 3. Polimorfismo: Permite tratar diferentes tipos uniformemente
     * 4. Flexibilidade: Cada modelo define sua lógica de mapeamento
     * 5. Escalabilidade: Suporta processamento de grandes volumes
     * 
     * DIFERENÇAS DA DataSheet:
     * - DataSheet.getData(): retorna UM objeto (T)
     * - MultipleDataSheet.getData(): retorna LISTA de objetos (List<DataModel>)
     * 
     * IMPLEMENTAÇÃO AVANÇADA:
     * @Override
     * public List<DataModel> getData() {
     *     setData();
     *     List<DataModel> resultados = new ArrayList<>();
     *     
     *     try {
     *         while (recordset.next()) {
     *             // Validação de dados
     *             String nome = recordset.getField("nome");
     *             if (nome == null || nome.trim().isEmpty()) {
     *                 log.warn("Registro com nome vazio ignorado");
     *                 continue; // Pula registros inválidos
     *             }
     *             
     *             // Transformação de dados
     *             String email = recordset.getField("email").toLowerCase();
     *             
     *             // Criação do modelo
     *             UsuarioModel usuario = UsuarioModel.builder()
     *                 .nome(nome.trim())
     *                 .email(email)
     *                 .build();
     *             
     *             resultados.add(usuario);
     *         }
     *         
     *         log.info("Processados {} registros com sucesso", resultados.size());
     *         
     *     } catch (FilloException e) {
     *         throw new DataException("Falha no processamento dos dados", e);
     *     }
     *     
     *     return resultados;
     * }
     * 
     * BOAS PRÁTICAS:
     * 1. Sempre validar dados antes de criar modelos
     * 2. Usar try-catch específico para FilloException
     * 3. Logar informações úteis sobre o processamento
     * 4. Considerar pular registros inválidos em vez de falhar
     * 5. Transformar dados conforme necessário (trim, toLowerCase, etc.)
     * 6. Retornar lista mesmo se vazia (nunca null)
     */
    public abstract List<DataModel> getData();
}
```

---

## 🎓 **RESUMO COMPARATIVO DAS CLASSES**

### **DataModel (Interface)**
- **Uso**: Interface marcadora para todos os modelos
- **Instanciação**: Implementada por classes modelo
- **Motivo**: Type safety e padronização

### **DataSheet<T> (Classe Abstrata)**
- **Uso**: Leitura de UM registro por vez
- **Instanciação**: Herdada por classes específicas
- **Motivo**: Testes simples com dados únicos

### **MultipleDataSheet (Classe Abstrata)**
- **Uso**: Leitura de MÚLTIPLOS registros
- **Instanciação**: Herdada por classes específicas  
- **Motivo**: Data-driven testing e processamento em lote

### **DataReader (Classe Concreta)**
- **Uso**: Conexão e execução de queries no Excel
- **Instanciação**: Direta com caminho do arquivo
- **Motivo**: Abstração da biblioteca Fillo

---

## 💡 **EXEMPLO COMPLETO DE USO**

```java
// 1. Modelo que implementa DataModel
public class UsuarioModel implements DataModel {
    private String nome;
    private String email;
    private String senha;
    // getters, setters, builder...
}

// 2. Classe para um registro (herda DataSheet)
public class UsuarioDataSheet extends DataSheet<UsuarioModel> {
    public UsuarioDataSheet(String excelPath) {
        super(excelPath, "TBL_USUARIOS");
    }
    
    @Override
    public UsuarioModel getData() {
        setData();
        return UsuarioModel.builder()
            .nome(getField("nome"))
            .email(getField("email"))
            .senha(getField("senha"))
            .build();
    }
}

// 3. Classe para múltiplos registros (herda MultipleDataSheet)
public class UsuarioMultipleDataSheet extends MultipleDataSheet {
    public UsuarioMultipleDataSheet(String excelPath) {
        super(excelPath, "TBL_USUARIOS");
    }
    
    @Override
    public List<DataModel> getData() {
        setData();
        List<DataModel> usuarios = new ArrayList<>();
        
        try {
            while (recordset.next()) {
                UsuarioModel usuario = UsuarioModel.builder()
                    .nome(recordset.getField("nome"))
                    .email(recordset.getField("email"))
                    .senha(recordset.getField("senha"))
                    .build();
                usuarios.add(usuario);
            }
        } catch (FilloException e) {
            throw new DataException("Erro ao processar dados", e);
        }
        
        return usuarios;
    }
}

// 4. Uso nos testes
public class UsuarioTest {
    public void testeComUmUsuario() {
        UsuarioDataSheet sheet = new UsuarioDataSheet("dados/usuarios.xlsx");
        UsuarioModel usuario = sheet.getData(); // UM usuário
        // usar dados do usuário...
    }
    
    public void testeComMultiplosUsuarios() {
        UsuarioMultipleDataSheet sheet = new UsuarioMultipleDataSheet("dados/usuarios.xlsx");
        List<DataModel> usuarios = sheet.getData(); // TODOS os usuários
        
        for (DataModel modelo : usuarios) {
            UsuarioModel usuario = (UsuarioModel) modelo;
            // executar teste para cada usuário...
        }
    }
}
```

Esta documentação fornece uma visão completa e detalhada de cada método das classes do pacote `data/`, explicando não apenas o que fazem, mas **por que existem**, **quando usar** e **como implementar** corretamente.
