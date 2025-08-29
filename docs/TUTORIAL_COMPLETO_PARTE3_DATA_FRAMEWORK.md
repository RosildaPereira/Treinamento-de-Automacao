# 🎯 **TUTORIAL COMPLETO - PARTE 3: PACOTE DATA E FRAMEWORK**
## **📋 RECEITA PARA RECRIAR PACOTE DATA, MANAGERS, CONTROLLERS E MODELS**

---

## 📋 **ÍNDICE DA PARTE 3**

### **📊 1. PACOTE DATA** - Manipulação de dados Excel/CSV
- **datasheet/** - Modelos de dados das planilhas
- **reader/** - Leitores de Excel e CSV
- **writer/** - Escritores de CSV

### **🏗️ 2. PACOTE TEST/MANAGER** - Gerenciadores thread-safe
- **TokenManager.java** - Tokens e autenticação
- **UsuarioManager.java** - Dados de usuários
- **ArtigosManager.java** - Dados de artigos
- **CategoriaManager.java** - Dados de categorias

### **📦 3. PACOTE TEST/MODEL** - Modelos de dados
- **builder/** - Builders para objetos
- **request/** - DTOs de requisição
- **response/** - DTOs de resposta

### **🎮 4. PACOTE TEST/CONTROLLERS** - Lógica de testes
- **UsuarioController.java** - Controller principal

### **📋 5. PACOTE TEST/SHEETS** - Acesso a planilhas
- **login/** - Dados de login
- **cadastro/** - Dados de cadastro
- **dados_usuario/** - Modelo de usuário

---

# 📊 **1. PACOTE DATA - MANIPULAÇÃO DE DADOS**

## **📄 DataReader.java**
**📍 Local**: `src/main/java/org/br/com/data/reader/DataReader.java`

```java
package org.br.com.data.reader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.br.com.core.exceptions.DataException;
import org.br.com.core.support.resource.ResourceUtils;

/**
 * ============================================================================
 * LEITOR DE DADOS: DataReader
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe responsável por ler dados de planilhas Excel usando a biblioteca Fillo.
 * Fornece interface simples para executar queries SQL em arquivos Excel,
 * abstraindo a complexidade da conexão e tratamento de erros.
 * 
 * QUANDO USAR:
 * - Ler dados de planilhas Excel para testes data-driven
 * - Executar queries SQL em arquivos .xlsx/.xls
 * - Buscar dados específicos baseados em critérios
 * - Integrar dados de Excel com framework de testes
 * 
 * CARACTERÍSTICAS:
 * - Auto-closeable para gerenciamento automático de recursos
 * - Tratamento de exceções específico
 * - Localização automática de arquivos
 * - Thread-safe para execução paralela
 */
public class DataReader implements AutoCloseable {

    // ========================================================================
    // ATRIBUTOS DA CLASSE
    // ========================================================================
    
    private Connection connection;
    private String filePath;

    // ========================================================================
    // CONSTRUTORES
    // ========================================================================
    
    /**
     * CONSTRUTOR: DataReader(String fileName)
     * 
     * PROPÓSITO:
     * Inicializa o leitor de dados conectando-se ao arquivo Excel especificado.
     * Usa ResourceUtils para localizar o arquivo automaticamente.
     * 
     * PARÂMETROS:
     * @param fileName - Nome do arquivo Excel (ex: "MassaDados.xlsx")
     * 
     * EXCEÇÕES:
     * @throws DataException - Se não conseguir conectar ao arquivo
     * 
     * COMO USAR:
     * try (DataReader reader = new DataReader("MassaDados.xlsx")) {
     *     // usar reader...
     * } catch (DataException e) {
     *     // tratar erro...
     * }
     */
    public DataReader(String fileName) {
        try {
            this.filePath = ResourceUtils.getPath(fileName);
            Fillo fillo = new Fillo();
            this.connection = fillo.getConnection(this.filePath);
        } catch (FilloException e) {
            throw new DataException("Erro ao conectar com arquivo: " + fileName, e);
        } catch (Exception e) {
            throw new DataException("Erro inesperado ao inicializar DataReader: " + fileName, e);
        }
    }

    // ========================================================================
    // MÉTODOS DE CONSULTA
    // ========================================================================
    
    /**
     * MÉTODO: executeQuery(String query)
     * 
     * PROPÓSITO:
     * Executa uma query SQL no arquivo Excel e retorna o resultado.
     * Permite consultas complexas com WHERE, ORDER BY, etc.
     * 
     * PARÂMETROS:
     * @param query - Query SQL para executar (ex: "SELECT * FROM TBL_CENARIOS WHERE ID_CENARIO='CT-001'")
     * 
     * RETORNO:
     * @return Recordset - Resultado da query (iterável)
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro na execução da query
     * 
     * EXEMPLOS DE QUERIES:
     * // Buscar cenário específico
     * "SELECT * FROM TBL_CENARIOS WHERE ID_CENARIO='CT-001'"
     * 
     * // Buscar usuários ativos
     * "SELECT * FROM TBL_USUARIOS WHERE STATUS='ATIVO'"
     * 
     * // Buscar com ordenação
     * "SELECT * FROM TBL_DADOS ORDER BY NOME"
     * 
     * COMO USAR:
     * try (DataReader reader = new DataReader("dados.xlsx")) {
     *     Recordset records = reader.executeQuery("SELECT * FROM TBL_CENARIOS");
     *     while (records.next()) {
     *         String id = records.getField("ID_CENARIO");
     *         String nome = records.getField("NOME");
     *         // processar dados...
     *     }
     * }
     */
    public Recordset executeQuery(String query) {
        try {
            return connection.executeQuery(query);
        } catch (FilloException e) {
            throw new DataException("Erro ao executar query: " + query, e);
        }
    }

    /**
     * MÉTODO: getField(Recordset recordset, String fieldName)
     * 
     * PROPÓSITO:
     * Extrai um campo específico do recordset com tratamento de erros.
     * Método de conveniência para acesso seguro a campos.
     * 
     * PARÂMETROS:
     * @param recordset - Recordset com os dados
     * @param fieldName - Nome do campo a extrair
     * 
     * RETORNO:
     * @return String - Valor do campo ou string vazia se não encontrado
     * 
     * COMO USAR:
     * Recordset records = reader.executeQuery("SELECT * FROM TBL_DADOS");
     * if (records.next()) {
     *     String email = reader.getField(records, "EMAIL");
     *     String senha = reader.getField(records, "SENHA");
     * }
     */
    public String getField(Recordset recordset, String fieldName) {
        try {
            String value = recordset.getField(fieldName);
            return value != null ? value.trim() : "";
        } catch (FilloException e) {
            throw new DataException("Erro ao acessar campo: " + fieldName, e);
        }
    }

    /**
     * MÉTODO: hasData(Recordset recordset)
     * 
     * PROPÓSITO:
     * Verifica se o recordset contém dados.
     * Útil para validar se uma query retornou resultados.
     * 
     * PARÂMETROS:
     * @param recordset - Recordset a verificar
     * 
     * RETORNO:
     * @return boolean - true se contém dados, false caso contrário
     * 
     * COMO USAR:
     * Recordset records = reader.executeQuery("SELECT * FROM TBL_DADOS WHERE ID='123'");
     * if (reader.hasData(records)) {
     *     // processar dados encontrados
     * } else {
     *     // lidar com ausência de dados
     * }
     */
    public boolean hasData(Recordset recordset) {
        try {
            return recordset.next();
        } catch (FilloException e) {
            return false;
        }
    }

