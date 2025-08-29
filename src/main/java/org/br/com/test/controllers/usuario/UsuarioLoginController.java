package org.br.com.test.controllers.usuario;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.br.com.core.support.Context;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.model.request.LoginRequest;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.sheets.login.LoginModel;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.br.com.test.manager.TokenManager.*;
import static org.br.com.test.manager.UsuarioManager.*;


/**
 * TODO: Documentar a classe UsuarioLoginController.
 * Classe responsável por controlar as operações de login de usuário.
 * Esta classe é instanciada em: [TODO: Adicionar onde a classe é instanciada]
 */
public class UsuarioLoginController {

	/**
	 * TODO: Documentar o campo loginModel.
	 * Modelo de dados para o login, instanciado a partir do contexto.
	 * Este campo é instanciado em: {@link #UsuarioLoginController()}
	 */
	private final LoginModel loginModel;

	/**
	 * TODO: Documentar o campo response.
	 * Objeto Response que armazena a resposta da requisição HTTP.
	 * Este campo é instanciado em: {@link #UsuarioLoginController()} e {@link #realizarLogin()}
	 */
	private Response response;

	/** TODO: Documentar o campo BASE_URL. */
	private static final String BASE_URL = "http://localhost:3000";
	/** TODO: Documentar o campo ENDPOINT_LOGIN. */
	private static final String ENDPOINT_LOGIN = "/auth/login";

	/**
	 * TODO: Documentar o construtor UsuarioLoginController.
	 * Construtor da classe UsuarioLoginController.
	 * Inicializa o modelo de login a partir do contexto e a resposta como nula.
	 */
	public UsuarioLoginController() {
		this.loginModel = (LoginModel) Context.getData();
		this.response = null;
	}

	/**
	 * TODO: Documentar o método realizarLogin.
	 * Realiza a operação de login enviando uma requisição POST para o endpoint de login.
	 * Extrai o token e o ID do usuário da resposta e os armazena.
	 * Este método é instanciado em: [TODO: Adicionar onde o método é instanciado]
	 */
	public void realizarLogin() {
		String email = loginModel.getEmail();
		String senha = loginModel.getSenha();
		LoginRequest loginRequest = LoginRequest.builder()
				.email(email)
				.senha(senha)
				.build();
		// TODO: Documentar a linha de código que inicia a requisição.
		this.response = given()
				// TODO: Documentar o filtro EvidenceFilter.
				.filter(new EvidenceFilter())
				// TODO: Documentar a configuração do cabeçalho Content-Type.
				.header(new Header("Content-Type", "application/json"))
				// TODO: Documentar a configuração da base URI.
				.baseUri(BASE_URL)
				// TODO: Documentar a configuração do corpo da requisição.
				.body(loginRequest)
				.when()
				// TODO: Documentar a chamada POST para o endpoint de login.
				.post(ENDPOINT_LOGIN);

		// TODO: Documentar a extração do token da resposta.
		String token = response.jsonPath().getString("token");
		// TODO: Documentar a extração do ID do usuário da resposta.
		String userId = response.jsonPath().getString("user.id");
		setToken(token);
		setUserId(userId);
		setIdUsuario(userId);
		// TODO: Documentar o log do token.
		LogFormatter.logStep("Token: " + token);
		LogFormatter.logStep(response.prettyPrint());
	}

}