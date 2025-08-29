# Livro do Projeto de Automação de Testes e API

Este documento serve como um guia completo sobre a arquitetura, tecnologias e metodologias empregadas neste projeto.

---

## 1. Introdução

Bem-vindo ao guia do projeto! Este documento detalha a arquitetura e as tecnologias por trás de um ecossistema de software completo, composto por dois componentes principais:

1.  **Uma API de back-end (`cms-for-qas-api`)**: Desenvolvida em Node.js com TypeScript, esta API serve como um sistema de gerenciamento de conteúdo (CMS) simplificado, projetado especificamente para ser o alvo dos testes de qualidade.
2.  **Um Framework de Automação de Testes (Java)**: Construído em Java, este framework robusto é projetado para validar de forma exaustiva todos os endpoints e funcionalidades da `cms-for-qas-api`.

O objetivo principal deste projeto é duplo: por um lado, fornecer uma API funcional e bem estruturada; por outro, garantir sua qualidade e confiabilidade através de uma suíte de testes automatizados abrangente. A automação de testes segue a metodologia de Desenvolvimento Guiado por Comportamento (BDD), o que torna os testes legíveis e fáceis de entender tanto para desenvolvedores quanto para analistas de qualidade.

Ao longo deste livro, exploraremos a estrutura de cada um desses componentes, as tecnologias utilizadas em seu desenvolvimento e as estratégias de teste empregadas para garantir que a API funcione conforme o esperado.

---

## 2. Estrutura do Projeto

A solução é dividida em dois projetos principais, cada um com sua própria estrutura e responsabilidades.

### 2.1. Framework de Automação de Testes (Java)

O projeto de automação, escrito em Java, segue uma estrutura modular e organizada para facilitar a manutenção e a escalabilidade dos testes.

```
/
├── src/
│   ├── main/
│   │   ├── java/org/br/com/
│   │   │   ├── aplication/         (Classes de Runner e configuração)
│   │   │   ├── controller/         (Controladores por entidade da API)
│   │   │   ├── core/
│   │   │   │   └── logger/         (Estrutura de log customizada)
│   │   │   ├── manager/            (Gerenciadores de estado, como Token)
│   │   │   ├── model/              (POJOs para representar dados da API)
│   │   │   ├── steps/              (Implementação dos passos do Cucumber)
│   │   │   └── util/               (Classes utilitárias)
│   │   └── resources/
│   │       ├── features/
│   │       │   └── (*.feature - Cenários de teste em Gherkin)
│   │       └── log4j2.xml          (Configuração de Logs)
│   └── test/
│       └── (Testes unitários para o framework, se aplicável)
└── pom.xml (Arquivo de configuração do Maven)
```

**Descrição dos Pacotes Principais:**

*   **`controller`**: Contém classes que abstraem a lógica de interação com os endpoints da API. Cada controlador (ex: `UsuariosController`) é responsável por montar e executar as requisições HTTP (usando REST Assured) para uma entidade específica.
*   **`manager`**: Responsável pelo gerenciamento de estado entre os passos dos testes. Por exemplo, o `TokenManager` armazena o token de autenticação após o login para que ele possa ser usado em requisições subsequentes.
*   **`model`**: Contém as classes POJO (Plain Old Java Objects) que mapeiam os corpos de requisição e resposta da API, permitindo uma manipulação de dados mais segura e orientada a objetos.
*   **`steps`**: O coração do BDD. Este pacote contém as classes que implementam os passos definidos nos arquivos `.feature`. Cada método é anotado com `@Given`, `@When`, `@Then`, etc.
*   **`util`**: Um conjunto de classes utilitárias para tarefas comuns, como gerar dados de teste (`Faker`), ler dados de planilhas ou formatar dados.
*   **`core/logger`**: Implementa um sistema de log customizado para padronizar as saídas de log do console, tornando-as mais legíveis.
*   **`resources/features`**: Onde vivem os cenários de teste escritos em Gherkin. Estes arquivos são a especificação viva do comportamento esperado da API.

### 2.2. API (`cms-for-qas-api`)

A API, desenvolvida com Node.js e TypeScript, também possui uma estrutura bem definida, seguindo as melhores práticas de desenvolvimento de APIs REST.

```
cms-for-qas-api/
├── src/
│   ├── database/
│   │   └── data-source.ts  (Configuração da conexão com o banco via TypeORM)
│   ├── entities/
│   │   └── (*.ts - Definições das tabelas do banco)
│   ├── middleware/
│   │   └── (*.ts - Middlewares do Express, ex: autenticação)
│   ├── routes/
│   │   └── (*.ts - Definição das rotas da API)
│   ├── services/
│   │   └── (*.ts - Lógica de negócio da aplicação)
│   ├── utils/
│   │   └── (*.ts - Funções utilitárias)
│   └── server.ts           (Arquivo principal que inicia o servidor Express)
├── package.json            (Dependências e scripts do Node.js)
├── tsconfig.json           (Configurações do compilador TypeScript)
└── swagger.yaml            (Documentação da API no formato OpenAPI)
```

