package org.br.com.test.model.error.timeStamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Essencial para a desserialização do Jackson/Gson
@JsonIgnoreProperties(ignoreUnknown = true) // Corrigido para annotation válida
public class ErrorBadRequestResponse {

	private String timestamp;
	private Integer code;
	private String exception;
	private String message;
	private String path;


}
