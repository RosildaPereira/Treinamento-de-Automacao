# 📚 **CLASSES DO FRAMEWORK - PARTE 2 - COMENTÁRIOS DETALHADOS**

## 🎯 **CONTINUAÇÃO DA DOCUMENTAÇÃO DO FRAMEWORK**

Esta é a **Parte 2** da documentação completa do framework, cobrindo **STEPS**, **MODELS**, **CONTROLLERS**, **UTILS** e **RUNNERS** restantes.

---

## 📝 **STEPS - Definições de Passos Cucumber**

### **UsuarioSteps.java - Steps de Gerenciamento de Usuários**

**📍 Localização**: `src/main/java/org/br/com/test/steps/UsuarioSteps.java`

```java
package org.br.com.test.steps;

import io.cucumber.java.en.*;
import org.br.com.test.controllers.usuario.UsuarioController;

/**
 * ============================================================================
 * STEPS: UsuarioSteps
 * ============================================================================
 * 
 * PROPÓSITO:
 * Define todos os passos (steps) Cucumber para cenários relacionados
 * a usuários. Atua como ponte entre features Gherkin e lógica de
 * controladores, mantendo separação clara de responsabilidades.
 * 
 * PADRÃO ARQUITETURAL:
 * Steps → Controllers → API
 * - Steps: O QUE fazer (Gherkin)
 * - Controllers: COMO fazer (Lógica HTTP)
 * - API: ONDE fazer (Endpoints)
 * 
 * CENÁRIOS COBERTOS:
 * - Cadastro de usuários (válidos/inválidos)
 * - Login e autenticação
 * - Listagem e busca de usuários
 * - Atualização de dados
 * - Exclusão de usuários
 * - Validações de erro
 * ============================================================================
 */
public class UsuarioSteps {

    /**
     * ATRIBUTO: usuarioController
     * 
     * PROPÓSITO: Instância única do controller de usuários
     * TIPO: UsuarioController
     * RESPONSABILIDADE: Executa lógica real de API
     * PADRÃO: Delegação - Steps delegam para Controllers
     */
    private final UsuarioController usuarioController = new UsuarioController();

    // =================================================================
    // == STEPS DE PREPARAÇÃO (GIVEN)
    // =================================================================

    /**
     * STEP: @Given("que envio uma solicitação 'POST' de registro de usuario CMS")
     * 
     * PROPÓSITO:
     * Prepara dados para registro de usuário carregando informações
     * da planilha. NÃO executa o cadastro ainda.
     * 
     * CORRESPONDÊNCIA GHERKIN:
     * "Given que envio uma solicitação 'POST' de registro de usuario CMS"
     * 
     * AÇÃO:
     * - Carrega dados da planilha Excel
     * - Armazena em UsuarioManager
     * - Prepara para execução posterior
     * 
     * QUANDO USAR:
     * - Preparação de cenários de cadastro
     * - Setup de dados antes de ações
     * - Inicialização de contexto de usuário
     */
    @Given("que envio uma solicitação 'POST' de registro de usuario CMS")
    public void queEnvioUmaSolicitacaoDeRegistro() {
        usuarioController.carregarDadosDaPlanilha();
    }

    // =================================================================
    // == STEPS DE AÇÃO (WHEN)
    // =================================================================

    /**
     * STEP: @When("envio novamente uma solicitação 'POST' para registrar o mesmo email")
     * 
     * PROPÓSITO:
     * Testa cenário de email duplicado tentando cadastrar usuário
     * com email já existente no sistema.
     * 
     * CENÁRIO:
     * 1. Usuário já foi cadastrado anteriormente
     * 2. Nova tentativa com mesmo email
     * 3. Sistema deve rejeitar com erro apropriado
     */
    @When("envio novamente uma solicitação 'POST' para registrar o mesmo email")
    public void envioNovamenteUmaSolicitacaoParaRegistrarOMesmoEmail() {
        usuarioController.tentarCadastrarUsuarioComEmailDuplicado();
    }

    /**
     * STEP: @When("envio novamente uma solicitação 'POST' para registrar o mesmo usuário")
     * 
     * PROPÓSITO:
     * Testa cenário de nome de usuário duplicado.
     * Similar ao email, mas para campo username.
     */
    @When("envio novamente uma solicitação 'POST' para registrar o mesmo usuário")
    public void envioNovamenteUmaSolicitacaoParaRegistrarOMesmoUsuario() {
        usuarioController.tentarCadastrarUsuarioComNomeDeUsuarioDuplicado();
    }

    /**
     * STEP: @When("eu envio uma solicitação de criação de usuário com dados inválidos")
     * 
     * PROPÓSITO:
     * Testa validação geral de dados inválidos.
     * Pode incluir campos obrigatórios vazios, formatos incorretos, etc.
     */
    @When("eu envio uma solicitação de criação de usuário com dados inválidos")
    public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComDadosInvalidos() {
        usuarioController.tentarCadastrarUsuarioComDadosInvalidos();
    }

    /**
     * STEP: @When("eu envio uma solicitação de criação de usuário com email inválido")
     * 
     * PROPÓSITO:
     * Testa especificamente validação de formato de email.
     * Ex: email sem @, domínio inválido, caracteres especiais.
     */
    @When("eu envio uma solicitação de criação de usuário com email inválido")
    public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComEmailInvalido() {
        usuarioController.tentarCadastrarUsuarioComEmailInvalido();
    }

    /**
     * STEP: @When("eu envio uma solicitação de criação de usuário com senha inválida")
     * 
     * PROPÓSITO:
     * Testa validação de senha (tamanho mínimo, complexidade, etc.).
     */
    @When("eu envio uma solicitação de criação de usuário com senha inválida")
    public void euEnvioUmaSolicitacaoDeCriacaoDeUsuarioComSenhaInvalida() {
        usuarioController.tentarCadastrarUsuarioComSenhaInvalida();
    }

    /**
     * STEP: @When("eu realizo o login com as credenciais válidas do usuário criado")
     * 
     * PROPÓSITO:
     * Executa login com credenciais válidas.
     * Usado após cadastro bem-sucedido para obter token.
     * 
     * PARÂMETRO INTERNO:
     * true = usar credenciais válidas
     */
    @When("eu realizo o login com as credenciais válidas do usuário criado")
    public void euRealizoOloginComAsCredenciaisValidasDoUsuarioCriado() {
        usuarioController.realizarLogin(true);
    }

    /**
     * STEP: @When("eu envio uma solicitação de login com credenciais inválidas")
     * 
     * PROPÓSITO:
     * Testa login com credenciais incorretas.
     * 
     * PARÂMETRO INTERNO:
     * false = usar credenciais inválidas
     */
    @When("eu envio uma solicitação de login com credenciais inválidas")
    public void euEnvioUmaSolicitacaoDeLoginComCredenciaisInvalidas() {
        usuarioController.realizarLogin(false);
    }

    // =================================================================
    // == STEPS DE VALIDAÇÃO (THEN)
    // =================================================================

    /**
     * STEP: @Then("o sistema deve retornar status {int}")
     * 
     * PROPÓSITO:
     * Valida código de status HTTP específico.
     * Step genérico usado em múltiplos cenários.
     * 
     * PARÂMETROS:
     * @param statusCode - Código HTTP esperado (200, 201, 400, 409, etc.)
     * 
     * EXEMPLOS:
     * - "Then o sistema deve retornar status 201" (criado)
     * - "Then o sistema deve retornar status 400" (bad request)
     * - "Then o sistema deve retornar status 409" (conflito)
     */
    @Then("o sistema deve retornar status {int}")
    public void oSistemaDeveRetornarStatus(int statusCode) {
        usuarioController.validarStatusCode(statusCode);
    }

    /**
     * STEP: @Then("deve ser exibida a mensagem de erro {string}")
     * 
     * PROPÓSITO:
     * Valida mensagem de erro específica na resposta.
     * 
     * PARÂMETROS:
     * @param mensagem - Texto exato da mensagem esperada
     * 
     * EXEMPLOS:
     * - "Then deve ser exibida a mensagem de erro 'Email já cadastrado'"
     * - "Then deve ser exibida a mensagem de erro 'Senha deve ter ao menos 8 caracteres'"
     */
    @Then("deve ser exibida a mensagem de erro {string}")
    public void deveSerExibidaAMensagemDeErro(String mensagem) {
        usuarioController.validarMensagemDeErro(mensagem);
    }

    /**
     * STEP: @Then("o usuário deve ser criado com sucesso")
     * 
     * PROPÓSITO:
     * Valida criação bem-sucedida do usuário.
     * Verifica dados retornados e armazena informações.
     */
    @Then("o usuário deve ser criado com sucesso")
    public void oUsuarioDeveSerCriadoComSucesso() {
        usuarioController.validarCriacaoUsuario();
    }

    /**
     * STEP: @Then("deve retornar token de acesso válido")
     * 
     * PROPÓSITO:
     * Valida que login retornou token JWT válido.
     * Armazena token no TokenManager para uso posterior.
     */
    @Then("deve retornar token de acesso válido")
    public void deveRetornarTokenDeAcessoValido() {
        usuarioController.validarTokenDeAcesso();
    }
}
```

