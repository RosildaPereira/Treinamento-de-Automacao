# 📚 **RESOURCES - CONFIGURAÇÕES E DADOS - COMENTÁRIOS DETALHADOS**

## 🎯 **VISÃO GERAL DOS RESOURCES**

O diretório `src/main/resources` contém todos os **arquivos de configuração**, **dados de teste**, **features Gherkin**, **templates de evidências** e **propriedades** do framework. É o centro de configuração e recursos externos do projeto.

---

## ⚙️ **1. CONFIGURAÇÕES PRINCIPAIS**

### **test.properties - Configurações de Log e Mascaramento**

**📍 Localização**: `src/main/resources/test.properties`

```properties
# Configurações de Log para Testes
# ========================================

# Controla se dados sensíveis são mascarados nos logs
# true = dados mascarados (recomendado para produção)
# false = dados visíveis (útil para debug)
log.mascarar.dados.sensiveis=true

# Controla se senhas são mascaradas
log.mascarar.senha=true

# Controla se IDs são mascarados
log.mascarar.id=false

# Controla se tokens são mascarados
log.mascarar.token=true

# Controla se emails são mascarados
log.mascarar.email=false

# Número de caracteres visíveis no início e fim de dados mascarados
log.caracteres.visiveis=3
```

**📋 DOCUMENTAÇÃO DETALHADA:**

```java
/**
 * ============================================================================
 * ARQUIVO: test.properties
 * ============================================================================
 * 
 * PROPÓSITO:
 * Configurações centralizadas para comportamento de logging e mascaramento
 * de dados sensíveis em testes automatizados. Permite controle fino sobre
 * que informações são expostas nos logs para segurança e debugging.
 * 
 * INTEGRAÇÃO:
 * - LogConfig.java: carrega e interpreta essas configurações
 * - LogFormatter.java: aplica mascaramento baseado nessas regras
 * - Controllers: usam mascaramento automático em logs
 * 
 * AMBIENTES:
 * - DEV: dados visíveis (false) para debugging
 * - QA: dados parcialmente mascarados
 * - PROD: dados totalmente mascarados (true)
 * ============================================================================
 */

/**
 * PROPRIEDADE: log.mascarar.dados.sensiveis
 * 
 * PROPÓSITO:
 * Controle master para ativação/desativação de mascaramento geral.
 * Quando true, ativa mascaramento para todos os tipos de dados sensíveis.
 * 
 * VALORES:
 * - true: Ativa mascaramento (RECOMENDADO para produção)
 * - false: Desativa mascaramento (útil para debug)
 * 
 * QUANDO USAR:
 * - true: Ambientes compartilhados, CI/CD, logs de produção
 * - false: Desenvolvimento local, debugging de problemas
 * 
 * EXEMPLO DE EFEITO:
 * true:  "Fazendo login com email: te***@****.com"
 * false: "Fazendo login com email: teste@gmail.com"
 * 
 * SEGURANÇA:
 * NUNCA deixar false em ambientes de produção ou compartilhados.
 */
log.mascarar.dados.sensiveis=true

/**
 * PROPRIEDADE: log.mascarar.senha
 * 
 * PROPÓSITO:
 * Controla especificamente se senhas são mascaradas nos logs.
 * Propriedade mais crítica de segurança do sistema.
 * 
 * VALORES:
 * - true: Senhas são mascaradas (SEMPRE recomendado)
 * - false: Senhas aparecem em texto plano (PERIGOSO)
 * 
 * COMPORTAMENTO:
 * true:  "Senha: s*n***123"
 * false: "Senha: senha123"
 * 
 * IMPORTANTE:
 * Esta propriedade NUNCA deve ser false em qualquer ambiente.
 * Senhas em logs são violação grave de segurança.
 * 
 * CASOS DE USO:
 * - true: SEMPRE (padrão obrigatório)
 * - false: NUNCA (violação de segurança)
 */
log.mascarar.senha=true

/**
 * PROPRIEDADE: log.mascarar.id
 * 
 * PROPÓSITO:
 * Controla se IDs de usuários/entidades são mascarados.
 * IDs geralmente são menos sensíveis que senhas/emails.
 * 
 * VALORES:
 * - true: IDs mascarados
 * - false: IDs visíveis (útil para debugging)
 * 
 * COMPORTAMENTO:
 * true:  "User ID: bb2***-****-****-****-***0f219a0ad9d1"
 * false: "User ID: bb275509-1234-5678-9abc-def0f219a0ad9d1"
 * 
 * CONSIDERAÇÕES:
 * - false: facilita debugging e rastreamento
 * - true: maior privacidade, dificulta debugging
 * 
 * RECOMENDAÇÃO:
 * false para desenvolvimento, true para produção se necessário.
 */
log.mascarar.id=false

/**
 * PROPRIEDADE: log.mascarar.token
 * 
 * PROPÓSITO:
 * Controla mascaramento de tokens JWT/Bearer/API keys.
 * Tokens são credenciais de acesso e devem ser protegidos.
 * 
 * VALORES:
 * - true: Tokens mascarados (recomendado)
 * - false: Tokens completos visíveis (debug apenas)
 * 
 * COMPORTAMENTO:
 * true:  "Token: eyJhbGciOiJIUzI1***..."
 * false: "Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI..."
 * 
 * SEGURANÇA:
 * Tokens podem ser usados para acesso não autorizado.
 * Mascaramento recomendado em ambientes compartilhados.
 * 
 * DEBUGGING:
 * false temporariamente para debug de problemas de autenticação.
 */
log.mascarar.token=true

/**
 * PROPRIEDADE: log.mascarar.email
 * 
 * PROPÓSITO:
 * Controla mascaramento de endereços de email.
 * Emails podem ser considerados PII (informação pessoal).
 * 
 * VALORES:
 * - true: Emails mascarados
 * - false: Emails visíveis (útil para testes)
 * 
 * COMPORTAMENTO:
 * true:  "Email: tes***@***ail.com"
 * false: "Email: teste@gmail.com"
 * 
 * CONSIDERAÇÕES:
 * - false: facilita identificação de dados de teste
 * - true: proteção de PII, compliance LGPD/GDPR
 * 
 * RECOMENDAÇÃO:
 * false para dados de teste fake, true para dados reais.
 */
log.mascarar.email=false

/**
 * PROPRIEDADE: log.caracteres.visiveis
 * 
 * PROPÓSITO:
 * Define quantos caracteres permanecem visíveis no início e fim
 * quando dados são mascarados. Controla nível de mascaramento.
 * 
 * VALOR: 3
 * Significa: 3 caracteres visíveis no início + "***" + 3 no final
 * 
 * EXEMPLOS COM DIFERENTES VALORES:
 * 
 * Valor = 1:
 * "senha123456" → "s***6"
 * 
 * Valor = 3:
 * "senha123456" → "sen***456"
 * 
 * Valor = 5:
 * "senha123456" → "senha***456" (pode expor demais)
 * 
 * BALANCEAMENTO:
 * - Muito baixo (1-2): dificulta debugging
 * - Ideal (3-4): equilibra segurança e utilidade
 * - Muito alto (5+): pode expor informações sensíveis
 * 
 * RECOMENDAÇÃO:
 * 3 caracteres oferece bom equilíbrio entre segurança e debugging.
 */
log.caracteres.visiveis=3
```

