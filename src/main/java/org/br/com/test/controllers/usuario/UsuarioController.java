package org.br.com.test.controllers.usuario;

import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.TokenManager;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.request.LoginRequest;
import org.br.com.test.model.request.UsuarioRequest;
import org.br.com.test.model.response.UsuarioResponse;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.core.support.Context;
import org.br.com.test.sheets.login.LoginModel;
import org.br.com.test.sheets.cadastro.CadastroDataSheet;
import org.br.com.test.utils.LogConfig;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;

public class UsuarioController {

	private Response response;
	private static final String BASE_URL = "http://localhost:3000";
	private static final String ENDPOINT_USUARIOS = "/usuarios";
	private static final String ENDPOINT_LOGIN = "/auth/login";

	/**
	 * Método auxiliar para obter a folha de dados de cadastro do contexto.
	 * Centraliza a lógica de busca e casting.
	 * TODO: Adicionar tratamento de exceção mais robusto para o casting.
	 * TODO: Verificar se 'cadastroSheet' é a chave correta para o contexto.
	 *
	 * @return CadastroDataSheet se encontrado no contexto, caso contrário null.
	 * @see org.br.com.core.support.Context
	 */
	private CadastroDataSheet getCadastroSheet() {
		Object sheet = Context.get("cadastroSheet");
		if (sheet instanceof CadastroDataSheet) {
			return (CadastroDataSheet) sheet;
		}
		return null; // Retorna nulo se não houver dados de cadastro no contexto
	}

	/**
	 * Método auxiliar para obter os dados de login do contexto.
	 * TODO: Garantir que LoginModel seja sempre o tipo esperado para Context.getData().
	 *
	 * @return LoginModel contendo os dados de login.
	 * @throws IllegalStateException se os dados de login não forem encontrados no contexto. */
	private LoginModel getDadosLoginExcel() {
		Object data = Context.getData();
		if (data instanceof LoginModel) {
			return (LoginModel) data;
		}
		// Lança uma exceção clara se os dados de login (essenciais) não forem encontrados
		throw new IllegalStateException("Dados de login (LoginModel) não encontrados no contexto.");
	}

	// =================================================================
	// == MÉTODOS DE AÇÃO (CRUD COMPLETO)
	// =================================================================

	/**
	 * Carrega os dados de usuário a partir da planilha (CadastroDataSheet ou LoginModel)
	 * e os armazena no {@link UsuarioManager}. Prioriza dados completos de cadastro.
	 * TODO: Refatorar para usar um único método de obtenção de dados da planilha.
	 */
	public void carregarDadosDaPlanilha() {
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;

		// Prioriza dados da TBL_CADASTRO (dados completos)
		if (cadastroSheet != null) {
			nomeCompleto = cadastroSheet.getNomeCompleto();
			nomeUsuario = cadastroSheet.getNomeUsuario();
			email = cadastroSheet.getEmail();
			senha = cadastroSheet.getSenha();
		}
		// Fallback para dados da TBL_CENARIOS se não houver dados de cadastro
		else {
			email = loginModel.getEmail();
			senha = loginModel.getSenha();
			nomeCompleto = "Usuario da Planilha";
			nomeUsuario = "usuario.planilha";
		}

		// Apenas salvar os dados no contexto, sem cadastrar. TODO: Verificar se o UsuarioManager é o local ideal para armazenar esses dados temporariamente.
		UsuarioManager.setEmailUsuario(email); // TODO: Instanciar UsuarioManager
		UsuarioManager.setSenhaUsuario(senha); // TODO: Instanciar UsuarioManager
		UsuarioManager.setNomeCompletoUsuario(nomeCompleto); // TODO: Instanciar UsuarioManager
		UsuarioManager.setNomeUsuario(nomeUsuario);

		LogFormatter.logStep("📋 Dados carregados da planilha:");
		LogFormatter.logStep("   👤 Nome: " + nomeCompleto);
		LogFormatter.logStep("   🔖 Usuário: " + nomeUsuario);
		LogFormatter.logStep("   📧 Email: " + email);
		LogFormatter.logStep("   🔑 Senha: " + senha);
		LogFormatter.logStep("ℹ️ Usuário não cadastrado - usando dados existentes da planilha");
	}