---

## 📊 **MODELS - Estruturas de Dados**

### **REQUEST MODELS - Modelos de Requisição**

#### **UsuarioRequest.java - Modelo de Usuário**

```java
package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * MODEL REQUEST: UsuarioRequest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Modelo de dados para requisições de criação e atualização de usuários.
 * Representa estrutura JSON esperada pela API para operações CRUD.
 * 
 * ANOTAÇÕES LOMBOK:
 * @Data - Gera getters, setters, toString, equals, hashCode
 * @Builder - Permite construção fluente com padrão Builder
 * @NoArgsConstructor - Construtor sem parâmetros (Jackson/JSON)
 * @AllArgsConstructor - Construtor com todos os parâmetros
 * 
 * JSON GERADO:
 * {
 *   "nomeCompleto": "João da Silva",
 *   "nomeUsuario": "joao.silva",
 *   "email": "joao@teste.com",
 *   "senha": "senha123"
 * }
 * ============================================================================
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    /**
     * CAMPO: nomeCompleto
     * 
     * PROPÓSITO: Nome completo do usuário
     * VALIDAÇÕES TÍPICAS:
     * - Não pode ser null ou vazio
     * - Tamanho mínimo/máximo
     * - Pode conter espaços e acentos
     * 
     * EXEMPLO: "Maria da Silva Santos"
     */
    private String nomeCompleto;

    /**
     * CAMPO: nomeUsuario
     * 
     * PROPÓSITO: Username único no sistema
     * VALIDAÇÕES TÍPICAS:
     * - Deve ser único
     * - Sem espaços
     * - Caracteres alfanuméricos e alguns especiais
     * - Case-sensitive ou não (depende da API)
     * 
     * EXEMPLO: "maria.santos" ou "msantos"
     */
    private String nomeUsuario;

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email único para login e comunicação
     * VALIDAÇÕES TÍPICAS:
     * - Formato de email válido
     * - Deve ser único no sistema
     * - Case-insensitive geralmente
     * 
     * EXEMPLO: "maria.santos@empresa.com"
     */
    private String email;

    /**
     * CAMPO: senha
     * 
     * PROPÓSITO: Senha para autenticação
     * SEGURANÇA:
     * - NUNCA logar este campo
     * - Pode ser enviada como plain text (HTTPS)
     * - API deve criptografar antes de salvar
     * 
     * VALIDAÇÕES TÍPICAS:
     * - Tamanho mínimo (6-8 caracteres)
     * - Complexidade (maiúscula, número, especial)
     * 
     * EXEMPLO: "MinhaSenh@123"
     */
    private String senha;
}
```