### **log4j2.xml - Configuração de Logging**

**📍 Localização**: `src/main/resources/log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%msg%n"/>
        </Console>
        
        <!-- File Appender -->
        <File name="FileAppender" fileName="target/log/execution.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n"/>
        </File>
        
        <!-- Rolling File Appender -->
        <RollingFile name="RollingFile"
                     fileName="target/log/automation-tmp.log"
                     filePattern="target/log/automation-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```

**📋 DOCUMENTAÇÃO DETALHADA:**

```xml
<!--
============================================================================
ARQUIVO: log4j2.xml
============================================================================

PROPÓSITO:
Configuração do Log4j2 para sistema de logging robusto e organizado.
Define múltiplos appenders para diferentes destinos e formatos de log,
permitindo rastreabilidade completa da execução de testes.

ESTRATÉGIA DE LOGGING:
1. Console: feedback imediato durante execução
2. Arquivo fixo: log permanente da sessão atual
3. Arquivo rotativo: histórico de logs com compressão automática

INTEGRAÇÃO:
- LogFormatter.java: usa esses appenders
- RunnerTestApi.java: gerencia arquivos de log
- Todas as classes: podem logar através do LogFormatter
============================================================================
-->

<Configuration status="WARN">
    <!--
    ATRIBUTO: status="WARN"
    
    PROPÓSITO:
    Define nível de log interno do Log4j2.
    Controla logs sobre o próprio sistema de logging.
    
    VALORES:
    - ERROR: apenas erros críticos do Log4j2
    - WARN: avisos e erros (RECOMENDADO)
    - INFO: informações detalhadas
    - DEBUG: debugging completo do Log4j2
    
    RECOMENDAÇÃO:
    WARN oferece equilíbrio entre informação e performance.
    -->
    
    <Appenders>
        <!--
        SEÇÃO: Appenders
        
        PROPÓSITO:
        Define ONDE e COMO os logs são escritos.
        Cada appender representa um destino diferente.
        
        TIPOS CONFIGURADOS:
        1. Console: saída imediata na tela
        2. File: arquivo permanente
        3. RollingFile: arquivo com rotação automática
        -->
        
        <!-- 
        APPENDER: Console
        
        PROPÓSITO:
        Exibe logs diretamente no console/terminal durante execução.
        Fornece feedback imediato para desenvolvedores e CI/CD.
        
        CARACTERÍSTICAS:
        - Target: SYSTEM_OUT (stdout padrão)
        - Pattern: apenas mensagem + quebra de linha
        - Uso: feedback em tempo real
        
        PADRÃO: %msg%n
        - %msg: conteúdo da mensagem
        - %n: quebra de linha (independente do SO)
        
        EXEMPLO DE SAÍDA:
        "Executando login com credenciais válidas"
        "Status code retornado: 200"
        -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%msg%n"/>
        </Console>
        
        <!--
        APPENDER: FileAppender
        
        PROPÓSITO:
        Arquivo de log permanente para sessão atual de execução.
        Usado para análise detalhada post-execução.
        
        CARACTERÍSTICAS:
        - Arquivo: target/log/execution.log
        - Sobrescreve a cada execução
        - Include timestamp, level e mensagem
        
        PADRÃO: %d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n
        - %d{...}: timestamp formatado
        - %-5level: nível com largura fixa 5 chars (esquerda)
        - %msg: mensagem
        - %n: quebra de linha
        
        EXEMPLO DE SAÍDA:
        "2024-01-15 14:30:25 INFO  - Iniciando execução de testes"
        "2024-01-15 14:30:26 ERROR - Falha na validação do status code"
        
        GESTÃO:
        RunnerTestApi.java renomeia este arquivo após execução
        para preservar logs históricos.
        -->
        <File name="FileAppender" fileName="target/log/execution.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n"/>
        </File>
        
        <!--
        APPENDER: RollingFile
        
        PROPÓSITO:
        Sistema avançado de logging com rotação automática.
        Previne crescimento excessivo de arquivos de log.
        
        CARACTERÍSTICAS:
        - Arquivo ativo: target/log/automation-tmp.log
        - Rotação por tempo: diária
        - Rotação por tamanho: 10MB máximo
        - Compressão: arquivos antigos em .gz
        - Retenção: máximo 10 arquivos
        
        PADRÃO: %d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n
        - %d{...}: timestamp completo
        - [%t]: nome da thread (importante para testes paralelos)
        - %-5level: nível do log
        - %logger{36}: nome da classe (máximo 36 chars)
        - %msg: mensagem
        
        EXEMPLO DE SAÍDA:
        "2024-01-15 14:30:25 [main] INFO  o.b.c.t.c.u.UsuarioController - Validando criação de usuário"
        "2024-01-15 14:30:26 [Test-1] ERROR o.b.c.t.s.UsuarioSteps - Falha na validação"
        
        POLÍTICAS DE ROTAÇÃO:
        1. TimeBasedTriggeringPolicy: nova data = novo arquivo
        2. SizeBasedTriggeringPolicy: arquivo > 10MB = rotação
        
        NOMENCLATURA DE ARQUIVOS:
        - automation-2024-01-15-1.log.gz (primeira rotação do dia)
        - automation-2024-01-15-2.log.gz (segunda rotação do dia)
        - automation-2024-01-16-1.log.gz (primeiro do dia seguinte)
        
        ESTRATÉGIA DE RETENÇÃO:
        DefaultRolloverStrategy max="10": mantém apenas 10 arquivos
        Arquivos mais antigos são automaticamente deletados.
        -->
        <RollingFile name="RollingFile"
                     fileName="target/log/automation-tmp.log"
                     filePattern="target/log/automation-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <!--
                POLÍTICA: TimeBasedTriggeringPolicy
                
                PROPÓSITO:
                Força criação de novo arquivo a cada dia.
                Garante separação de logs por data.
                
                COMPORTAMENTO:
                - 00:00:00: novo arquivo é criado automaticamente
                - Arquivo anterior é comprimido e arquivado
                -->
                <TimeBasedTriggeringPolicy />
                
                <!--
                POLÍTICA: SizeBasedTriggeringPolicy
                
                PROPÓSITO:
                Previne arquivos muito grandes que podem afetar performance.
                
                TAMANHO: 10MB
                - Suficiente para execuções longas
                - Não muito grande para abrir/processar
                
                COMPORTAMENTO:
                Quando arquivo ativo atinge 10MB, inicia nova rotação
                mesmo que seja o mesmo dia.
                -->
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            
            <!--
            ESTRATÉGIA: DefaultRolloverStrategy
            
            PROPÓSITO:
            Define quantos arquivos históricos manter.
            
            MÁXIMO: 10 arquivos
            - Equilibra histórico vs. espaço em disco
            - Suficiente para várias semanas de histórico
            
            COMPORTAMENTO:
            Quando 11º arquivo seria criado, o mais antigo é deletado.
            -->
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
    </Appenders>
    
    <Loggers>
        <!--
        SEÇÃO: Loggers
        
        PROPÓSITO:
        Define QUAIS mensagens são logadas e através de quais appenders.
        
        HIERARQUIA:
        - Root: logger padrão para todas as classes
        - Pode ter loggers específicos para pacotes/classes
        -->
        
        <!--
        LOGGER: Root
        
        PROPÓSITO:
        Logger padrão aplicado a todas as classes do projeto.
        
        NÍVEL: info
        
        HIERARQUIA DE NÍVEIS:
        1. ERROR: apenas erros críticos
        2. WARN: avisos e erros
        3. INFO: informações importantes (CONFIGURADO)
        4. DEBUG: informações detalhadas de debugging
        5. TRACE: informações muito detalhadas
        
        IMPACTO DO NÍVEL INFO:
        - LogFormatter.logStep(): será logado (usa INFO)
        - LogFormatter.logError(): será logado (usa ERROR)
        - Logs DEBUG/TRACE: serão ignorados
        
        APPENDERS VINCULADOS:
        Todos os três appenders recebem mensagens:
        1. Console: para feedback imediato
        2. FileAppender: para arquivo permanente
        3. RollingFile: para histórico com rotação
        
        RESULTADO:
        Cada mensagem aparece nos 3 destinos simultaneamente.
        -->
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```

