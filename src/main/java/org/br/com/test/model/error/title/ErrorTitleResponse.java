package org.br.com.test.model.error.title;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Essencial para a desserialização do Jackson/Gson
@JsonIgnoreProperties(ignoreUnknown = true) // Corrigido para annotation válida
public class ErrorTitleResponse {

	private Integer statusCode;
	private String statusError;
	private String path;
	private ErrorTitleModel error;
}
