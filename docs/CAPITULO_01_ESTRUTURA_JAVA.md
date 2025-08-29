# Capítulo 1: Estrutura de um Framework de Testes Java

Bem-vindo ao primeiro capítulo do nosso guia de estudo aprofundado!

Neste capítulo, vamos mergulhar na arquitetura de um framework de automação de testes em Java, usando o seu projeto como nosso principal estudo de caso. Entender a estrutura não é apenas sobre saber onde cada arquivo está, mas por que eles estão organizados dessa forma e como cada peça se conecta para criar uma máquina de testes robusta e de fácil manutenção.

---

### Página 1: O Alicerce - Maven e o `pom.xml`

Todo projeto Java robusto começa com um bom sistema de build e gerenciamento de dependências. Em nosso caso, essa ferramenta é o **Maven**. O Maven utiliza um arquivo de configuração central chamado `pom.xml` (Project Object Model) para gerenciar tudo.

**A Estrutura Padrão de Diretórios do Maven:**

O Maven define uma estrutura de diretórios padrão que ajuda a manter a organização:
*   `src/main/java`: Para o código fonte principal da aplicação. Em nosso framework, colocamos todo o nosso código de automação aqui para que ele possa ser empacotado e reutilizado como uma biblioteca, se necessário.
*   `src/test/java`: Tradicionalmente, este é o local para os testes unitários do próprio código. No nosso caso, como o projeto inteiro é sobre testes, a distinção é mais fluida, mas a estrutura é mantida por convenção.
*   `src/main/resources`: Para arquivos de recursos que são usados pelo código principal, como arquivos de configuração (`log4j2.xml`) ou os próprios `.feature` files do Cucumber.
*   `target/`: Este diretório é criado pelo Maven durante o processo de build. Ele contém o código compilado (`.class` files), os relatórios de teste e o pacote final do projeto (um arquivo `.jar` ou `.war`). **Você nunca deve editar arquivos neste diretório diretamente.**

**Dissecando o `pom.xml`:**

O `pom.xml` é o coração do projeto Maven. Vamos analisar suas seções mais importantes:

1.  **Coordenadas do Projeto:**
    ```xml
    <groupId>org.br.com</groupId>
    <artifactId>AUTOMACAO-REST-API-csm-for-qas-api</artifactId>
    <version>1.0-SNAPSHOT</version>
    ```
    *   `groupId`: Geralmente o nome do domínio da sua organização, ao contrário. Identifica seu projeto de forma única entre todos os outros.
    *   `artifactId`: O nome do seu projeto.
    *   `version`: A versão atual do seu projeto. `SNAPSHOT` indica uma versão em desenvolvimento.

2.  **Propriedades (`<properties>`):**
    ```xml
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <cucumber.version>7.20.1</cucumber.version>
        ...
    </properties>
    ```
    Esta seção é usada para definir "variáveis" que podem ser reutilizadas em todo o `pom.xml`. É uma excelente prática para centralizar as versões das dependências. Isso garante que, se você usar múltiplos artefatos do Cucumber, todos estarão na mesma versão, evitando conflitos.

3.  **Dependências (`<dependencies>`):**
    Aqui listamos todas as bibliotecas externas que nosso projeto precisa. Cada `<dependency>` tem pelo menos um `groupId`, `artifactId`, e `version`.
    *   **`rest-assured`**: A DSL para escrever testes de API.
    *   **`cucumber-java` & `cucumber-junit`**: Para escrever e executar testes BDD.
    *   **`lombok`**: Para reduzir código repetitivo.
    *   **`jackson-databind`**: Para converter objetos Java para JSON e vice-versa.
    *   **`log4j-core`**: Para um sistema de log robusto.
    *   **`javafaker`**: Para gerar dados de teste aleatórios.

4.  **Build (`<build>`):**
    Esta seção define como o Maven deve construir o projeto. O mais importante aqui é o `maven-compiler-plugin` (que diz ao Maven para usar Java 21) e o `maven-surefire-plugin`.
    *   **`maven-surefire-plugin`**: É o plugin responsável por executar os testes. Ele pode ser configurado para rodar classes de teste específicas, ou, como no nosso caso, para passar parâmetros da linha de comando para os testes, como as tags do Cucumber (`-Dcucumber.filter.tags="@SuaTag"`).

---

### Página 2: A Arquitetura em Camadas do Framework

