package org.br.com.test.utils.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;

import java.util.Collection;

public class HooksUsers {

	@Getter
	private static String tagCenario;

	@Before
	public void pegarTagCenario(Scenario cenario) {
		tagCenario = extrairTagDoCenario(cenario.getSourceTagNames());
	}


	/**
	 * Metodo privado para extrair a TAG do cenario atual a partir da lista de nomes de tags.
	 *
	 * @param sourceTagNames A colecao de nomes de tags do cenario atual.
	 * @return A TAG do cenario, sem o prefixo "@".
	 */
	private String extrairTagDoCenario(Collection<String> sourceTagNames) {
		for (String tagName : sourceTagNames) {
			if (tagName.startsWith("@") && tagName.contains("CT-") && !tagName.equals("@feature")) {
				return tagName.substring(1); // Remove o "@" da TAG
			}
		}
		return null;
	}
}
