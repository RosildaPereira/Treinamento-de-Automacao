package org.br.com.test.sheets.dados_usuario;


import org.br.com.test.sheets.ExcelDataReader;
import org.br.com.test.utils.support.data.DataResource;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Classe mestre e única para ler dados de usuário da planilha Excel.
 * Ela busca dados na aba "TBL_DADOS_ID" usando um "ID_USUARIO" como chave.
 */
public class DadosUsuarioSheet {

    private static final String EXCEL_FILE_NAME = "MassaDadosCMS.xlsx";
    private static final String SHEET_NAME_DADOS_ID = "TBL_DADOS_ID";

    // Nomes das colunas
    private static final String FIELD_ID_USUARIO = "ID_USUARIO";
    private static final String FIELD_TIPO_ID = "TIPO_ID";
    private static final String FIELD_VALOR_ID = "VALOR_ID";
    private static final String FIELD_EMAIL = "EMAIL";
    private static final String FIELD_SENHA = "SENHA";

    private final Map<String, String> userData;

    public DadosUsuarioSheet(String idUsuario) {
        Objects.requireNonNull(idUsuario, "ID_USUARIO não pode ser nulo para buscar dados.");
        if (idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID_USUARIO não pode ser vazio para buscar dados.");
        }
        this.userData = loadDataFromIdUsuario(idUsuario);
    }

    private Map<String, String> loadDataFromIdUsuario(String idUsuario) {
        String excelFilePath = DataResource.getPath(EXCEL_FILE_NAME);

        try (ExcelDataReader reader = new ExcelDataReader(excelFilePath, SHEET_NAME_DADOS_ID)) {
            Map<String, String> data = reader.getRowData(FIELD_ID_USUARIO, idUsuario);
            if (data.isEmpty()) {
                throw new RuntimeException("Falha ao carregar dados: Nenhuma linha encontrada na TBL_DADOS_ID para o ID_USUARIO: " + idUsuario);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler o arquivo Excel para TBL_DADOS_ID.", e);
        }
    }

    /**
     * Retorna o modelo de dados de usuário preenchido.
     */
    public DadosUsuarioModel getData() {
        return DadosUsuarioModel.builder()
                .idUsuario(getField(FIELD_ID_USUARIO))
                .email(getField(FIELD_EMAIL))
                .senha(getField(FIELD_SENHA))
                .tipoId(getField(FIELD_TIPO_ID))
                .valorId(getField(FIELD_VALOR_ID))
                .build();
    }

    public String getField(String fieldName) {
        return userData.getOrDefault(fieldName, "");
    }
}