Um bom framework de automação não é apenas um amontoado de scripts de teste. Ele é um software bem arquitetado. Nosso projeto segue o **Princípio da Responsabilidade Única (Single Responsibility Principle)**, onde cada pacote e classe tem um propósito claro e bem definido.

**Diagrama de Arquitetura Simplificado:**
```
[ .feature Files ] -> [ Steps ] -> [ Controllers ] -> [ API ]
       ^                  |                |
       |                  +----[ Manager ]-+
       |                  |
       +----[ Model ]-----+
```

Vamos detalhar cada camada:

*   **`model` (Os Dados):**
    *   **Responsabilidade:** Representar os dados que fluem entre o nosso framework e a API. Contém classes POJO (Plain Old Java Objects) que espelham a estrutura dos JSONs de request e response.
    *   **Por que usar?** Em vez de montar strings de JSON manualmente (o que é frágil e propenso a erros), nós criamos instâncias de objetos Java (ex: `new User("Jules", "jules@example.com")`). A biblioteca Jackson, usada pelo Rest Assured, faz a conversão para JSON automaticamente. Isso nos dá segurança de tipos e facilita a leitura do código.

*   **`controller` (Os Motores):**
    *   **Responsabilidade:** Orquestrar as chamadas para a API. Cada classe de `controller` (ex: `UserController`) é responsável por uma entidade da API. Ela contém métodos que representam as ações (ex: `cadastrarUsuario`, `buscarUsuarioPorId`).
    *   **Por que usar?** Esta camada abstrai a complexidade do REST Assured. As classes de `steps` não precisam saber sobre `given/when/then`, headers, ou content-type. Elas simplesmente chamam um método como `userController.cadastrarUsuario(meuUsuario)`. Isso torna os `steps` muito mais limpos e focados na lógica do teste, não nos detalhes da implementação HTTP.

---

### Página 3: As Camadas de Lógica e Estado

Continuando nossa análise das camadas, vamos focar nas peças que unem tudo e gerenciam a lógica do teste.

*   **`steps` (A Cola do BDD):**
    *   **Responsabilidade:** "Colar" as frases em linguagem natural dos arquivos `.feature` com o código Java que executa as ações.
    *   **Como funciona?** Cada método em uma classe de `steps` é anotado com `@Given`, `@When`, `@Then`, `@And`, ou `@But`. O Cucumber lê o `.feature` e procura por um método com uma anotação que corresponda à frase.
    *   **Boas Práticas:**
        *   **Steps devem ser finos:** A lógica de negócio complexa não deve estar aqui. Os steps devem delegar. Um step "Quando eu cadastro um usuário" deve chamar o `userController.cadastrarUsuario()`. Um step "Então o status da resposta deve ser 201" deve conter a asserção do Rest Assured.
        *   **Reutilização:** Escreva steps genéricos que possam ser reutilizados em múltiplos cenários.

*   **`manager` (O Cérebro de Estado):**
    *   **Responsabilidade:** Gerenciar o estado que precisa ser compartilhado entre diferentes steps de um mesmo cenário. O exemplo mais clássico é o token de autenticação.
    *   **Por que usar?** O Cucumber cria uma nova instância das classes de `steps` para cada cenário. Isso significa que uma variável de instância em uma classe de `steps` não pode ser acessada por outra. O `manager` (geralmente implementado como um Singleton ou usando Injeção de Dependência) resolve isso.
    *   **Exemplo de Fluxo:**
        1.  O step de `@Given("que eu sou um usuário autenticado")` chama o `authController.login()` e armazena o token recebido no `TokenManager.getInstance().setToken(token)`.
        2.  O step de `@When("eu tento cadastrar um novo produto")` precisa do token. Ele o recupera chamando `TokenManager.getInstance().getToken()` e o passa para o `productController`.

---

### Página 4: Utilitários e o Fluxo Completo

Nenhuma fortaleza está completa sem suas ferramentas de apoio e uma compreensão clara de como as tropas se movem.

*   **`util` (A Caixa de Ferramentas):**
    *   **Responsabilidade:** Conter classes e métodos auxiliares que podem ser usados em qualquer lugar do framework.
    *   **Exemplos:**
        *   Uma classe para ler dados de um arquivo Excel.
        *   Uma classe para gerar dados aleatórios usando `JavaFaker`.
        *   Uma classe para formatar datas ou moedas.
    *   **Por que usar?** Evita a duplicação de código. Se você precisa ler um arquivo de propriedades em três lugares diferentes, é melhor ter um método `PropertiesReader.getProperty("minha.prop")` do que escrever a lógica de `FileInputStream` três vezes.