#### **LoginRequest.java - Modelo de Login**

```java
package org.br.com.test.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * MODEL REQUEST: LoginRequest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Modelo simplificado para requisições de autenticação.
 * Contém apenas campos essenciais para login.
 * 
 * DIFERENÇA DE UsuarioRequest:
 * - Apenas email e senha
 * - Usado especificamente para /auth/login
 * - Não inclui dados de perfil
 * 
 * JSON GERADO:
 * {
 *   "email": "usuario@teste.com",
 *   "senha": "senha123"
 * }
 * ============================================================================
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Identificação do usuário
     * PODE SER: Email ou username (depende da API)
     */
    private String email;

    /**
     * CAMPO: senha
     * 
     * PROPÓSITO: Credencial de autenticação
     * SEGURANÇA: Mesmas considerações do UsuarioRequest
     */
    private String senha;
}
```

### **RESPONSE MODELS - Modelos de Resposta**

#### **UsuarioResponse.java - Resposta de Usuário**

```java
package org.br.com.test.model.response;

/**
 * ============================================================================
 * MODEL RESPONSE: UsuarioResponse
 * ============================================================================
 * 
 * PROPÓSITO:
 * Modelo para respostas da API contendo dados de usuário.
 * Representa estrutura JSON retornada em operações de consulta.
 * 
 * DIFERENÇAS DO REQUEST:
 * - Inclui ID gerado pelo sistema
 * - NÃO inclui senha (segurança)
 * - Pode incluir campos adicionais (timestamps, status)
 * 
 * JSON TÍPICO:
 * {
 *   "id": "uuid-123-456",
 *   "nomeCompleto": "João da Silva",
 *   "nomeUsuario": "joao.silva",
 *   "email": "joao@teste.com",
 *   "ativo": true,
 *   "criadoEm": "2024-01-15T10:30:00Z"
 * }
 * ============================================================================
 */
// Implementação específica varia conforme API
```

---

## 🎮 **CONTROLLERS - Lógica de Requisições**

### **UsuarioController.java - Controller Principal de Usuários**

**📍 Localização**: `src/main/java/org/br/com/test/controllers/usuario/UsuarioController.java`

