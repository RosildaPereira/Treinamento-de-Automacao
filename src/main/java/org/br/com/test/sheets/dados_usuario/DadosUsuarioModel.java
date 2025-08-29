package org.br.com.test.sheets.dados_usuario;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.br.com.test.sheets.login.LoginModel;

/**
 * Modelo mestre e unificado para representar os dados de um usuário,
 * incluindo informações de identificação adicionais.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DadosUsuarioModel extends LoginModel {
    // Campos que antes estavam em DadosIdModel
    private String tipoId;
    private String valorId;

    // Outros campos de usuário podem ser adicionados aqui se necessário
    // private String nomeCompleto;
    // private String nomeUsuario;
}