**O Fluxo de Execução de um Teste (Ponta a Ponta):**

Vamos rastrear um cenário simples: "Cadastrar um novo usuário com sucesso".

1.  **Runner:** Você executa o teste (via Maven ou pela sua IDE). O JUnit executa a classe Runner (ex: `RunCucumberTest.java`).
2.  **Cucumber:** O Runner, configurado com as anotações do Cucumber, começa a ler o arquivo `user.feature`.
3.  **Cenário:** O Cucumber encontra o cenário "Cadastrar um novo usuário com sucesso".
4.  **`@Given`:** Ele lê a linha `Dado que eu tenho os dados de um novo usuário`. Ele procura nas classes de `steps` por um método anotado com `@Given("que eu tenho os dados de um novo usuário")`.
5.  **Lógica do Step:** Dentro desse método, o código Java é executado. Provavelmente, ele cria um objeto `User` (do pacote `model`) e o preenche com dados, talvez usando o `JavaFaker` (do pacote `util`). O objeto `User` é armazenado em uma variável local na classe de steps.
6.  **`@When`:** O Cucumber lê a linha `Quando eu envio uma requisição POST para /usuarios`. Ele encontra o método de step correspondente.
7.  **Delegação para o Controller:** O código neste step chama o método `userController.cadastrarUsuario(user)`.
8.  **Lógica do Controller:** O `userController` pega o objeto `User`, usa o Rest Assured para construir a requisição HTTP (definindo o `body` com o objeto `user`, o `Content-Type` para `application/json`), executa a chamada para a API e armazena a resposta.
9.  **`@Then`:** O Cucumber lê a linha `Então a resposta deve ter o status 201`. O método de step correspondente executa a asserção do Rest Assured na resposta que foi armazenada pelo controller, verificando o status code (`response.then().statusCode(201)`).

---

### Página 5: Exercício Prático

Agora é sua vez de colocar a mão na massa e solidificar o conhecimento sobre a estrutura.

**Objetivo:** Adicionar a funcionalidade para buscar um usuário por ID, seguindo a arquitetura existente. A API já possui o endpoint `GET /usuarios/{id}`.

**Passos:**

1.  **Model:** Verifique se o seu `User.java` no pacote `model` já possui todos os campos que a API retorna ao buscar um usuário (id, nome, email, etc.). Se não, adicione-os.

2.  **Controller:** Na classe `UserController.java` (crie-a no pacote `controller.user` se não existir), adicione um novo método:
    ```java
    public Response buscarUsuarioPorId(String userId) {
        // Use a sintaxe do Rest Assured para fazer uma requisição GET
        // para o endpoint "/usuarios/{id}".
        // Dica: use o método .pathParam("id", userId) do Rest Assured.
        // O método deve retornar o objeto Response do Rest Assured.
    }
    ```

3.  **Feature:** Crie um novo arquivo chamado `busca_usuario.feature` dentro de `src/main/resources/features/` ou adicione ao `usuarios.feature` existente. Escreva um novo cenário:
    ```gherkin
    #language: pt
    Funcionalidade: Busca de Usuários

    Cenário: Buscar um usuário existente por ID
      Dado que um usuário foi cadastrado previamente
      Quando eu envio uma requisição GET para /usuarios/{id} com o id do usuário cadastrado
      Então a resposta deve ter o status 200
      E o corpo da resposta deve conter os dados corretos do usuário
    ```

4.  **Steps:** Em uma classe de steps apropriada (ex: `UserSteps.java`), implemente os novos passos que você escreveu.
    *   O passo `Dado` pode reutilizar a lógica de cadastro de usuário que já existe. Armazene o ID do usuário criado em uma variável no `manager` ou na própria classe de step.
    *   O passo `Quando` deve chamar o seu novo método `userController.buscarUsuarioPorId()`.
    *   O passo `Então` para o status 200 é simples.
    *   Para o passo `E` (validar o corpo), você pode desserializar a resposta para um objeto `User` (`response.as(User.class)`) e usar asserções do JUnit (`Assert.assertEquals`) para comparar o nome, email, etc., com os dados do usuário que você cadastrou no passo `Dado`.

5.  **Execute:** Rode o seu novo cenário e veja-o passar!

Este exercício forçará você a navegar e a modificar cada camada do framework (`model`, `controller`, `feature`, `steps`), solidificando sua compreensão de como elas trabalham juntas.
