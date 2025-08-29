package org.br.com.test.controllers.categoria;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.CategoriaManager;
import org.br.com.test.manager.TokenManager;
import org.br.com.core.filter.EvidenceFilter;
import org.br.com.test.utils.JavaFaker;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class CategoriaController {
	private Response response;
	private static final String BASE_URL = "http://localhost:3000";
	private static final String ENDPOINT_CATEGORIA = "/categorias";

	public CategoriaController() {
		response = null;
	}

	public void cadastrarNovaCategoria() {
		String token = TokenManager.getToken();

		Map<String, String> categoriaRequest = JavaFaker.categoriaJavaFake();

		response = given()
				.filter(new EvidenceFilter())
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.body(categoriaRequest)
				.when()
				.post(ENDPOINT_CATEGORIA)
				.then()
				.extract().response();

		// Extrair e salvar todos os dados da categoria
		String nome = response.jsonPath().getString("nome");
		String descricao = response.jsonPath().getString("descricao");
		String categoriaId = response.jsonPath().getString("id");
		
		// Salvar no CategoriaManager
		CategoriaManager.setNomeCategoria(nome);
		CategoriaManager.setDescricaoCategoria(descricao);
		CategoriaManager.setCategoriaId(categoriaId);

		LogFormatter.logStep("Categoria ID: " + categoriaId);
		LogFormatter.logStep("Nome: " + nome);
		LogFormatter.logStep("Descrição: " + descricao);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.prettyPrint());
	}

	public void validarStatusCodeCategoria(int statusCode) {
		response.then()
				.statusCode(statusCode);
		LogFormatter.logStep("Validação de Status Code "+ statusCode);
		assertEquals("Status code não corresponde ao esperado", statusCode, this.response.getStatusCode());
	}

	public void listarCategorias() {
		String token = TokenManager.getToken();
		
		response = given()
				.filter(new EvidenceFilter())
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_CATEGORIA)
				.then()
				.extract().response();
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.prettyPrint());
	}

	public void buscarCategoriaPorId() {
		String token = TokenManager.getToken();
		String categoriaId = CategoriaManager.getCategoriaId();
		
		response = given()
				.filter(new EvidenceFilter())
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.get(ENDPOINT_CATEGORIA + "/" + categoriaId);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.asString());
	}

	public void atualizarCategoriaPorId() {
		String token = TokenManager.getToken();
		String categoriaId = CategoriaManager.getCategoriaId();

		Map<String, String> novaCategoria = JavaFaker.categoriaJavaFake();

		response = given()
				.filter(new EvidenceFilter())
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.body(novaCategoria)
				.when()
				.put(ENDPOINT_CATEGORIA + "/" + categoriaId);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.asString());
	}

	public void excluirCategoriaPorId() {
		String token = TokenManager.getToken();
		String categoriaId = CategoriaManager.getCategoriaId();

		response = given()
				.filter(new EvidenceFilter())
				.header("accept", "*/*")
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.delete(ENDPOINT_CATEGORIA + "/" + categoriaId);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.asString());
	}

	public void excluirCategoriaEmMassa(String id) {

		String token = TokenManager.getToken();

		response = given()
				.filter(new EvidenceFilter())
				.header("accept", "*/*")
				.header("Authorization", "Bearer " + token)
				.baseUri(BASE_URL)
				.when()
				.delete(ENDPOINT_CATEGORIA + "/" + id);
		// Log do body da resposta formatado em JSON
		LogFormatter.logStep(response.asString());

	}
}
