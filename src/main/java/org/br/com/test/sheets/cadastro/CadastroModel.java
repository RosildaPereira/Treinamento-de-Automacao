package org.br.com.test.sheets.cadastro;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.br.com.test.sheets.login.LoginModel;

/**
 * Modelo padrão para representar os dados de cadastro de um usuário.
 * Herda os campos de login (email, senha, idUsuario) de LoginModel.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadastroModel extends LoginModel {
    private String nomeCompleto;
    private String nomeUsuario;
}