package org.br.com.test.model.error.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Essencial para a desserialização do Jackson/Gson
@JsonIgnoreProperties(ignoreUnknown = true) // Corrigido para annotation válida
public class ErrorResponse {

	private Integer statusCode;
	private String statusError;
	private String path;
	private ErrorModel error;
	private List<ErrorFieldModel> erros; // para padrão { "erros": [...] }
	private List<ErrorFieldModel> errors; // para padrão { "errors": [...] }

}