	/**
	 * Tenta cadastrar um novo usuário utilizando um e-mail que já existe na base de dados.
	 * Os dados são obtidos da planilha.
	 * TODO: Adicionar validação para garantir que o e-mail realmente exista antes de tentar duplicar. */
	public void tentarCadastrarUsuarioComEmailDuplicado() {
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;

		if (cadastroSheet != null) {
			nomeCompleto = cadastroSheet.getNomeCompleto();
			nomeUsuario = cadastroSheet.getNomeUsuario();
			email = cadastroSheet.getEmail();
			senha = cadastroSheet.getSenha();
		} else {
			email = loginModel.getEmail();
			senha = loginModel.getSenha();
			nomeCompleto = "Usuario Duplicado";
			nomeUsuario = "usuario.duplicado";
		}

		UsuarioRequest usuarioDuplicado = UsuarioRequest.builder() // TODO: Instanciar UsuarioRequest
				.nomeCompleto(nomeCompleto)
				.nomeUsuario(nomeUsuario)
				.email(email)
				.senha(senha)
				.build();

		response = given()
				.filter(new EvidenceFilter()) // TODO: Instanciar EvidenceFilter
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL)
				.body(usuarioDuplicado) // TODO: Enviar o objeto UsuarioRequest no corpo da requisição
				.when()
				.post(ENDPOINT_USUARIOS);

