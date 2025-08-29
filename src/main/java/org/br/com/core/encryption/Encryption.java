package org.br.com.core.encryption;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * TODO: Esta classe é responsável por fornecer métodos para criptografar e descriptografar textos
 * usando o algoritmo AES.
 * É instanciada em [TODO: Adicionar onde a classe é instanciada].
 */
public class Encryption {

	/**
	 * TODO: Este método criptografa um texto usando uma chave secreta.
	 *
	 * @param texto O texto a ser criptografado.
	 * @param chaveSecreta A chave secreta usada para a criptografia.
	 * @return O texto criptografado em formato Base64.
	 * @throws Exception Se ocorrer um erro durante a criptografia.
	 */
	public static String criptografar(String texto, String chaveSecreta) throws Exception {
		// TODO: Cria uma chave secreta a partir dos bytes da chave secreta fornecida, usando o algoritmo AES.
		SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
		// TODO: Obtém uma instância do Cipher para o algoritmo AES.
		Cipher cipher = Cipher.getInstance("AES");
		// TODO: Inicializa o Cipher no modo de criptografia com a chave secreta.
		cipher.init(Cipher.ENCRYPT_MODE, chave);
		// TODO: Criptografa o texto e armazena os bytes criptografados.
		byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
		// TODO: Codifica os bytes criptografados para uma string Base64 e a retorna.
		return Base64.getEncoder().encodeToString(textoCriptografado);
	}

	/**
	 * TODO: Este método descriptografa um texto criptografado usando uma chave secreta.
	 *
	 * @param textoCriptografado O texto criptografado em formato Base64 a ser descriptografado.
	 * @param chaveSecreta A chave secreta usada para a descriptografia.
	 * @return O texto descriptografado.
	 * @throws Exception Se ocorrer um erro durante a descriptografia.
	 */
	public static String descriptografar(String textoCriptografado, String chaveSecreta) throws Exception {
		// TODO: Cria uma chave secreta a partir dos bytes da chave secreta fornecida, usando o algoritmo AES.
		SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
		// TODO: Obtém uma instância do Cipher para o algoritmo AES.
		Cipher cipher = Cipher.getInstance("AES");
		// TODO: Inicializa o Cipher no modo de descriptografia com a chave secreta.
		cipher.init(Cipher.DECRYPT_MODE, chave);
		// TODO: Decodifica a string Base64 do texto criptografado para bytes.
		byte[] textoDecodificado = Base64.getDecoder().decode(textoCriptografado);
		// TODO: Descriptografa os bytes decodificados e armazena os bytes descriptografados.
		byte[] textoDescriptografado = cipher.doFinal(textoDecodificado);
		// TODO: Converte os bytes descriptografados para uma string e a retorna.
		return new String(textoDescriptografado);
	}

}
