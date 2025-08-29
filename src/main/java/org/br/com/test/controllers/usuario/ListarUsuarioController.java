package org.br.com.test.controllers.usuario;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.br.com.core.support.Context;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.model.error.field.ErrorFieldModel;
import org.br.com.test.model.error.field.ErrorResponse;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.sheets.login.LoginModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static org.br.com.test.manager.TokenManager.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Controlador responsável por gerenciar as operações de listagem de usuários.
 * Esta classe interage com a API de usuários para realizar requisições de listagem
 * e validar as respostas.
 */
public class ListarUsuarioController {

	private LoginModel loginModel;

	private Response response;

	private static final String BASE_URL = "http://localhost:3000";

	private static final String ENDPOINT_USUARIOS = "/usuarios";

	/**
	 * TODO: Construtor da classe ListarUsuarioController.
	 * Inicializa o loginModel a partir do Contexto e a resposta como nula.
	 */
	public ListarUsuarioController() {
		loginModel = (LoginModel) Context.getData();
		response = null;
	}
	/**
	 * TODO: Realiza uma requisição GET para listar usuários com autenticação.
	 * Obtém o token de autenticação, adiciona os cabeçalhos necessários e envia a requisição.
	 * A resposta é armazenada na variável 'response'.
	 */
	public void listarUsuariosComAutenticacao() {
		String token = getToken();

		response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header(new Header("Content-Type", "application/json"))
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_USUARIOS)
				.then()
				.extract().response();

		LogFormatter.logStep(response.getBody().asPrettyString().substring(1, 267) + "...");
	}

	/**
	 * TODO: Valida o código de status da resposta da API.
	 * Compara o código de status recebido com o código de status esperado.
	 * @param statusCode O código de status esperado.
	 */
	public void validarStatusCode(int statusCode) {
		assertEquals("Status code não corresponde ao esperado", statusCode, response.getStatusCode());
		LogFormatter.logStep("Validação de Status Code: " + statusCode + " [OK]");
	}

	/**
	 * TODO: Realiza uma requisição GET para listar usuários sem autenticação.
	 * Adiciona um token inválido no cabeçalho de autorização para simular a falta de autenticação.
	 */
	public void listarUsuariosSemAutenticacao() {

		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header("Authorization", "Bearer " + "token")
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_USUARIOS);

		LogFormatter.logStep("Requisição GET para listar usuários SEM autenticação enviada.");
//		LogFormatter.logStep(response.getBody().asPrettyString());
	}
	/**
	 * TODO: Valida a presença de pelo menos uma das mensagens de erro esperadas na resposta da API.
	 * Divide a string de mensagens esperadas em uma lista e verifica se alguma delas está presente
	 * no corpo da resposta de erro.
	 * @param mensagensEsperadas Uma string contendo mensagens de erro separadas por vírgula.
	 */
	public void validarPresencaDePeloMenosUmaMensagemDeErro(String mensagensEsperadas) {
		List<String> listaMensagensEsperadas = Arrays.asList(mensagensEsperadas.split("\\s*,\\s*"));

		// TODO: O método 'anyMatch' procura por uma mensagem e, se encontrar, a guarda.
		// Agora o método 'anyMatch' vai procurar por uma mensagem e, se encontrar, a guardamos.
		Optional<String> mensagemEncontrada = listaMensagensEsperadas.stream()
				.filter(this::encontrouMensagem) // Filtra a lista, mantendo apenas as mensagens que foram encontradas na resposta
				.findFirst(); // Pega a primeira que foi encontrada

		if (mensagemEncontrada.isPresent()) {
			// Se uma mensagem foi encontrada, o log agora pode dizer QUAL foi.
			LogFormatter.logStep("Validação de pelo menos uma mensagem de erro realizada com sucesso.");
			LogFormatter.logStep("Mensagem encontrada que satisfez a condição: '" + mensagemEncontrada.get() + "'");
			// TODO: Loga o corpo completo da resposta de erro para depuração.
			LogFormatter.logStep("Corpo da Resposta de Erro Validado:\n" + response.getBody().asPrettyString());
		} else {
			// Se nenhuma foi encontrada, a falha continua a mesma.
			// TODO: Loga o corpo do erro recebido antes de falhar o teste.
			LogFormatter.logStep("CORPO DO ERRO RECEBIDO: " + response.getBody().asString());
			fail("Nenhuma das mensagens de erro esperadas foi encontrada na resposta da API. Esperado (qualquer uma): " + listaMensagensEsperadas);
		}
	}

	/**
	 * TODO: Verifica se uma mensagem específica está presente no corpo da resposta de erro.
	 * Tenta desserializar a resposta para diferentes modelos de erro e verifica a presença da mensagem.
	 * @param mensagem A mensagem a ser procurada.
	 * @return true se a mensagem for encontrada, false caso contrário.
	 */
	private boolean encontrouMensagem(String mensagem) {
		String corpoResposta = response.getBody().asString();

		try {
			ErrorResponse errorResponse = response.as(ErrorResponse.class);

			if (errorResponse.getError() != null && mensagem.equals(errorResponse.getError().getErro())) {
				// TODO: Verifica se a mensagem corresponde ao campo 'erro' do objeto 'error'.
				return true;
			}

			// TODO: Predicado para verificar se a mensagem está nos campos 'mensagem' ou 'msg' de ErrorFieldModel.
			Predicate<ErrorFieldModel> contemMensagem = erro -> mensagem.equals(erro.getMensagem()) || mensagem.equals(erro.getMsg());

			if (errorResponse.getErros() != null && errorResponse.getErros().stream().anyMatch(contemMensagem)) {
				// TODO: Verifica se a mensagem está presente na lista 'erros'.
				return true;
			}

			if (errorResponse.getErrors() != null && errorResponse.getErrors().stream().anyMatch(contemMensagem)) {
				// TODO: Verifica se a mensagem está presente na lista 'errors'.
				return true;
			}
		} catch (Exception e) {
			LogFormatter.logStep("Desserialização para ErrorResponse falhou (isso pode ser esperado). Tentando extração direta.");
		}

		try {
			String erroSimples = response.jsonPath().getString("erro");
			// TODO: Tenta extrair a mensagem diretamente do campo 'erro' via JsonPath.
			if (mensagem.equals(erroSimples)) {
				return true;
			}
		} catch (Exception e) {
			// O campo 'erro' pode não existir, o que também não é um problema.
		}

		return false;
	}

}
