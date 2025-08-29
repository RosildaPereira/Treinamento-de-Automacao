package org.br.com.test.model.error.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorFieldModel {
	// Para o padrão {"erros": [...]}
	private String campo;
	private String mensagem;

	// Para o padrão {"errors": [...]}
	private String msg;
	private String path;
	private String type;
	private String value;
	private String location;
}