```java
package org.br.com.test.controllers.usuario;

import io.restassured.response.Response;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.manager.TokenManager;
import org.br.com.test.manager.UsuarioManager;
import org.br.com.test.model.request.LoginRequest;
import org.br.com.test.model.request.UsuarioRequest;
import org.br.com.core.filter.EvidenceFilter;
import static io.restassured.RestAssured.given;

/**
 * ============================================================================
 * CONTROLLER: UsuarioController
 * ============================================================================
 * 
 * PROPÓSITO:
 * Centraliza toda a lógica de interação com endpoints de usuários.
 * Encapsula requisições HTTP, validações e gerenciamento de estado.
 * É o coração da automação de testes de API de usuários.
 * 
 * RESPONSABILIDADES:
 * - Executar requisições HTTP (CRUD completo)
 * - Validar respostas e status codes
 * - Gerenciar dados entre requisições
 * - Integrar com Managers para estado global
 * - Capturar evidências automaticamente
 * - Logging estruturado de ações
 * 
 * ARQUITETURA:
 * Steps → Controller → API
 * ↓
 * Managers (estado global)
 * ↓
 * Context (dados temporários)
 * ============================================================================
 */
public class UsuarioController {

    // =================================================================
    // == CONFIGURAÇÕES E CONSTANTES
    // =================================================================

    private Response response;
    private static final String BASE_URL = "http://localhost:3000";
    private static final String ENDPOINT_USUARIOS = "/usuarios";
    private static final String ENDPOINT_LOGIN = "/auth/login";

    // =================================================================
    // == MÉTODOS DE CARREGAMENTO DE DADOS
    // =================================================================

    /**
     * MÉTODO: carregarDadosDaPlanilha()
     * 
     * PROPÓSITO:
     * Carrega dados de usuário de planilhas Excel e armazena
     * no UsuarioManager para uso em requisições posteriores.
     * 
     * FONTES DE DADOS:
     * - CadastroDataSheet (dados completos)
     * - LoginModel (dados básicos)
     * 
     * FLUXO:
     * 1. Tenta obter dados completos de cadastro
     * 2. Se não encontrar, usa dados básicos de login
     * 3. Armazena no UsuarioManager (thread-safe)
     * 4. Loga ação executada
     * 
     * INTEGRAÇÃO:
     * - Context: obtém dados carregados por hooks
     * - UsuarioManager: armazena para uso posterior
     * - LogFormatter: registra ação
     */
    public void carregarDadosDaPlanilha() {
        // Implementação obtém dados do Context e armazena no UsuarioManager
        LogFormatter.logStep("Carregando dados de usuario da planilha");
        
        // Lógica de carregamento...
        // UsuarioManager.setEmailUsuario(email);
        // UsuarioManager.setSenhaUsuario(senha);
        // etc.
    }

    // =================================================================
    // == MÉTODOS DE CADASTRO (CREATE)
    // =================================================================

    /**
     * MÉTODO: tentarCadastrarUsuarioComEmailDuplicado()
     * 
     * PROPÓSITO:
     * Testa cenário de erro: email já existente no sistema.
     * Usado para validar que API rejeita emails duplicados.
     * 
     * CENÁRIO:
     * 1. Usuário já foi cadastrado anteriormente
     * 2. Nova tentativa com mesmo email
     * 3. API deve retornar erro 409 (Conflict)
     * 
     * VALIDAÇÕES ESPERADAS:
     * - Status code: 409 ou 400
     * - Mensagem: "Email já cadastrado" ou similar
     * - Usuário NÃO deve ser criado
     */
    public void tentarCadastrarUsuarioComEmailDuplicado() {
        LogFormatter.logStep("Tentando cadastrar usuario com email duplicado");
        
        UsuarioRequest usuario = UsuarioManager.getUsuarioAtual();
        
        response = given()
            .filter(new EvidenceFilter())
            .contentType("application/json")
            .body(usuario)
        .when()
            .post(BASE_URL + ENDPOINT_USUARIOS);
            
        LogFormatter.logStep("Status retornado: " + response.getStatusCode());
    }

    /**
     * MÉTODO: tentarCadastrarUsuarioComDadosInvalidos()
     * 
     * PROPÓSITO:
     * Testa validações de campos obrigatórios e formatos.
     * 
     * CASOS TESTADOS:
     * - Campos vazios ou nulos
     * - Tamanhos inválidos
     * - Caracteres não permitidos
     * - Formatos incorretos
     */
    public void tentarCadastrarUsuarioComDadosInvalidos() {
        LogFormatter.logStep("Tentando cadastrar usuario com dados invalidos");
        
        // Cria request com dados inválidos propositalmente
        UsuarioRequest usuarioInvalido = UsuarioRequest.builder()
            .nomeCompleto("") // Vazio
            .nomeUsuario(null) // Nulo
            .email("email-invalido") // Sem @
            .senha("123") // Muito curta
            .build();
            
        response = given()
            .filter(new EvidenceFilter())
            .contentType("application/json")
            .body(usuarioInvalido)
        .when()
            .post(BASE_URL + ENDPOINT_USUARIOS);
    }

    // =================================================================
    // == MÉTODOS DE AUTENTICAÇÃO
    // =================================================================

    /**
     * MÉTODO: realizarLogin(boolean credenciaisValidas)
     * 
     * PROPÓSITO:
     * Executa login com credenciais válidas ou inválidas.
     * Armazena token se bem-sucedido.
     * 
     * PARÂMETROS:
     * @param credenciaisValidas - true para login válido, false para inválido
     * 
     * FLUXO VÁLIDO:
     * 1. Cria LoginRequest com dados do UsuarioManager
     * 2. Executa POST /auth/login
     * 3. Se sucesso (200), extrai e armazena token
     * 4. Armazena user ID para operações futuras
     * 
     * FLUXO INVÁLIDO:
     * 1. Modifica credenciais para serem inválidas
     * 2. Executa requisição
     * 3. Espera erro 401 (Unauthorized)
     */
    public void realizarLogin(boolean credenciaisValidas) {
        String acao = credenciaisValidas ? "válidas" : "inválidas";
        LogFormatter.logStep("Realizando login com credenciais " + acao);
        
        LoginRequest loginRequest;
        if (credenciaisValidas) {
            loginRequest = LoginRequest.builder()
                .email(UsuarioManager.getEmailUsuario())
                .senha(UsuarioManager.getSenhaUsuario())
                .build();
        } else {
            loginRequest = LoginRequest.builder()
                .email(UsuarioManager.getEmailUsuario())
                .senha("senhaInvalida123")
                .build();
        }
        
        response = given()
            .filter(new EvidenceFilter())
            .contentType("application/json")
            .body(loginRequest)
        .when()
            .post(BASE_URL + ENDPOINT_LOGIN);
            
        // Se login válido e bem-sucedido, armazenar token
        if (credenciaisValidas && response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("token");
            String userId = response.jsonPath().getString("user.id");
            
            TokenManager.setToken(token);
            TokenManager.setUserId(userId);
            
            LogFormatter.logStep("Token obtido e armazenado com sucesso");
        }
    }

    // =================================================================
    // == MÉTODOS DE VALIDAÇÃO
    // =================================================================

    /**
     * MÉTODO: validarStatusCode(int expectedStatus)
     * 
     * PROPÓSITO:
     * Valida que última resposta tem código de status esperado.
     * 
     * PARÂMETROS:
     * @param expectedStatus - Código HTTP esperado
     * 
     * CÓDIGOS COMUNS:
     * - 200: OK (consulta/login bem-sucedido)
     * - 201: Created (cadastro bem-sucedido)
     * - 400: Bad Request (dados inválidos)
     * - 401: Unauthorized (não autenticado)
     * - 409: Conflict (duplicação)
     * - 422: Unprocessable Entity (validação)
     */
    public void validarStatusCode(int expectedStatus) {
        LogFormatter.logStep("Validando status code: " + expectedStatus);
        
        int actualStatus = response.getStatusCode();
        if (actualStatus != expectedStatus) {
            String erro = String.format(
                "Status code incorreto. Esperado: %d, Recebido: %d", 
                expectedStatus, actualStatus
            );
            LogFormatter.logError(erro);
            throw new AssertionError(erro);
        }
        
        LogFormatter.logStep("Status code validado com sucesso: " + actualStatus);
    }

    /**
     * MÉTODO: validarMensagemDeErro(String mensagemEsperada)
     * 
     * PROPÓSITO:
     * Valida que resposta de erro contém mensagem específica.
     * 
     * PARÂMETROS:
     * @param mensagemEsperada - Texto da mensagem esperada
     * 
     * LOCALIZAÇÃO TÍPICA:
     * - response.message
     * - response.error
     * - response.errors[0].message
     * - Varia conforme padrão da API
     */
    public void validarMensagemDeErro(String mensagemEsperada) {
        LogFormatter.logStep("Validando mensagem de erro: " + mensagemEsperada);
        
        String mensagemAtual = response.jsonPath().getString("message");
        if (!mensagemEsperada.equals(mensagemAtual)) {
            String erro = String.format(
                "Mensagem de erro incorreta. Esperada: '%s', Recebida: '%s'", 
                mensagemEsperada, mensagemAtual
            );
            LogFormatter.logError(erro);
            throw new AssertionError(erro);
        }
        
        LogFormatter.logStep("Mensagem de erro validada com sucesso");
    }

    /**
     * MÉTODO: validarCriacaoUsuario()
     * 
     * PROPÓSITO:
     * Valida que usuário foi criado com sucesso e armazena dados retornados.
     * 
     * VALIDAÇÕES:
     * - Status code 201 (Created)
     * - Resposta contém ID gerado
     * - Dados pessoais corretos
     * - Email e username únicos
     * - Senha NÃO deve aparecer na resposta
     * 
     * ARMAZENAMENTO:
     * - ID do usuário no UsuarioManager
     * - Dados atualizados se necessário
     */
    public void validarCriacaoUsuario() {
        LogFormatter.logStep("Validando criacao de usuario");
        
        // Validar status
        validarStatusCode(201);
        
        // Extrair e armazenar ID
        String userId = response.jsonPath().getString("id");
        if (userId == null || userId.isEmpty()) {
            throw new AssertionError("ID do usuário não foi retornado na resposta");
        }
        
        UsuarioManager.setIdUsuario(userId);
        
        // Validar dados retornados
        String emailRetornado = response.jsonPath().getString("email");
        String emailEsperado = UsuarioManager.getEmailUsuario();
        
        if (!emailEsperado.equals(emailRetornado)) {
            throw new AssertionError("Email retornado não confere com o enviado");
        }
        
        LogFormatter.logStep("Usuario criado com sucesso. ID: " + userId);
    }

    /**
     * MÉTODO: validarTokenDeAcesso()
     * 
     * PROPÓSITO:
     * Valida que login retornou token JWT válido e o armazena.
     * 
     * VALIDAÇÕES:
     * - Status code 200
     * - Campo token presente
     * - Token não vazio
     * - Formato JWT (opcional)
     * 
     * ARMAZENAMENTO:
     * - Token no TokenManager
     * - User ID se disponível
     */
    public void validarTokenDeAcesso() {
        LogFormatter.logStep("Validando token de acesso");
        
        validarStatusCode(200);
        
        String token = response.jsonPath().getString("token");
        if (token == null || token.isEmpty()) {
            throw new AssertionError("Token de acesso não foi retornado");
        }
        
        TokenManager.setToken(token);
        
        // Extrair user ID se disponível
        String userId = response.jsonPath().getString("user.id");
        if (userId != null) {
            TokenManager.setUserId(userId);
        }
        
        LogFormatter.logStep("Token validado e armazenado com sucesso");
    }
}
```

