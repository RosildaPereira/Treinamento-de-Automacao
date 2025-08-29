package org.br.com.test.controllers.usuario;

import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.TokenManager;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.error.field.ErrorFieldModel;
import org.br.com.test.model.error.field.ErrorResponse;
import org.br.com.core.filter.EvidenceFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExcluirUsuarioController {

	private Response response;

	private static final String BASE_URL = "http://localhost:3000";

	private static final String ENDPOINT_USUARIOS = "/usuarios";

	public ExcluirUsuarioController() {
		response = null;
	}

	public void excluirUsuarioPorId() {
		String token = TokenManager.getToken();
		String idUsuario = UsuarioManager.getIdUsuario();
		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.delete(ENDPOINT_USUARIOS + "/" + idUsuario);
	}

	public void excluirUsuarioPorIdSemAutenticacao() {
		String token = TokenManager.getToken();
		String idUsuario = null;
		this.response = given()
				.filter(new EvidenceFilter()) // ADICIONAMOS O FILTRO AQUI
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.delete(ENDPOINT_USUARIOS + "/" + idUsuario);

		LogFormatter.logStep(response.getBody().asPrettyString());
	}



	public void validarStatusCode(int statusCode) {
		assertEquals("Status code não corresponde ao esperado", statusCode, response.getStatusCode());
		LogFormatter.logStep("Validação de Status Code: " + statusCode + " [OK]");
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