		LogFormatter.logStep("AÇÃO: Tentativa de cadastro com e-mail duplicado");
		LogFormatter.logStep("👤 Nome: " + nomeCompleto);
		LogFormatter.logStep("🔖 Nome Usuário: " + nomeUsuario);
		LogFormatter.logStep("📧 Email: " + email);
	}

	/**
	 * Tenta cadastrar um novo usuário utilizando um nome de usuário que já existe na base de dados.
	 * Os dados são obtidos da planilha.
	 * TODO: Adicionar validação para garantir que o nome de usuário realmente exista antes de tentar duplicar. */
	public void tentarCadastrarUsuarioComNomeDeUsuarioDuplicado() {
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;

		if (cadastroSheet != null) {
			nomeCompleto = cadastroSheet.getNomeCompleto();
			nomeUsuario = cadastroSheet.getNomeUsuario(); // Nome que já existe
			email = cadastroSheet.getEmail();
			senha = cadastroSheet.getSenha();
		} else {
			email = loginModel.getEmail();
			senha = loginModel.getSenha();
			nomeCompleto = "Usuario Nome Duplicado";
			nomeUsuario = "usuario.duplicado";
		}

		UsuarioRequest usuarioDuplicado = UsuarioRequest.builder() // TODO: Instanciar UsuarioRequest
				.nomeCompleto(nomeCompleto)
				.nomeUsuario(nomeUsuario)
				.email(email)
				.senha(senha)
				.build();

		response = given()
				.filter(new EvidenceFilter()) // TODO: Instanciar EvidenceFilter
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL)
				.body(usuarioDuplicado) // TODO: Enviar o objeto UsuarioRequest no corpo da requisição
				.when()
				.post(ENDPOINT_USUARIOS);

		LogFormatter.logStep("AÇÃO: Tentativa de cadastro com nome de usuário duplicado");
		LogFormatter.logStep("🔖 Nome Usuário duplicado: " + nomeUsuario);
		LogFormatter.logStep("📧 Email: " + email);
	}

	/**
	 * Tenta cadastrar um novo usuário com um formato de e-mail inválido.
	 * Os dados são obtidos da planilha.
	 * TODO: Adicionar exemplos de e-mails inválidos para testes. */
	public void tentarCadastrarUsuarioComEmailInvalido() {
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;

		if (cadastroSheet != null) {
			nomeCompleto = cadastroSheet.getNomeCompleto();
			nomeUsuario = cadastroSheet.getNomeUsuario();
			email = cadastroSheet.getEmail(); // Email inválido da planilha
			senha = cadastroSheet.getSenha();
		} else {
			email = loginModel.getEmail(); // Email inválido da planilha
			senha = loginModel.getSenha();
			nomeCompleto = "Usuario Email Invalido";
			nomeUsuario = "usuario.email.invalido";
		}

		UsuarioRequest usuarioEmailInvalido = UsuarioRequest.builder() // TODO: Instanciar UsuarioRequest
				.nomeCompleto(nomeCompleto)
				.nomeUsuario(nomeUsuario)
				.email(email)
				.senha(senha)
				.build();

		response = given()
				.filter(new EvidenceFilter()) // TODO: Instanciar EvidenceFilter
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL)
				.body(usuarioEmailInvalido) // TODO: Enviar o objeto UsuarioRequest no corpo da requisição
				.when()
				.post(ENDPOINT_USUARIOS);

		LogFormatter.logStep("AÇÃO: Tentativa de cadastro com email inválido");
		LogFormatter.logStep("📧 Email inválido: " + email);
	}

	/**
	 * Tenta cadastrar um novo usuário com uma senha que não atende aos requisitos de segurança.
	 * Os dados são obtidos da planilha.
	 * TODO: Definir os requisitos de senha esperados para o sistema. */
	public void tentarCadastrarUsuarioComSenhaInvalida() {
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;

		if (cadastroSheet != null) {
			nomeCompleto = cadastroSheet.getNomeCompleto();
			nomeUsuario = cadastroSheet.getNomeUsuario();
			email = cadastroSheet.getEmail();
			senha = cadastroSheet.getSenha(); // Senha inválida da planilha
		} else {
			email = loginModel.getEmail();
			senha = loginModel.getSenha(); // Senha inválida da planilha
			nomeCompleto = "Usuario Senha Invalida";
			nomeUsuario = "usuario.senha.invalida";
		}

		UsuarioRequest usuarioSenhaInvalida = UsuarioRequest.builder() // TODO: Instanciar UsuarioRequest
				.nomeCompleto(nomeCompleto)
				.nomeUsuario(nomeUsuario)
				.email(email)
				.senha(senha)
				.build();

		response = given()
				.filter(new EvidenceFilter()) // TODO: Instanciar EvidenceFilter
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL)
				.body(usuarioSenhaInvalida) // TODO: Enviar o objeto UsuarioRequest no corpo da requisição
				.when()
				.post(ENDPOINT_USUARIOS);

		LogFormatter.logStep("AÇÃO: Tentativa de cadastro com senha inválida");
		LogFormatter.logStep("🔑 Senha inválida: " + mascararSenha(senha));
	}

	/**
	 * Tenta cadastrar um usuário com dados inválidos, como e-mail e senha vazios.
	 * TODO: Expandir para testar outros campos inválidos (ex: nome muito curto/longo).
	 */

	public void tentarCadastrarUsuarioComDadosInvalidos() {
		UsuarioRequest usuarioInvalido = new UsuarioRequest("Nome Exemplo", "usuario.exemplo", "", "");
		response = given().filter(new EvidenceFilter()).header("Content-Type", "application/json").baseUri(BASE_URL).body(usuarioInvalido).when().post(ENDPOINT_USUARIOS);
		LogFormatter.logStep("AÇÃO: Tentativa de cadastro com dados inválidos (e-mail e senha vazios).");
	}

	/**
	 * Realiza uma tentativa de login no sistema.
	 *
	 * @param comCredenciaisValidas true para usar credenciais válidas, false para usar credenciais inválidas.
	 * TODO: Separar a lógica de login válido e inválido em métodos distintos para clareza. */
	public void realizarLogin(boolean comCredenciaisValidas) {
		LoginRequest loginRequest;
		CadastroDataSheet cadastroSheet = getCadastroSheet();
		LoginModel loginModel = getDadosLoginExcel();

		String email;
		String senha;

		if (comCredenciaisValidas) {
			// Priorizar dados da TBL_CADASTRO
			if (cadastroSheet != null) {
				email = cadastroSheet.getEmail();
				senha = cadastroSheet.getSenha();
			} else {
				email = loginModel.getEmail();
				senha = loginModel.getSenha();
			}
			LogFormatter.logStep("AÇÃO: Realizando login com credenciais válidas: " + email);
		} else {
			// Para credenciais inválidas, usar dados da planilha mas com senha errada
			if (cadastroSheet != null) {
				email = cadastroSheet.getEmail();
			} else {
				email = loginModel.getEmail();
			}
			senha = "senha_errada_123";
			LogFormatter.logStep("AÇÃO: Tentando realizar login com credenciais inválidas.");
		}

		loginRequest = LoginRequest.builder() // TODO: Instanciar LoginRequest
				.email(email)
				.senha(senha)
				.build();

		response = given()
				.filter(new EvidenceFilter())
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL) // TODO: Usar a constante BASE_URL
				.body(loginRequest) // TODO: Enviar o objeto LoginRequest no corpo da requisição
				.when()
				.post(ENDPOINT_LOGIN);

		// Se login foi bem-sucedido, salvar o token
		if (response.getStatusCode() == 200) {
			String token = response.jsonPath().getString("token"); // TODO: Extrair o token da resposta
			String userId = response.jsonPath().getString("user.id"); // TODO: Extrair o ID do usuário da resposta

			if (token != null && !token.isEmpty()) {
				TokenManager.setToken(token); // TODO: Instanciar TokenManager
				LogFormatter.logStep("✅ Token salvo com sucesso: " + mascararToken(token)); // TODO: Mascarar o token para logs
			} else {
				LogFormatter.logStep("⚠️ Token não encontrado na resposta");
			}

			if (userId != null && !userId.isEmpty()) {
				TokenManager.setUserId(userId);
				UsuarioManager.setIdUsuario(userId);
				LogFormatter.logStep("✅ User ID salvo: " + mascararId(userId));
			}

			LogFormatter.logStep("✅ Login realizado com sucesso");
		} else {
			// Login falhou - verificar se é erro de credenciais inválidas
			String responseBody = response.getBody().asPrettyString();
			if (responseBody.contains("Email ou senha inválidos") ||
					responseBody.contains("credenciais inválidas") ||
					response.getStatusCode() == 401) {

				LogFormatter.logStep("❌ Login falhou - Credenciais inválidas");
				LogFormatter.logStep("📋 ATENÇÃO: Usuário não encontrado ou credenciais incorretas");
			} else {
				LogFormatter.logStep("❌ Login falhou - Status: " + response.getStatusCode());
				LogFormatter.logStep("Resposta: " + responseBody);
			}
		}
	}

	// ... O restante da classe permanece o mesmo ...
	// (listarUsuarios, buscarUsuarioPorId, atualizarUsuario, excluirUsuario, métodos de validação e mascaramento)
	public void listarUsuarios(boolean comAutenticacao) {
	/**
	 * Lista todos os usuários cadastrados no sistema.
	 *
	 * @param comAutenticacao true para incluir o token de autenticação na requisição, false caso contrário.
	 * TODO: Adicionar paginação e filtros para a listagem de usuários. */
		var request = given().filter(new EvidenceFilter()).baseUri(BASE_URL);
		if (comAutenticacao) {
			String token = TokenManager.getToken();
			if (token != null && !token.isEmpty()) {
				request.header("Authorization", "Bearer " + token);
				LogFormatter.logStep("🔑 Usando token: " + mascararToken(token));
			} else {
				LogFormatter.logStep("⚠️ Token não encontrado para autenticação");
			}
		}
		response = request.when().get(ENDPOINT_USUARIOS);
		LogFormatter.logStep("AÇÃO: Listagem de usuários (Autenticado: " + comAutenticacao + ").");
	}

	/**
	 * Busca um usuário específico pelo seu ID.
	 *
	 * @param comIdValido true para usar um ID de usuário válido (do {@link UsuarioManager}), false para usar um ID inexistente.
	 * @param comAutenticacao true para incluir o token de autenticação na requisição, false caso contrário.
	 * TODO: Adicionar validação para o formato do ID. */
	public void buscarUsuarioPorId(boolean comIdValido, boolean comAutenticacao) {
		String id = comIdValido ? UsuarioManager.getIdUsuario() : "id-inexistente";

		// Verificar se o ID é válido
		if (id == null || id.isEmpty()) {
			id = "id-inexistente";
			LogFormatter.logStep("⚠️ ID do usuário não encontrado, usando ID padrão");
		}

		var request = given()
				.filter(new EvidenceFilter())
				.baseUri(BASE_URL)
				.pathParam("id", id);

		if (comAutenticacao) {
			String token = TokenManager.getToken();
			if (token != null && !token.isEmpty()) {
				request.header("Authorization", "Bearer " + token);
				LogFormatter.logStep("🔑 Usando token: " + mascararToken(token));
			} else {
				LogFormatter.logStep("⚠️ Token não encontrado para autenticação");
			}
		}

		response = request.when().get(ENDPOINT_USUARIOS + "/{id}");
		LogFormatter.logStep("AÇÃO: Busca de usuário pelo ID: " + id + " (Autenticado: " + comAutenticacao + ")");
	}

	/**
	 * Atualiza os dados de um usuário existente.
	 *
	 * @param comIdValido true para usar um ID de usuário válido (do {@link UsuarioManager}), false para usar um ID inexistente.
	 * TODO: Permitir a atualização de campos específicos em vez de todos os campos. */
	public void atualizarUsuario(boolean comIdValido) {
		String id = comIdValido ? UsuarioManager.getIdUsuario() : "id-inexistente";

		// Verificar se o ID é válido
		if (id == null || id.isEmpty()) {
			id = "id-inexistente";
			LogFormatter.logStep("⚠️ ID do usuário não encontrado, usando ID padrão");
		}

		String token = TokenManager.getToken();
		if (token == null || token.isEmpty()) {
			LogFormatter.logStep("⚠️ Token não encontrado para autenticação");
		} else {
			LogFormatter.logStep("🔑 Usando token: " + mascararToken(token));
		}

		// Gerar dados únicos para evitar conflitos
		String timestamp = String.valueOf(System.currentTimeMillis());
		UsuarioRequest usuarioAtualizado = UsuarioRequest.builder() // TODO: Instanciar UsuarioRequest
				.nomeCompleto("Usuario Atualizado " + timestamp)
				.nomeUsuario("usuario.atualizado." + timestamp)
				.email("usuario.atualizado." + timestamp + "@exemplo.com")
				.senha("novaSenha123")
				.build();

		response = given()
				.filter(new EvidenceFilter())
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.body(usuarioAtualizado)
				.when()
				.put(ENDPOINT_USUARIOS + "/" + id);

		LogFormatter.logStep("AÇÃO: Atualização do usuário ID: " + id);
		LogFormatter.logStep("📝 Novos dados: " + usuarioAtualizado.getNomeUsuario() + " / " + usuarioAtualizado.getEmail());


	}

	/**
	 * Exclui um usuário do sistema.
	 *
	 * @param comIdValido true para usar um ID de usuário válido (do {@link UsuarioManager}), false para usar um ID inexistente.
	 * @param comAutenticacao true para incluir o token de autenticação na requisição, false caso contrário.
	 * TODO: Implementar exclusão lógica (soft delete) em vez de exclusão física. */
	public void excluirUsuario(boolean comIdValido, boolean comAutenticacao) {
		String id = comIdValido ? UsuarioManager.getIdUsuario() : "id-inexistente";
		String token = TokenManager.getToken();

		// VALIDAÇÃO CRÍTICA (A parte mais importante)
		if (comIdValido && (id == null || id.isEmpty())) {
			Assert.fail("Pré-requisito para exclusão falhou: Não há um ID de usuário válido. Verifique se o login foi bem-sucedido.");
		}
		if (comAutenticacao && (token == null || token.isEmpty())) {
			Assert.fail("Pré-requisito para exclusão falhou: Não há um token de autenticação. Verifique se o login foi bem-sucedido.");
		}

		// Monta a requisição com base nos parâmetros
		var request = given()
				.filter(new EvidenceFilter())
				.baseUri(BASE_URL);

		if (comAutenticacao) {
			request.header("Authorization", "Bearer " + token);
			LogFormatter.logStep("🔑 Usando token: " + mascararToken(token));
		}

		response = request.when().delete(ENDPOINT_USUARIOS + "/" + id);
		LogFormatter.logStep("AÇÃO: Exclusão do usuário ID: " + id + " (Autenticado: " + comAutenticacao + ")");
	}


	// =================================================================
	// == MÉTODOS DE VALIDAÇÃO
	// =================================================================

	/**
	 * Valida se o status code da resposta HTTP corresponde ao valor esperado.
	 *
	 * @param statusCode O status code esperado.
	 * TODO: Adicionar validação para diferentes categorias de status codes (2xx, 4xx, 5xx). */
	public void validarStatusCode(int statusCode) {
		assertEquals("Status code não corresponde ao esperado. Body: " + response.getBody().asString(), statusCode, response.getStatusCode());
		LogFormatter.logStep("VALIDAÇÃO: Status Code esperado '" + statusCode + "' recebido com sucesso.");
	}

	/**
	 * Valida o corpo da resposta de criação de usuário, verificando se o e-mail e nome de usuário
	 * retornados correspondem aos dados utilizados na requisição.
	 * TODO: Validar outros campos da resposta, como o ID do usuário criado.
	 */
	public void validarCorpoUsuarioCriadoComSucesso() {
		UsuarioResponse usuarioResponse = response.as(UsuarioResponse.class);
		assertEquals("O e-mail não corresponde ao esperado.", UsuarioManager.getEmailUsuario(), usuarioResponse.getEmail());
		assertEquals("O nome de usuário não corresponde ao esperado.", UsuarioManager.getNomeUsuario(), usuarioResponse.getNomeUsuario());
		LogFormatter.logStep("VALIDAÇÃO: Corpo da resposta de criação de usuário validado com sucesso.");
	}

	/**
	 * Valida o corpo da resposta de login bem-sucedido.
	 * Verifica se o ID do usuário e o e-mail estão presentes e, se aplicável, se o ID corresponde ao esperado da planilha.
	 * TODO: Validar a presença e o formato do token de autenticação. */
	public void validarCorpoUsuarioLogadoComSucesso() {
		// Obter ID esperado da planilha (ID real, não mascarado)
		Object data = Context.getData();
		String expectedId = null;

		if (data instanceof LoginModel) {
			LoginModel dadosExcel = (LoginModel) data;
			expectedId = dadosExcel.getIdUsuario(); // ID real da planilha
		}

		// Validar que o ID retornado corresponde ao esperado
		// Se o ID da planilha for um UUID válido (36 caracteres), validar exatamente
		// Se não for, apenas validar que existe um ID válido
		if (expectedId != null && !expectedId.isEmpty() && expectedId.length() >= 36) {
			// ID da planilha é um UUID válido - validar exatamente
			response.then()
					.body("user.id", equalTo(expectedId)) // Compara com ID real
					.body("user.email", notNullValue())
					.body("user.email", not(equalTo("")));

			LogFormatter.logStep("VALIDAÇÃO: ID do usuário validado com sucesso");
		} else {
			// ID da planilha não é um UUID válido - apenas validar que existe
			response.then()
					.body("user.id", notNullValue())
					.body("user.id", not(equalTo("")))
					.body("user.email", notNullValue())
					.body("user.email", not(equalTo("")));

			LogFormatter.logStep("VALIDAÇÃO: Login realizado com sucesso");
		}
	}

	/**
	 * Valida uma resposta de erro simples, verificando o status code e uma mensagem de erro específica.
	 *
	 * @param statusCode O status code esperado para o erro.
	 * @param mensagemEsperada A mensagem de erro esperada no corpo da resposta (campo "erro").
	 * TODO: Generalizar para suportar diferentes nomes de campos de erro (ex: "message", "error_description"). */
	public void validarErroSimples(int statusCode, String mensagemEsperada) {
		LogFormatter.logStep("VALIDAÇÃO: Verificando erro simples com status " + statusCode + " e mensagem '" + mensagemEsperada + "'");
		response.then()
				.statusCode(statusCode)
				.body("erro", equalTo(mensagemEsperada));
		LogFormatter.logStep("VALIDAÇÃO: Erro simples verificado com sucesso.");
		LogFormatter.logStep("Corpo da Resposta de Erro:\n" + response.getBody().asPrettyString());
	}

	/**
	 * Valida uma resposta de erro de campo, verificando o status code e a presença de uma mensagem de erro
	 * associada a um campo específico.
	 *
	 * @param statusCode O status code esperado para o erro.
	 * @param nomeDoCampo O nome do campo que gerou o erro (ex: "email", "senha").
	 * @param mensagemEsperada A mensagem de erro esperada para o campo. */
	public void validarErroDeCampo(int statusCode, String nomeDoCampo, String mensagemEsperada) {
		response.then().statusCode(statusCode);
		LogFormatter.logStep("VALIDAÇÃO: Verificando erro de campo para '" + nomeDoCampo + "' com status " + statusCode);

		String responseBody = response.getBody().asPrettyString();
		LogFormatter.logStep("RESPOSTA:");
		LogFormatter.logStep(responseBody);

		// Verificar se a resposta contém erros
		boolean erroEncontrado = false;

		// Verificar se há um array de erros na resposta
		if (responseBody.contains("\"erros\"") || responseBody.contains("\"errors\"")) {
			// Buscar por erros específicos no array
			if (responseBody.contains("\"campo\"") || responseBody.contains("\"field\"")) {
				// Verificar se há um erro para o campo específico
				if (responseBody.contains("\"" + nomeDoCampo + "\"") ||
						responseBody.contains("\"field\":\"" + nomeDoCampo + "\"") ||
						responseBody.contains("\"campo\":\"" + nomeDoCampo + "\"")) {

					// Verificar se a mensagem está presente
					if (responseBody.contains(mensagemEsperada)) {
						erroEncontrado = true;
					}
				}
			}
		}

		// Verificar formato alternativo de erro
		if (!erroEncontrado) {
			// Verificar se há erro de validação geral
			if (responseBody.contains("validation") ||
					responseBody.contains("error") ||
					responseBody.contains("erro")) {

				// Verificar se o campo está mencionado
				if (responseBody.toLowerCase().contains(nomeDoCampo.toLowerCase())) {
					erroEncontrado = true;
				}
			}
		}

		// Verificar casos específicos
		if (!erroEncontrado) {
			// Caso específico para email inválido
			if (nomeDoCampo.equals("email") && mensagemEsperada.contains("inválido")) {
				if (responseBody.contains("email") &&
						(responseBody.contains("inválido") || responseBody.contains("invalid"))) {
					erroEncontrado = true;
				}
			}

			// Caso específico para senha inválida
			if (nomeDoCampo.equals("senha") && mensagemEsperada.contains("mínimo")) {
				if (responseBody.contains("senha") &&
						(responseBody.contains("mínimo") || responseBody.contains("minimum"))) {
					erroEncontrado = true;
				}
			}

			// Caso específico para nome de usuário duplicado
			if (nomeDoCampo.equals("nomeUsuario") && mensagemEsperada.contains("já está em uso")) {
				if (responseBody.contains("nomeUsuario") &&
						(responseBody.contains("já está em uso") || responseBody.contains("already in use"))) {
					erroEncontrado = true;
				}
			}
		}

		if (!erroEncontrado) {
			LogFormatter.logStep("ERRO: Resposta não contém o erro esperado");
			LogFormatter.logStep("Esperado: Campo '" + nomeDoCampo + "' com mensagem '" + mensagemEsperada + "'");
			LogFormatter.logStep("Resposta atual: " + responseBody);
		}

		assertTrue("Não foi encontrado um erro para o campo '" + nomeDoCampo + "' com a mensagem '" + mensagemEsperada + "'", erroEncontrado);
		LogFormatter.logStep("VALIDAÇÃO: Status Code esperado '" + statusCode + "' recebido com sucesso.");
	}

	// Métodos para mascarar informações sensíveis nos logs
	/**
	 * Mascara uma senha para exibição em logs, substituindo parte dela por asteriscos.
	 *
	 * @param senha A senha a ser mascarada.
	 * @return A senha mascarada ou a senha original se o mascaramento estiver desativado. */
	private String mascararSenha(String senha) {
		if (senha == null || senha.isEmpty()) {
			return "***";
		}

		if (!LogConfig.isMascararSenha()) {
			return senha;
		}

		int caracteresVisiveis = LogConfig.getCaracteresVisiveis();
		if (senha.length() <= caracteresVisiveis * 2) {
			return "***" + senha.substring(senha.length() - 1);
		}

		return senha.substring(0, caracteresVisiveis) + "***" + senha.substring(senha.length() - caracteresVisiveis);
	}

	/**
	 * Mascara um ID (geralmente UUID) para exibição em logs.
	 *
	 * @param id O ID a ser mascarado.
	 * @return O ID mascarado ou o ID original se o mascaramento estiver desativado. */
	private String mascararId(String id) {
		if (id == null || id.isEmpty()) {
			return "***";
		}

		if (!LogConfig.isMascararId()) {
			return id;
		}

		int caracteresVisiveis = LogConfig.getCaracteresVisiveis();
		if (id.length() <= caracteresVisiveis * 2) {
			return "***" + id.substring(id.length() - 1);
		}

		return id.substring(0, caracteresVisiveis) + "-****-****-" + id.substring(id.length() - caracteresVisiveis);
	}

	/**
	 * Mascara um token de autenticação para exibição em logs.
	 *
	 * @param token O token a ser mascarado.
	 * @return O token mascarado ou o token original se o mascaramento estiver desativado. */
	private String mascararToken(String token) {
		if (token == null || token.isEmpty()) {
			return "***";
		}

		if (!LogConfig.isMascararToken()) {
			return token;
		}

		int caracteresVisiveis = LogConfig.getCaracteresVisiveis();
		if (token.length() <= caracteresVisiveis * 2) {
			return "***" + token.substring(token.length() - 1);
		}

		return token.substring(0, caracteresVisiveis) + "****" + token.substring(token.length() - caracteresVisiveis);
	}
}