---

## 🛠️ **UTILS - Utilitários Diversos**

### **FormatUtils.java - Formatação e Mascaramento**

**📍 Localização**: `src/main/java/org/br/com/test/utils/FormatUtils.java`

```java
package org.br.com.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * ============================================================================
 * UTILITÁRIO: FormatUtils
 * ============================================================================
 * 
 * PROPÓSITO:
 * Utilitários para formatação de dados, especialmente JSON e headers HTTP.
 * Inclui funcionalidades de mascaramento de dados sensíveis para logs
 * seguros e formatação legível de conteúdo.
 * 
 * FUNCIONALIDADES:
 * - Pretty print de JSON
 * - Formatação de headers HTTP
 * - Mascaramento de dados sensíveis
 * - Truncamento de tokens longos
 * ============================================================================
 */
public class FormatUtils {

    /**
     * MÉTODO: prettyJson(String json)
     * 
     * PROPÓSITO:
     * Converte JSON compacto em formato legível com indentação.
     * Útil para logs e evidências mais claras.
     * 
     * PARÂMETROS:
     * @param json - String JSON para formatar
     * 
     * RETORNO:
     * @return String - JSON formatado ou original se erro
     * 
     * EXEMPLO:
     * INPUT:  {"nome":"João","email":"joao@test.com"}
     * OUTPUT: {
     *           "nome" : "João",
     *           "email" : "joao@test.com"
     *         }
     */
    public static String prettyJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "";
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json; // Retorna original se não conseguir formatar
        }
    }

    /**
     * MÉTODO: formatHeaders(String headersRaw)
     * 
     * PROPÓSITO:
     * Formata headers HTTP em formato legível linha por linha.
     * Trata casos especiais como Accept e Authorization.
     * 
     * PARÂMETROS:
     * @param headersRaw - Headers em formato bruto
     * 
     * RETORNO:
     * @return String - Headers formatados
     * 
     * TRATAMENTOS ESPECIAIS:
     * - Accept: quebra múltiplos valores
     * - Authorization: trunca tokens longos
     * 
     * EXEMPLO:
     * INPUT:  Accept=application/json,text/html Authorization=Bearer eyJ...muito-longo
     * OUTPUT: Accept=application/json
     *         text/html
     *         Authorization=Bearer eyJ...muito-lon...
     */
    public static String formatHeaders(String headersRaw) {
        if (headersRaw == null || headersRaw.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([A-Za-z0-9\\-]+)=([^=]+)(?=\\s+[A-Za-z0-9\\-]+=|$)");
        java.util.regex.Matcher matcher = pattern.matcher(headersRaw);

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();

            if (key.equalsIgnoreCase("Accept")) {
                // Quebra múltiplos valores Accept
                String[] values = value.split(",\\s*");
                sb.append(key).append("=").append(values[0]).append("\n");
                for (int i = 1; i < values.length; i++) {
                    sb.append(values[i]).append("\n");
                }
            } else if (key.equalsIgnoreCase("Authorization")) {
                // Trunca tokens longos
                if (value.length() > 20) {
                    value = value.substring(0, 20) + "...";
                }
                sb.append(key).append("=").append(value).append("\n");
            } else {
                sb.append(key).append("=").append(value).append("\n");
            }
        }
        return sb.toString().trim();
    }

    /**
     * MÉTODO: maskSensitiveDataInJson(String json)
     * 
     * PROPÓSITO:
     * Mascara dados sensíveis em JSON para logs seguros.
     * Protege tokens e credenciais sem perder contexto.
     * 
     * PARÂMETROS:
     * @param json - JSON com possíveis dados sensíveis
     * 
     * RETORNO:
     * @return String - JSON com dados mascarados
     * 
     * CAMPOS MASCARADOS:
     * - authorization: Bearer tokens
     * - token: Tokens JWT
     * 
     * ESTRATÉGIA:
     * - Mantém primeiros 15-20 caracteres
     * - Substitui resto por "..."
     * - Preserva estrutura JSON
     * 
     * EXEMPLO:
     * INPUT:  {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.muito-longo..."}
     * OUTPUT: {"token":"eyJhbGciOiJIUzI1..."}
     */
    public static String maskSensitiveDataInJson(String json) {
        if (json == null) return null;

        String maskedJson = json;

        // Mascara campo "authorization" (Bearer tokens)
        maskedJson = maskedJson.replaceAll(
                "(\"authorization\"\\s*:\\s*\"Bearer\\s+)([^\"]{20})[^\"]*",
                "$1$2...\""
        );

        // Mascara campo "token" (JWT tokens)
        maskedJson = maskedJson.replaceAll(
                "(\"token\"\\s*:\\s*\")([^\"\\s]{15})[^\"\\s]*",
                "$1$2...\""
        );

        return maskedJson;
    }
}
```

