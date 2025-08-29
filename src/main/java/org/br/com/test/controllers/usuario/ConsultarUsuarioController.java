package org.br.com.test.controllers.usuario;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.br.com.core.support.Context;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.TokenManager;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.model.error.field.ErrorFieldModel;
import org.br.com.test.model.error.field.ErrorResponse;
import org.br.com.test.sheets.cadastro.CadastroDataSheet;
import org.br.com.test.sheets.login.LoginModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConsultarUsuarioController {

	private final LoginModel loginModel;
	private final CadastroDataSheet cadastroDataSheet;
	private Response response;

	private static final String BASE_URL = "http://localhost:3000";
	private static final String ENDPOINT_USUARIOS = "/usuarios";

	public ConsultarUsuarioController() {
		// Esta parte está perfeita, carregando os dados necessários.
		this.cadastroDataSheet = (CadastroDataSheet) Context.get("cadastroSheet");
		this.loginModel = (LoginModel) Context.getData();
		this.response = null;
	}

	/**
	 * Consulta um usuário usando o ID da planilha.
	 * Versão simples e direta, confiando que os pré-requisitos (login e dados) estão corretos.
	 */
	public void consultarUsuarioPorId() {
		// Pega o ID da fonte de dados correta (TBL_CADASTRO)
		String idUsuario = this.cadastroDataSheet.getIdUsuario();
		// Pega o token que (supostamente) foi gerado no passo de login
		String token = TokenManager.getToken();

		LogFormatter.logStep("AÇÃO: Consultando usuário pelo ID: " + idUsuario);

		this.response = given()
				.filter(new EvidenceFilter()) // Garante que a requisição seja logada na evidência
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_USUARIOS + "/" + idUsuario);

		LogFormatter.logStep("Resposta recebida:\n" + response.asPrettyString());
	}

	// ... O restante da classe com os outros métodos e validações permanece o mesmo ...
	// O código abaixo é idêntico ao que você já tem e está ótimo.

	/**
	 * Tenta consultar um usuário com um ID que provavelmente não existe.
	 */
	public void consultarUsuarioComIdInexistente() {
		String idInexistente = "id-que-nao-existe-12345";
		LogFormatter.logStep("AÇÃO: Consultando usuário com ID inexistente: " + idInexistente);

		String token = TokenManager.getToken();
		enviarRequisicaoGet(ENDPOINT_USUARIOS + "/" + idInexistente, token);
	}

	/**
	 * Tenta listar todos os usuários sem fornecer um token de autenticação.
	 */
	public void listarUsuariosSemAutenticacao() {
		LogFormatter.logStep("AÇÃO: Tentativa de listar usuários SEM autenticação.");
		enviarRequisicaoGet(ENDPOINT_USUARIOS, null);
	}

	private void enviarRequisicaoGet(String endpoint, String token) {
		RequestSpecification request = given()
				.filter(new EvidenceFilter())
				.baseUri(BASE_URL);

		if (token != null && !token.isEmpty()) {
			request.header("Authorization", "Bearer " + token);
		}

		this.response = request.when().get(endpoint);
	}

	public void validarStatusCode(int statusCode) {
		if (this.response == null) {
			fail("A resposta da API é nula. A requisição de consulta pode não ter sido executada corretamente.");
		}
		assertEquals("Status code não corresponde ao esperado. Body: " + response.getBody().asString(), statusCode, response.getStatusCode());
		LogFormatter.logStep("Validação de Status Code: " + statusCode + " [OK]");
	}

	public void validarPresencaDePeloMenosUmaMensagemDeErro(String mensagensEsperadas) {
		List<String> listaMensagensEsperadas = Arrays.asList(mensagensEsperadas.split("\\s*,\\s*"));

		Optional<String> mensagemEncontrada = listaMensagensEsperadas.stream()
				.filter(this::encontrouMensagemNaResposta)
				.findFirst();

		if (mensagemEncontrada.isPresent()) {
			LogFormatter.logStep("Validação de mensagem de erro realizada com sucesso.");
			LogFormatter.logStep("Mensagem encontrada: '" + mensagemEncontrada.get() + "'");
		} else {
			LogFormatter.logStep("CORPO DO ERRO RECEBIDO: " + response.getBody().asString());
			fail("Nenhuma das mensagens de erro esperadas foi encontrada. Esperado (qualquer uma): " + listaMensagensEsperadas);
		}
	}

	private boolean encontrouMensagemNaResposta(String mensagem) {
		try {
			ErrorResponse errorResponse = response.as(ErrorResponse.class);
			Predicate<ErrorFieldModel> contemMensagem = erro -> mensagem.equals(erro.getMensagem()) || mensagem.equals(erro.getMsg());

			return (errorResponse.getError() != null && mensagem.equals(errorResponse.getError().getErro())) ||
					(errorResponse.getErros() != null && errorResponse.getErros().stream().anyMatch(contemMensagem)) ||
					(errorResponse.getErrors() != null && errorResponse.getErrors().stream().anyMatch(contemMensagem));

		} catch (Exception e) {
			try {
				return mensagem.equals(response.jsonPath().getString("erro"));
			} catch (Exception ex) {
				return false;
			}
		}
	}
}