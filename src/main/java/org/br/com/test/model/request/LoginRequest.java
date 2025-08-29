package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para requisições de login
 * Contém apenas os campos necessários para autenticação
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	private String email;
	private String senha;
} 