---

## 📊 **2. DADOS DE TESTE**

### **ESTRUTURA_PLANILHA_EXCEL.md - Documentação da Massa de Dados**

**📍 Localização**: `src/main/resources/data/ESTRUTURA_PLANILHA_EXCEL.md`

```markdown
# Estrutura da Planilha MassaDadosCMS.xlsx

## ============================================================================
## DOCUMENTAÇÃO: ESTRUTURA_PLANILHA_EXCEL.md
## ============================================================================
##
## PROPÓSITO:
## Documenta a estrutura completa das planilhas Excel utilizadas para
## Data-Driven Testing. Define tabelas, relacionamentos e fluxo de dados
## para cenários automatizados de teste de API.
##
## VANTAGENS DA ESTRUTURA:
## - Centralização de dados via PROCV (VLOOKUP)
## - Separação clara entre cenários e dados fonte
## - Flexibilidade para adicionar novos campos
## - Performance otimizada com consulta única
## - Facilidade de manutenção
## ============================================================================

## TABELAS CRIADAS

### 1. TBL_CENARIOS (PRINCIPAL)
## PROPÓSITO:
## Tabela principal que contém todos os dados necessários através de PROCV da TBL_CADASTRO.
## Cada linha representa um cenário de teste completo com dados específicos.
##
## ESTRUTURA:
| SEQ | ID_CENARIO | NOME | FEATURE | ID_USUARIO | EMAIL | SENHA |
|-----|------------|------|---------|------------|-------|-------|
| 1   | CT-1001    | Criar um novo usuário CMS com sucesso | usuario | 0001 | Dagmar.Mitchell25@hotmail.com | nWJzWZhqf5ZooiA |
| 2   | CT-1002    | Tentar criar um usuário com e-mail já existente | usuario | 0002 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF |
| 3   | CT-1003    | Tentar criar um usuário com nome de usuário já existente | usuario | 0003 | Ashly.Rath@gmail.com | Vp6mdcGBLMJ6P3Y |

## CAMPOS DETALHADOS:
##
## SEQ:
## - Número sequencial para organização
## - Tipo: Integer
## - Uso: Controle interno da planilha
##
## ID_CENARIO:
## - Identificador único do cenário de teste
## - Formato: CT-XXXX
## - Uso: Tag no Cucumber, busca de dados
## - Exemplo: CT-1002, CT-1003
##
## NOME:
## - Descrição textual do cenário
## - Tipo: String
## - Uso: Documentação, relatórios
## - Deve corresponder ao nome no arquivo .feature
##
## FEATURE:
## - Nome do arquivo .feature correspondente
## - Tipo: String
## - Uso: Organização, mapeamento
## - Exemplo: "usuario", "categoria", "artigos"
##
## ID_USUARIO:
## - Identificador do usuário para lookup
## - Formato: 0001, 0002, 0003...
## - Uso: PROCV para buscar dados completos
##
## EMAIL:
## - Email do usuário para login
## - Origem: PROCV da TBL_CADASTRO
## - Uso: Autenticação, identificação
##
## SENHA:
## - Senha do usuário para login
## - Origem: PROCV da TBL_CADASTRO
## - Uso: Autenticação
## - Consideração: Pode ser criptografada

### 2. TBL_CADASTRO (FONTE DOS DADOS)
## PROPÓSITO:
## Tabela fonte com dados completos dos usuários.
## Utilizada pelo PROCV da TBL_CENARIOS para buscar informações.
##
## ESTRUTURA:
| SEQ | ID_USUARIO | NOME_COMPLETO | NOME_USUARIO | EMAIL | ID | SENHA |
|-----|------------|---------------|--------------|-------|----|-------|
| 1   | 0001       | Glenda Mertz  | Ceasar.Ward71 | Dagmar.Mitchell25@hotmail.com | b3ee84a9-8789-42ad-a108-8dbd00609c22 | nWJzWZhqf5ZooiA |
| 2   | 0002       | Mr. Candace Lueilwitz | Mozelle54 | Fredrick_Quitzon3@hotmail.com | 3da73e8f-4790-4cc8-ae67-11c87d6544d3 | dFhGC3kHlqcywKF |

## CAMPOS DETALHADOS:
##
## ID_USUARIO:
## - Chave primária para PROCV
## - Formato: 0001, 0002, 0003...
## - Relacionamento: TBL_CENARIOS.ID_USUARIO
##
## NOME_COMPLETO:
## - Nome completo do usuário
## - Uso: Cadastro, perfil, validações
## - Gerado: JavaFaker ou manual
##
## NOME_USUARIO:
## - Username único no sistema
## - Uso: Login alternativo, identificação
## - Validação: Unicidade necessária
##
## EMAIL:
## - Endereço de email único
## - Uso: Login principal, comunicação
## - Validação: Formato de email válido
##
## ID:
## - UUID gerado pelo sistema após cadastro
## - Tipo: String UUID
## - Uso: Identificação única no banco
##
## SENHA:
## - Senha para autenticação
## - Considerações: Criptografia opcional
## - Força: Seguir políticas de senha

### 3. TBL_DADOS_ID (OPCIONAL)
## PROPÓSITO:
## Dados adicionais de identificação se necessário.
## Extensão para campos específicos ou metadados.

## CENÁRIOS DE TESTE

### ❌ CT-1001: Comentado
- **Motivo**: Não precisamos mais testar cadastro de usuário
- **Status**: Comentado na feature
- **Razão**: Foco em operações com usuários existentes

### ✅ CT-1002 em diante: Ativos
- **CT-1002**: Teste de email duplicado
- **CT-1003**: Teste de nome usuário duplicado  
- **CT-1004**: Teste de dados inválidos
- **CT-1005**: Teste de login com sucesso
- **CT-1006**: Teste de login inválido
- **CT-1007**: Teste de listagem com autenticação
- **CT-1008**: Teste de listagem sem autenticação
- **CT-1009**: Teste de busca por ID
- **CT-1010**: Teste de busca sem ID
- **CT-1011**: Teste de atualização
- **CT-1012**: Teste de atualização inválida
- **CT-1013**: Teste de exclusão
- **CT-1014**: Teste de exclusão sem autenticação

## FLUXO DE FUNCIONAMENTO (SIMPLIFICADO)

### PROCESSO DE EXECUÇÃO:
1. **Cenário executa** com tag `@CT-1002` (ou superior)
2. **Hook captura** a tag e extrai `CT-1002`
3. **Context.setIdUsuario("CT-1002")**
4. **LoginDataSheet** busca diretamente na TBL_CENARIOS:
   - WHERE ID_CENARIO = 'CT-1002' → retorna todos os dados
5. **Context.setData(loginModel)**
6. **Steps/Controllers** usam os dados para login e outras operações

### INTEGRAÇÃO COM CLASSES:
## LoginDataSheet.java:
## - Lê TBL_CENARIOS usando ID_CENARIO como filtro
## - Retorna LoginModel com dados completos
##
## Hook (HooksEvidenciasApi.java):
## - Extrai tag do cenário (@CT-XXXX)
## - Inicializa LoginDataSheet
## - Armazena dados no Context
##
## Controllers:
## - Obtém dados via Context.getData()
## - Usa email/senha para autenticação
## - Usa outros campos conforme necessário

## VANTAGENS DA NOVA ESTRUTURA

- ✅ **Simplicidade**: Uma única consulta na TBL_CENARIOS
- ✅ **Performance**: Sem joins ou múltiplas consultas
- ✅ **Manutenção**: Dados centralizados via PROCV
- ✅ **Flexibilidade**: Fácil adicionar novos campos
- ✅ **Foco**: Apenas testes de login e operações com usuários existentes

## EXEMPLO DE USO NOS TESTES

### Login de Usuário
```java
// Os dados vêm diretamente da TBL_CENARIOS
LoginModel dados = (LoginModel) Context.getData();
String email = dados.getEmail();  // "Fredrick_Quitzon3@hotmail.com"
String senha = dados.getSenha();  // "dFhGC3kHlqcywKF"

