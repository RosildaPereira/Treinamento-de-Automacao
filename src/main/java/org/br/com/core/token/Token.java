package org.br.com.core.token;

/**
 * TODO: Interface que representa um token, definindo os métodos para obter o tipo de produto e o token de acesso.
 * Esta interface é implementada por classes que gerenciam a autenticação e autorização.
 *
 * TODO: Esta interface é instanciada em:
 * - {@code org.br.com.core.token.TokenImpl} (implementação concreta)
 * - {@code org.br.com.core.web.AbstractWebClient} (utiliza a interface para obter tokens)
 * - {@code org.br.com.core.web.WebClient} (utiliza a interface para obter tokens)
 */
public interface Token {

	/**
	 * TODO: Retorna o tipo de produto associado a este token.
	 * Este método é crucial para identificar a qual serviço ou aplicação o token pertence.
	 * @return Uma String representando o tipo de produto.
	 */
	String getProductType();

	/**
	 * TODO: Retorna o token de acesso real (string do token).
	 * Este é o valor que será usado para autenticar requisições.
	 * @return Uma String representando o token de acesso.
	 */
	String getAccessToken();

}