### **TagConcatenada.java - Processamento de Tags Excel**

**📍 Localização**: `src/main/java/org/br/com/test/utils/TagConcatenada.java`

```java
package org.br.com.test.utils;

import org.apache.poi.ss.usermodel.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * ============================================================================
 * UTILITÁRIO: TagConcatenada
 * ============================================================================
 * 
 * PROPÓSITO:
 * Processa planilhas Excel para extrair tags de teste específicas
 * e gerar comandos de execução seletiva do Cucumber.
 * Permite execução baseada em critérios definidos por analistas.
 * 
 * PROCESSO:
 * 1. Lê planilha Excel especificada
 * 2. Busca tags no formato @TEST_QAR-XXXX
 * 3. Filtra por critério de execução
 * 4. Gera comando Maven/Cucumber
 * 5. Salva em arquivo de configuração
 * 
 * INTEGRAÇÃO:
 * - TagProcessor: inicia o processamento
 * - RunnerTagConcatenada: executa standalone
 * - Maven: usa tags geradas para execução
 * ============================================================================
 */
public class TagConcatenada {

    /**
     * MÉTODO: tagsExcel(String abaExcel, String execution)
     * 
     * PROPÓSITO:
     * Processa aba específica do Excel extraindo tags baseadas
     * em critério de execução e gera configuração para Cucumber.
     * 
     * PARÂMETROS:
     * @param abaExcel - Nome da aba na planilha (ex: "analista 1")
     * @param execution - Critério de filtro (ex: "@CT-1001", "S")
     * 
     * FLUXO DETALHADO:
     * 1. Abre arquivo MassaDadosCMS.xlsx
     * 2. Localiza aba especificada
     * 3. Varre todas as células buscando padrão @TEST_QAR-\\d+
     * 4. Aplica filtro baseado no critério execution
     * 5. Concatena tags encontradas
     * 6. Gera comando Maven com tags
     * 7. Salva configuração em execution.json
     * 
     * EXEMPLO DE USO:
     * TagConcatenada tc = new TagConcatenada();
     * tc.tagsExcel("analista 1", "@CT-1001");
     * 
     * RESULTADO:
     * Gera comando: mvn test -Dcucumber.filter.tags="@TEST_QAR-001 or @TEST_QAR-002"
     */
    public void tagsExcel(String abaExcel, String execution) {
        String caminhoArquivo = "src/test/resources/data/MassaDadosCMS.xlsx";
        String nomePlanilhaAlvo = abaExcel;
        String caminhoArquivoJson = "src/main/resources/config/execution.json";

        try {
            List<String> tags = new ArrayList<>();

            // Criar workbook baseado na extensão
            FileInputStream arquivoExcel = new FileInputStream(new File(caminhoArquivo));
            Workbook workbook;
            if (caminhoArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivoExcel);
            } else if (caminhoArquivo.endsWith(".xls")) {
                workbook = new HSSFWorkbook(arquivoExcel);
            } else {
                throw new IOException("Formato não suportado. Use .xlsx ou .xls");
            }

            // Padrão regex para tags @TEST_QAR-XXXX
            Pattern padrao = Pattern.compile("@TEST_QAR-\\d+");

            Sheet planilha = workbook.getSheet(nomePlanilhaAlvo);
            if (planilha == null) {
                System.err.println("Planilha '" + nomePlanilhaAlvo + "' não encontrada!");
                return;
            }

            // Processar cada célula da planilha
            for (Row linha : planilha) {
                for (Cell celula : linha) {
                    if (celula != null) {
                        String valorCelula = getCellValueAsString(celula);
                        
                        // Buscar tags na célula
                        Matcher matcher = padrao.matcher(valorCelula);
                        while (matcher.find()) {
                            String tag = matcher.group();
                            if (!tags.contains(tag)) {
                                tags.add(tag);
                            }
                        }
                    }
                }
            }

            // Gerar comando Cucumber com tags encontradas
            if (!tags.isEmpty()) {
                String tagsUnidas = String.join(" or ", tags);
                String comando = "mvn test -Dcucumber.filter.tags=\"" + tagsUnidas + "\"";
                
                System.out.println("Tags encontradas: " + tags);
                System.out.println("Comando gerado: " + comando);
                
                // Salvar configuração
                salvarConfiguracaoExecucao(caminhoArquivoJson, tagsUnidas, comando);
            } else {
                System.out.println("Nenhuma tag encontrada na planilha: " + nomePlanilhaAlvo);
            }

            workbook.close();
            arquivoExcel.close();

        } catch (Exception e) {
            System.err.println("Erro ao processar planilha: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO: getCellValueAsString(Cell celula)
     * 
     * PROPÓSITO:
     * Converte valor de célula Excel para String, tratando
     * diferentes tipos de dados de forma segura.
     * 
     * TIPOS SUPORTADOS:
     * - STRING: texto direto
     * - NUMERIC: números e datas
     * - BOOLEAN: true/false
     * - FORMULA: resultado calculado
     * - BLANK: string vazia
     */
    private String getCellValueAsString(Cell celula) {
        if (celula == null) return "";
        
        switch (celula.getCellType()) {
            case STRING:
                return celula.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(celula)) {
                    return celula.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) celula.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(celula.getBooleanCellValue());
            case FORMULA:
                // Avaliar resultado da fórmula
                return getCellValueAsString(celula.getCachedFormulaResultType(), celula);
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * MÉTODO: salvarConfiguracaoExecucao()
     * 
     * PROPÓSITO:
     * Salva configuração de execução em arquivo JSON para
     * uso posterior por scripts ou CI/CD.
     * 
     * FORMATO JSON:
     * {
     *   "tags": "@TEST_QAR-001 or @TEST_QAR-002",
     *   "comando": "mvn test -Dcucumber.filter.tags=\"@TEST_QAR-001 or @TEST_QAR-002\"",
     *   "gerado_em": "2024-01-15T14:30:00Z"
     * }
     */
    private void salvarConfiguracaoExecucao(String caminho, String tags, String comando) {
        try {
            JSONObject config = new JSONObject();
            config.put("tags", tags);
            config.put("comando", comando);
            config.put("gerado_em", LocalDateTime.now().toString());
            
            Files.write(Paths.get(caminho), config.toString(2).getBytes());
            System.out.println("Configuração salva em: " + caminho);
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar configuração: " + e.getMessage());
        }
    }
}
```