// Fazer login
LoginRequest request = LoginRequest.builder()
    .email(email)
    .senha(senha)
    .build();
```

### Operações com Usuário Logado
```java
// Usar dados do usuário logado para outras operações
LoginModel usuario = (LoginModel) Context.getData();
String email = usuario.getEmail();

// Fazer requisições de listagem, busca, atualização, etc.
given()
    .header("Authorization", "Bearer " + TokenManager.getToken())
    .when()
    .get("/usuarios")
    .then()
    .statusCode(200);
```

## CONSIDERAÇÕES IMPORTANTES

### SEGURANÇA:
- ✅ **Senhas**: Considerar criptografia se necessário
- ✅ **Emails**: Usar dados fake para testes
- ✅ **IDs**: UUIDs reais ou fake dependendo do ambiente

### MANUTENÇÃO:
- ✅ **Sincronização**: Manter TBL_CENARIOS e TBL_CADASTRO sincronizadas
- ✅ **PROCV**: Verificar fórmulas Excel regularmente
- ✅ **Dados**: Atualizar quando API muda

### PERFORMANCE:
- ✅ **Consultas**: Uma busca por cenário (otimizado)
- ✅ **Cache**: Context armazena dados para reutilização
- ✅ **Thread-Safety**: Isolamento garantido pelo ThreadLocal
```

