package org.br.com.core.data;

import com.codoid.products.fillo.Recordset;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.exceptions.DataException;

import java.util.List;

@Log4j2
public abstract class MultipleDataSheet {

	// TODO: excelFilePath é o caminho do arquivo Excel que será lido.
	private final String excelFilePath;
	// TODO: defaultSheetName é o nome da planilha padrão a ser usada.
	private String defaultSheetName;
	// TODO: recordset armazena o conjunto de registros retornado de uma consulta.
	protected Recordset recordset;
	// TODO: recordLineFound indica se uma linha de registro foi encontrada.
	boolean recordLineFound;

	/**
	 * TODO: Construtor para MultipleDataSheet.
	 * Inicializa o caminho do arquivo Excel.
	 * @param excelFilePath O caminho completo para o arquivo Excel.
	 */
	public MultipleDataSheet(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}

	/**
	 * TODO: Construtor para MultipleDataSheet.
	 * Inicializa o caminho do arquivo Excel e o nome da planilha padrão.
	 * @param excelFilePath O caminho completo para o arquivo Excel.
	 * @param defaultSheetName O nome da planilha padrão a ser usada.
	 */
	public MultipleDataSheet(String excelFilePath, String defaultSheetName) {
		this.excelFilePath = excelFilePath;
		this.defaultSheetName = defaultSheetName;
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) usando o nome da planilha padrão.
	 * Executa a consulta 'select * from [sheet_name]' na planilha padrão.
	 */
	protected void setData() {
		// TODO: Chama setDataFromSheetName para definir os dados da planilha padrão.
		setDataFromSheetName(defaultSheetName);
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) a partir de uma planilha específica.
	 * Constrói uma consulta SQL para selecionar todos os dados da planilha fornecida.
	 * @param sheetName O nome da planilha a ser consultada. Deve seguir o padrão de nomes de tabelas de banco de dados.
	 */
	protected void setDataFromSheetName(String sheetName) {
		// TODO: Formata a query para selecionar todos os dados da planilha.
		String query = String.format("select * from `%s`", sheetName);
		// TODO: Chama setDataFromQuery para executar a consulta.
		setDataFromQuery(query);
	}

	/**
	 * TODO: Define o conjunto de registros (recordset) a partir de uma consulta SQL.
	 * Abre um DataReader, executa a consulta e armazena o resultado no recordset.
	 * Em caso de erro, loga a mensagem e lança uma DataException.
	 * @param query A consulta SQL a ser executada no arquivo Excel.
	 */
	protected void setDataFromQuery(String query) {
		// TODO: Usa um try-with-resources para garantir que o DataReader seja fechado.
		try (DataReader dataReader = new DataReader(excelFilePath)) {
			// TODO: Executa a consulta e armazena o Recordset.
			recordset = dataReader.executeQuery(query);
			// TODO: Reseta a flag recordLineFound.
			recordLineFound = false;
		} catch (Exception e) {
			String message = String.format("Error in execute query '%s'", query);
			log.error(message, e);
			throw new DataException(message, e);
		}
	}

	/**
	 * TODO: Método abstrato para obter uma lista de objetos DataModel.
	 * As classes concretas devem implementar este método para converter os dados do recordset em uma lista de DataModel.
	 * @return Uma lista de objetos DataModel.
	 */
	public abstract List<DataModel> getData();

}