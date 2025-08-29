📁 AUTOMACAO-REST-API-cms-for-qas-api (Estrutura Nova)
├── 📁 src
│   └── 📁 test                      // <-- PONTO CRÍTICO 1: TODO o código de automação vai para cá, seguindo o padrão Maven.
│       ├── 📁 java
│       │   └── 📁 org.br.com.test
│       │       ├── 📁 api
│       │       │   └── 📁 clients         → Camada de Acesso: Apenas faz chamadas HTTP (Rest-Assured). Análoga à 'Repository'.
│       │       │       ├── 📄 UsuarioClient.java
│       │       │       ├── 📄 ArtigosClient.java
│       │       │       └── 📄 CategoriaClient.java
│       │       │
│       │       ├── 📁 controllers       → Camada de Orquestração: Lógica de negócio do TESTE e validações (Asserts). Análoga à 'Service'.
│       │       │   ├── 📄 UsuarioController.java
│       │       │   ├── 📄 ArtigosController.java
│       │       │   └── 📄 CategoriaController.java
│       │       │
│       │       ├── 📁 steps             → Camada de "Cola": Traduz Gherkin para chamadas de Controllers. Análoga à 'Controller' do backend.
│       │       │   ├── 📄 UsuarioSteps.java
│       │       │   ├── 📄 ArtigosSteps.java
│       │       │   └── 📄 CategoriaSteps.java
│       │       │
│       │       ├── 📁 model             → DTOs: Objetos de Transferência de Dados (Request/Response) e modelos de erro.
│       │       │   ├── 📁 request
│       │       │   │   ├── 📄 UsuarioRequest.java
│       │       │   │   ├── 📄 LoginRequest.java
│       │       │   │   ├── 📄 ArtigosRequest.java
│       │       │   │   └── 📄 CategoriaRequest.java
│       │       │   ├── 📁 response
│       │       │   │   └── 📄 UsuarioResponse.java
│       │       │   └── 📁 error
│       │       │       ├── 📄 ErrorModel.java
│       │       │       ├── 📄 ErrorFieldModel.java
│       │       │       └── ... (outros modelos de erro)
│       │       │
│       │       └── 📁 support           → PASTA RAIZ PARA TODO O CÓDIGO DE APOIO (o antigo 'core', 'utils', etc.)
│       │           ├── 📁 config        // Responsabilidade: Carregar configurações externas do ambiente.
│       │           │   └── 📄 TestConfig.java      // (Antiga LogConfig, agora mais genérica para ler test.properties)
│       │           │
│       │           ├── 📁 context       // Responsabilidade: Gerenciar o estado e dados durante a execução de um cenário.
│       │           │   └── 📄 Context.java          // (Vem da pasta 'core/support')
│       │           │
│       │           ├── 📁 data          // Responsabilidade: Tudo relacionado a dados de teste (leitura, geração).
│       │           │   ├── 📁 providers // (Antiga 'sheets') Leitura de dados de fontes externas como Excel.
│       │           │   │   ├── 📄 LoginDataProvider.java     // (Antiga LoginDataSheet)
│       │           │   │   ├── 📄 CadastroDataProvider.java  // (Antiga CadastroDataSheet)
│       │           │   │   └── 📄 ExcelDataReader.java      // Utilitário genérico para ler Excel.
│       │           │   └── 📁 generators// (Parte da antiga 'utils') Geração de dados dinâmicos.
│       │           │       ├── 📄 UserDataGenerator.java // (Antiga JavaFaker/FakerApiData)
│       │           │       └── 📄 CpfGenerator.java      // (Antiga GeradorDeCpf)
│       │           │
│       │           ├── 📁 evidence      // Responsabilidade: Geração de evidências de teste.
│       │           │   ├── 📄 DocxEvidenceGenerator.java // (Antiga GeradorDocxApi)
│       │           │   └── 📄 EvidenceFilter.java       // (Vem da pasta 'core/filter')
│       │           │
│       │           ├── 📁 exceptions    // Responsabilidade: Exceções customizadas do framework.
│       │           │   └── 📄 DataException.java       // (Vem da pasta 'core/exceptions')
│       │           │
│       │           ├── 📁 hooks         // Responsabilidade: Códigos que rodam antes/depois dos cenários (Cucumber Hooks).
│       │           │   ├── 📄 HooksEvidenciasApi.java
│       │           │   └── 📄 HooksDados.java
│       │           │
│       │           ├── 📁 manager       // Responsabilidade: Gerenciar estado entre steps (Token, IDs) usando ThreadLocal.
│       │           │   ├── 📄 TokenManager.java
│       │           │   ├── 📄 UsuarioManager.java
│       │           │   ├── 📄 ArtigosManager.java
│       │           │   └── 📄 CategoriaManager.java
│       │           │
│       │           └── 📁 util          // Responsabilidade: Utilitários genéricos e sem estado (stateless).
│       │               ├── 📄 DateUtils.java          // (Antiga DataUtils)
│       │               ├── 📄 FormatUtils.java        // Utilitários para formatar JSON, etc.
│       │               ├── 📄 ResourceUtils.java      // (Vem da pasta 'core/support/resource')
│       │               └── 📄 Encryption.java         // (Vem da pasta 'core/encryption')
│       │
│       └── 📁 resources
│           ├── 📁 features
│           │   ├── 📄 1_usuario.feature
│           │   └── 📄 2_artigos.feature
│           ├── 📁 data
│           │   └── 📄 MassaDadosCMS.xlsx
│           ├── 📁 templates
│           │   └── 📄 Evidencia Modelo API.docx // Template para as evidências
│           ├── 📄 log4j2.xml
│           └── 📄 test.properties
│
├── 📁 target                        → Artefatos de build (gerado pelo Maven)
├── 📄 pom.xml                       → Configuração de build do Maven
├── 📄 .gitignore                    → Arquivos ignorados pelo Git
└── 📄 README.md                     → Documentação do projeto