    // ========================================================================
    // MÉTODOS UTILITÁRIOS
    // ========================================================================
    
    /**
     * MÉTODO: getFilePath()
     * 
     * PROPÓSITO:
     * Retorna o caminho completo do arquivo sendo lido.
     * Útil para logging e debugging.
     * 
     * RETORNO:
     * @return String - Caminho absoluto do arquivo
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * MÉTODO: isConnected()
     * 
     * PROPÓSITO:
     * Verifica se a conexão com o arquivo está ativa.
     * 
     * RETORNO:
     * @return boolean - true se conectado, false caso contrário
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    // ========================================================================
    // IMPLEMENTAÇÃO DE AutoCloseable
    // ========================================================================
    
    /**
     * MÉTODO: close()
     * 
     * PROPÓSITO:
     * Fecha a conexão com o arquivo Excel automaticamente.
     * Implementa AutoCloseable para uso com try-with-resources.
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro ao fechar conexão
     * 
     * NOTA:
     * Este método é chamado automaticamente quando usado com try-with-resources.
     * Não é necessário chamá-lo manualmente nesse caso.
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                throw new DataException("Erro ao fechar conexão com arquivo: " + filePath, e);
            }
        }
    }
}
```

## **📄 ExcelDataReader.java**
**📍 Local**: `src/main/java/org/br/com/test/sheets/ExcelDataReader.java`

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
 * LEITOR EXCEL: ExcelDataReader
 * ============================================================================
 * 
 * PROPÓSITO:
 * Leitor avançado de planilhas Excel usando Apache POI. Fornece funcionalidades
 * robustas para mapear colunas, buscar dados por critérios e tratar diferentes
 * tipos de células. Ideal para testes data-driven complexos.
 * 
 * QUANDO USAR:
 * - Ler planilhas Excel com estrutura complexa
 * - Buscar dados baseados em valores de lookup
 * - Tratar diferentes tipos de dados (string, número, data, fórmula)
 * - Mapear automaticamente cabeçalhos de colunas
 * 
 * CARACTERÍSTICAS:
 * - Auto-closeable para gerenciamento de recursos
 * - Mapeamento automático de colunas
 * - Tratamento robusto de tipos de células
 * - Busca por valor de lookup em colunas específicas
 */
public class ExcelDataReader implements AutoCloseable {

    // ========================================================================
    // ATRIBUTOS DA CLASSE
    // ========================================================================
    
    private final Workbook workbook;
    private final Sheet sheet;
    private final Map<String, Integer> columnIndexes;

    // ========================================================================
    // CONSTRUTOR
    // ========================================================================
    
    /**
     * CONSTRUTOR: ExcelDataReader(String filePath, String sheetName)
     * 
     * PROPÓSITO:
     * Inicializa o leitor conectando-se ao arquivo e aba específicos.
     * Mapeia automaticamente os cabeçalhos das colunas.
     * 
     * PARÂMETROS:
     * @param filePath - Caminho completo do arquivo Excel
     * @param sheetName - Nome da aba a ser lida
     * 
     * EXCEÇÕES:
     * @throws IOException - Se erro ao abrir arquivo ou aba não encontrada
     * 
     * COMO USAR:
     * try (ExcelDataReader reader = new ExcelDataReader("dados.xlsx", "TBL_CENARIOS")) {
     *     Map<String, String> dados = reader.getRowData("ID_CENARIO", "CT-001");
     * }
     */
    public ExcelDataReader(String filePath, String sheetName) throws IOException {
        this.workbook = WorkbookFactory.create(new FileInputStream(filePath));
        this.sheet = workbook.getSheet(sheetName);
        
        if (this.sheet == null) {
            throw new IOException("Aba '" + sheetName + "' não encontrada no arquivo: " + filePath);
        }
        
        this.columnIndexes = mapColumnHeaders();
    }

    // ========================================================================
    // MÉTODOS PRIVADOS
    // ========================================================================
    
    /**
     * MÉTODO: mapColumnHeaders()
     * 
     * PROPÓSITO:
     * Mapeia os cabeçalhos da primeira linha para índices de colunas.
     * Facilita o acesso aos dados por nome da coluna.
     * 
     * RETORNO:
     * @return Map<String, Integer> - Mapeamento nome -> índice
     * 
     * EXEMPLO:
     * // Se a primeira linha contém: ID_CENARIO | NOME | EMAIL
     * // O mapa retornado será: {"ID_CENARIO": 0, "NOME": 1, "EMAIL": 2}
     */
    private Map<String, Integer> mapColumnHeaders() {
        Map<String, Integer> headers = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                String headerName = getCellValueAsString(cell);
                if (!headerName.isEmpty()) {
                    headers.put(headerName.toUpperCase(), cell.getColumnIndex());
                }
            }
        }
        
        return headers;
    }

    /**
     * MÉTODO: getCellValueAsString(Cell cell)
     * 
     * PROPÓSITO:
     * Converte o valor de uma célula para String, tratando todos os tipos
     * possíveis (STRING, NUMERIC, BOOLEAN, FORMULA, etc.).
     * 
     * PARÂMETROS:
     * @param cell - Célula a ser convertida
     * 
     * RETORNO:
     * @return String - Valor da célula como string
     * 
     * TIPOS TRATADOS:
     * - STRING: retorna valor direto
     * - NUMERIC: formata números e datas
     * - BOOLEAN: converte para "true"/"false"
     * - FORMULA: avalia e retorna resultado
     * - BLANK/_NONE: retorna string vazia
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
                
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Tratar como data
                    return cell.getDateCellValue().toString();
                } else {
                    // Tratar como número
                    double numericValue = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("#");
                    return df.format(numericValue);
                }
                
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
                
            case FORMULA:
                // Avaliar fórmula e retornar resultado
                try {
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    
                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue().trim();
                        case NUMERIC:
                            DecimalFormat df = new DecimalFormat("#");
                            return df.format(cellValue.getNumberValue());
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return "";
                    }
                } catch (Exception e) {
                    return "";
                }
                
            case BLANK:
            case _NONE:
            default:
                return "";
        }
    }

    // ========================================================================
    // MÉTODOS PÚBLICOS
    // ========================================================================
    
    /**
     * MÉTODO: getRowData(String lookupColumn, String lookupValue)
     * 
     * PROPÓSITO:
     * Busca uma linha baseada em valor de lookup em coluna específica
     * e retorna todos os dados dessa linha como mapa nome->valor.
     * 
     * PARÂMETROS:
     * @param lookupColumn - Nome da coluna para busca (ex: "ID_CENARIO")
     * @param lookupValue - Valor a buscar (ex: "CT-001")
     * 
     * RETORNO:
     * @return Map<String, String> - Dados da linha encontrada
     * 
     * COMPORTAMENTO:
     * - Busca sequencial linha por linha
     * - Retorna primeira ocorrência encontrada
     * - Retorna mapa vazio se não encontrar
     * - Nomes das colunas são convertidos para maiúsculo
     * 
     * EXEMPLO:
     * // Buscar dados do cenário CT-001
     * Map<String, String> dados = reader.getRowData("ID_CENARIO", "CT-001");
     * String nome = dados.get("NOME");
     * String email = dados.get("EMAIL");
     * String senha = dados.get("SENHA");
     * 
     * TRATAMENTO DE CASOS:
     * - Coluna não existe: retorna mapa vazio
     * - Valor não encontrado: retorna mapa vazio
     * - Múltiplas ocorrências: retorna primeira
     */
    public Map<String, String> getRowData(String lookupColumn, String lookupValue) {
        Map<String, String> rowData = new HashMap<>();
        
        // Verificar se coluna existe
        Integer lookupColumnIndex = columnIndexes.get(lookupColumn.toUpperCase());
        if (lookupColumnIndex == null) {
            return rowData; // Coluna não encontrada
        }
        
        // Buscar linha com valor correspondente
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;
            
            Cell lookupCell = row.getCell(lookupColumnIndex);
            String cellValue = getCellValueAsString(lookupCell);
            
            // Verificar se valor corresponde
            if (Objects.equals(cellValue, lookupValue)) {
                // Extrair todos os dados da linha
                for (Map.Entry<String, Integer> header : columnIndexes.entrySet()) {
                    String columnName = header.getKey();
                    Integer columnIndex = header.getValue();
                    
                    Cell dataCell = row.getCell(columnIndex);
                    String dataValue = getCellValueAsString(dataCell);
                    
                    rowData.put(columnName, dataValue);
                }
                break; // Primeira ocorrência encontrada
            }
        }
        
        return rowData;
    }

    /**
     * MÉTODO: getColumnNames()
     * 
     * PROPÓSITO:
     * Retorna lista com nomes de todas as colunas mapeadas.
     * Útil para validação e debugging.
     * 
     * RETORNO:
     * @return Set<String> - Nomes das colunas
     */
    public java.util.Set<String> getColumnNames() {
        return columnIndexes.keySet();
    }

    /**
     * MÉTODO: hasColumn(String columnName)
     * 
     * PROPÓSITO:
     * Verifica se uma coluna específica existe na planilha.
     * 
     * PARÂMETROS:
     * @param columnName - Nome da coluna a verificar
     * 
     * RETORNO:
     * @return boolean - true se coluna existe
     */
    public boolean hasColumn(String columnName) {
        return columnIndexes.containsKey(columnName.toUpperCase());
    }

    /**
     * MÉTODO: getRowCount()
     * 
     * PROPÓSITO:
     * Retorna número total de linhas na planilha (excluindo cabeçalho).
     * 
     * RETORNO:
     * @return int - Número de linhas de dados
     */
    public int getRowCount() {
        return sheet.getLastRowNum(); // Linha 0 é cabeçalho
    }

    // ========================================================================
    // IMPLEMENTAÇÃO DE AutoCloseable
    // ========================================================================
    
    /**
     * MÉTODO: close()
     * 
     * PROPÓSITO:
     * Fecha o workbook e libera recursos.
     * Implementa AutoCloseable para uso com try-with-resources.
     * 
     * EXCEÇÕES:
     * @throws IOException - Se erro ao fechar workbook
     */
    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}
