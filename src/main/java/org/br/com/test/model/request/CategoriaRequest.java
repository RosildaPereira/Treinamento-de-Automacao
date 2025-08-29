package org.br.com.test.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaRequest {
	private String nome;
	private String descricao;
}