### **Planilhas Excel - MassaDadosCMS.xlsx**

**📍 Localização**: `src/main/resources/data/MassaDadosCMS.xlsx`

```java
/**
 * ============================================================================
 * ARQUIVO: MassaDadosCMS.xlsx
 * ============================================================================
 * 
 * PROPÓSITO:
 * Planilha principal contendo massa de dados para testes automatizados.
 * Implementa estrutura de Data-Driven Testing com relacionamentos
 * otimizados através de fórmulas PROCV (VLOOKUP).
 * 
 * ESTRUTURA INTERNA:
 * - TBL_CENARIOS: dados por cenário de teste
 * - TBL_CADASTRO: repositório central de usuários
 * - TBL_DADOS_ID: dados auxiliares (opcional)
 * 
 * INTEGRAÇÃO:
 * - LoginDataSheet.java: lê dados desta planilha
 * - Hook: carrega dados baseado em tags
 * - Context: armazena dados para uso nos testes
 * 
 * VANTAGENS:
 * - Centralização de dados de teste
 * - Facilidade de manutenção
 * - Versionamento com Git
 * - Flexibilidade para novos cenários
 * ============================================================================
 */

/**
 * ABA: TBL_CENARIOS
 * 
 * PROPÓSITO:
 * Tabela principal que mapeia cenários de teste para dados específicos.
 * Cada linha representa um caso de teste completo.
 * 
 * COLUNAS:
 * - SEQ: Sequencial para organização
 * - ID_CENARIO: Tag do Cucumber (CT-XXXX)
 * - NOME: Descrição do cenário
 * - FEATURE: Arquivo .feature correspondente
 * - ID_USUARIO: Chave para busca em TBL_CADASTRO
 * - EMAIL: Email do usuário (vem via PROCV)
 * - SENHA: Senha do usuário (vem via PROCV)
 * 
 * EXEMPLO DE LINHA:
 * SEQ=2, ID_CENARIO=CT-1002, NOME="Tentar criar usuário com email duplicado",
 * FEATURE=usuario, ID_USUARIO=0002, EMAIL=PROCV(...), SENHA=PROCV(...)
 * 
 * FÓRMULAS PROCV:
 * EMAIL: =PROCV(E2,TBL_CADASTRO!$B:$G,4,0)
 * SENHA: =PROCV(E2,TBL_CADASTRO!$B:$G,6,0)
 * 
 * Onde E2 contém ID_USUARIO e busca na TBL_CADASTRO
 */

/**
 * ABA: TBL_CADASTRO
 * 
 * PROPÓSITO:
 * Repositório central com dados completos de usuários.
 * Fonte de dados para as fórmulas PROCV da TBL_CENARIOS.
 * 
 * COLUNAS:
 * - SEQ: Sequencial
 * - ID_USUARIO: Chave primária (0001, 0002, ...)
 * - NOME_COMPLETO: Nome completo do usuário
 * - NOME_USUARIO: Username único
 * - EMAIL: Email único para login
 * - ID: UUID gerado após cadastro (opcional)
 * - SENHA: Senha para autenticação
 * 
 * CARACTERÍSTICAS:
 * - Dados podem ser gerados por JavaFaker
 * - Emails únicos para evitar conflitos
 * - Senhas podem ser criptografadas
 * - UUIDs preenchidos após testes de cadastro
 * 
 * EXEMPLO DE LINHA:
 * ID_USUARIO=0002, NOME_COMPLETO="Mr. Candace Lueilwitz",
 * NOME_USUARIO="Mozelle54", EMAIL="Fredrick_Quitzon3@hotmail.com",
 * SENHA="dFhGC3kHlqcywKF"
 */

/**
 * RELACIONAMENTOS:
 * 
 * TBL_CENARIOS.ID_USUARIO (chave estrangeira)
 *           ↓ PROCV
 * TBL_CADASTRO.ID_USUARIO (chave primária)
 * 
 * BENEFÍCIOS:
 * - Normalização de dados
 * - Facilidade de manutenção
 * - Reutilização de usuários
 * - Consistência de dados
 * 
 * FLUXO DE DADOS:
 * 1. Hook extrai tag @CT-XXXX do cenário
 * 2. LoginDataSheet busca linha com ID_CENARIO = "CT-XXXX"
 * 3. PROCV automaticamente popula EMAIL e SENHA
 * 4. Dados são carregados no Context
 * 5. Controllers usam dados para requisições
 */
```

