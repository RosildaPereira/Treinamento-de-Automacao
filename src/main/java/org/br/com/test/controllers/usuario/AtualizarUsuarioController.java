package org.br.com.test.controllers.usuario;


import io.restassured.http.Header;
import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.TokenManager;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.error.field.ErrorFieldModel;
import org.br.com.test.model.error.field.ErrorResponse;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.utils.JavaFaker;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AtualizarUsuarioController {

	private Response response;

	private static final String BASE_URL = "http://localhost:3000";

	private static final String ENDPOINT_USUARIOS = "/usuarios";

	private static final String ENDPOINT_LOGIN = "/auth/login";

	public AtualizarUsuarioController() {
		response = null;
	}

	public void atualizarUsuarioPorId() {
		String token = TokenManager.getToken();
		String userId = UsuarioManager.getIdUsuario();

		Map<String, String> novosDados = JavaFaker.DadosAtualizacaoJavaFakeMap();

		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header(new Header("Content-Type", "application/json"))
				.header(new Header("Authorization", "Bearer " + token))
				.baseUri(BASE_URL)
				.body(novosDados)
				.when()
				.put(ENDPOINT_USUARIOS + "/" + userId);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.getBody().asPrettyString());
	}

	public void atualizarUsuarioSemId() {
		String token = "TokenManager.getToken()";
		String userId = " ";

		Map<String, String> novosDados = JavaFaker.DadosAtualizacaoJavaFakeMap();

		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header(new Header("Content-Type", "application/json"))
				.header(new Header("Authorization", "Bearer " + token))
				.baseUri(BASE_URL)
				.body(novosDados)
				.when()
				.put(ENDPOINT_USUARIOS + "/" + userId);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.getBody().asPrettyString());
	}

	public void validarStatusCode(int statusCode) {
		assertEquals("Status code não corresponde ao esperado", statusCode, response.getStatusCode());
		LogFormatter.logStep("Validação de Status Code: " + statusCode + " [OK]");
	}

	public void listarUsuariosSemAutenticacao() {

		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header("Authorization", "Bearer " + "token")
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_USUARIOS);

		LogFormatter.logStep("Requisição GET para listar usuários SEM autenticação enviada.");
		LogFormatter.logStep(response.getBody().asPrettyString());
	}

	public void validarPresencaDePeloMenosUmaMensagemDeErro(String mensagensEsperadas) {
		List<String> listaMensagensEsperadas = Arrays.asList(mensagensEsperadas.split("\\s*,\\s*"));

		// Agora o método 'anyMatch' vai procurar por uma mensagem e, se encontrar, a guardamos.
		Optional<String> mensagemEncontrada = listaMensagensEsperadas.stream()
				.filter(this::encontrouMensagem) // Filtra a lista, mantendo apenas as mensagens que foram encontradas na resposta
				.findFirst(); // Pega a primeira que foi encontrada

		if (mensagemEncontrada.isPresent()) {
			// Se uma mensagem foi encontrada, o log agora pode dizer QUAL foi.
			LogFormatter.logStep("Validação de pelo menos uma mensagem de erro realizada com sucesso.");
			LogFormatter.logStep("Mensagem encontrada que satisfez a condição: '" + mensagemEncontrada.get() + "'");
			LogFormatter.logStep("Corpo da Resposta de Erro Validado:\n" + response.getBody().asPrettyString());
		} else {
			// Se nenhuma foi encontrada, a falha continua a mesma.
			LogFormatter.logStep("CORPO DO ERRO RECEBIDO: " + response.getBody().asString());
			fail("Nenhuma das mensagens de erro esperadas foi encontrada na resposta da API. Esperado (qualquer uma): " + listaMensagensEsperadas);
		}
	}

	private boolean encontrouMensagem(String mensagem) {
		String corpoResposta = response.getBody().asString();

		try {
			ErrorResponse errorResponse = response.as(ErrorResponse.class);

			if (errorResponse.getError() != null && mensagem.equals(errorResponse.getError().getErro())) {
				return true;
			}

			Predicate<ErrorFieldModel> contemMensagem = erro -> mensagem.equals(erro.getMensagem()) || mensagem.equals(erro.getMsg());

			if (errorResponse.getErros() != null && errorResponse.getErros().stream().anyMatch(contemMensagem)) {
				return true;
			}

			if (errorResponse.getErrors() != null && errorResponse.getErrors().stream().anyMatch(contemMensagem)) {
				return true;
			}
		} catch (Exception e) {
			LogFormatter.logStep("Desserialização para ErrorResponse falhou (isso pode ser esperado). Tentando extração direta.");
		}

		try {
			String erroSimples = response.jsonPath().getString("erro");
			if (mensagem.equals(erroSimples)) {
				return true;
			}
		} catch (Exception e) {
			// O campo 'erro' pode não existir, o que também não é um problema.
		}

		return false;
	}


}
