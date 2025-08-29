# Automacao de Testes API REST - Serverest

Este projeto contém testes automatizados para a API Serverest, uma API REST para simulacao de um e-commerce. A API está disponível em http://localhost:3000/ e oferece endpoints para gerenciamento de usuários, produtos e carrinhos de compras.

## 🚀 Tecnologias Utilizadas

- Java 8+
- Cucumber
- JUnit
- RestAssured
- Lombok
- Maven

## 📋 Pré-requisitos

- Java JDK 8 ou superior
- Maven
- Node.js (para rodar o Serverest localmente)

## 🔧 Instalacao

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/automacao-rest-api.git
cd automacao-rest-api
```

2. Instale as dependências do Maven:
```bash
mvn clean install
```

3. Instale e inicie o Serverest:
```bash
npm install -g serverest@latest
serverest
```

## 🏃‍♂️ Executando os Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes específicos por tag
```bash
mvn test -Dcucumber.filter.tags="@FuncionalideUsuario"
mvn test -Dcucumber.filter.tags="@FuncionalideProdutos"
mvn test -Dcucumber.filter.tags="@FuncionalidadesCarrinhos"
```

## 📝 Estrutura do Projeto

```
src/test/
├── java/org/br/com/testes/
│   ├── controllers/         # Controladores para cada entidade
│   │   ├── usuarios/       # Controlador de usuários
│   │   ├── produtos/       # Controlador de produtos
│   │   └── carrinhos/      # Controlador de carrinhos
│   ├── manager/            # Gerenciadores de estado
│   │   ├── TokenManager    # Gerenciamento de tokens
│   │   ├── UsuarioManager  # Gerenciamento de usuários
│   │   ├── ProdutosManager # Gerenciamento de produtos
│   │   └── CarrinhosManager # Gerenciamento de carrinhos
│   ├── model/              # Modelos de dados
│   │   ├── usuario/        # Modelos de usuário
│   │   ├── produtos/       # Modelos de produto
│   │   └── carrinhos/      # Modelos de carrinho
│   ├── steps/              # Implementações dos steps do Cucumber
│   └── utils/              # Utilitários (FakerApiData, etc)
└── resources/
    └── features/           # Arquivos .feature do Cucumber
        ├── usuarios.feature
        ├── produtos.feature
        └── carrinhos.feature
```

## 🔍 Funcionalidades Testadas

### Usuários (@FuncionalideUsuario)
- Cadastro de usuário (CT-1001)
- Login (CT-1002)
- Busca por ID (CT-1003)
- Listagem (CT-1004)
- Edicao (CT-1005)
- Exclusão (CT-1006)
- Validacao de email duplicado (CT-1007)

### Produtos (@FuncionalideProdutos)
- Cadastro de produto (CT-2001)
- Busca por ID (CT-2002)
- Listagem (CT-2003)
- Edicao (CT-2004)
- Exclusão (CT-2005)

### Carrinhos (@FuncionalidadesCarrinhos)
- Cadastro de carrinho (CT-3001)
- Busca por ID (CT-3002)
- Listagem (CT-3003)
- Exclusão (CT-3004)
- Cancelamento de compra e reabastecimento de estoque (CT-3005)

## 📊 Relatórios

Os relatórios dos testes são gerados em:
- HTML: `target/cucumber-reports/cucumber.html`
- JSON: `target/cucumber-reports/cucumber.json`

## 🔗 Documentacao da API

A API Serverest está disponível em:
- Local: http://localhost:3000
- Online: https://serverest.dev

### Endpoints Principais

#### Usuários
- POST /usuarios - Cadastrar usuário
- POST /login - Login
- GET /usuarios - Listar usuários
- GET /usuarios/{id} - Buscar usuário
- PUT /usuarios/{id} - Editar usuário
- DELETE /usuarios/{id} - Excluir usuário

#### Produtos
- POST /produtos - Cadastrar produto
- GET /produtos - Listar produtos
- GET /produtos/{id} - Buscar produto
- PUT /produtos/{id} - Editar produto
- DELETE /produtos/{id} - Excluir produto

#### Carrinhos
- POST /carrinhos - Cadastrar carrinho
- GET /carrinhos - Listar carrinhos
- GET /carrinhos/{id} - Buscar carrinho
- PUT /carrinhos/{id} - Editar carrinho
- DELETE /carrinhos/concluir-compra - Concluir compra
- DELETE /carrinhos/cancelar-compra - Cancelar compra

## 🔐 Autenticacao

A API utiliza autenticacao via token Bearer. O token é obtido através do endpoint de login e deve ser incluído no header `Authorization` de todas as requisições que necessitam de autenticacao.

Exemplo de uso do token:
```java
given()
    .header("Authorization", "Bearer " + token)
    .contentType(ContentType.JSON)
    .when()
    .get("/endpoint")