```

## **📄 CsvWriter.java**
**📍 Local**: `src/main/java/org/br/com/data/writer/CsvWriter.java`

```java
package org.br.com.data.writer;

import com.opencsv.CSVWriter;
import org.br.com.core.exceptions.DataException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * ============================================================================
 * ESCRITOR CSV: CsvWriter
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe utilitária para escrever dados em arquivos CSV usando OpenCSV.
 * Facilita a geração de arquivos CSV para relatórios, exportação de dados
 * e integração com ferramentas externas.
 * 
 * QUANDO USAR:
 * - Exportar resultados de testes para CSV
 * - Gerar relatórios de execução
 * - Criar arquivos de dados para análise
 * - Integrar com ferramentas de BI/análise
 * 
 * CARACTERÍSTICAS:
 * - Auto-closeable para gerenciamento de recursos
 * - Suporte a múltiplas linhas
 * - Tratamento de exceções específico
 * - Formatação CSV padrão
 */
public class CsvWriter implements AutoCloseable {

    // ========================================================================
    // ATRIBUTOS DA CLASSE
    // ========================================================================
    
    private CSVWriter csvWriter;
    private String filePath;

    // ========================================================================
    // CONSTRUTOR
    // ========================================================================
    
    /**
     * CONSTRUTOR: CsvWriter(String filePath)
     * 
     * PROPÓSITO:
     * Inicializa o escritor CSV para o arquivo especificado.
     * Cria o arquivo se não existir, sobrescreve se existir.
     * 
     * PARÂMETROS:
     * @param filePath - Caminho completo do arquivo CSV a criar
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro ao criar/abrir arquivo
     * 
     * COMO USAR:
     * try (CsvWriter writer = new CsvWriter("relatorio.csv")) {
     *     writer.writeHeader(new String[]{"ID", "Nome", "Status"});
     *     writer.writeRow(new String[]{"001", "Teste 1", "PASSOU"});
     * }
     */
    public CsvWriter(String filePath) {
        try {
            this.filePath = filePath;
            FileWriter fileWriter = new FileWriter(filePath);
            this.csvWriter = new CSVWriter(fileWriter);
        } catch (IOException e) {
            throw new DataException("Erro ao criar arquivo CSV: " + filePath, e);
        }
    }

    // ========================================================================
    // MÉTODOS DE ESCRITA
    // ========================================================================
    
    /**
     * MÉTODO: writeHeader(String[] headers)
     * 
     * PROPÓSITO:
     * Escreve linha de cabeçalho no arquivo CSV.
     * Deve ser chamado antes de escrever dados.
     * 
     * PARÂMETROS:
     * @param headers - Array com nomes das colunas
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro na escrita
     * 
     * EXEMPLO:
     * writer.writeHeader(new String[]{"ID_Cenario", "Nome", "Status", "Duracao"});
     */
    public void writeHeader(String[] headers) {
        try {
            csvWriter.writeNext(headers);
            csvWriter.flush();
        } catch (IOException e) {
            throw new DataException("Erro ao escrever cabeçalho CSV", e);
        }
    }

    /**
     * MÉTODO: writeRow(String[] rowData)
     * 
     * PROPÓSITO:
     * Escreve uma linha de dados no arquivo CSV.
     * Dados são escritos na ordem das colunas do cabeçalho.
     * 
     * PARÂMETROS:
     * @param rowData - Array com valores da linha
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro na escrita
     * 
     * EXEMPLO:
     * writer.writeRow(new String[]{"CT-001", "Login válido", "PASSOU", "2.5s"});
     */
    public void writeRow(String[] rowData) {
        try {
            csvWriter.writeNext(rowData);
            csvWriter.flush();
        } catch (IOException e) {
            throw new DataException("Erro ao escrever linha CSV", e);
        }
    }

    /**
     * MÉTODO: writeRows(List<String[]> allRows)
     * 
     * PROPÓSITO:
     * Escreve múltiplas linhas de uma vez no arquivo CSV.
     * Mais eficiente para grandes volumes de dados.
     * 
     * PARÂMETROS:
     * @param allRows - Lista com arrays de dados
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro na escrita
     * 
     * EXEMPLO:
     * List<String[]> dados = Arrays.asList(
     *     new String[]{"CT-001", "Login", "PASSOU"},
     *     new String[]{"CT-002", "Logout", "PASSOU"},
     *     new String[]{"CT-003", "Cadastro", "FALHOU"}
     * );
     * writer.writeRows(dados);
     */
    public void writeRows(List<String[]> allRows) {
        try {
            csvWriter.writeAll(allRows);
            csvWriter.flush();
        } catch (IOException e) {
            throw new DataException("Erro ao escrever múltiplas linhas CSV", e);
        }
    }

    // ========================================================================
    // MÉTODOS UTILITÁRIOS
    // ========================================================================
    