**Descrição dos Diretórios Principais:**

*   **`database`**: Contém a configuração da fonte de dados (`data-source.ts`) para o TypeORM, definindo como a aplicação se conecta ao banco de dados.
*   **`entities`**: Define as entidades do banco de dados usando o TypeORM. Cada arquivo (`User.ts`, `Article.ts`) representa uma tabela.
*   **`middleware`**: Contém funções de middleware para o Express.js, como o `authMiddleware.ts`, que intercepta requisições para verificar a autenticação via token JWT.
*   **`routes`**: Define os endpoints da API (ex: `userRoutes.ts`), agrupando as rotas e associando-as aos serviços correspondentes.
*   **`services`**: Onde a lógica de negócio principal reside. Os serviços interagem com o banco de dados e executam as operações necessárias.
*   **`server.ts`**: O ponto de entrada da aplicação, responsável por inicializar o servidor Express, configurar middlewares e carregar as rotas.
*   **`swagger.yaml`**: Arquivo de especificação OpenAPI que documenta a API, usado para gerar a documentação interativa.

---

## 3. Técnicas de Programação e Tecnologias

Esta seção aborda as principais tecnologias, bibliotecas e padrões de programação que sustentam o projeto.

### 3.1. Framework de Testes (Java)

O lado Java da solução utiliza um conjunto de ferramentas robustas e populares do ecossistema para construir um framework de automação de testes eficiente.

*   **Java 21**: O projeto utiliza uma versão moderna da linguagem Java, aproveitando seus recursos mais recentes.
*   **Maven**: Usado como a ferramenta de gerenciamento de dependências e de build do projeto. O `pom.xml` centraliza todas as bibliotecas externas.
*   **REST Assured**: A principal biblioteca para testes de API em Java. Ela fornece uma DSL (Domain-Specific Language) fluente para criar requisições HTTP e validar respostas.
*   **Cucumber**: A base para a abordagem de BDD (Behavior-Driven Development). Permite escrever cenários de teste em linguagem natural (Gherkin).
*   **Lombok**: Biblioteca que ajuda a reduzir código repetitivo (boilerplate) com anotações como `@Data`, `@Getter`, e `@Setter`.
*   **Jackson**: Biblioteca para serialização e desserialização de JSON, convertendo objetos Java em JSON e vice-versa.
*   **Log4j 2**: Utilizado para um logging avançado e configurável, permitindo formatação de mensagens e controle de níveis (INFO, DEBUG, ERROR).
*   **Apache POI & Fillo**: Bibliotecas para manipulação de arquivos Excel, essenciais para abordagens de Testes Orientados a Dados (Data-Driven Testing).
*   **OpenCSV**: Biblioteca para ler e escrever em arquivos CSV, outra alternativa para testes orientados a dados.
*   **JavaFaker**: Ferramenta para gerar dados de teste massivos e realistas (nomes, e-mails, etc.).
*   **iText**: Utilizado para a geração de evidências em formato PDF.
*   **JUnit**: O framework que serve de base para a execução dos testes do Cucumber.

### 3.2. API (`cms-for-qas-api`)

A API foi construída com tecnologias modernas do ecossistema Node.js, com foco em desempenho e manutenibilidade.

*   **Node.js & TypeScript**: A API usa TypeScript, um superset do JavaScript que adiciona tipagem estática, para aumentar a robustez do código.
*   **Express.js**: O framework web que gerencia as rotas, requisições HTTP e middlewares.
*   **TypeORM & SQLite**: O TypeORM é o ORM (Object-Relational Mapper) que facilita a interação com o banco de dados (SQLite), abstraindo a escrita de SQL.
*   **JSON Web Tokens (JWT)**: Tecnologia usada para implementar a autenticação. A API gera um token que o cliente envia para acessar rotas protegidas.
*   **Bcrypt**: Biblioteca para hashing de senhas, garantindo que as senhas nunca sejam armazenadas em texto plano.
*   **Express Validator**: Middleware para validação de dados de entrada diretamente na definição das rotas.
*   **Swagger / OpenAPI**: A especificação OpenAPI (`swagger.yaml`) é usada para gerar uma documentação interativa da API.
*   **Dotenv**: Utilizado para carregar variáveis de ambiente de um arquivo `.env`, separando as configurações do código.

---

## 4. Técnicas de Teste de Software

O framework de automação foi projetado com uma abordagem multicamadas para garantir uma cobertura de teste completa e eficiente. As seguintes técnicas são o pilar da nossa estratégia de qualidade.

