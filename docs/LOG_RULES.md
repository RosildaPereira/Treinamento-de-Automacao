# 📋 Regras e Boas Práticas - Sistema de Logs

## 🎯 Objetivo

Este documento define as regras, padrões e boas práticas para implementação e uso do sistema de logs em projetos de automação.

## 📝 Regras Obrigatórias

### 1. Estrutura de Logs

#### ✅ **OBRIGATÓRIO**
```java
// Sempre usar LogFormatter para logs estruturados
LogFormatter.logStep("Mensagem do passo");
LogFormatter.logError("Mensagem de erro");
LogFormatter.logSuccess("Mensagem de sucesso");
LogFormatter.logWarning("Mensagem de aviso");
```

#### ❌ **PROIBIDO**
```java
// NÃO usar System.out.println diretamente
System.out.println("Mensagem");

// NÃO usar logger diretamente sem formatação
logger.info("Mensagem");
```

### 2. Identificação de Cenários

#### ✅ **OBRIGATÓRIO**
- Todo cenário deve ter um ID único (UUID)
- Usar Context.startContext() no início de cada cenário
- Usar Context.clearContext() no final de cada cenário

#### ❌ **PROIBIDO**
- Executar cenários sem identificação única
- Manter contexto entre cenários

### 3. Formatação de Mensagens

#### ✅ **PADRÃO CORRETO**
```java
// Passos de teste
LogFormatter.logStep("Enviando requisição POST para /api/users");

// Validações
LogFormatter.logStep("Validando status code: 201");

// Resultados
LogFormatter.logSuccess("PASSED");
LogFormatter.logError("FAILED - Status code esperado: 200, recebido: 500");
```

#### ❌ **PADRÃO INCORRETO**
```java
// Mensagens muito genéricas
LogFormatter.logStep("Fazendo teste");

// Mensagens sem contexto
LogFormatter.logStep("OK");

// Mensagens em inglês misturado
LogFormatter.logStep("Sending request to endpoint");
```

### 4. Logs de Requisições HTTP

#### ✅ **OBRIGATÓRIO**
```java
// Sempre logar método, URL e body (se aplicável)
LogFormatter.logRequest("POST", "/api/users", requestBody);

// Sempre logar status code e response
LogFormatter.logResponse(201, responseBody);
```

#### ❌ **PROIBIDO**
```java
// Logar dados sensíveis (senhas, tokens)
LogFormatter.logStep("Password: " + password);

// Logar dados muito grandes (mais de 1000 caracteres)
LogFormatter.logStep("Response: " + veryLargeResponse);
```

## 🔧 Padrões de Implementação

### 1. Step Definitions

#### ✅ **ESTRUTURA CORRETA**
```java
@Given("que eu tenho um endpoint configurado")
public void queEuTenhoUmEndpointConfigurado() {
    LogFormatter.logStep("Configurando endpoint para teste");
    
    try {
        // Implementação do step
        LogFormatter.logStep("Endpoint configurado com sucesso");
    } catch (Exception e) {
        LogFormatter.logError("Erro ao configurar endpoint: " + e.getMessage());
        throw e;
    }
}
```

#### ❌ **ESTRUTURA INCORRETA**
```java
@Given("que eu tenho um endpoint configurado")
public void queEuTenhoUmEndpointConfigurado() {
    // Sem logs
    // Implementação sem tratamento de erro
}
```

### 2. Validações

#### ✅ **PADRÃO CORRETO**
```java
@Then("eu recebo status code {int}")
public void euReceboStatusCode(int expectedStatusCode) {
    LogFormatter.logStep("Validando status code: " + expectedStatusCode);
    
    if (actualStatusCode == expectedStatusCode) {
        LogFormatter.logSuccess("PASSED");
    } else {
        LogFormatter.logError("FAILED - Esperado: " + expectedStatusCode + 
                            ", Recebido: " + actualStatusCode);
        throw new AssertionError("Status code não confere");
    }
}
```

### 3. Tratamento de Erros

#### ✅ **PADRÃO CORRETO**
```java
try {
    // Operação que pode falhar
    LogFormatter.logStep("Executando operação crítica");
    
} catch (Exception e) {
    LogFormatter.logError("Erro na operação: " + e.getMessage());
    // Log detalhado para debugging
    LogFormatter.logStep("Stack trace: " + e.getStackTrace()[0]);
    throw e;
}
```

## 📊 Níveis de Log

### 1. INFO (Padrão)
- Passos de teste
- Validações
- Configurações
- Resultados de sucesso

### 2. WARN (Avisos)
- Configurações padrão sendo usadas
- Timeouts próximos do limite
- Dados opcionais não fornecidos