---

## 🎭 **3. FEATURES GHERKIN**

### **1.usuario.feature - Cenários de Usuário**

**📍 Localização**: `src/main/resources/features/1.usuario.feature`

```gherkin
@usuario
Feature: Gerenciamento de Usuários CMS
  Como um administrador de sistema
  Eu quero gerenciar usuários CMS
  Para que eu possa controlar o acesso ao sistema

  @cadastrar @crud
  Scenario: Criar um novo usuário CMS com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    Then a resposta da API Create deve retornar o código de status 201
    And os dados do usuário na resposta de criação devem estar corretos

  @CT-1001 @api @crud
  Scenario: Realizar login com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    Then a resposta da API Login deve retornar o código de status 200
    And o corpo da resposta de login deve conter os dados do usuário
```

**📋 DOCUMENTAÇÃO DETALHADA:**

```gherkin
# ============================================================================
# FEATURE: 1.usuario.feature
# ============================================================================
#
# PROPÓSITO:
# Define cenários de teste em linguagem Gherkin para funcionalidades
# relacionadas a usuários no sistema CMS. Implementa BDD (Behavior
# Driven Development) com cenários legíveis e executáveis.
#
# ESTRUTURA:
# - Feature: descrição geral da funcionalidade
# - Background: setup comum (se necessário)
# - Scenarios: casos de teste específicos
# - Tags: organização e execução seletiva
#
# INTEGRAÇÃO:
# - UsuarioSteps.java: implementa os passos
# - UsuarioController.java: executa lógica de API
# - Planilha Excel: fornece dados de teste
# ============================================================================

# TAG PRINCIPAL: @usuario
# 
# PROPÓSITO:
# Agrupa todos os cenários relacionados a usuários.
# Permite execução seletiva de testes de usuário.
#
# USO:
# mvn test -Dcucumber.filter.tags="@usuario"
@usuario

# FEATURE: Gerenciamento de Usuários CMS
#
# ESTRUTURA BDD:
# - Como: papel do usuário (administrador)
# - Eu quero: objetivo/funcionalidade
# - Para que: benefício/justificativa
#
# BENEFÍCIOS:
# - Linguagem natural legível por não-técnicos
# - Documenta comportamento esperado
# - Liga requisitos de negócio a testes técnicos
Feature: Gerenciamento de Usuários CMS
  Como um administrador de sistema
  Eu quero gerenciar usuários CMS
  Para que eu possa controlar o acesso ao sistema

  # CENÁRIO: Cadastro de usuário
  #
  # TAGS:
  # @cadastrar: categoria funcional
  # @crud: operação Create
  #
  # ESTRUTURA:
  # - Given: condição inicial
  # - When: ação (implícita no Given)
  # - Then: resultado esperado
  # - And: validação adicional
  #
  # DADOS:
  # Vem da planilha Excel via hook automático
  @cadastrar @crud
  Scenario: Criar um novo usuário CMS com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    Then a resposta da API Create deve retornar o código de status 201
    And os dados do usuário na resposta de criação devem estar corretos

  # CENÁRIO: Login de usuário
  #
  # TAG ESPECÍFICA: @CT-1001
  # PROPÓSITO: Mapeia para dados específicos na planilha
  # RELACIONAMENTO: TBL_CENARIOS onde ID_CENARIO = "CT-1001"
  #
  # FLUXO:
  # 1. Hook extrai @CT-1001
  # 2. Carrega dados da planilha
  # 3. Given: cadastra usuário (dados da planilha)
  # 4. When: faz login (mesmas credenciais)
  # 5. Then: valida resposta
  @CT-1001 @api @crud
  Scenario: Realizar login com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    Then a resposta da API Login deve retornar o código de status 200
    And o corpo da resposta de login deve conter os dados do usuário

  # OUTROS CENÁRIOS TÍPICOS:
  # @CT-1002: Email duplicado
  # @CT-1003: Username duplicado
  # @CT-1004: Dados inválidos
  # @CT-1005: Login com sucesso
  # @CT-1006: Login inválido
  # @CT-1007: Listagem com auth
  # @CT-1008: Listagem sem auth
  # etc.
```

### **Estrutura Geral das Features**

