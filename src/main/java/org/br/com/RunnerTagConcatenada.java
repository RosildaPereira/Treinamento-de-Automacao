package org.br.com;

import org.br.com.core.processor.TagProcessor;
import org.br.com.test.model.builder.TagBuilder;

import java.io.IOException;

public class RunnerTagConcatenada {
	/**
	 * TODO: Documentar o método main
	 * Método principal que inicia a execução do processamento de tags.
	 * @param args Argumentos da linha de comando (não utilizados neste contexto).
	 * @throws IOException Exceção de I/O, caso ocorra algum problema na leitura de arquivos.
	 * @throws InterruptedException Exceção de interrupção, caso a thread seja interrompida.
	 */
	/**
	 * TODO: Documentar o método main
	 * Método principal que inicia a execução do processamento de tags.
	 * @param args Argumentos da linha de comando (não utilizados neste contexto).
	 * @throws IOException Exceção de I/O, caso ocorra algum problema na leitura de arquivos.
	 * @throws InterruptedException Exceção de interrupção, caso a thread seja interrompida.
	 */
	public static void main(String [] args) throws IOException, InterruptedException {
		/**
		 * TODO: Documentar a instância de TagBuilder
		 * Instância de TagBuilder, utilizada para construir as configurações iniciais para o processamento de tags.
		 * Esta classe é instanciada a partir de org.br.com.test.model.builder.TagBuilder.
		 */
		/**
		 * TODO: Documentar a instância de TagBuilder
		 * Instância de TagBuilder, utilizada para construir as configurações iniciais para o processamento de tags.
		 * Esta classe é instanciada a partir de org.br.com.test.model.builder.TagBuilder.
		 */
		TagBuilder inicializador = TagBuilder.builder()//
				/**
				 * TODO: Documentar a configuração da aba do analista
				 * Define a aba na planilha onde o código irá buscar as tags.
				 * @param "analista 1" Nome da aba do analista.
				 */
				/**
				 * TODO: Documentar a configuração da aba do analista
				 * Define a aba na planilha onde o código irá buscar as tags.
				 * @param "analista 1" Nome da aba do analista.
				 */
				.abaAnalista("analista 1")//
				.execution("@CT-1001")
				.build();
		/**
		 * TODO: Documentar a instância de TagProcessor
		 * Instância de TagProcessor, responsável por iniciar o processamento das tags com base nas configurações fornecidas.
		 * Esta classe é instanciada a partir de org.br.com.core.processor.TagProcessor.
		 */
		/**
		 * TODO: Documentar a instância de TagProcessor
		 * Instância de TagProcessor, responsável por iniciar o processamento das tags com base nas configurações fornecidas.
		 * Esta classe é instanciada a partir de org.br.com.core.processor.TagProcessor.
		 */
		TagProcessor processor = new TagProcessor();
		processor.start(inicializador);
	}
}
