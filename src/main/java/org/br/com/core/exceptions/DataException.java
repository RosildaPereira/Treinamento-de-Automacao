package org.br.com.core.exceptions;

/**
 * Exceção personalizada para lidar com erros relacionados a dados.
 * Estende {@link RuntimeException} para indicar que é uma exceção não verificada.
 * TODO: Esta classe é instanciada em (adicionar locais de instanciação).
 */
public class DataException extends RuntimeException {

	/**
	 * O serialVersionUID é usado para garantir que uma classe serializada
	 * seja compatível com a versão da classe que a desserializa.
	 * TODO: Verificar se o serialVersionUID precisa ser atualizado em futuras modificações.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que cria uma nova exceção {@code DataException} com a mensagem especificada.
	 * @param message A mensagem de detalhe (que é salva para recuperação posterior pelo método {@link Throwable#getMessage()}).
	 * TODO: Adicionar exemplos de uso deste construtor.
	 */
	public DataException(String message) {
		super(message);
	}

	/**
	 * Construtor que cria uma nova exceção {@code DataException} com a causa especificada.
	 * @param cause A causa (que é salva para recuperação posterior pelo método {@link Throwable#getCause()}).
	 *              (Um valor {@code null} é permitido e indica que a causa é inexistente ou desconhecida.)
	 * TODO: Adicionar exemplos de uso deste construtor.
	 */
	public DataException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construtor que cria uma nova exceção {@code DataException} com a mensagem e a causa especificadas.
	 * @param message A mensagem de detalhe.
	 * @param e A causa.
	 * TODO: Adicionar exemplos de uso deste construtor.
	 */
	public DataException(String message, Throwable e) {
		super(message, e);
	}
}