```

## 📦 Dados de Teste

O projeto utiliza a biblioteca Faker para gerar dados aleatórios para os testes:
- Usuários: nome, email, senha
- Produtos: nome, preço, descricao, quantidade
- Carrinhos: produtos e quantidades






# Logger Java Padrão (core/logger)

## Visão Geral

Este módulo define um padrão de logger Java para projetos de automacao de testes, aplicações backend ou qualquer sistema que exija rastreabilidade, clareza e padronizacao de logs.
A estrutura foi pensada para ser plugável, reutilizável e facilmente adaptável a diferentes projetos.

---

## Estrutura Recomendada

```
core/
└── logger/
    ├── Console.java
    ├── ContextTime.java
    └── (outros utilitários de log)
```

---

## Objetivos

- Centralizar e padronizar a geracao de logs.
- Facilitar a manutencao e evolucao do sistema de logging.
- Permitir fácil integracao com frameworks de teste, aplicações web, microserviços, etc.
- Suportar logs coloridos, logs de contexto, logs de tempo de execucao e outros formatos.

---

## Passo a Passo para Criacao e Uso

### 1. Crie a pasta `core/logger` no seu projeto

```bash
mkdir -p src/main/java/core/logger
```

### 2. Adicione as classes utilitárias

#### Exemplo: Console.java

```java
package core.logger;

public class Console {
    public static void log(String message) {
        System.out.println(message);
    }
    // Adicione métodos para logs coloridos, níveis (INFO, ERROR, etc.) conforme necessidade
}
```

#### Exemplo: ContextTime.java

```java
package core.logger;

public class ContextTime {
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - startTime;
    }
    // Permite medir o tempo de execucao de blocos de código
}
```

### 3. Importe e utilize o logger no seu projeto

```java
import core.logger.Console;
import core.logger.ContextTime;

public class ExemploUsoLogger {
    public static void main(String[] args) {
        Console.log("Iniciando processo...");

        ContextTime timer = new ContextTime();
        timer.start();

        // ... código a ser medido ...

        Console.log("Tempo de execucao: " + timer.elapsed() + " ms");
    }
}
```

---

## Boas Práticas

- Sempre utilize o logger centralizado para todas as saídas de log.
- Evite `System.out.println` dispersos pelo código.
- Adapte o logger para suportar diferentes níveis (INFO, WARN, ERROR) e formatos conforme a necessidade do projeto.
- Considere integracao futura com frameworks como Log4j, SLF4J ou Allure, se necessário.

---

## Customizacao

- Adicione métodos para logs coloridos (ANSI), logs em arquivo, logs JSON, etc.
- Implemente logs de contexto para rastrear execuções paralelas ou multi-thread.
- Crie wrappers para integracao com frameworks de teste (JUnit, Cucumber, etc).

---

## Exemplo de Estrutura Completa

```
core/
└── logger/
    ├── Console.java
    ├── ContextTime.java
    ├── LogFormatter.java
    ├── LogFileManager.java
    └── (outros utilitários)
```

---

## Licença

Este módulo pode ser utilizado livremente em projetos internos ou open source, desde que mantida a referência ao autor original. 
