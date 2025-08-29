package org.br.com.model;

import lombok.Data;

/**
 * Modelo de dados (POJO) que representa as informações de uma Categoria.
 * Usado para transportar dados entre as camadas da aplicação.
 * A anotação @Data do Lombok gera automaticamente os métodos necessários.
 */
@Data
public class CategoriaModel {

    private String id;
    private String nome;

}