### 4.1. Behavior-Driven Development (BDD) com Cucumber

No centro da nossa estratégia está o BDD, que nos permite escrever testes compreensíveis por todos os envolvidos no projeto.

*   **Gherkin (`.feature` files)**: Os cenários são escritos em linguagem natural usando os blocos `Dado` (Given), `Quando` (When) e `Então` (Then) para descrever o comportamento esperado da API.
*   **Step Definitions (`steps` package)**: Cada linha em Gherkin é ligada a um método Java no pacote `steps`, que traduz o texto em ações concretas, como chamadas de API.

### 4.2. Testes de API com REST Assured

Para a interação com a API, utilizamos a biblioteca REST Assured, que possui uma sintaxe fluente seguindo o padrão `given/when/then`:

```java
given()
    .header("Authorization", token)
    .contentType(ContentType.JSON)
    .body(produto)
.when()
    .post("/produtos")
.then()
    .statusCode(201)
    .body("message", equalTo("Cadastro realizado com sucesso"));
```

*   **`given()`**: Prepara a requisição (headers, corpo, etc.).
*   **`when()`**: Executa a requisição HTTP.
*   **`then()`**: Valida a resposta (status code, corpo, etc.).

### 4.3. Estratégias de Dados de Teste

*   **Geração Dinâmica com `JavaFaker`**: Gera dados aleatórios e realistas (nomes, e-mails) a cada execução para evitar testes viciados.
*   **Massa de Dados com `gerador-massa-unificado`**: Utiliza uma dependência customizada para criar conjuntos de dados de teste complexos e consistentes.
*   **Testes Orientados a Dados**: O framework usa **Apache POI** e **OpenCSV** para permitir que os testes sejam executados com diferentes conjuntos de dados lidos de planilhas Excel ou arquivos CSV.

### 4.4. Gerenciamento de Autenticação

Para lidar com endpoints que exigem autenticação, o `TokenManager` é utilizado:
1.  Um cenário de login é executado.
2.  O `TokenManager` captura e armazena o token JWT.
3.  Testes subsequentes recuperam o token do `TokenManager` para se autenticarem, evitando logins repetidos e acelerando a suíte de testes.

### 4.5. Relatórios e Evidências

*   **Relatórios HTML**: O Cucumber gera relatórios HTML interativos em `target/cucumber-reports/cucumber.html`, mostrando o resultado de cada cenário.
*   **Geração de PDF com iText**: O projeto usa a biblioteca iText para gerar documentos PDF como evidência formal dos testes.

---

## 5. Resumo para Anotações Pessoais

Aqui está um resumo dos pontos-chave do projeto, ideal para anotações rápidas.

### Visão Geral do Projeto
*   **Dois Componentes Principais:**
    1.  **API (Node.js/TypeScript):** Um backend (`cms-for-qas-api`) que serve como um sistema de gerenciamento de conteúdo.
    2.  **Framework de Testes (Java):** Uma suíte de automação para validar a API.
*   **Objetivo:** Garantir a qualidade da API através de testes automatizados.

### Estrutura do Framework de Testes (Java)
*   **`controller`**: Abstrai as chamadas da API.
*   **`manager`**: Gerencia o estado (ex: token de autenticação).
*   **`model`**: POJOs que representam os dados da API.
*   **`steps`**: Código que implementa os testes do Cucumber.
*   **`features`**: Arquivos de teste em linguagem natural (Gherkin).

### Tecnologias Chave (Java)
*   **Cucumber:** Para BDD (testes em linguagem natural).
*   **REST Assured:** Para fazer as chamadas HTTP para a API.
*   **Apache POI / OpenCSV:** Para testes orientados a dados (ler de Excel/CSV).
*   **JavaFaker:** Para gerar dados de teste dinâmicos.

### Estrutura da API (Node.js)
*   **`entities`**: Define as tabelas do banco de dados (com TypeORM).
*   **`routes`**: Define os endpoints da API.
*   **`services`**: Contém a lógica de negócio.
*   **`middleware`**: Funções para tarefas como autenticação (JWT).

### Tecnologias Chave (API)
*   **Express.js:** Framework para construir a API.
*   **TypeORM:** ORM para interagir com o banco de dados (SQLite).
*   **JWT & Bcrypt:** Para autenticação segura e hashing de senhas.
*   **Swagger:** Para documentação interativa da API.

### Estratégia de Teste
*   **BDD:** Testes escritos em Gherkin, focando no comportamento.
*   **Abstração:** `Controllers` escondem a complexidade do REST Assured.
*   **Gerenciamento de Estado:** `TokenManager` reutiliza o token de login.
*   **Dados Dinâmicos:** `JavaFaker` e `gerador-massa-unificado` criam dados variados.
*   **Relatórios:** Cucumber gera relatórios HTML detalhados.
