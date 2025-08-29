package org.br.com.test.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Essencial para ignorar campos não mapeados como 'artigos'
public class UsuarioResponse {

    private String id;
    private String nomeCompleto;
    private String nomeUsuario;
    private String email;
    private String dataCriacao;

}