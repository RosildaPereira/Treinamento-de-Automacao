package org.br.com.test.sheets;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utilitário genérico e robusto para ler dados de arquivos Excel (.xlsx).
 * Implementa AutoCloseable para garantir o fechamento seguro dos recursos.
 */
public class ExcelDataReader implements AutoCloseable {

    private final Workbook workbook;
    private final Sheet sheet;
    private final Map<String, Integer> columnIndexes;

    public ExcelDataReader(String filePath, String sheetName) throws IOException {
        Objects.requireNonNull(filePath, "O caminho do arquivo não pode ser nulo.");
        Objects.requireNonNull(sheetName, "O nome da aba não pode ser nulo.");

        FileInputStream fis = new FileInputStream(filePath);
        this.workbook = WorkbookFactory.create(fis);
        this.sheet = workbook.getSheet(sheetName);
        if (this.sheet == null) {
            workbook.close(); // Libera o arquivo em caso de erro
            throw new IllegalArgumentException("Aba '" + sheetName + "' não encontrada no arquivo: " + filePath);
        }
        this.columnIndexes = mapColumnHeaders();
    }

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

    public Map<String, String> getRowData(String lookupColumn, String lookupValue) {
        Map<String, String> rowData = new HashMap<>();
        Integer lookupColumnIndex = columnIndexes.get(lookupColumn);

        if (lookupColumnIndex == null) {
            // Se a coluna de busca não existe, não há como encontrar a linha.
            return rowData;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(lookupColumnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String cellValue = getCellValueAsString(cell);

                if (lookupValue.equals(cellValue)) {
                    // Linha encontrada, extrair todos os dados
                    for (Map.Entry<String, Integer> header : columnIndexes.entrySet()) {
                        Cell dataCell = row.getCell(header.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        rowData.put(header.getKey(), getCellValueAsString(dataCell));
                    }
                    break; // Para a busca após encontrar a primeira correspondência
                }
            }
        }
        return rowData;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            // Usa o método moderno e não obsoleto para obter o tipo do resultado da fórmula
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Formata números para evitar notação científica e ".0" em inteiros.
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        // Usa um formato que não agrupa milhares e mostra decimais se necessário.
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

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}