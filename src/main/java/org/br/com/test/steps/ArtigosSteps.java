package org.br.com.test.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.br.com.test.controllers.artigos.ArtigosController;

public class ArtigosSteps {

	private final ArtigosController artigosController;

	public ArtigosSteps() {
		this.artigosController = new ArtigosController();
	}


	@Given("que envio uma requisição de cadastro de Artigos")
	public void queEnvioUmaRequisicaoDeCadastroDeArtigos() throws Exception {
		artigosController.cadastrarArtigo();
	}

	@And("crio uma categoria para os artigos")
	public void crioUmaCategoriaParaOsArtigos() {
		artigosController.criarCategoriaAntesDoArtigo();
	}

	@And("crio um artigo para os test")
	public void crioUmArtigoParaOstest() {
		artigosController.cadastrarArtigo();
	}

	@Then("a API Artigos deve retornar o código de status {int}")
	public void aApiArtigosDeveRetornarOCodigoDeStatus(int statusCode) {
		artigosController.validarStatusCodeArtigos(statusCode);
	}


	@When("eu envio a requisição de listar Artigos com autenticação")
	public void euEnvioARequisicaoDeListarArtigosComAutenticacao() {
		artigosController.listarArtigos();
	}

	@Given("eu envio a requisição de busca de artigos por ID")
	public void euEnvioARequisicaoDeBuscaDeArtigosPorID() {
		artigosController.buscarArtigoPorId();
	}

	@Given("eu envio a requisição PUT Artigos com ID")
	public void euEnvioARequisicaoPUTArtigosComID() {
		artigosController.atualizarArtigoPorId();
	}

	@Given("eu envio a requisição DELETE Artigos com ID")
	public void euEnvioARequisicaoDeDELETEParaOID() {
		artigosController.excluirArtigoPorId();
	}


	@Then("a API Artigos deve retornar o status code {int} para exclusão")
	public void aApiArtigosDeveRetornarOStatusCodeParaExclusao(int statusCode) {
		artigosController.validarStatusCodeExclusao(statusCode);
	}

	@Given("envio uma solicitação de DELETE para o Artigos {string}")
	public void envioUmaSolicitacaoDeDELETEParaOArtigos(String arg0) {
		artigosController.excluirArtigosEmMassa(arg0);
	}
}
