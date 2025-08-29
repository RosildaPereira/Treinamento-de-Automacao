package org.br.com.core.data;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import org.br.com.core.exceptions.DataException;
import org.br.com.core.support.logger.LogFormatter;

public abstract class DataSheet<T> {

	// TODO: excelFilePath é o caminho do arquivo Excel que será lido ou manipulado.
	private final String excelFilePath;
	// TODO: defaultSheetName é o nome da planilha padrão a ser utilizada.
	private String defaultSheetName;
	// TODO: recordset armazena o conjunto de registros retornado por uma consulta.
	private Recordset recordset;
	// TODO: recordLineFound indica se uma linha de registro foi encontrada.
	boolean recordLineFound;

	/**
	 * TODO: Construtor para DataSheet.
	 * Inicializa uma nova instância de DataSheet com o caminho do arquivo Excel.
	 *
	 * @param excelFilePath O caminho completo para o arquivo Excel.
	 */
	public DataSheet(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}

	/**
	 * TODO: Construtor para DataSheet.
	 * Inicializa uma nova instância de DataSheet com o caminho do arquivo Excel e o nome da planilha padrão.
	 *
	 * @param excelFilePath O caminho completo para o arquivo Excel.
	 * @param defaultSheetName O nome da planilha padrão a ser utilizada.
	 */
	public DataSheet(String excelFilePath, String defaultSheetName) {
		this.excelFilePath = excelFilePath;
		this.defaultSheetName = defaultSheetName;
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) usando o nome da planilha padrão.
	 * Executa a consulta 'select * from [nome_da_planilha_padrao]' para popular o recordset.
	 *
	 * <p>Esta classe é instanciada em:
	 * <ul>
	 *     <li>{@link org.br.com.core.data.DataSheet#DataSheet(String)}</li>
	 *     <li>{@link org.br.com.core.data.DataSheet#DataSheet(String, String)}</li>
	 * </ul>
	 * </p>
	 *
	 */
	protected void setData() {
		setDataFromSheetName(defaultSheetName);
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) a partir de um nome de planilha específico.
	 * Executa a consulta 'select * from [sheetName]' para popular o recordset.
	 *
	 * @param sheetName O nome da planilha a ser consultada.
	 */
	protected void setDataFromSheetName(String sheetName) {
		String query = String.format("select * from `%s`", sheetName);
		setDataFromQuery(query);
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) a partir de uma consulta SQL.
	 * Executa a consulta fornecida para popular o recordset.
	 *
	 * @param query A consulta SQL a ser executada.
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

	/**
	 * TODO: Obtém o valor de um campo da linha de registro atual ou da próxima linha, se nenhuma linha tiver sido processada.
	 * Se {@code recordLineFound} for verdadeiro, retorna o campo da linha atual.
	 * Caso contrário, itera sobre o recordset até encontrar um valor para o campo.
	 *
	 * @param fieldName O nome do campo a ser obtido.
	 * @return O valor do campo como String.
	 */
	protected String getField(String fieldName) {
		String field = null;
		try {
			if (recordLineFound) {
				field = recordset.getField(fieldName);
			} else {
				while (recordset.next()) {
					field = recordset.getField(fieldName);
				}
				if (field != null) {
					recordLineFound = true;
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

    public abstract T getData();

	/**
	 * TODO: Atualiza um campo na planilha padrão.
	 * Constrói e executa uma consulta SQL de atualização no formato:
	 * 'update [defaultSheetName] set [fieldToUpdate] = '[fieldToUpdateValue]' where [registerField] = '[registerFieldValue]''.
	 *
	 * @param fieldToUpdate O nome do campo a ser atualizado.
	 * @param fieldToUpdateValue O novo valor para o campo.
	 * @param registerField O nome do campo usado como critério para encontrar o registro.
	 * @param registerFieldValue O valor do campo de registro para identificar a linha a ser atualizada.
	 */
	protected void updateField(String fieldToUpdate, String fieldToUpdateValue, String registerField,
							   String registerFieldValue) {
		updateField(defaultSheetName, fieldToUpdate, fieldToUpdateValue, registerField, registerFieldValue);
	}

	/**
	 * TODO: Atualiza um campo em uma planilha específica.
	 * Constrói e executa uma consulta SQL de atualização no formato:
	 * 'update [sheetName] set [fieldToUpdate] = '[fieldToUpdateValue]' where [registerField] = '[registerFieldValue]''.
	 *
	 * @param sheetName O nome da planilha onde a atualização será realizada.
	 * @param fieldToUpdate O nome do campo a ser atualizado.
	 * @param fieldToUpdateValue O novo valor para o campo.
	 * @param registerField O nome do campo usado como critério para encontrar o registro.
	 * @param registerFieldValue O valor do campo de registro para identificar a linha a ser atualizada.
	 */
	protected void updateField(String sheetName, String fieldToUpdate, String fieldToUpdateValue, String registerField,
							   String registerFieldValue) {
		String query = String.format("update %s set %s = '%s' where %s = '%s'", sheetName, fieldToUpdate,
				fieldToUpdateValue, registerField, registerFieldValue);
		updateOrInsert(query);
	}

	/**
	 * TODO: Executa uma consulta de atualização ou inserção no arquivo Excel.
	 * Utiliza um {@link DataReader} para executar a consulta SQL fornecida.
	 *
	 * @param query A consulta SQL de atualização ou inserção a ser executada.
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
