package org.br.com.test.steps;

import io.cucumber.java.en.*;
import org.br.com.test.controllers.usuario.UsuarioController;

public class UsuarioSteps {

	private final UsuarioController usuarioController = new UsuarioController();

	// =================================================================
	// == STEPS DE AÇÃO (GIVEN / WHEN)
	// =================================================================

	@Given("que envio uma solicitação 'POST' de registro de usuario CMS")
	public void queEnvioUmaSolicitacaoDeRegistro() {
		usuarioController.carregarDadosDaPlanilha(); // Apenas carrega dados, não cadastra
	}

	@When("envio novamente uma solicitação 'POST' para registrar o mesmo email")
	public void envioNovamenteUmaSolicitacaoParaRegistrarOMesmoEmail() {
		usuarioController.tentarCadastrarUsuarioComEmailDuplicado();
	}

	@When("envio novamente uma solicitação 'POST' para registrar o mesmo usuário")
	public void envioNovamenteUmaSolicitacaoParaRegistrarOMesmoUsuario() {
		usuarioController.tentarCadastrarUsuarioComNomeDeUsuarioDuplicado();
	}

	@When("eu envio uma solicitação de criação de usuário com dados inválidos")
	public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComDadosInvalidos() {
		usuarioController.tentarCadastrarUsuarioComDadosInvalidos();
	}

	@When("eu envio uma solicitação de criação de usuário com email inválido")
	public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComEmailInvalido() {
		usuarioController.tentarCadastrarUsuarioComEmailInvalido();
	}

	@When("eu envio uma solicitação de criação de usuário com senha inválida")
	public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComSenhaInvalida() {
		usuarioController.tentarCadastrarUsuarioComSenhaInvalida();
	}

	@When("eu realizo o login com as credenciais válidas do usuário criado")
	public void euRealizoOloginComAsCredenciaisValidasDoUsuarioCriado() {
		usuarioController.realizarLogin(true);
	}

	@When("eu envio uma solicitação de login com credenciais inválidas")
	public void euEnvioUmaSolicitacaoDeLoginComCredenciaisInvalidas() {
		usuarioController.realizarLogin(false);
	}

	@When("eu envio a requisição de listar de usuários com autenticação")
	public void euEnvioARequisicaoDeListarDeUsuariosComAutenticacao() {
		usuarioController.listarUsuarios(true);
	}

	@When("eu envio a requisição de listar de usuários sem autenticação")
	public void euEnvioARequisicaoDeListarDeUsuariosSemAutenticacao() {
		usuarioController.listarUsuarios(false);
	}

	@When("eu envio a requisição de busca de usuário por ID")
	public void euEnvioARequisicaoDeBuscaDeUsuarioPorID() {
		usuarioController.buscarUsuarioPorId(true, true);
	}

	@Given("eu envio a requisição de busca de usuário sem ID")
	public void euEnvioARequisicaoDeBuscaDeUsuarioSemID() {
		usuarioController.buscarUsuarioPorId(false, false);
	}

	@When("que envio a solicitação de PUT com ID")
	public void queEnvioASolicitacaoDePUTComID() {
		usuarioController.atualizarUsuario(true);
	}

	@When("que envio a solicitação de PUT sem ID")
	public void queEnvioASolicitacaoDePUTSemID() {
		usuarioController.atualizarUsuario(false);
	}

	@When("envio uma solicitação de DELETE para o ID")
	public void envioUmaSolicitacaoDeDELETEParaOID() {
		usuarioController.excluirUsuario(true, true);
	}

	@When("envio uma solicitação de DELETE para o ID sem autenticação")
	public void envioUmaSolicitacaoDeDELETEParaOIDSemAutenticacao() {
		usuarioController.excluirUsuario(true, false);
	}


	// =================================================================
	// == STEPS DE VALIDAÇÃO (THEN / AND)
	// =================================================================

	@Then("a resposta da API {word} deve retornar o código de status {int}")
	public void aRespostaDaApiDeveRetornarOCodigoDeStatus(String api, int statusCode) {
		usuarioController.validarStatusCode(statusCode);
	}

	@Then("os dados do usuário na resposta de criação devem estar corretos")
	public void osDadosDoUsuarioNaRespostaDeCriacaoDevemEstarCorretos() {
		usuarioController.validarCorpoUsuarioCriadoComSucesso();
	}

	@Then("o corpo da resposta de login deve conter os dados do usuário")
	public void oCorpoDaRespostaDeLoginDeveConterOsDadosDoUsuario() {
		usuarioController.validarCorpoUsuarioLogadoComSucesso();
	}

	@Then("a resposta da API {word} deve retornar status {int} com a mensagem de erro {string}")
	public void aRespostaDaApiDeveRetornarStatusComMensagemDeErro(String api, int statusCode, String mensagem) {
		usuarioController.validarErroSimples(statusCode, mensagem);
	}

	@Then("a resposta da API {word} deve retornar status {int} com erro no campo {string} e mensagem {string}")
	public void aRespostaDaApiDeveRetornarStatusComErroNoCampoEMensagem(String api, int statusCode, String campo, String mensagem) {
		usuarioController.validarErroDeCampo(statusCode, campo, mensagem);
	}
}