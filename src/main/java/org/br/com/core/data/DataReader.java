package org.br.com.core.data;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import lombok.extern.log4j.Log4j2;
import org.br.com.core.exceptions.DataException;

/**
 * Classe responsável por ler dados de arquivos Excel usando a biblioteca Fillo.
 * Implementa {@link AutoCloseable} para garantir o fechamento da conexão.
 * TODO: Esta classe é instanciada em diversos testes para leitura de dados de massa.
 */
@Log4j2
public class DataReader implements AutoCloseable {

	private final Fillo fillo;
	private Connection connection;

	/**
	 * Construtor da classe DataReader.
	 * Inicializa o objeto Fillo e estabelece a conexão com o arquivo Excel.
	 * TODO: O objeto Fillo é inicializado aqui.
	 *
	 * @param excelFilePath O caminho completo do arquivo Excel a ser lido.
	 */
	public DataReader(String excelFilePath) {
		synchronized (fillo = new Fillo()) {
			setConnection(excelFilePath);
		}
	}

	/**
	 * Estabelece a conexão com o arquivo Excel especificado.
	 * TODO: Este método é chamado apenas no construtor.
	 *
	 * @param excelFilePath O caminho completo do arquivo Excel.
	 * @throws DataException Se ocorrer um erro ao tentar obter a conexão com o arquivo.
	 */
	private void setConnection(String excelFilePath) {
		try {
			// TODO: A conexão com o arquivo Excel é estabelecida aqui.
			connection = fillo.getConnection(excelFilePath);
		} catch (FilloException e) {
			String message = "Error in get connection with file: " + excelFilePath;
			log.error(message, e);
			// TODO: Lança uma exceção personalizada em caso de falha na conexão.
			throw new DataException(message, e);
		}
	}

	/**
	 * Executa uma consulta no arquivo Excel e retorna um Recordset.
	 * TODO: Este método é usado para buscar dados do Excel.
	 *
	 * @param query A consulta Fillo a ser executada (ex: "SELECT * FROM Sheet1").
	 * @return Um objeto Recordset contendo os resultados da consulta.
	 * @throws DataException Se ocorrer um erro durante a execução da consulta.
	 */
	public Recordset executeQuery(String query) {
		try {
			return connection.executeQuery(query);
		} catch (FilloException e) {
			String message = "Error in execute with query: " + query;
			log.error(message, e);
			// TODO: Lança uma exceção personalizada em caso de falha na execução da query.
			throw new DataException(message, e);
		}
	}

	/**
	 * Executa uma operação de atualização no arquivo Excel e retorna o número de linhas afetadas.
	 * TODO: Este método é usado para atualizar dados no Excel.
	 * @param query A consulta Fillo de atualização a ser executada (ex: "UPDATE Sheet1 SET Column1 = 'NewValue' WHERE ID = 1").
	 * @return O número de linhas afetadas pela operação de atualização.
	 * @throws DataException Se ocorrer um erro durante a execução da atualização.
	 */
	public int updateQuery(String query) {
		try {
			return connection.executeUpdate(query);
		} catch (FilloException e) {
			String message = "Error in update query with: " + query;
			log.error(message, e);
			throw new DataException(message, e);
		}
	}

	/**
	 * Fecha a conexão com o arquivo Excel.
	 * Este método é automaticamente chamado quando a classe é usada em um bloco try-with-resources.
	 * TODO: É crucial chamar este método para liberar recursos.
	 * @throws Exception Se ocorrer um erro ao tentar fechar a conexão.
	 */
	@Override
	public void close() throws Exception {
		// TODO: A conexão com o Fillo é fechada aqui.
		connection.close();
	}
}