### 3. ERROR (Erros)
- Falhas de validação
- Exceções capturadas
- Timeouts excedidos
- Problemas de configuração

### 4. DEBUG (Desenvolvimento)
- Dados internos
- Valores de variáveis
- Detalhes de implementação

## 🎨 Formatação Visual

### 1. Separadores
```java
// Início de execução
LogFormatter.logStep("==================================== Execucao: " + data + " ====================================");

// Início de cenário
LogFormatter.logStep("+-- Nome do Cenário");

// Passos
LogFormatter.logStep("| '-- Passo do teste");

// Fim de cenário
LogFormatter.logStep("'-- PASSED/FAILED");

// Resumo
LogFormatter.logStep("========================================== Resumo da Execucao ==========================================");
```

### 2. Emojis e Símbolos
```java
// Sucesso
LogFormatter.logSuccess("✅ PASSED");

// Erro
LogFormatter.logError("❌ FAILED");

// Aviso
LogFormatter.logWarning("⚠️ AVISO");

// Informação
LogFormatter.logStep("ℹ️ Informação importante");
```

## 🔒 Segurança

### 1. Dados Sensíveis
#### ✅ **PERMITIDO**
```java
// Logar IDs, URLs, status codes
LogFormatter.logStep("User ID: " + userId);
LogFormatter.logStep("Endpoint: " + endpoint);
```

#### ❌ **PROIBIDO**
```java
// Logar senhas, tokens, dados pessoais
LogFormatter.logStep("Password: " + password);
LogFormatter.logStep("Token: " + authToken);
LogFormatter.logStep("CPF: " + cpf);
```

### 2. Sanitização
```java
// Antes de logar, sempre sanitizar dados sensíveis
String sanitizedBody = body.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"***\"");
LogFormatter.logStep("Request body: " + sanitizedBody);
```

## 📈 Performance

### 1. Otimizações
- Evitar logs em loops grandes
- Usar StringBuilder para mensagens complexas
- Não logar dados muito grandes

### 2. Exemplo de Otimização
```java
// ❌ RUIM - Log em loop
for (int i = 0; i < 1000; i++) {
    LogFormatter.logStep("Processando item " + i);
}

// ✅ BOM - Log resumido
LogFormatter.logStep("Processando 1000 itens...");
for (int i = 0; i < 1000; i++) {
    // Processamento sem log
}
LogFormatter.logStep("Processamento concluído");
```

## 🧪 Testes

### 1. Validação de Logs
```java
@Test
public void testLogFormatting() {
    // Capturar output do log
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    
    LogFormatter.logStep("Teste de log");
    
    String logOutput = output.toString();
    assertTrue(logOutput.contains("[INFO]"));
    assertTrue(logOutput.contains("| '-- Teste de log"));
}
```

### 2. Cobertura de Logs
- Todo step deve ter pelo menos um log
- Todo erro deve ser logado
- Todo resultado deve ser logado

## 🔄 Manutenção

### 1. Revisão Periódica
- Revisar logs mensalmente
- Identificar padrões de erro
- Otimizar mensagens muito frequentes

### 2. Atualização de Padrões
- Manter documentação atualizada
- Treinar novos membros da equipe
- Revisar regras trimestralmente

## 📋 Checklist de Conformidade

### Para Desenvolvedores
- [ ] Todos os steps têm logs apropriados
- [ ] Erros são logados com contexto
- [ ] Dados sensíveis não são logados
- [ ] Mensagens seguem padrão estabelecido
- [ ] Context é iniciado e limpo corretamente

### Para Code Review
- [ ] Verificar conformidade com regras de log
- [ ] Validar formatação das mensagens
- [ ] Confirmar tratamento de erros
- [ ] Verificar segurança dos dados logados
- [ ] Validar performance dos logs

### Para Testes
- [ ] Logs aparecem na execução
- [ ] Formatação está correta
- [ ] IDs únicos são gerados
- [ ] Context é gerenciado adequadamente
- [ ] Relatórios são gerados

## 🎯 Benefícios das Regras

1. **Consistência**: Logs padronizados em todo o projeto
2. **Debugging**: Facilita identificação de problemas
3. **Manutenibilidade**: Código mais organizado e legível
4. **Segurança**: Proteção de dados sensíveis
5. **Performance**: Logs otimizados e eficientes
6. **Colaboração**: Equipe alinhada com padrões

## 📞 Suporte

Para dúvidas sobre implementação ou sugestões de melhorias:
- Criar issue no repositório
- Consultar documentação técnica
- Solicitar revisão de código
- Participar de treinamentos da equipe 