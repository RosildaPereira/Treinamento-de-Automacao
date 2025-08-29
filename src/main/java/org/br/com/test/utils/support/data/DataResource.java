package org.br.com.test.utils.support.data;

import org.br.com.core.support.resource.ResourceUtils;

public class DataResource {

	// Define o diretório padrão para os arquivos de dados.
	private static final String DATA_FOLDER = "data/";

	/**
	 * Obtém o caminho completo para um arquivo de dados, assumindo que ele está
	 * dentro da pasta 'data' nos recursos.
	 *
	 * @param fileName O nome do arquivo (ex: "MassaDadosCMS.xlsx").
	 * @return O caminho completo para o recurso (ex: "data/MassaDadosCMS.xlsx").
	 */
	public static String getPath(String fileName) {
		// Concatena a pasta padrão com o nome do arquivo antes de passar para o ResourceUtils.
		return ResourceUtils.getPath(DATA_FOLDER + fileName);
	}
}