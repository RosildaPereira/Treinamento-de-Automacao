package org.br.com.test.utils;

import java.util.Random;
import java.util.stream.IntStream;


public class GeradorDeCpf {

	/**
	 * Gera um número de CPF válido e aleatório.
	 *
	 * @return Uma string com 11 dígitos representando um CPF válido.
	 */
	public static String gerar() {
		Random random = new Random();
		int[] digitos = new int[9];

		// Gera os 9 primeiros dígitos aleatoriamente
		for (int i = 0; i < 9; i++) {
			digitos[i] = random.nextInt(10);
		}

		// Calcula os dois dígitos verificadores
		int d1 = calcularDigitoVerificador(digitos, 10);
		int d2 = calcularDigitoVerificador(IntStream.concat(IntStream.of(digitos), IntStream.of(d1)).toArray(), 11);

		// Constrói a string final do CPF
		StringBuilder cpfBuilder = new StringBuilder();
		for (int digito : digitos) {
			cpfBuilder.append(digito);
		}
		cpfBuilder.append(d1).append(d2);

		return cpfBuilder.toString();
	}

	/**
	 * Calcula um dígito verificador de CPF com base nos dígitos fornecidos.
	 *
	 * @param digitos O array de dígitos para o cálculo.
	 * @param pesoInicial O peso inicial para a multiplicação (10 para o primeiro dígito, 11 para o segundo).
	 * @return O dígito verificador calculado.
	 */
	private static int calcularDigitoVerificador(int[] digitos, int pesoInicial) {
		int soma = 0;
		for (int i = 0; i < digitos.length; i++) {
			soma += digitos[i] * (pesoInicial - i);
		}

		// AQUI ESTÁ A LÓGICA CORRETA E CLARA
		int resto = soma % 11;

		// Se o resto for 0 ou 1, o dígito é 0. Caso contrário, é 11 - resto.
		return (resto < 2) ? 0 : (11 - resto);
	}

}