    /**
     * MÉTODO: flush()
     * 
     * PROPÓSITO:
     * Força a escrita dos dados em buffer para o arquivo.
     * Garante que dados sejam persistidos imediatamente.
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro no flush
     */
    public void flush() {
        try {
            csvWriter.flush();
        } catch (IOException e) {
            throw new DataException("Erro ao fazer flush do CSV", e);
        }
    }

    /**
     * MÉTODO: getFilePath()
     * 
     * PROPÓSITO:
     * Retorna o caminho do arquivo sendo escrito.
     * Útil para logging e referência.
     * 
     * RETORNO:
     * @return String - Caminho do arquivo
     */
    public String getFilePath() {
        return this.filePath;
    }

    // ========================================================================
    // IMPLEMENTAÇÃO DE AutoCloseable
    // ========================================================================
    
    /**
     * MÉTODO: close()
     * 
     * PROPÓSITO:
     * Fecha o escritor CSV e libera recursos.
     * Garante que todos os dados sejam escritos antes de fechar.
     * 
     * EXCEÇÕES:
     * @throws DataException - Se erro ao fechar
     */
    @Override
    public void close() {
        if (csvWriter != null) {
            try {
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                throw new DataException("Erro ao fechar arquivo CSV: " + filePath, e);
            }
        }
    }
}
```

---

# 🏗️ **2. PACOTE TEST/MANAGER - GERENCIADORES THREAD-SAFE**

## **📄 TokenManager.java**
**📍 Local**: `src/main/java/org/br/com/test/manager/TokenManager.java`

```java
package org.br.com.test.manager;

/**
 * ============================================================================
 * GERENCIADOR: TokenManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia tokens de autenticação e IDs de usuário de forma thread-safe.
 * Utiliza ThreadLocal para garantir isolamento de dados entre threads
 * paralelas, evitando contaminação de dados entre cenários diferentes.
 * 
 * QUANDO USAR:
 * - Armazenar tokens de autenticação durante cenários
 * - Manter ID do usuário logado para requisições subsequentes
 * - Compartilhar dados de autenticação entre steps
 * - Garantir isolamento em execução paralela
 * 
 * CARACTERÍSTICAS:
 * - Thread-safe usando ThreadLocal
 * - Métodos estáticos para facilidade de uso
 * - Limpeza automática de dados
 * - Isolamento completo entre threads
 */
public class TokenManager {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE
    // ========================================================================
    
    /**
     * ATRIBUTO: token
     * 
     * PROPÓSITO: Armazena token de autenticação por thread
     * TIPO: ThreadLocal<String>
     * THREAD-SAFETY: Cada thread tem sua própria instância
     * USO: Manter token JWT/Bearer para requisições autenticadas
     */
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    /**
     * ATRIBUTO: userId
     * 
     * PROPÓSITO: Armazena ID do usuário logado por thread
     * TIPO: ThreadLocal<String>
     * THREAD-SAFETY: Cada thread tem sua própria instância
     * USO: Identificar usuário para operações específicas
     */
    private static final ThreadLocal<String> userId = new ThreadLocal<>();

    // ========================================================================
    // MÉTODOS DE GERENCIAMENTO DE TOKEN
    // ========================================================================
    
    /**
     * MÉTODO: getToken()
     * 
     * PROPÓSITO:
     * Recupera o token de autenticação da thread atual.
     * Retorna null se nenhum token foi definido.
     * 
     * RETORNO:
     * @return String - Token de autenticação ou null
     * 
     * QUANDO USAR:
     * - Adicionar token ao header Authorization
     * - Verificar se usuário está autenticado
     * - Logs de requisições autenticadas
     * 
     * EXEMPLO:
     * String authToken = TokenManager.getToken();
     * if (authToken != null) {
     *     given()
     *         .header("Authorization", "Bearer " + authToken)
     *         .when()
     *         .get("/api/usuarios");
     * }
     */
    public static String getToken() {
        return token.get();
    }

    /**
     * MÉTODO: setToken(String tk)
     * 
     * PROPÓSITO:
     * Define o token de autenticação para a thread atual.
     * Substitui token anterior se existir.
     * 
     * PARÂMETROS:
     * @param tk - Token de autenticação a armazenar
     * 
     * QUANDO USAR:
     * - Após login bem-sucedido
     * - Ao renovar token expirado
     * - Em setup de cenários autenticados
     * 
     * EXEMPLO:
     * // Após login
     * Response loginResponse = given()
     *     .body(loginRequest)
     *     .post("/auth/login");
     * 
     * String token = loginResponse.jsonPath().getString("token");
     * TokenManager.setToken(token);
     */
    public static void setToken(String tk) {
        token.set(tk);
    }

    // ========================================================================
    // MÉTODOS DE GERENCIAMENTO DE USER ID
    // ========================================================================
    
    /**
     * MÉTODO: getUserId()
     * 
     * PROPÓSITO:
     * Recupera o ID do usuário logado da thread atual.
     * Retorna null se nenhum ID foi definido.
     * 
     * RETORNO:
     * @return String - ID do usuário ou null
     * 
     * QUANDO USAR:
     * - Buscar dados específicos do usuário
     * - Operações que requerem ID do usuário
     * - Validações de propriedade de recursos
     * 
     * EXEMPLO:
     * String idUsuario = TokenManager.getUserId();
     * if (idUsuario != null) {
     *     given()
     *         .header("Authorization", "Bearer " + TokenManager.getToken())
     *         .when()
     *         .get("/api/usuarios/" + idUsuario)
     *         .then()
     *         .statusCode(200);
     * }
     */
    public static String getUserId() {
        return userId.get();
    }

    /**
     * MÉTODO: setUserId(String id)
     * 
     * PROPÓSITO:
     * Define o ID do usuário para a thread atual.
     * Substitui ID anterior se existir.
     * 
     * PARÂMETROS:
     * @param id - ID do usuário a armazenar
     * 
     * QUANDO USAR:
     * - Após login ou cadastro bem-sucedido
     * - Ao extrair ID da resposta de login
     * - Em setup de cenários com usuário específico
     * 
     * EXEMPLO:
     * // Extrair ID da resposta de login
     * Response loginResponse = given()
     *     .body(loginRequest)
     *     .post("/auth/login");
     * 
     * String userId = loginResponse.jsonPath().getString("user.id");
     * TokenManager.setUserId(userId);
     */
    public static void setUserId(String id) {
        userId.set(id);
    }

    // ========================================================================
    // MÉTODOS DE LIMPEZA
    // ========================================================================
    
    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove token e ID do usuário da thread atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
     * 
     * QUANDO USAR:
     * - Ao final de cada cenário (@After hook)
     * - Antes de logout
     * - Em limpeza de dados entre testes
     * - Para garantir isolamento entre cenários
     * 
     * IMPORTANTE:
     * Este método DEVE ser chamado ao final de cada cenário para
     * evitar que dados de um teste contaminem o próximo teste.
     * 
     * EXEMPLO:
     * @After
     * public void tearDown() {
     *     TokenManager.remove();
     *     // outras limpezas...
     * }
     */
    public static void remove() {
        token.remove();
        userId.remove();
    }

    // ========================================================================
    // MÉTODOS UTILITÁRIOS
    // ========================================================================
    
