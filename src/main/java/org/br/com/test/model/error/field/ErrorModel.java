package org.br.com.test.model.error.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propriedades desconhecidas durante a desserialização
public class ErrorModel {

	private String code; // Para erros tipo 404/500
	private String message; // Para erros tipo 404/500
	private List<ErrorFieldModel> fields; // Para erros de validação detalhada
	private Object data; // Para dados extras

	// Para erro 400 (padrão novo)
	private String erro; // Mensagem principal do erro (ex: "Email já cadastrado")
	private List<ApiErrorDetail> errors; // Lista de detalhes (msg, param, location)
}
