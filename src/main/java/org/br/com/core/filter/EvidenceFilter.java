package org.br.com.core.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.br.com.test.utils.hooks.HooksEvidenciasApi;

/**
 * TODO: Esta classe {@code EvidenceFilter} é um filtro de Requisições e Respostas
 * para a API RestAssured. Ela é responsável por capturar e registrar dados
 * de requisições e respostas HTTP para fins de evidência, utilizando a classe
 * {@link HooksEvidenciasApi}.
 *
 * <p>Esta classe implementa a interface {@link Filter} do RestAssured,
 * permitindo que ela seja encadeada na execução de requisições.</p>
 *
 * @author [Seu Nome/Nome da Equipe]
 * @version 1.0
 * @since 2023-10-26
 */
public class EvidenceFilter implements Filter {

    /**
     * TODO: Este método {@code filter} é a implementação da interface {@link Filter}
     * e é chamado para cada requisição HTTP. Ele intercepta a requisição e a resposta,
     * extrai informações relevantes e as envia para a classe {@link HooksEvidenciasApi}
     * para registro.
     *
     * @param requestSpec TODO: Especificação da requisição filtrável, contendo detalhes como método, URI, cabeçalhos e corpo.
     * @param responseSpec TODO: Especificação da resposta filtrável.
     * @param ctx TODO: Contexto do filtro, usado para prosseguir com a cadeia de filtros.
     * @return TODO: A resposta HTTP processada.
     */
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();
        String headers = requestSpec.getHeaders().toString();
        String body = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "";

        HooksEvidenciasApi.capturarDadosRequisicao(method, uri, headers, body);

        String statusCode = String.valueOf(response.getStatusCode());
        String responseBody = response.getBody().asString();

        HooksEvidenciasApi.capturarDadosResposta(statusCode, responseBody);

        return response;
    }
}