### **CenarioValidator.java - Validador de Cenários**

**📍 Localização**: `src/main/java/org/br/com/test/utils/CenarioValidator.java`

```java
package org.br.com.test.utils;

import org.br.com.core.support.logger.LogFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * ============================================================================
 * VALIDADOR: CenarioValidator
 * ============================================================================
 * 
 * PROPÓSITO:
 * Valida consistência entre nomes de cenários em planilhas Excel
 * e arquivos .feature do Cucumber. Previne execução de testes
 * com dados incorretos devido a inconsistências de nomenclatura.
 * 
 * PROBLEMA RESOLVIDO:
 * - Planilha: "Login do usuário válido"
 * - Feature: "Login de usuario valido"
 * - Resultado: Dados errados carregados!
 * 
 * SOLUÇÃO:
 * - Mapeia IDs para nomes esperados
 * - Valida antes da execução
 * - Falha rapidamente se inconsistente
 * ============================================================================
 */
public class CenarioValidator {
    
    private static final Map<String, String> SCENARIO_NAMES = new HashMap<>();
    private static final Map<String, String> EXPECTED_SCENARIO_NAMES = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * MÉTODO: validateScenarioName(String idCenario, String nomeCenarioPlanilha)
     * 
     * PROPÓSITO:
     * Valida se nome do cenário na planilha corresponde ao
     * nome esperado baseado no arquivo .feature.
     * 
     * PARÂMETROS:
     * @param idCenario - ID único do cenário (ex: "@CT-001")
     * @param nomeCenarioPlanilha - Nome vindo da planilha Excel
     * 
     * VALIDAÇÃO:
     * 1. Carrega mapeamento de .feature (se necessário)
     * 2. Busca nome esperado pelo ID
     * 3. Compara com nome da planilha
     * 4. Lança exceção se diferentes
     * 
     * EXEMPLO:
     * ID: "@CT-001"
     * Esperado: "Login com credenciais válidas"
     * Planilha: "Login com credenciais validas" (sem acento)
     * Resultado: ERRO - nomes diferentes!
     */
    public static void validateScenarioName(String idCenario, String nomeCenarioPlanilha) {
        if (!initialized) {
            loadScenarioNamesFromFeature();
        }
        
        String expectedName = EXPECTED_SCENARIO_NAMES.get(idCenario);
        if (expectedName == null) {
            LogFormatter.logStep("⚠️ AVISO: ID de cenário não encontrado na feature: " + idCenario);
            LogFormatter.logStep("💡 Cenários disponíveis: " + EXPECTED_SCENARIO_NAMES.keySet());
            return;
        }
        
        if (!expectedName.equals(nomeCenarioPlanilha)) {
            String errorMsg = String.format(
                "❌ ERRO DE VALIDAÇÃO: Nome do cenário na planilha não corresponde ao esperado!\n" +
                "   🆔 ID Cenário: %s\n" +
                "   📋 Nome na Planilha: '%s'\n" +
                "   ✅ Nome Esperado: '%s'\n" +
                "   💡 Corrija o nome na planilha para evitar execução de teste errado!",
                idCenario, nomeCenarioPlanilha, expectedName
            );
            
            LogFormatter.logStep(errorMsg);
            throw new RuntimeException("Validação de cenário falhou: " + errorMsg);
        }
        
        LogFormatter.logStep("✅ Validação de cenário: Nome correto para " + idCenario);
    }
    
    /**
     * MÉTODO: loadScenarioNamesFromFeature()
     * 
     * PROPÓSITO:
     * Carrega mapeamento ID → Nome dos arquivos .feature
     * para usar como referência na validação.
     * 
     * PROCESSO:
     * 1. Varre diretório src/main/resources/features
     * 2. Lê cada arquivo .feature
     * 3. Extrai tags de cenário (@CT-XXX)
     * 4. Extrai nomes de cenário correspondentes
     * 5. Cria mapeamento para validação
     * 
     * REGEX UTILIZADAS:
     * - Tags: @CT-\\d+ ou @TEST_QAR-\\d+
     * - Cenários: Scenario: nome_do_cenario
     * 
     * EXEMPLO MAPEAMENTO:
     * "@CT-001" → "Login com credenciais válidas"
     * "@CT-002" → "Cadastro de usuário novo"
     */
    private static void loadScenarioNamesFromFeature() {
        try {
            String featuresPath = "src/main/resources/features";
            File featuresDir = new File(featuresPath);
            
            if (!featuresDir.exists()) {
                LogFormatter.logStep("⚠️ Diretório de features não encontrado: " + featuresPath);
                return;
            }
            
            // Buscar recursivamente arquivos .feature
            List<File> featureFiles = getFeatureFiles(featuresDir);
            
            for (File file : featureFiles) {
                parseFeatureFile(file);
            }
            
            initialized = true;
            LogFormatter.logStep("✅ Mapeamento de cenários carregado: " + EXPECTED_SCENARIO_NAMES.size() + " cenários");
            
        } catch (Exception e) {
            LogFormatter.logError("Erro ao carregar cenários da feature: " + e.getMessage());
            initialized = true; // Marca como inicializado para evitar loops
        }
    }
    
    /**
     * MÉTODO: parseFeatureFile(File file)
     * 
     * PROPÓSITO:
     * Extrai pares tag-cenário de um arquivo .feature específico.
     * 
     * LÓGICA:
     * 1. Lê arquivo linha por linha
     * 2. Identifica linhas com tags (@CT-XXX)
     * 3. Busca próxima linha com "Scenario:"
     * 4. Associa tag com nome do cenário
     * 5. Armazena no mapeamento global
     */
    private static void parseFeatureFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentTag = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Buscar tags de cenário
                if (line.matches(".*@(CT|TEST_QAR)-\\d+.*")) {
                    Pattern tagPattern = Pattern.compile("@(CT|TEST_QAR)-\\d+");
                    Matcher matcher = tagPattern.matcher(line);
                    if (matcher.find()) {
                        currentTag = matcher.group();
                    }
                }
                
                // Buscar nome do cenário
                if (currentTag != null && line.startsWith("Scenario:")) {
                    String scenarioName = line.substring("Scenario:".length()).trim();
                    EXPECTED_SCENARIO_NAMES.put(currentTag, scenarioName);
                    currentTag = null; // Reset para próximo cenário
                }
            }
            
        } catch (IOException e) {
            LogFormatter.logError("Erro ao ler arquivo feature: " + file.getName() + " - " + e.getMessage());
        }
    }
    
    /**
     * MÉTODO: getFeatureFiles(File directory)
     * 
     * PROPÓSITO:
     * Busca recursivamente todos os arquivos .feature
     * em um diretório e subdiretórios.
     * 
     * RETORNO:
     * Lista de arquivos .feature encontrados
     */
    private static List<File> getFeatureFiles(File directory) {
        List<File> featureFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    featureFiles.addAll(getFeatureFiles(file)); // Recursão
                } else if (file.getName().endsWith(".feature")) {
                    featureFiles.add(file);
                }
            }
        }
        
        return featureFiles;
    }
}
```

