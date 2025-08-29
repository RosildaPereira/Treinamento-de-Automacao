package org.br.com.test.sheets.cadastro;

import org.br.com.test.sheets.ExcelDataReader;
import org.br.com.test.utils.support.data.DataResource;

import java.io.IOException;
import java.util.Map;

/**
 * Classe padrão e única para ler dados de cadastro da planilha Excel.
 * Ela busca dados na aba "TBL_CADASTRO" usando um "ID_MASSA".
 */
public class CadastroDataSheet {

    private static final String EXCEL_FILE_NAME = "MassaDadosCMS.xlsx";
    private static final String SHEET_NAME_CADASTRO = "TBL_CADASTRO";

    // Nomes das colunas
    private static final String FIELD_ID_MASSA = "ID_MASSA";
    private static final String FIELD_NOME_COMPLETO = "NOME_COMPLETO";
    private static final String FIELD_NOME_USUARIO = "NOME_USUARIO";
    private static final String FIELD_EMAIL = "EMAIL";
    private static final String FIELD_SENHA = "SENHA";
    private static final String FIELD_ID_USUARIO = "ID_USUARIO";

    private final Map<String, String> cadastroData;

    public CadastroDataSheet(String idMassa) {
        if (idMassa == null || idMassa.trim().isEmpty()) {
            throw new IllegalArgumentException("ID_MASSA não pode ser nulo ou vazio para buscar dados de cadastro.");
        }
        this.cadastroData = loadDataFromIdMassa(idMassa);
    }

    private Map<String, String> loadDataFromIdMassa(String idMassa) {
        String excelFilePath = DataResource.getPath(EXCEL_FILE_NAME);

        try (ExcelDataReader reader = new ExcelDataReader(excelFilePath, SHEET_NAME_CADASTRO)) {
            Map<String, String> data = reader.getRowData(FIELD_ID_MASSA, idMassa);
            if (data.isEmpty()) {
                throw new RuntimeException("Falha ao carregar dados: Nenhuma linha encontrada na TBL_CADASTRO para o ID_MASSA: " + idMassa);
            }
            return data;
        } catch (IOException e) {
            System.err.println("Erro de I/O ao carregar dados de cadastro para ID_MASSA: " + idMassa);
            e.printStackTrace();
            throw new RuntimeException("Falha ao ler o arquivo Excel.", e);
        }
    }

    /**
     * Retorna o modelo de dados de cadastro preenchido.
     */
    public CadastroModel getData() {
        return CadastroModel.builder()
                .email(random(getField(FIELD_EMAIL)))
                .senha(getField(FIELD_SENHA))
                .idUsuario(getField(FIELD_ID_USUARIO))
                .nomeCompleto(getField(FIELD_NOME_COMPLETO))
                .nomeUsuario(getField(FIELD_NOME_USUARIO))
                .build();
    }

    public String getField(String fieldName) {
        return cadastroData.getOrDefault(fieldName, "");
    }

    private String random(String field) {
        if ("RANDOM".equalsIgnoreCase(field)) {
            // Idealmente, teríamos geradores específicos para cada campo.
            return "random_" + System.currentTimeMillis() + "@exemplo.com";
        }
        return field;
    }

    // Métodos de acesso direto continuam úteis
    public String getNomeCompleto() {
        return getField(FIELD_NOME_COMPLETO);
    }

    public String getNomeUsuario() {
        return getField(FIELD_NOME_USUARIO);
    }

    public String getEmail() {
        return getField(FIELD_EMAIL);
    }

    public String getSenha() {
        return getField(FIELD_SENHA);
    }

    public String getIdUsuario() {
        return getField(FIELD_ID_USUARIO);
    }
}