    /**
     * MÉTODO: isAuthenticated()
     * 
     * PROPÓSITO:
     * Verifica se a thread atual possui token válido.
     * Conveniência para validar estado de autenticação.
     * 
     * RETORNO:
     * @return boolean - true se token existe e não é vazio
     * 
     * EXEMPLO:
     * if (TokenManager.isAuthenticated()) {
     *     // fazer requisição autenticada
     * } else {
     *     // fazer login primeiro
     * }
     */
    public static boolean isAuthenticated() {
        String currentToken = getToken();
        return currentToken != null && !currentToken.trim().isEmpty();
    }

    /**
     * MÉTODO: hasUserId()
     * 
     * PROPÓSITO:
     * Verifica se a thread atual possui ID de usuário.
     * Conveniência para validar se ID está disponível.
     * 
     * RETORNO:
     * @return boolean - true se ID existe e não é vazio
     * 
     * EXEMPLO:
     * if (TokenManager.hasUserId()) {
     *     String id = TokenManager.getUserId();
     *     // usar ID para operações
     * }
     */
    public static boolean hasUserId() {
        String currentUserId = getUserId();
        return currentUserId != null && !currentUserId.trim().isEmpty();
    }

    /**
     * MÉTODO: getAuthorizationHeader()
     * 
     * PROPÓSITO:
     * Retorna header Authorization formatado para uso direto.
     * Conveniência para evitar concatenação manual.
     * 
     * RETORNO:
     * @return String - Header formatado "Bearer <token>" ou null
     * 
     * EXEMPLO:
     * String authHeader = TokenManager.getAuthorizationHeader();
     * if (authHeader != null) {
     *     given()
     *         .header("Authorization", authHeader)
     *         .when()
     *         .get("/api/protected");
     * }
     */
    public static String getAuthorizationHeader() {
        String currentToken = getToken();
        if (currentToken != null && !currentToken.trim().isEmpty()) {
            return "Bearer " + currentToken;
        }
        return null;
    }
}
```

## **📄 UsuarioManager.java**
**📍 Local**: `src/main/java/org/br/com/test/manager/UsuarioManager.java`

```java
package org.br.com.test.manager;

import org.br.com.test.model.request.UsuarioRequest;

/**
 * ============================================================================
 * GERENCIADOR: UsuarioManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados específicos do usuário durante a execução de cenários.
 * Utiliza ThreadLocal para garantir isolamento thread-safe dos dados,
 * permitindo execução paralela sem contaminação de dados.
 * 
 * QUANDO USAR:
 * - Armazenar dados do usuário atual do cenário
 * - Compartilhar informações entre steps
 * - Manter estado do usuário durante o teste
 * - Criar objetos UsuarioRequest a partir dos dados
 * 
 * CARACTERÍSTICAS:
 * - Thread-safe com ThreadLocal
 * - Armazena dados completos do usuário
 * - Métodos de conveniência para UsuarioRequest
 * - Limpeza automática de dados
 */
public class UsuarioManager {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE
    // ========================================================================
    
    private static ThreadLocal<String> emailUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> senhaUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> idUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeCompletoUsuario = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeUsuario = new ThreadLocal<String>();

    // ========================================================================
    // MÉTODOS DE EMAIL
    // ========================================================================
    
    /**
     * MÉTODO: getEmailUsuario()
     * 
     * PROPÓSITO:
     * Recupera o email do usuário da thread atual.
     * 
     * RETORNO:
     * @return String - Email do usuário ou null
     */
    public static String getEmailUsuario() {
        return emailUsuario.get();
    }

    /**
     * MÉTODO: setEmailUsuario(String tk)
     * 
     * PROPÓSITO:
     * Define o email do usuário para a thread atual.
     * 
     * PARÂMETROS:
     * @param tk - Email do usuário
     */
    public static void setEmailUsuario(String tk) {
        emailUsuario.set(tk);
    }

    // ========================================================================
    // MÉTODOS DE SENHA
    // ========================================================================
    
    /**
     * MÉTODO: getSenhaUsuario()
     * 
     * PROPÓSITO:
     * Recupera a senha do usuário da thread atual.
     * 
     * RETORNO:
     * @return String - Senha do usuário ou null
     */
    public static String getSenhaUsuario() {
        return senhaUsuario.get();
    }

    /**
     * MÉTODO: setSenhaUsuario(String tk)
     * 
     * PROPÓSITO:
     * Define a senha do usuário para a thread atual.
     * 
     * PARÂMETROS:
     * @param tk - Senha do usuário
     */
    public static void setSenhaUsuario(String tk) {
        senhaUsuario.set(tk);
    }

    // ========================================================================
    // MÉTODOS DE ID
    // ========================================================================
    
    /**
     * MÉTODO: getIdUsuario()
     * 
     * PROPÓSITO:
     * Recupera o ID do usuário da thread atual.
     * 
     * RETORNO:
     * @return String - ID do usuário ou null
     */
    public static String getIdUsuario() {
        return idUsuario.get();
    }

    /**
     * MÉTODO: setIdUsuario(String tk)
     * 
     * PROPÓSITO:
     * Define o ID do usuário para a thread atual.
     * 
     * PARÂMETROS:
     * @param tk - ID do usuário
     */
    public static void setIdUsuario(String tk) {
        idUsuario.set(tk);
    }

    // ========================================================================
    // MÉTODOS DE NOME COMPLETO
    // ========================================================================
    
    /**
     * MÉTODO: getNomeCompletoUsuario()
     * 
     * PROPÓSITO:
     * Recupera o nome completo do usuário da thread atual.
     * 
     * RETORNO:
     * @return String - Nome completo ou null
     */
    public static String getNomeCompletoUsuario() {
        return nomeCompletoUsuario.get();
    }

    /**
     * MÉTODO: setNomeCompletoUsuario(String nome)
     * 
     * PROPÓSITO:
     * Define o nome completo do usuário para a thread atual.
     * 
     * PARÂMETROS:
     * @param nome - Nome completo do usuário
     */
    public static void setNomeCompletoUsuario(String nome) {
        nomeCompletoUsuario.set(nome);
    }

    // ========================================================================
    // MÉTODOS DE NOME DE USUÁRIO
    // ========================================================================
    
    /**
     * MÉTODO: getNomeUsuario()
     * 
     * PROPÓSITO:
     * Recupera o nome de usuário (username) da thread atual.
     * 
     * RETORNO:
     * @return String - Nome de usuário ou null
     */
    public static String getNomeUsuario() {
        return nomeUsuario.get();
    }

    /**
     * MÉTODO: setNomeUsuario(String nome)
     * 
     * PROPÓSITO:
     * Define o nome de usuário para a thread atual.
     * 
     * PARÂMETROS:
     * @param nome - Nome de usuário
     */
    public static void setNomeUsuario(String nome) {
        nomeUsuario.set(nome);
    }

    // ========================================================================
    // MÉTODOS DE LIMPEZA
    // ========================================================================
    
    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove todos os dados do usuário da thread atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
     * 
     * QUANDO USAR:
     * - Ao final de cada cenário (@After hook)
     * - Em limpeza de dados entre testes
     * - Para garantir isolamento entre cenários
     */
    public static void remove() {
        emailUsuario.remove();
        senhaUsuario.remove();
        idUsuario.remove();
        nomeCompletoUsuario.remove();
        nomeUsuario.remove();
    }

    // ========================================================================
    // MÉTODOS DE CONVENIÊNCIA
    // ========================================================================
    
