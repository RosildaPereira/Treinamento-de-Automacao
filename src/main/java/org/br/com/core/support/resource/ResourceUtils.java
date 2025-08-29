package org.br.com.core.support.resource;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ResourceUtils {

	/**
	 * Encontra o caminho absoluto de um arquivo de recurso, procurando em vários locais comuns.
	 * Isso torna o framework mais robusto e menos dependente da configuração de execução do IDE.
	 *
	 * @param resourceName O nome do arquivo a ser encontrado (ex: "MassaDadosCMS.xlsx").
	 * @return O caminho absoluto do arquivo.
	 * @throws RuntimeException se o arquivo não for encontrado em nenhum dos locais.
	 */
	public static String getPath(String resourceName) {
		// 1. Tenta carregar diretamente do ClassLoader (método padrão)
		URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(resourceName);
		if (resourceUrl != null) {
			try {
				return new File(resourceUrl.toURI()).getAbsolutePath();
			} catch (URISyntaxException e) {
				throw new RuntimeException("Falha ao converter URL do recurso para URI: " + resourceUrl, e);
			}
		}

		// 2. Se falhar, tenta caminhos relativos comuns a partir da raiz do projeto
		String[] possiblePaths = {
				"src/main/resources/",
				"src/test/resources/",
				"data/",
				"" // Raiz do projeto
		};

		for (String path : possiblePaths) {
			File file = new File(path + resourceName);
			if (file.exists() && !file.isDirectory()) {
				System.out.println("INFO: Recurso '" + resourceName + "' encontrado em: " + file.getAbsolutePath());
				return file.getAbsolutePath();
			}
		}

		// 3. Se tudo falhar, lança um erro claro.
		throw new RuntimeException("FALHA CRÍTICA AO CARREGAR RECURSO: O arquivo '" + resourceName + "' não foi encontrado no classpath ou em diretórios comuns do projeto.");
	}
}