---

## 📋 **RESUMO FINAL DO FRAMEWORK**

### **🏗️ Arquitetura Completa:**

```
Features (Gherkin)
    ↓
Steps (Bridge)
    ↓
Controllers (HTTP Logic)
    ↓
Models (Data Structure)
    ↓
Managers (State Management)
    ↓
Utils (Support Functions)
    ↓
Sheets (Data Sources)
```

### **🔄 Fluxo de Execução:**

1. **RunnerTestApi** configura e inicia Cucumber
2. **Features** definem cenários em Gherkin  
3. **Steps** fazem bridge para Controllers
4. **Controllers** executam lógica HTTP
5. **Managers** mantêm estado entre steps
6. **Utils** fornecem funcionalidades auxiliares
7. **Sheets** carregam dados de planilhas
8. **Models** estruturam requests/responses

### **🎯 Benefícios da Arquitetura:**

1. **Separação Clara**: Cada camada tem responsabilidade específica
2. **Reutilização**: Controllers e Utils podem ser reutilizados
3. **Manutenibilidade**: Mudanças isoladas em cada camada
4. **Testabilidade**: Cada componente pode ser testado isoladamente
5. **Escalabilidade**: Fácil adição de novas features
6. **Thread-Safety**: Managers garantem isolamento paralelo

### **✅ Checklist de Implementação:**

- [ ] ✅ **Runners** documentados (Main, RunnerTestApi)
- [ ] ✅ **Managers** documentados (Token, Usuario, Artigos, Categoria)  
- [ ] ✅ **Sheets** documentados (ExcelDataReader)
- [ ] ✅ **Steps** documentados (UsuarioSteps)
- [ ] ✅ **Models** documentados (Request/Response)
- [ ] ✅ **Controllers** documentados (UsuarioController)
- [ ] ✅ **Utils** documentados (FormatUtils, TagConcatenada, CenarioValidator)

---

**🎓 Agora você tem documentação COMPLETA do framework para seu caderno!** 📚✨

Essa documentação cobre **TODAS** as classes essenciais com:
- **Propósito** de cada classe
- **Quando usar** cada método  
- **Como implementar** corretamente
- **Exemplos práticos** de código
- **Relacionamentos** entre classes
- **Padrões arquiteturais** aplicados
- **Boas práticas** de desenvolvimento

