//package org.br.com.test.steps;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.br.com.test.controllers.usuario.UsuarioController;
//import org.br.com.test.controllers.usuario.UsuarioLoginController;
//
//public class LoginSteps {
//
//	private final UsuarioLoginController usuarioLoginController;
//	private final UsuarioController usuarioController; // Precisamos criar um usuário antes de logar
//
//	public LoginSteps() {
//		this.usuarioLoginController = new UsuarioLoginController();
//		this.usuarioController = new UsuarioController();
//	}
//
//	// Step de pré-condição (Background)
//	@Given("um usuário válido foi criado na API")
//	public void umUsuarioValidoFoiCriadoNaAPI() {
//		usuarioController.cadastrarNovoUsuario();
//		usuarioController.validarStatusCode(201); // Garante que a criação funcionou
//	}
//
//	@When("eu envio uma solicitação de login com credenciais válidas")
//	public void euEnvioUmaSolicitacaoDeLoginComCredenciaisValidas() {
//		usuarioLoginController.realizarLoginComCredenciaisValidas();
//	}
//
//	@When("eu envio uma solicitação de login com credenciais inválidas")
//	public void euEnvioUmaSolicitacaoDeLoginComCredenciaisInvalidas() {
//		usuarioLoginController.realizarLoginComCredenciaisInvalidas();
//	}
//
//	// Este step agora é genérico e pode ser usado em qualquer feature
//	@Then("a resposta da API retorna o status code {int}")
//	public void aRespostaDaAPIRetornaOStatusCode(int statusCode) {
//		usuarioLoginController.validarStatusCode(statusCode);
//	}
//
//	@Then("valido a resposta com os dados do usuário logado com sucesso")
//	public void validoARespostaComOsDadosDoUsuarioLogadoComSucesso() {
//		usuarioLoginController.validarDadosRetornados();
//	}
//
//	@Then("alguma das mensagens de erro deve ser exibida: {string}")
//	public void algumaDasMensagensDeErroDeveSerExibida(String mensagens) {
//		usuarioLoginController.validarPresencaDePeloMenosUmaMensagemDeErro(mensagens);
//	}
//}