```java
/**
 * ============================================================================
 * DIRETÓRIO: src/main/resources/features/
 * ============================================================================
 * 
 * PROPÓSITO:
 * Contém todos os arquivos .feature com cenários Gherkin.
 * Organização modular por funcionalidade do sistema.
 * 
 * ARQUIVOS:
 * - 1.usuario.feature: Gerenciamento de usuários
 * - 2.categoria.feature: Gerenciamento de categorias
 * - 3.artigos.feature: Gerenciamento de artigos
 * - login.feature: Cenários específicos de autenticação
 * 
 * CONVENÇÕES:
 * - Numeração para ordem de execução
 * - Nome descritivo da funcionalidade
 * - Extensão .feature obrigatória
 * 
 * INTEGRAÇÃO:
 * - RunnerTestApi.java: configura path das features
 * - Steps: implementam passos definidos aqui
 * - Tags: permitem execução seletiva
 * ============================================================================
 */

/**
 * PADRÕES DE TAGS:
 * 
 * FUNCIONAIS:
 * @usuario, @categoria, @artigos - agrupamento por módulo
 * @crud, @api, @login - agrupamento por operação
 * 
 * IDENTIFICAÇÃO:
 * @CT-XXXX - mapeamento para dados de planilha
 * @TEST_QAR-XXXX - identificação externa de QA
 * 
 * EXECUÇÃO:
 * @smoke - testes críticos
 * @regression - testes de regressão
 * @skip - temporariamente desabilitados
 * 
 * EXEMPLOS DE EXECUÇÃO:
 * mvn test -Dcucumber.filter.tags="@usuario"
 * mvn test -Dcucumber.filter.tags="@CT-1002"
 * mvn test -Dcucumber.filter.tags="@api and @crud"
 * mvn test -Dcucumber.filter.tags="@smoke or @regression"
 */

/**
 * MELHORES PRÁTICAS:
 * 
 * NOMEAÇÃO:
 * - Cenários descritivos e únicos
 * - Verbos no infinitivo para ações
 * - Resultado esperado claro
 * 
 * ESTRUTURA:
 * - Um conceito por cenário
 * - Passos reutilizáveis
 * - Dados parametrizáveis
 * 
 * MANUTENÇÃO:
 * - Revisar regularmente
 * - Sincronizar com requisitos
 * - Atualizar quando API muda
 */
```

---

## 📄 **4. TEMPLATES DE EVIDÊNCIAS**

### **Templates de Evidências DOCX**

**📍 Localização**: `src/main/resources/`

```java
/**
 * ============================================================================
 * TEMPLATES: Evidências de Teste
 * ============================================================================
 * 
 * PROPÓSITO:
 * Templates padronizados para geração automática de evidências de teste.
 * Cada template é específico para um tipo de teste e contém estrutura
 * pré-definida para documentação de resultados.
 * 
 * ARQUIVOS DISPONÍVEIS:
 * - Evidência de Teste-nome cliente-nome projeto-ItemTeste.docx
 * - Evidencia Modelo.docx (template genérico)
 * - Evidencia Modelo Mobile.docx (específico para mobile)
 * - Evidencia Modelo WEB.docx (específico para web)
 * - Evidencia Modelo API.docx (específico para API)
 * 
 * CARACTERÍSTICAS:
 * - Formatação profissional padronizada
 * - Campos marcadores para substituição automática
 * - Seções estruturadas (objetivo, steps, resultados)
 * - Compatibilidade com geradores automáticos
 * ============================================================================
 */

/**
 * TEMPLATE: Evidencia Modelo API.docx
 * 
 * PROPÓSITO:
 * Template específico para evidências de testes de API.
 * Contém seções otimizadas para documentar requisições HTTP,
 * payloads, responses e validações.
 * 
 * SEÇÕES TÍPICAS:
 * 1. Cabeçalho com informações do projeto
 * 2. Identificação do caso de teste
 * 3. Objetivo do teste
 * 4. Pré-condições
 * 5. Dados de entrada
 * 6. Passos executados
 * 7. Requisições HTTP (method, URL, headers, body)
 * 8. Respostas HTTP (status, headers, body)
 * 9. Validações realizadas
 * 10. Resultado (PASSOU/FALHOU)
 * 11. Evidências (screenshots se aplicável)
 * 12. Observações
 * 
 * MARCADORES PARA SUBSTITUIÇÃO:
 * {{NOME_PROJETO}} - Nome do projeto
 * {{ID_CASO_TESTE}} - Identificador do caso
 * {{NOME_CENARIO}} - Nome do cenário
 * {{DATA_EXECUCAO}} - Data/hora da execução
 * {{ENDPOINT}} - URL do endpoint testado
 * {{METODO_HTTP}} - GET, POST, PUT, DELETE
 * {{REQUEST_HEADERS}} - Headers da requisição
 * {{REQUEST_BODY}} - Payload enviado
 * {{RESPONSE_STATUS}} - Código de status retornado
 * {{RESPONSE_HEADERS}} - Headers da resposta
 * {{RESPONSE_BODY}} - Payload retornado
 * {{RESULTADO}} - PASSOU/FALHOU
 * {{OBSERVACOES}} - Comentários adicionais
 * 
 * INTEGRAÇÃO:
 * - GeradorDocxApi.java: usa este template
 * - HooksEvidenciasApi.java: trigger de geração
 * - EvidenceFilter.java: captura dados para preenchimento
 */

/**
 * TEMPLATE: Evidencia Modelo WEB.docx
 * 
 * PROPÓSITO:
 * Template para evidências de testes web (Selenium).
 * Inclui seções para screenshots, elementos interagidos,
 * validações visuais e fluxos de navegação.
 * 
 * SEÇÕES ESPECÍFICAS WEB:
 * - URL da página testada
 * - Browser e versão utilizada
 * - Screenshots antes/durante/depois
 * - Elementos interagidos (CSS selectors)
 * - Ações realizadas (click, type, select)
 * - Validações visuais
 * - Performance (tempo de carregamento)
 */

/**
 * TEMPLATE: Evidencia Modelo Mobile.docx
 * 
 * PROPÓSITO:
 * Template para evidências de testes mobile (Appium).
 * Adaptado para documentar interações em dispositivos móveis,
 * gestos, orientações e especificidades mobile.
 * 
 * SEÇÕES ESPECÍFICAS MOBILE:
 * - Dispositivo e OS testado
 * - App e versão
 * - Screenshots de telas
 * - Gestos realizados (tap, swipe, pinch)
 * - Orientações testadas
 * - Permissões de app
 * - Performance mobile
 */
```

