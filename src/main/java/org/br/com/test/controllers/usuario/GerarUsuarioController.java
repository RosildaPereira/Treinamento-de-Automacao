package org.br.com.test.controllers.usuario;


import io.restassured.http.Header;
import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.error.field.ErrorFieldModel;
import org.br.com.test.model.error.field.ErrorResponse;
import org.br.com.test.model.request.UsuarioRequest;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.utils.massas.FakerApiData;


import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GerarUsuarioController {

	public Response response;

	private static final String BASE_URL = "http://localhost:3000";

	private static final String ENDPOINT_USUARIOS = "/usuarios";

	public GerarUsuarioController() {
		response = null;
	}

	//esse
	public void cadastrarNovoUsuario() {
		UsuarioRequest usuarioGerado = FakerApiData.gerarUsuarioRequestSimples();

		response = given()
				.filter(new EvidenceFilter())
				.header(new Header("Content-Type", "application/json"))
				.header(new Header("accept", "application/json"))
				.baseUri(BASE_URL)
				.body(usuarioGerado)
				.when()
				.post(ENDPOINT_USUARIOS);

		// Apenas armazena os dados se a criação for bem-sucedida (status 201)
		if (response.getStatusCode() == 201) {
			UsuarioManager.setEmailUsuario(usuarioGerado.getEmail());
			UsuarioManager.setSenhaUsuario(usuarioGerado.getSenha());
			UsuarioManager.setNomeCompletoUsuario(usuarioGerado.getNomeCompleto());
			UsuarioManager.setNomeUsuario(usuarioGerado.getNomeUsuario());
			String userId = response.jsonPath().getString("id");
			UsuarioManager.setIdUsuario(userId);
			LogFormatter.logStep("Usuario ID: " + userId);
		}
	}

//	public void tentarCadastrarUsuarioComEmailNulo() {
//		UsuarioRequest usuarioInvalido = UsuarioRequest.builder()
//				.nomeCompleto(FakerApiData.gerarUsuarioRequestSimples().getNomeCompleto())
//				.nomeUsuario(FakerApiData.gerarUsuarioRequestSimples().getNomeUsuario())
//				.email(null) // Forçando o dado inválido
//				.senha("senhaValida123")
//				.build();
//
//		// Reutiliza o método de cadastro principal
//		cadastrarNovoUsuario(usuarioInvalido);
//	}


	public void validarStatusCode(int statusCode) {
		response.then()
				.statusCode(statusCode);
		LogFormatter.logStep("Validação de Status Code " + statusCode);
		assertEquals("Status code não corresponde ao esperado", statusCode, this.response.getStatusCode());
	}

	public void validarUsuarioCadastrado() {
		String email = UsuarioManager.getEmailUsuario();
		String nomeCompleto = UsuarioManager.getNomeCompletoUsuario();
		String nomeUsuario = UsuarioManager.getNomeUsuario();

		response.then()
				.body("email", equalTo(email))
				.body("nomeCompleto", equalTo(nomeCompleto))
				.body("nomeUsuario", equalTo(nomeUsuario));

		LogFormatter.logStep("Validação de usuário cadastrado com sucesso.");
	}

	/**
	 * NOVO MÉTODO: Valida uma resposta de erro da API.
	 * @param mensagemEsperada A mensagem de erro que deve ser retornada no corpo da resposta.
	 */
	public void validarMensagemDeErro(String mensagemEsperada) {
		String body = response.getBody().asString();
		ErrorResponse errorResponse = response.as(ErrorResponse.class);

		// Busca em 'errors' (padrão inglês)
		if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
			boolean mensagemEncontrada = errorResponse.getErrors().stream()
				.anyMatch(e -> mensagemEsperada.equals(e.getMsg()));
			if (!mensagemEncontrada) {
				System.out.println("Body erro: " + body);
			}
			assertTrue("Mensagem de erro não encontrada: " + mensagemEsperada, mensagemEncontrada);
			LogFormatter.logStep("Validação da mensagem de erro '" + mensagemEsperada + "' realizada com sucesso.");
			return;
		}
		// Busca em 'erros' (padrão português)
		if (errorResponse.getErros() != null && !errorResponse.getErros().isEmpty()) {
			boolean mensagemEncontrada = errorResponse.getErros().stream()
				.anyMatch(e -> mensagemEsperada.equals(e.getMensagem()));
			if (!mensagemEncontrada) {
				System.out.println("Body erro: " + body);
			}
			assertTrue("Mensagem de erro não encontrada: " + mensagemEsperada, mensagemEncontrada);
			LogFormatter.logStep("Validação da mensagem de erro '" + mensagemEsperada + "' realizada com sucesso.");
			return;
		}
		// Busca em 'error' wrapper (fallback)
		if (errorResponse.getError() != null) {
			assertEquals(mensagemEsperada, errorResponse.getError().getErro());
			LogFormatter.logStep("Validação da mensagem de erro '" + mensagemEsperada + "' realizada com sucesso.");
			return;
		}
		System.out.println("Body erro: " + body);
		fail("Nenhum erro encontrado na resposta da API.");
	}

    /**
     * Tenta cadastrar um usuário com o mesmo e-mail já cadastrado.
     * Deve retornar erro 409 (conflito).
     */ //esse
    public void tentarCadastrarUsuarioDuplicado() {
        UsuarioRequest usuarioDuplicado = FakerApiData.gerarUsuarioRequestComDados(
            UsuarioManager.getNomeCompletoUsuario(),
            UsuarioManager.getNomeUsuario(),
            UsuarioManager.getEmailUsuario(),
            UsuarioManager.getSenhaUsuario()
        );
        cadastrarNovoUsuario(usuarioDuplicado);
        LogFormatter.logStep("Usuario ID: " + UsuarioManager.getIdUsuario());
    }

    // Novo método: cadastra usuário com dados fornecidos
    public void cadastrarNovoUsuario(UsuarioRequest usuarioRequest) {
        response = given()
            .filter(new EvidenceFilter())
            .header(new Header("Content-Type", "application/json"))
            .header(new Header("accept", "application/json"))
            .baseUri(BASE_URL)
            .body(usuarioRequest)
            .when()
            .post(ENDPOINT_USUARIOS);

        if (response.getStatusCode() == 201) {
            UsuarioManager.setEmailUsuario(usuarioRequest.getEmail());
            UsuarioManager.setSenhaUsuario(usuarioRequest.getSenha());
            UsuarioManager.setNomeCompletoUsuario(usuarioRequest.getNomeCompleto());
            UsuarioManager.setNomeUsuario(usuarioRequest.getNomeUsuario());
            String userId = response.jsonPath().getString("id");
            UsuarioManager.setIdUsuario(userId);
            LogFormatter.logStep("Usuario ID: " + userId);
        }
    }

    // Método para gerar e cadastrar usuário aleatório
    public void cadastrarNovoUsuarioAleatorio() {
        UsuarioRequest usuarioGerado = FakerApiData.gerarUsuarioRequestSimples();
        cadastrarNovoUsuario(usuarioGerado);
    }

    public void tentarCadastrarUsuarioComCampoNulo(String metodo, String campo) {
        UsuarioRequest.UsuarioRequestBuilder builder = UsuarioRequest.builder()
            .nomeCompleto(FakerApiData.gerarUsuarioRequestSimples().getNomeCompleto())
            .nomeUsuario(FakerApiData.gerarUsuarioRequestSimples().getNomeUsuario())
            .email(FakerApiData.gerarUsuarioRequestSimples().getEmail())
            .senha(null);

        // Define o campo como nulo conforme parâmetro
        switch (campo.toLowerCase()) {
            case "email":
                builder.email(null);
                break;
            case "nomecompleto":
                builder.nomeCompleto(null);
                break;
            case "nomeusuario":
                builder.nomeUsuario(null);
                break;
            case "senha":
                builder.senha(null);
                break;
            default:
                // Não faz nada se campo não reconhecido
        }

        UsuarioRequest usuarioNulo = builder.build();
        cadastrarNovoUsuario(usuarioNulo);
    }

    public void validarMensagemDeErroNoBody(String mensagemEsperada) {
        String body = response.getBody().asString();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // Busca em 'errors'
        if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
            boolean mensagemEncontrada = errorResponse.getErrors().stream()
                .anyMatch(e -> mensagemEsperada.equals(e.getMsg()));
            if (!mensagemEncontrada) {
                System.out.println("Body erro: " + body);
            }
            assertTrue("Mensagem de erro não encontrada: " + mensagemEsperada, mensagemEncontrada);
            return;
        }
        // Busca em 'erros'
        if (errorResponse.getErros() != null && !errorResponse.getErros().isEmpty()) {
            boolean mensagemEncontrada = errorResponse.getErros().stream()
                .anyMatch(e -> mensagemEsperada.equals(e.getMensagem()));
            if (!mensagemEncontrada) {
                System.out.println("Body erro: " + body);
            }
            assertTrue("Mensagem de erro não encontrada: " + mensagemEsperada, mensagemEncontrada);
            return;
        }
        System.out.println("Body erro: " + body);
        fail("Nenhum erro encontrado na resposta da API.");
    }

    public void validarAlgumaMensagemDeErroNoBody(String... mensagensEsperadas) {
        String body = response.getBody().asString();
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        List<String> mensagensBody = new ArrayList<>();
        if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
            mensagensBody.addAll(errorResponse.getErrors().stream()
                .map(ErrorFieldModel::getMsg)
                .filter(Objects::nonNull)
                .toList());
        }
        if (errorResponse.getErros() != null && !errorResponse.getErros().isEmpty()) {
            mensagensBody.addAll(errorResponse.getErros().stream()
                .map(ErrorFieldModel::getMensagem)
                .filter(Objects::nonNull)
                .toList());
        }

        boolean algumaEncontrada = Arrays.stream(mensagensEsperadas)
            .anyMatch(m -> mensagensBody.contains(m));

        if (!algumaEncontrada) {
            System.out.println("Body erro: " + body);
        }
        assertTrue("Nenhuma das mensagens esperadas foi encontrada: " + Arrays.toString(mensagensEsperadas), algumaEncontrada);
    }
}