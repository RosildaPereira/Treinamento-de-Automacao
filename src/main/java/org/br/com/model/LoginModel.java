package org.br.com.model;

import lombok.Data;

/**
 * Modelo de dados (POJO) que representa as informações de um usuário.
 * Usado para transportar dados entre as camadas da aplicação, como
 * da planilha de dados para os steps de teste.
 * A anotação @Data do Lombok gera automaticamente getters, setters,
 * toString(), equals() e hashCode().
 */
@Data
public class LoginModel {

    private String email;
    private String senha;
    private String nome;
    private String idUsuario;

}