### **Uso dos Templates**

```java
/**
 * ============================================================================
 * INTEGRAÇÃO: Como os Templates são Utilizados
 * ============================================================================
 * 
 * FLUXO DE GERAÇÃO DE EVIDÊNCIAS:
 * 
 * 1. DURANTE O TESTE:
 * - EvidenceFilter captura dados de requisições
 * - PDFLoggerFilter gera evidências em PDF
 * - Screenshots são capturadas (se web/mobile)
 * 
 * 2. NO HOOK @After:
 * - HooksEvidenciasApi é executado
 * - Dados capturados são consolidados
 * - Template apropriado é selecionado
 * 
 * 3. GERAÇÃO:
 * - GeradorDocxApi substitui marcadores
 * - Insere dados reais do teste
 * - Adiciona imagens se necessário
 * - Salva arquivo final com timestamp
 * 
 * 4. ANEXAÇÃO:
 * - Arquivo gerado é anexado ao relatório Cucumber
 * - Disponível para download/análise
 * 
 * EXEMPLO DE CÓDIGO:
 * ```java
 * // No HooksEvidenciasApi
 * String templatePath = "src/main/resources/Evidencia Modelo API.docx";
 * String outputPath = "evidence/" + idEvidencia + "_" + timestamp + ".docx";
 * 
 * GeradorDocxApi gerador = new GeradorDocxApi(templatePath);
 * gerador.substituirMarcador("{{ID_CASO_TESTE}}", idEvidencia);
 * gerador.substituirMarcador("{{NOME_CENARIO}}", nomeCenario);
 * gerador.substituirMarcador("{{REQUEST_BODY}}", requestBody);
 * gerador.substituirMarcador("{{RESPONSE_STATUS}}", statusCode);
 * gerador.salvar(outputPath);
 * 
 * // Anexar ao relatório
 * scenario.attach(Files.readAllBytes(Paths.get(outputPath)), 
 *                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
 *                "Evidencia_" + idEvidencia);
 * ```
 * ============================================================================
 */

/**
 * PERSONALIZAÇÃO DOS TEMPLATES:
 * 
 * ADICIONANDO NOVOS MARCADORES:
 * 1. Editar template DOCX
 * 2. Inserir {{NOVO_MARCADOR}} onde necessário
 * 3. Atualizar gerador para substituir marcador
 * 4. Passar dados no momento da geração
 * 
 * CRIANDO NOVOS TEMPLATES:
 * 1. Copiar template existente mais similar
 * 2. Adaptar seções conforme necessário
 * 3. Definir marcadores específicos
 * 4. Testar geração com dados reais
 * 5. Integrar ao código de geração
 * 
 * BOAS PRÁTICAS:
 * - Manter formatação consistente
 * - Usar nomes de marcadores claros
 * - Testar em diferentes versões do Word
 * - Validar acessibilidade dos documentos
 * - Considerar tamanho final dos arquivos
 */
```

---

## 📋 **RESUMO DOS RESOURCES**

### **🗂️ Estrutura Completa:**

```
src/main/resources/
├── 📁 data/                                    # Dados de teste
│   ├── 📊 MassaDadosCMS.xlsx                  # Planilha principal
│   ├── 📊 MassaDados.xlsx                     # Planilha auxiliar
│   └── 📄 ESTRUTURA_PLANILHA_EXCEL.md        # Documentação
├── 📁 features/                               # Cenários Gherkin
│   ├── 🎭 1.usuario.feature                   # Testes de usuário
│   ├── 🎭 2.categoria.feature                 # Testes de categoria
│   ├── 🎭 3.artigos.feature                   # Testes de artigos
│   └── 🎭 login.feature                       # Testes de login
├── ⚙️ test.properties                         # Configurações de teste
├── 📝 log4j2.xml                             # Configuração de logs
└── 📄 Templates DOCX                          # Evidências padronizadas
    ├── Evidencia Modelo API.docx
    ├── Evidencia Modelo WEB.docx
    ├── Evidencia Modelo Mobile.docx
    └── Evidencia Modelo.docx
```

### **🎯 Funcionalidades Principais:**

1. **⚙️ Configuração Flexível**: Properties permitem ajustar comportamento sem recompilação
2. **📊 Data-Driven Testing**: Planilhas Excel centralizam dados de teste
3. **🎭 BDD com Gherkin**: Features legíveis por stakeholders não-técnicos
4. **📝 Logging Robusto**: Sistema completo com rotação e múltiplos destinos
5. **📄 Evidências Automáticas**: Templates profissionais para documentação

### **✅ Benefícios da Estrutura:**

- **🔧 Manutenibilidade**: Configurações centralizadas e documentadas
- **📈 Escalabilidade**: Fácil adição de novos cenários e dados
- **🛡️ Segurança**: Mascaramento configurável de dados sensíveis
- **📊 Rastreabilidade**: Logs detalhados e evidências automáticas
- **🤝 Colaboração**: Gherkin facilita comunicação entre equipes
- **📋 Compliance**: Templates profissionais para auditoria

**🎓 Material completo dos RESOURCES para seu caderno!** 📚✨