    /**
     * MÉTODO: getUsuarioAtual()
     * 
     * PROPÓSITO:
     * Cria um objeto UsuarioRequest com todos os dados atuais do usuário.
     * Conveniência para criar requests sem acessar campos individuais.
     * 
     * RETORNO:
     * @return UsuarioRequest - Objeto com dados completos ou null se vazio
     * 
     * EXEMPLO:
     * UsuarioRequest usuario = UsuarioManager.getUsuarioAtual();
     * if (usuario != null) {
     *     Response response = given()
     *         .body(usuario)
     *         .post("/api/usuarios");
     * }
     */
    public static UsuarioRequest getUsuarioAtual() {
        // Verificar se pelo menos email está definido
        if (getEmailUsuario() == null) {
            return null;
        }
        
        return UsuarioRequest.builder()
                .email(getEmailUsuario())
                .senha(getSenhaUsuario())
                .nomeCompleto(getNomeCompletoUsuario())
                .nomeUsuario(getNomeUsuario())
                .build();
    }

    /**
     * MÉTODO: setUsuarioCompleto(UsuarioRequest usuario)
     * 
     * PROPÓSITO:
     * Define todos os dados do usuário a partir de um objeto UsuarioRequest.
     * Conveniência para definir múltiplos campos de uma vez.
     * 
     * PARÂMETROS:
     * @param usuario - Objeto com dados do usuário
     * 
     * EXEMPLO:
     * UsuarioRequest dadosUsuario = lerDadosDaPlanilha();
     * UsuarioManager.setUsuarioCompleto(dadosUsuario);
     */
    public static void setUsuarioCompleto(UsuarioRequest usuario) {
        if (usuario != null) {
            setEmailUsuario(usuario.getEmail());
            setSenhaUsuario(usuario.getSenha());
            setNomeCompletoUsuario(usuario.getNomeCompleto());
            setNomeUsuario(usuario.getNomeUsuario());
        }
    }

    /**
     * MÉTODO: hasUsuarioData()
     * 
     * PROPÓSITO:
     * Verifica se existe pelo menos um dado de usuário definido.
     * 
     * RETORNO:
     * @return boolean - true se algum dado existe
     */
    public static boolean hasUsuarioData() {
        return getEmailUsuario() != null || 
               getSenhaUsuario() != null || 
               getIdUsuario() != null || 
               getNomeCompletoUsuario() != null || 
               getNomeUsuario() != null;
    }
}
```

## **📄 ArtigosManager.java**
**📍 Local**: `src/main/java/org/br/com/test/manager/ArtigosManager.java`

```java
package org.br.com.test.manager;

import io.restassured.response.Response;

/**
 * ============================================================================
 * GERENCIADOR: ArtigosManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados relacionados a artigos durante a execução de cenários.
 * Armazena IDs, nomes e a última resposta de API de forma thread-safe
 * para permitir reutilização em steps subsequentes.
 * 
 * QUANDO USAR:
 * - Armazenar dados de artigos entre steps
 * - Manter IDs para operações CRUD
 * - Compartilhar resposta da API entre validações
 * - Relacionar artigos com autores e categorias
 */
public class ArtigosManager {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE
    // ========================================================================
    
    private static final ThreadLocal<String> artigoId = new ThreadLocal<String>();
    private static final ThreadLocal<String> autorId = new ThreadLocal<String>();
    private static final ThreadLocal<String> categoriaId = new ThreadLocal<String>();
    private static final ThreadLocal<String> nomeCategoria = new ThreadLocal<String>();
    private static final ThreadLocal<String> nomeAutor = new ThreadLocal<String>();
    private static final ThreadLocal<Response> response = new ThreadLocal<Response>();

    // ========================================================================
    // MÉTODOS DE ARTIGO
    // ========================================================================
    
    /**
     * MÉTODO: getArtigoId()
     * 
     * PROPÓSITO:
     * Recupera o ID do artigo atual da thread.
     * 
     * RETORNO:
     * @return String - ID do artigo ou null
     */
    public static String getArtigoId() {
        return artigoId.get();
    }

    /**
     * MÉTODO: setArtigoId(String id)
     * 
     * PROPÓSITO:
     * Define o ID do artigo para a thread atual.
     * 
     * PARÂMETROS:
     * @param id - ID do artigo
     */
    public static void setArtigoId(String id) {
        artigoId.set(id);
    }

    // ========================================================================
    // MÉTODOS DE AUTOR
    // ========================================================================
    
    /**
     * MÉTODO: getAutorId()
     * 
     * PROPÓSITO:
     * Recupera o ID do autor do artigo atual.
     * 
     * RETORNO:
     * @return String - ID do autor ou null
     */
    public static String getAutorId() {
        return autorId.get();
    }

    /**
     * MÉTODO: setAutorId(String id)
     * 
     * PROPÓSITO:
     * Define o ID do autor para a thread atual.
     * 
     * PARÂMETROS:
     * @param id - ID do autor
     */
    public static void setAutorId(String id) {
        autorId.set(id);
    }

    /**
     * MÉTODO: getNomeAutor()
     * 
     * PROPÓSITO:
     * Recupera o nome do autor do artigo atual.
     * 
     * RETORNO:
     * @return String - Nome do autor ou null
     */
    public static String getNomeAutor() {
        return nomeAutor.get();
    }

    /**
     * MÉTODO: setNomeAutor(String nome)
     * 
     * PROPÓSITO:
     * Define o nome do autor para a thread atual.
     * 
     * PARÂMETROS:
     * @param nome - Nome do autor
     */
    public static void setNomeAutor(String nome) {
        nomeAutor.set(nome);
    }

    // ========================================================================
    // MÉTODOS DE CATEGORIA
    // ========================================================================
    
    /**
     * MÉTODO: getCategoriaId()
     * 
     * PROPÓSITO:
     * Recupera o ID da categoria do artigo atual.
     * 
     * RETORNO:
     * @return String - ID da categoria ou null
     */
    public static String getCategoriaId() {
        return categoriaId.get();
    }

    /**
     * MÉTODO: setCategoriaId(String id)
     * 
     * PROPÓSITO:
     * Define o ID da categoria para a thread atual.
     * 
     * PARÂMETROS:
     * @param id - ID da categoria
     */
    public static void setCategoriaId(String id) {
        categoriaId.set(id);
    }

    /**
     * MÉTODO: getNomeCategoria()
     * 
     * PROPÓSITO:
     * Recupera o nome da categoria do artigo atual.
     * 
     * RETORNO:
     * @return String - Nome da categoria ou null
     */
    public static String getNomeCategoria() {
        return nomeCategoria.get();
    }

    /**
     * MÉTODO: setNomeCategoria(String nome)
     * 
     * PROPÓSITO:
     * Define o nome da categoria para a thread atual.
     * 
     * PARÂMETROS:
     * @param nome - Nome da categoria
     */
    public static void setNomeCategoria(String nome) {
        nomeCategoria.set(nome);
    }

    // ========================================================================
    // MÉTODOS DE RESPONSE
    // ========================================================================
    
    /**
     * MÉTODO: getResponse()
     * 
     * PROPÓSITO:
     * Recupera a última resposta de API da thread atual.
     * 
     * RETORNO:
     * @return Response - Última resposta REST Assured ou null
     */
    public static Response getResponse() {
        return response.get();
    }

    /**
     * MÉTODO: setResponse(Response resp)
     * 
     * PROPÓSITO:
     * Define a resposta de API para a thread atual.
     * 
     * PARÂMETROS:
     * @param resp - Resposta REST Assured
     */
    public static void setResponse(Response resp) {
        response.set(resp);
    }

