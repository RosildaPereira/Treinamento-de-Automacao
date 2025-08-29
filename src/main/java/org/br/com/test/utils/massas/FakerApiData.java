package org.br.com.test.utils.massas;

import com.github.javafaker.Faker;
import org.br.com.test.model.request.UsuarioRequest;

import java.util.Locale;

public class FakerApiData {
	private static final Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));

	public static UsuarioRequest gerarUsuarioRequestSimples() {
		return UsuarioRequest.builder()
				.nomeCompleto(faker.name().fullName())
				.nomeUsuario(faker.name().username())
				.email(faker.internet().emailAddress())
				.senha(faker.internet().password(8, 16, true, true, true))
				.build();
	}

	public static UsuarioRequest gerarUsuarioRequestComDados(String nomeCompleto, String nomeUsuario, String email, String senha) {
		return UsuarioRequest.builder()
				.nomeCompleto(nomeCompleto)
				.nomeUsuario(nomeUsuario)
				.email(email)
				.senha(senha)
				.build();
	}
}