📁 AUTOMACAO-REST-API-cms-for-qas-api (Estrutura Antiga)
├── 📁 src
│   └── 📁 main                      // <-- PONTO CRÍTICO 1: Código de teste em 'src/main', quebrando a convenção do Maven.
│       ├── 📁 java
│       │   └── 📁 org.br.com
│       │       ├── 📁 core              → Continha o "motor" do framework. Boa intenção, mas as classes poderiam ser melhor classificadas.
│       │       │   ├── 📁 data
│       │       │   │   ├── 📄 DataReader.java      // Responsável pela conexão de baixo nível com a planilha (usando a lib Fillo).
│       │       │   │   ├── 📄 DataSheet.java       // Classe base abstrata para ler uma única linha de dados de uma aba do Excel.
│       │       │   │   ├── 📄 DataModel.java       // Interface de marcação para identificar classes que são modelos de dados.
│       │       │   │   └── 📄 MultipleDataSheet.java // Classe base abstrata para ler múltiplas linhas de dados de uma aba.
│       │       │   ├── 📁 encryption
│       │       │   │   └── 📄 Encryption.java      // Utilitário para criptografar e descriptografar dados.
│       │       │   ├── 📁 exceptions
│       │       │   │   └── 📄 DataException.java   // Exceção customizada para erros relacionados ao acesso de dados.
│       │       │   ├── 📁 filter
│       │       │   │   ├── 📄 EvidenceFilter.java  // Interceptador do Rest-Assured para capturar dados para evidências.
│       │       │   │   └── 📄 PDFLoggerFilter.java // Filtro antigo para gerar evidências em PDF.
│       │       │   ├── 📁 processor
│       │       │   │   └── 📄 TagProcessor.java    // Processador para lógica de tags concatenadas.
│       │       │   ├── 📁 support
│       │       │   │   ├── 📄 Context.java          // Gerenciava o estado e contadores da execução.
│       │       │   │   ├── 📁 logger
│       │       │   │   │   ├── 📄 Console.java      // Constantes para formatação de log.
│       │       │   │   │   ├── 📄 ContextTime.java  // Media o tempo de execução.
│       │       │   │   │   └── 📄 LogFormatter.java  // Classe central para formatar e imprimir logs.
│       │       │   │   └── 📁 resource
│       │       │   │       └── 📄 ResourceUtils.java // Utilitário para encontrar o caminho de arquivos de recurso.
│       │       │   └── 📁 token
│       │       │       ├── 📄 GerarToken.java      // Lógica antiga e comentada para gerar tokens.
│       │       │       ├── 📄 GerarTokenResquest.java // DTO para a requisição de token.
│       │       │       └── 📄 Token.java           // Interface para definir um contrato de token.
│       │       │
│       │       ├── 📁 test
│       │       │   ├── 📁 controllers   → PONTO CRÍTICO 2: Camada "God Object" que acumulava múltiplas responsabilidades.
│       │       │   │   │                 // Fazia chamadas HTTP, orquestrava o teste e continha as validações.
│       │       │   │   ├── 📁 artigos
│       │       │   │   │   └── 📄 ArtigosController.java
│       │       │   │   ├── 📁 categoria
│       │       │   │   │   └── 📄 CategoriaController.java
│       │       │   │   └── 📁 usuario
│       │       │   │       ├── 📄 UsuarioController.java        // Controller principal que fazia quase tudo.
│       │       │   │       ├── 📄 AtualizarUsuarioController.java // Tentativas de separar responsabilidades que
│       │       │   │       ├── 📄 ConsultarUsuarioController.java // geravam complexidade e problemas de estado
│       │       │   │       ├── 📄 ExcluirUsuarioController.java   // (NullPointerException).
│       │       │   │       └── 📄 GerarUsuarioController.java
│       │       │   │
│       │       │   ├── 📁 manager       → Gerenciava estado compartilhado (tokens, IDs) com ThreadLocal. (Boa prática).
│       │       │   │   ├── 📄 TokenManager.java
│       │       │   │   ├── 📄 UsuarioManager.java
│       │       │   │   ├── 📄 ArtigosManager.java
│       │       │   │   └── 📄 CategoriaManager.java
│       │       │   │
│       │       │   ├── 📁 model         → DTOs: Objetos de Transferência de Dados (Request/Response). (Estrutura correta).
│       │       │   │   ├── 📁 builder
│       │       │   │   │   └── 📄 TagBuilder.java
│       │       │   │   ├── 📁 error
│       │       │   │   │   ├── 📁 field
│       │       │   │   │   │   ├── 📄 ApiErrorDetail.java
│       │       │   │   │   │   ├── 📄 ErrorFieldModel.java
│       │       │   │   │   │   ├── 📄 ErrorModel.java
│       │       │   │   │   │   └── 📄 ErrorResponse.java
│       │       │   │   │   ├── 📁 timeStamp
│       │       │   │   │   │   └── 📄 ErrorBadRequestResponse.java
│       │       │   │   │   └── 📁 title
│       │       │   │   │       ├── 📄 ErrorTitleModel.java
│       │       │   │   │       └── 📄 ErrorTitleResponse.java
│       │       │   │   ├── 📁 request
│       │       │   │   │   ├── 📄 ArtigosRequest.java
│       │       │   │   │   ├── 📄 CategoriaRequest.java
│       │       │   │   │   ├── 📄 LoginRequest.java
│       │       │   │   │   └── 📄 UsuarioRequest.java
│       │       │   │   └── 📁 response
│       │       │   │       └── 📄 UsuarioResponse.java
│       │       │   │
│       │       │   ├── 📁 steps         → Camada de "Cola": Traduzia Gherkin para chamadas de Controllers. (Estrutura correta).
│       │       │   │   ├── 📄 ArtigosSteps.java
│       │       │   │   ├── 📄 CategoriaSteps.java
│       │       │   │   ├── 📄 LoginSteps.java // Classe de steps comentada.
│       │       │   │   └── 📄 UsuarioSteps.java
│       │       │   │
│       │       │   ├── 📁 sheets        → Responsável pela leitura de dados do Excel. (Boa prática, mas pode ser melhor classificada).
│       │       │   │   ├── 📁 cadastro
│       │       │   │   │   ├── 📄 CadastroDataSheet.java
│       │       │   │   │   └── 📄 CadastroModel.java
│       │       │   │   ├── 📁 dados_usuario
│       │       │   │   │   ├── 📄 DadosUsuarioModel.java
│       │       │   │   │   └── 📄 DadosUsuarioSheet.java
│       │       │   │   └── 📁 login
│       │       │   │       ├── 📄 LoginDataSheet.java
│       │       │   │       └── 📄 LoginModel.java
│       │       │   │
│       │       │   └── 📁 utils         → PONTO CRÍTICO 3: "Gaveta de bagunça" com responsabilidades mistas.
│       │       │       ├── 📁 dataUsers
│       │       │       │   └── 📄 Users.java
│       │       │       ├── 📁 evidence
│       │       │       │   └── 📄 GeradorDocxApi.java     // Lógica para criar o arquivo .docx de evidência.
│       │       │       ├── 📁 hooks
│       │       │       │   ├── 📄 HooksDados.java         // Carregava dados da planilha antes do cenário.
│       │       │       │   ├── 📄 HooksEvidenciasApi.java  // Orquestrava a geração de evidências.
│       │       │       │   └── 📄 HooksUsers.java
│       │       │       ├── 📁 support
│       │       │       │   └── 📁 data
│       │       │       │       └── 📄 DataResource.java
│       │       │       ├── 📄 CenarioValidator.java       // Lógica para validar nomes de cenários.
│       │       │       ├── 📄 DataUtils.java              // Utilitários para formatação de data/hora.
│       │       │       ├── 📄 FakerApiData.java           // Versão antiga do gerador de dados.
│       │       │       ├── 📄 FormatUtils.java            // Utilitários para formatar JSON e Headers.
│       │       │       ├── 📄 GeradorDeCpf.java           // Gerador de CPF válido.
│       │       │       ├── 📄 GeradorMassaRunner.java     // Script para gerar massa de dados.
│       │       │       ├── 📄 JavaFaker.java              // Gerador de dados fakes.
│       │       │       ├── 📄 LogConfig.java              // Leitor de configurações do 'test.properties'.
│       │       │       ├── 📄 LogFileManager.java
│       │       │       ├── 📄 ScenarioCountExtract.java   // Script para contar cenários e steps.
│       │       │       ├── 📄 TagConcatenada.java
│       │       │       ├── 📄 TagExtractor.java           // Script para extrair tags das features.
│       │       │       ├── 📄 TesteCenarioValidator.java  // Runner para testar a validação de cenários.
│       │       │       └── 📄 ValidadorMassaRunner.java   // Script para validar a massa de dados gerada.
│       │       │
│       │       ├── 📄 Main.java
│       │       ├── 📄 RunnerTagConcatenada.java
│       │       └── 📄 RunnerTestApi.java // Runner principal dos testes.
│       │
│       └── 📁 resources
│           ├── 📁 features
│           │   └── 📄 1_usuario.feature
│           ├── 📁 data
│           │   └── 📄 MassaDadosCMS.xlsx
│           └── 📄 test.properties
│
├── 📁 target                        → Artefatos de build (gerado pelo Maven)
├── 📄 pom.xml                       → Configuração de build do Maven
├── 📄 .gitignore                    → Arquivos ignorados pelo Git
└── 📄 README.md                     → Documentação do projeto