    // ========================================================================
    // MÉTODOS DE LIMPEZA
    // ========================================================================
    
    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove todos os dados de artigos da thread atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
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

## **📄 CategoriaManager.java**
**📍 Local**: `src/main/java/org/br/com/test/manager/CategoriaManager.java`

```java
package org.br.com.test.manager;

/**
 * ============================================================================
 * GERENCIADOR: CategoriaManager
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia dados específicos de categorias durante a execução de cenários.
 * Utiliza ThreadLocal para garantir isolamento thread-safe dos dados,
 * permitindo armazenar ID, nome e descrição de categorias.
 * 
 * QUANDO USAR:
 * - Armazenar dados de categoria entre steps
 * - Manter ID para operações CRUD em categorias
 * - Compartilhar informações de categoria entre validações
 * - Relacionar categorias com artigos
 */
public class CategoriaManager {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE
    // ========================================================================
    
    private static ThreadLocal<String> categoriaId = new ThreadLocal<String>();
    private static ThreadLocal<String> nomeCategoria = new ThreadLocal<String>();
    private static ThreadLocal<String> descricaoCategoria = new ThreadLocal<String>();

    // ========================================================================
    // MÉTODOS DE ID DA CATEGORIA
    // ========================================================================
    
    /**
     * MÉTODO: getCategoriaId()
     * 
     * PROPÓSITO:
     * Recupera o ID da categoria da thread atual.
     * 
     * RETORNO:
     * @return String - ID da categoria ou null
     */
    public static String getCategoriaId() {
        return categoriaId.get();
    }

    /**
     * MÉTODO: setCategoriaId(String id)
     * 
     * PROPÓSITO:
     * Define o ID da categoria para a thread atual.
     * 
     * PARÂMETROS:
     * @param id - ID da categoria
     */
    public static void setCategoriaId(String id) {
        categoriaId.set(id);
    }

    // ========================================================================
    // MÉTODOS DE NOME DA CATEGORIA
    // ========================================================================
    
    /**
     * MÉTODO: getNomeCategoria()
     * 
     * PROPÓSITO:
     * Recupera o nome da categoria da thread atual.
     * 
     * RETORNO:
     * @return String - Nome da categoria ou null
     */
    public static String getNomeCategoria() {
        return nomeCategoria.get();
    }

    /**
     * MÉTODO: setNomeCategoria(String nome)
     * 
     * PROPÓSITO:
     * Define o nome da categoria para a thread atual.
     * 
     * PARÂMETROS:
     * @param nome - Nome da categoria
     */
    public static void setNomeCategoria(String nome) {
        nomeCategoria.set(nome);
    }

    // ========================================================================
    // MÉTODOS DE DESCRIÇÃO DA CATEGORIA
    // ========================================================================
    
    /**
     * MÉTODO: getDescricaoCategoria()
     * 
     * PROPÓSITO:
     * Recupera a descrição da categoria da thread atual.
     * 
     * RETORNO:
     * @return String - Descrição da categoria ou null
     */
    public static String getDescricaoCategoria() {
        return descricaoCategoria.get();
    }

    /**
     * MÉTODO: setDescricaoCategoria(String descricao)
     * 
     * PROPÓSITO:
     * Define a descrição da categoria para a thread atual.
     * 
     * PARÂMETROS:
     * @param descricao - Descrição da categoria
     */
    public static void setDescricaoCategoria(String descricao) {
        descricaoCategoria.set(descricao);
    }

    // ========================================================================
    // MÉTODOS DE LIMPEZA
    // ========================================================================
    
    /**
     * MÉTODO: remove()
     * 
     * PROPÓSITO:
     * Remove todos os dados de categoria da thread atual.
     * CRÍTICO para evitar vazamento de dados entre cenários.
     */
    public static void remove() {
        categoriaId.remove();
        nomeCategoria.remove();
        descricaoCategoria.remove();
    }
}
```

---

# 📦 **3. PACOTE TEST/MODEL - MODELOS DE DADOS**

## **📄 TagBuilder.java**
**📍 Local**: `src/main/java/org/br/com/test/model/builder/TagBuilder.java`

```java
package org.br.com.test.model.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * BUILDER: TagBuilder
 * ============================================================================
 * 
 * PROPÓSITO:
 * Builder para configurar critérios de processamento de tags a partir
 * de planilhas Excel. Facilita a criação de objetos para execução
 * seletiva de testes baseada em dados externos.
 * 
 * QUANDO USAR:
 * - Configurar execução de testes baseada em planilhas
 * - Definir critérios de seleção de cenários
 * - Integrar com TagProcessor para processamento de tags
 * - Criar pipelines de execução dinâmica
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagBuilder {

    /**
     * CAMPO: abaAnalista
     * 
     * PROPÓSITO:
     * Nome da aba na planilha Excel que contém os critérios de execução.
     * 
     * TIPO: String
     * EXEMPLO: "TBL_EXECUCAO", "Criterios_Regressao"
     * 
     * USO:
     * Define qual aba da planilha será lida para extrair os critérios
     * de quais cenários devem ser executados.
     */
    private String abaAnalista;

    /**
     * CAMPO: execution
     * 
     * PROPÓSITO:
     * Critério/valor que determina quais cenários devem ser executados.
     * 
     * TIPO: String
     * EXEMPLO: "S" (sim), "EXECUTAR", "ATIVO"
     * 
     * USO:
     * Valor usado para filtrar quais linhas da planilha representam
     * cenários que devem ser incluídos na execução atual.
     */
    private String execution;

    /**
     * EXEMPLO DE USO:
     * 
     * // Configurar para executar cenários marcados como "S" na aba "TBL_EXECUCAO"
     * TagBuilder criterios = TagBuilder.builder()
     *     .abaAnalista("TBL_EXECUCAO")
     *     .execution("S")
     *     .build();
     * 
     * // Processar tags baseadas nos critérios
     * TagProcessor processor = new TagProcessor();
     * processor.start(criterios);
     * 
     * INTEGRAÇÃO COM PLANILHA:
     * A planilha deve ter estrutura como:
     * | CENARIO     | TAG       | EXECUTAR |
     * |-------------|-----------|----------|
     * | Login       | @CT-001   | S        |
     * | Cadastro    | @CT-002   | N        |
     * | Busca       | @CT-003   | S        |
     * 
     * Com execution="S", apenas Login e Busca serão incluídos.
     */
}
```

## **📄 LoginRequest.java**
**📍 Local**: `src/main/java/org/br/com/test/model/request/LoginRequest.java`

```java
package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * REQUEST DTO: LoginRequest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Data Transfer Object para requisições de login/autenticação.
 * Encapsula credenciais do usuário de forma padronizada e type-safe,
 * facilitando serialização JSON e validação.
 * 
 * QUANDO USAR:
 * - Requisições de login em APIs
 * - Autenticação com email/senha
 * - Geração de tokens de acesso
 * - Testes de autenticação
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email do usuário para identificação
     * TIPO: String
     * VALIDAÇÕES: Deve ser formato de email válido
     * 
     * EXEMPLO: "usuario@teste.com"
     */
    private String email;

    /**
     * CAMPO: senha
     * 
     * PROPÓSITO: Senha do usuário para autenticação
     * TIPO: String
     * SEGURANÇA: NUNCA logar este campo
     * 
     * EXEMPLO: "senha123"
     */
    private String senha;

    /**
     * EXEMPLO DE USO:
     * 
     * // Criar requisição de login
     * LoginRequest loginReq = LoginRequest.builder()
     *     .email("admin@sistema.com")
     *     .senha("senha123")
     *     .build();
     * 
     * // Fazer requisição HTTP
     * Response response = given()
     *     .contentType(ContentType.JSON)
     *     .body(loginReq)
     *     .when()
     *     .post("/auth/login");
     * 
     * JSON GERADO:
     * {
     *     "email": "admin@sistema.com",
     *     "senha": "senha123"
     * }
     */
}
```

## **📄 UsuarioRequest.java**
**📍 Local**: `src/main/java/org/br/com/test/model/request/UsuarioRequest.java`

```java
package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * REQUEST DTO: UsuarioRequest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Data Transfer Object para requisições de criação e atualização de usuários.
 * Encapsula todos os dados necessários para operações CRUD de usuários
 * de forma padronizada e type-safe.
 * 
 * QUANDO USAR:
 * - Cadastro de novos usuários
 * - Atualização de dados de usuários
 * - Testes de validação de campos
 * - Requisições POST/PUT para /usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    /**
     * CAMPO: nomeCompleto
     * 
     * PROPÓSITO: Nome completo do usuário
     * TIPO: String
     * VALIDAÇÕES: Geralmente obrigatório, mínimo 2 palavras
     * 
     * EXEMPLO: "João Silva Santos"
     */
    private String nomeCompleto;

    /**
     * CAMPO: nomeUsuario
     * 
     * PROPÓSITO: Nome de usuário único no sistema (username)
     * TIPO: String
     * VALIDAÇÕES: Deve ser único, sem espaços
     * 
     * EXEMPLO: "joao.silva"
     */
    private String nomeUsuario;

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email único do usuário
     * TIPO: String
     * VALIDAÇÕES: Formato de email válido, deve ser único
     * 
     * EXEMPLO: "joao.silva@empresa.com"
     */
    private String email;

    /**
     * CAMPO: senha
     * 
     * PROPÓSITO: Senha do usuário
     * TIPO: String
     * SEGURANÇA: NUNCA logar este campo
     * VALIDAÇÕES: Política de senha (tamanho, complexidade)
     * 
     * EXEMPLO: "SenhaSegura123!"
     */
    private String senha;

    /**
     * EXEMPLO DE USO:
     * 
     * // Criar usuário completo
     * UsuarioRequest novoUsuario = UsuarioRequest.builder()
     *     .nomeCompleto("Maria Silva")
     *     .nomeUsuario("maria.silva")
     *     .email("maria@teste.com")
     *     .senha("senha123")
     *     .build();
     * 
     * // Requisição de cadastro
     * Response response = given()
     *     .contentType(ContentType.JSON)
     *     .body(novoUsuario)
     *     .when()
     *     .post("/usuarios");
     * 
     * JSON GERADO:
     * {
     *     "nomeCompleto": "Maria Silva",
     *     "nomeUsuario": "maria.silva",
     *     "email": "maria@teste.com",
     *     "senha": "senha123"
     * }
     */
}
```

## **📄 UsuarioResponse.java**
**📍 Local**: `src/main/java/org/br/com/test/model/response/UsuarioResponse.java`

```java
package org.br.com.test.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * RESPONSE DTO: UsuarioResponse
 * ============================================================================
 * 
 * PROPÓSITO:
 * Data Transfer Object para respostas de operações com usuários.
 * Representa a estrutura de dados retornada pela API ao criar,
 * buscar ou atualizar usuários.
 * 
 * QUANDO USAR:
 * - Deserializar respostas da API
 * - Validar estrutura de dados retornados
 * - Extrair informações específicas das respostas
 * - Testes de validação de campos de resposta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    /**
     * CAMPO: id
     * 
     * PROPÓSITO: Identificador único do usuário gerado pelo sistema
     * TIPO: String (UUID)
     * ORIGEM: Gerado automaticamente pela API
     * 
     * EXEMPLO: "bb275509-1234-5678-9abc-def0f219a0ad9d1"
     */
    private String id;

    /**
     * CAMPO: nomeCompleto
     * 
     * PROPÓSITO: Nome completo do usuário conforme cadastrado
     * TIPO: String
     * ORIGEM: Copiado do request de criação
     * 
     * EXEMPLO: "João Silva Santos"
     */
    private String nomeCompleto;

    /**
     * CAMPO: nomeUsuario
     * 
     * PROPÓSITO: Nome de usuário único no sistema
     * TIPO: String
     * ORIGEM: Copiado do request de criação
     * 
     * EXEMPLO: "joao.silva"
     */
    private String nomeUsuario;

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email do usuário conforme cadastrado
     * TIPO: String
     * ORIGEM: Copiado do request de criação
     * 
     * EXEMPLO: "joao.silva@empresa.com"
     */
    private String email;

    /**
     * NOTA IMPORTANTE:
     * O campo 'senha' NÃO está presente na resposta por motivos de segurança.
     * A API nunca deve retornar senhas em texto plano.
     */

    /**
     * EXEMPLO DE USO:
     * 
     * // Fazer requisição e deserializar resposta
     * Response response = given()
     *     .body(usuarioRequest)
     *     .post("/usuarios");
     * 
     * UsuarioResponse usuario = response.as(UsuarioResponse.class);
     * 
     * // Validar dados retornados
     * assert usuario.getId() != null;
     * assert usuario.getEmail().equals(usuarioRequest.getEmail());
     * assert usuario.getNomeCompleto().equals(usuarioRequest.getNomeCompleto());
     * 
     * // Extrair ID para uso posterior
     * String userId = usuario.getId();
     * TokenManager.setUserId(userId);
     * 
     * JSON TÍPICO RECEBIDO:
     * {
     *     "id": "bb275509-1234-5678-9abc-def0f219a0ad9d1",
     *     "nomeCompleto": "João Silva Santos",
     *     "nomeUsuario": "joao.silva", 
     *     "email": "joao.silva@empresa.com"
     * }
     */
}
```

---

## ✅ **CHECKLIST DA PARTE 3 - CONCLUÍDA**

### **📊 PACOTE DATA:**
- [ ] ✅ **DataReader.java** - Leitor Fillo para Excel
- [ ] ✅ **ExcelDataReader.java** - Leitor Apache POI avançado
- [ ] ✅ **CsvWriter.java** - Escritor CSV com OpenCSV

### **🏗️ PACOTE TEST/MANAGER:**
- [ ] ✅ **TokenManager.java** - Tokens thread-safe
- [ ] ✅ **UsuarioManager.java** - Dados de usuários thread-safe
- [ ] ✅ **ArtigosManager.java** - Dados de artigos thread-safe
- [ ] ✅ **CategoriaManager.java** - Dados de categorias thread-safe

### **📦 PACOTE TEST/MODEL:**
- [ ] ✅ **TagBuilder.java** - Builder para processamento de tags
- [ ] ✅ **LoginRequest.java** - DTO para login
- [ ] ✅ **UsuarioRequest.java** - DTO para criação/atualização
- [ ] ✅ **UsuarioResponse.java** - DTO para respostas

---

## 🎯 **PRÓXIMA ETAPA - PARTE 4**

**📋 Faltam ainda:**
- **🎮 UsuarioController.java** - Controller principal (complexo)
- **🎭 UsuarioSteps.java** - Steps do Cucumber
- **📋 Classes de Sheets** (login, cadastro, dados_usuario)
- **🧪 Utils e Hooks**
- **📄 Runners** (Main, RunnerTestApi, RunnerTagConcatenada)
- **🎭 Features Gherkin**
- **📊 Planilhas Excel**

**🚀 PARTE 3 completa! Continuando para a PARTE 4 final...** 📚✨
