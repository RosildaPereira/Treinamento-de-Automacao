package org.br.com;

import br.com.geradormassa.service.GeradorService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ponto de entrada para executar tarefas manuais de setup,
 * como a geração de massa de dados.
 *
 * <p>Esta classe é responsável por iniciar o processo de preparação do ambiente de teste,
 * incluindo a geração de massa de dados para simular cenários de teste.</p>
 */
/**
 * Classe principal (Main) que serve como ponto de entrada para a aplicação.
 */
public class Main {

	/**
	 * Método principal que inicia a execução do script de preparação de ambiente.
	 * Este método é o ponto de entrada da aplicação.
	 *
	 * <p>Ele chama o método {@link #prepararAmbienteDeTeste()} para realizar as operações
	 * de setup e trata quaisquer exceções que possam ocorrer durante a execução.</p>
	 *
	 * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
	 */
	public static void main(String[] args) {
		System.out.println("--- INICIANDO SCRIPT DE PREPARAÇÃO DE AMBIENTE ---");
		try {
			// TODO: Chamar o método 'prepararAmbienteDeTeste()' para iniciar a preparação do ambiente.
			prepararAmbienteDeTeste();
			System.out.println("\n--- SCRIPT FINALIZADO COM SUCESSO! ---");
		} catch (Exception e) {
			// TODO: Tratar exceções que possam ocorrer durante a execução do script, imprimindo uma mensagem de erro.
			System.err.println("\n--- OCORREU UM ERRO DURANTE A EXECUÇÃO DO SCRIPT ---");
			// Imprime a causa do erro para facilitar a depuração.
			// TODO: Imprimir o stack trace da exceção para depuração.
			e.printStackTrace();
		}
	}

	/**
	 * Prepara o ambiente de teste, incluindo a geração de massa de dados.
	 * Este método é estático e pode ser chamado diretamente pelo método 'main'.
	 *
	 * <p>Ele realiza as seguintes etapas:</p>
	 * <ol>
	 *     <li>Instancia o serviço {@link GeradorService}.</li>
	 *     <li>Define a quantidade de usuários a serem gerados.</li>
	 *     <li>Define o diretório e o nome do arquivo de saída para a massa de dados.</li>
	 *     <li>Cria o diretório de saída se ele não existir.</li>
	 *     <li>Chama o método {@code gerarMassaUnificada} do {@link GeradorService}
	 *         para criar o arquivo de massa de dados.</li>
	 * </ol>
	 *
	 * @throws IOException Se ocorrer um erro durante a manipulação de arquivos,
	 *                     como a criação de diretórios ou a escrita do arquivo.
	 */
	public static void prepararAmbienteDeTeste() throws IOException {
		// 1. Crie uma instância do serviço da sua biblioteca
		// TODO: Instanciar 'GeradorService', que é a classe responsável pela lógica de geração de massa de dados.
		// 'GeradorService' é instanciado aqui.
		GeradorService gerador = new GeradorService();

		// 2. Use os métodos do serviço para obter o que você precisa
		// TODO: Definir a quantidade de usuários a serem gerados para a massa de teste.
		int quantidadeDeUsuarios = 30;
		// TODO: Definir o diretório de saída onde o arquivo de massa de dados será salvo.
		String diretorioSaida = "output";
		// TODO: Definir o nome do arquivo de saída para a massa de dados.
		String nomeArquivo = "massaDeTeste.csv";

		// 3. Prática recomendada: Use a API 'Path' para manipular caminhos de arquivo
		// TODO: Criar um objeto Path para o caminho do arquivo.
		Path caminhoDoArquivo = Paths.get(diretorioSaida, nomeArquivo);

		// 4. Garante que o diretório de saída exista antes de tentar escrever o arquivo
		// TODO: Criar os diretórios necessários para o caminho do arquivo, se eles não existirem.
		Files.createDirectories(caminhoDoArquivo.getParent());

		System.out.println("Iniciando a geração de massa para o teste...");
		System.out.println("Arquivo de saída: " + caminhoDoArquivo.toAbsolutePath());

		// TODO: Chamar o método 'gerarMassaUnificada()' da instância 'gerador' (GeradorService)
		// para gerar a massa de dados e salvá-la no caminho especificado.
		// O método 'gerarMassaUnificada()' da instância 'gerador' (GeradorService) é chamado aqui.
		gerador.gerarMassaUnificada(quantidadeDeUsuarios, caminhoDoArquivo.toString());

		System.out.println("Massa de dados pronta para ser usada!");
	}
}