package org.br.com.test.sheets.login;

import lombok.Data;
import lombok.NoArgsConstructor; // <-- Add this import
import lombok.experimental.SuperBuilder;
import org.br.com.core.data.DataModel;

@Data
@SuperBuilder
@NoArgsConstructor // <-- Add this annotation
public class LoginModel implements DataModel {
	private String email;
	private String senha;
	private String idUsuario; // O UUID do usuário
}