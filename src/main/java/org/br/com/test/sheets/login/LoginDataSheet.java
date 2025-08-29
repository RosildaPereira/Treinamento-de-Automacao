package org.br.com.test.sheets.login;

// A linha abaixo foi corrigida para apontar para o novo pacote
import org.br.com.test.sheets.ExcelDataReader;
import org.br.com.test.utils.GeradorDeCpf;
import org.br.com.test.utils.support.data.DataResource;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Classe padrão e única para ler dados de cenário da planilha Excel.
 * Ela busca dados na aba "TBL_CENARIOS" usando um "ID_CENARIO".
 */
public class LoginDataSheet {

	// ==================================================================================
	// == PONTO DE ATUALIZAÇÃO ==
	// Se você RENOMEAR os nomes das colunas na sua planilha "MassaDadosCMS.xlsx",
	// é APENAS AQUI que você precisa fazer alterações.
	// ==================================================================================

	private static final String EXCEL_FILE_NAME = "MassaDadosCMS.xlsx";
	private static final String SHEET_NAME_CENARIOS = "TBL_CENARIOS";

	// <<< ATENÇÃO AQUI: Colunas da TBL_CENARIOS
	// Se você renomear a coluna "ID_CENARIO" no Excel para "ID_DO_CENARIO",
	// você mudaria a linha abaixo para:
	// private static final String FIELD_ID_CENARIO = "ID_DO_CENARIO";
	private static final String FIELD_ID_CENARIO = "ID_CENARIO";

	// <<< ATENÇÃO AQUI:
	// Se você renomear a coluna "ID_MASSA" no Excel, mude o valor aqui.
	private static final String FIELD_ID_MASSA = "ID_MASSA"; // Chave para ligar com outras tabelas

	// <<< ATENÇÃO AQUI:
	// Se você renomear a coluna "EMAIL" no Excel, mude o valor aqui.
	private static final String FIELD_EMAIL = "EMAIL";

	// <<< ATENÇÃO AQUI:
	// Se você renomear a coluna "SENHA" no Excel para "PASSWORD", por exemplo,
	// você mudaria a linha abaixo para:
	// private static final String FIELD_SENHA = "PASSWORD";
	private static final String FIELD_SENHA = "SENHA";

	// <<< ATENÇÃO AQUI:
	// Se você renomear a coluna "ID" no Excel, mude o valor aqui.
	private static final String FIELD_ID_USUARIO_NA_PLANILHA = "ID";

	// ==================================================================================
	// == FIM DO PONTO DE ATUALIZAÇÃO ==
	// O resto do código não precisa ser alterado.
	// ==================================================================================

	private final Map<String, String> scenarioData;

	public LoginDataSheet(String idScenario) {
		// O prefixo "CT-" parece ser um padrão na sua planilha
		this.scenarioData = loadScenarioData("CT-" + idScenario);
	}

	private Map<String, String> loadScenarioData(String idCenarioCompleto) {
		String excelFilePath = DataResource.getPath(EXCEL_FILE_NAME);

		try (ExcelDataReader reader = new ExcelDataReader(excelFilePath, SHEET_NAME_CENARIOS)) {
			Map<String, String> data = reader.getRowData(FIELD_ID_CENARIO, idCenarioCompleto);
			if (data.isEmpty()) {
				throw new RuntimeException("Cenário não encontrado: Nenhuma linha na TBL_CENARIOS para o ID_CENARIO: " + idCenarioCompleto);
			}
			return data;
		} catch (IOException e) {
			System.err.println("ERRO: Não foi possível carregar dados do Excel para o cenário: " + idCenarioCompleto);
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	/**
	 * Retorna o modelo de dados preenchido com as informações de login.
	 */
	public LoginModel getData() {
		return LoginModel.builder()
				.email(random(getField(FIELD_EMAIL)))
				.senha(getField(FIELD_SENHA))
				.idUsuario(getField(FIELD_ID_USUARIO_NA_PLANILHA))
				.build();
	}

	/**
	 * Expõe o ID_MASSA para que outras classes DataSheet possam buscar dados relacionados.
	 * Ex: new CadastroDataSheet(loginSheet.getIdMassa());
	 */
	public String getIdMassa() {
		return getField(FIELD_ID_MASSA);
	}

	private String getField(String fieldName) {
		return scenarioData.getOrDefault(fieldName, "");
	}

	private String random(String field) {
		if ("RANDOM".equalsIgnoreCase(field)) {
			return GeradorDeCpf.gerar(); // Ou um gerador de e-mail aleatório
		}
		return field;
	}
}