package org.br.com.model;

import lombok.Builder;
import lombok.Data;

/**
 * Modelo de dados (POJO) que representa os dados de um usuário lidos da planilha.
 * A anotação @Data do Lombok gera getters/setters.
 * A anotação @Builder permite a construção fluente de objetos.
 */
@Data
@Builder
public class DadosUsuarioModel {
    private String idUsuario;
    private String email;
    private String senha;
    private String tipoId;
    private String valorId;
}