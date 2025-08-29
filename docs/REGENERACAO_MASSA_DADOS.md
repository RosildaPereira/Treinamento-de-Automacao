# 🔄 Sistema de Regeneração de Massa de Dados

## 🎯 **Problema Identificado**

Após excluir um usuário, os testes subsequentes que tentam fazer login com as credenciais do usuário excluído falham com erro "Email ou senha inválidos". Isso é esperado, mas quebra o fluxo dos testes.

## 🎯 **Melhorias Implementadas**

### **1. Senhas Mais Robustas**
O sistema agora gera senhas mais seguras e padronizadas:

**Padrões de senha:**
- `@Pass123`, `P@ss321`
- `Password@321`, `Passowrd123@`
- `@Senha123`, `S@nha321`
- `Test@123`, `T@st321`
- `@User123`, `U@ser321`
- `@Admin123`, `A@dmin321`
- `@Secure123`, `S@cure321`
- `@Strong123`, `S@trong321`

**Variação automática:** Números aleatórios (100-999) substituem "123"

### **2. Uso de Dados da Planilha sem Cadastro**
O método `carregarDadosDaPlanilha()` agora:
- ✅ **Não cadastra** usuários novos
- ✅ **Usa apenas** dados da planilha existente
- ✅ **Salva dados** no contexto para uso posterior
- ✅ **Logs claros** indicando origem dos dados

**Logs esperados:**
```
📋 Usando dados da TBL_CADASTRO da planilha
📋 Dados carregados da planilha:
   👤 Nome: Ernest Klein
   🔖 Usuário: Jakayla.Bosco19
   📧 Email: Kira.Rodriguez15@yahoo.com
   🔑 Senha: ***iCTL
ℹ️ Usuário não cadastrado - usando dados existentes da planilha
```

### **3. Comportamento do Teste de Exclusão**
O teste `CT-1013` agora:
- ✅ **Carrega dados** da planilha (não cadastra)
- ✅ **Tenta fazer login** com dados existentes
- ✅ **Se login falhar** (usuário excluído), o teste **falha**
- ✅ **Indica necessidade** de atualizar a planilha

**Fluxo esperado:**
1. **Carrega dados** da planilha
2. **Tenta login** com credenciais
3. **Se falhar**: Teste falha (usuário não existe)
4. **Se passar**: Continua para exclusão
5. **Após exclusão**: Próxima execução falhará no login

**Benefício:** Você sabe exatamente quando precisa atualizar a planilha!

## ✅ **Solução Implementada**

### **1. Detecção Automática de Falha de Login**

O sistema detecta quando o login falha após exclusão de usuário e informa que precisa de nova massa:

```java
if (responseBody.contains("Email ou senha inválidos") || 
    responseBody.contains("credenciais inválidas") ||
    response.getStatusCode() == 401) {
    
    LogFormatter.logStep("❌ Login falhou - Credenciais inválidas");
    LogFormatter.logStep("📋 NECESSÁRIO: Gerar nova massa de dados para este cenário");
    LogFormatter.logStep("💡 Use o comando: mvn exec:java -Dexec.mainClass=\"org.br.com.test.utils.massas.GeradorMassaRunner\"");
    LogFormatter.logStep("📁 Os dados serão salvos em: output/fakerCsv.csv");
}
```

### **2. Gerador Manual de Massa com Cadastro Real**

Quando necessário, use o comando para gerar nova massa com cadastro real na API:

```bash
mvn exec:java -Dexec.mainClass="org.br.com.test.utils.massas.GeradorMassaRunner"
```

**O gerador agora:**
- ✅ **Cadastra usuários reais** na API
- ✅ **Extrai IDs reais** retornados pela API
- ✅ **Salva dados autênticos** no CSV
- ✅ **Garante compatibilidade** com o banco de dados
- ✅ **Corrige caracteres especiais** (acentos) com UTF-8
- ✅ **CSV simplificado** sem colunas desnecessárias

### **3. Arquivo CSV Gerado com Dados Reais**

O arquivo `output/fakerCsv.csv` é criado com dados reais da API:

```csv
NOME_COMPLETO,NOME_USUARIO,EMAIL,SENHA,ID_USUARIO,DATA_GERACAO
Davi Miguel Garcês Jr.,davi.miguel.garces,davi.miguel.garces@example.com,P@ssw0rd123,12345678-1234-1234-1234-123456789abc,2025-07-25 21:30:45
```

**Colunas removidas:** `ID_MASSA`, `SEQ`
**Caracteres especiais:** Corrigidos com codificação UTF-8

### **3. Validar Massas Geradas**
Após gerar as massas, valide se estão funcionando:
```bash
mvn exec:java -Dexec.mainClass="org.br.com.test.utils.massas.ValidadorMassaRunner"
```

**O validador:**
- ✅ **Testa login** para cada massa gerada
- ✅ **Atualiza o CSV** com status de validação
- ✅ **Adiciona colunas**: `LOGIN_VALIDADO` e `DATA_VALIDACAO`
- ✅ **Preserva dados** existentes no CSV

**Saída esperada:**
```
🔍 Validador de Massa de Dados
===============================
📊 Total de massas encontradas: 5
🔍 Iniciando validação...
================================================
📋 Validando massa 1/5:
   👤 Nome: João Silva Santos
   🔖 Usuário: joao.silva.santos
   📧 Email: joao.silva.santos@example.com
   🔑 Senha: P@ssw0rd123
   🆔 ID: 12345678-1234-1234-1234-123456789abc
   ✅ LOGIN SUCESSO - Usuário válido!
   ---
================================================
📊 RESULTADO DA VALIDAÇÃO:
   ✅ Sucessos: 5
   ❌ Falhas: 0
   📈 Taxa de sucesso: 100.0%
📁 CSV atualizado com status de validação!
🎉 TODAS as massas estão válidas!
```

**CSV Atualizado:**
```csv
NOME_COMPLETO,NOME_USUARIO,EMAIL,ID_USUARIO,SENHA,DATA_GERACAO,LOGIN_VALIDADO,DATA_VALIDACAO
João Silva Santos,joao.silva.santos,joao.silva.santos@example.com,12345678-1234-1234-1234-123456789abc,P@ssw0rd123,2025-07-25 21:30:45,true,2025-07-25 22:45:12
Maria Silva Santos,maria.silva.santos,maria.silva.santos@example.com,87654321-4321-4321-4321-cba987654321,P@ssw0rd456,2025-07-25 21:31:15,false,2025-07-25 22:45:15
```

**Se houver falhas:**
```
⚠️ ATENÇÃO: 3 massas falharam na validação!
💡 Recomendação: Gerar nova massa com dados frescos
```

### **4. Verificar Arquivo CSV Gerado**
Verifique o arquivo `output/fakerCsv.csv`

### **5. Atualizar Planilha**
Copie os dados do CSV para a planilha Excel nas tabelas:
- **TBL_CENARIOS**: Atualizar EMAIL e SENHA
- **TBL_CADASTRO**: Adicionar nova linha com dados completos

## 📋 **Estrutura do CSV Gerado com Dados Reais**

| Campo | Descrição | Exemplo |
|-------|-----------|---------|
| NOME_COMPLETO | Nome completo com acentos | Davi Miguel Garcês Jr. |
| NOME_USUARIO | Nome de usuário único | davi.miguel.garces |
| EMAIL | Email único gerado | davi.miguel.garces@example.com |
| ID_USUARIO | **ID REAL da API** | 12345678-1234-1234-1234-123456789abc |
| SENHA | Senha válida | P@ssw0rd123 |
| DATA_GERACAO | Data/hora da geração | 2025-07-25 21:30:45 |
| LOGIN_VALIDADO | Status da validação (true/false) | true |
| DATA_VALIDACAO | Data/hora da validação | 2025-07-25 22:45:12 |

## ⚙️ **Configurações**

### **Quantidade de Registros**
No arquivo `GeradorMassaRunner.java`, linha 25:
```java
private static final int QUANTIDADE_REGISTROS = 5; // ← Mude aqui a quantidade
```

### **Comportamento de Acumulação**
O sistema **acrescenta** novos registros ao arquivo existente:

**Primeira execução:**
```csv
NOME_COMPLETO,NOME_USUARIO,EMAIL,SENHA,ID_USUARIO,DATA_GERACAO
Davi Miguel Garcês Jr.,davi.miguel.garces,davi.miguel.garces@example.com,P@ssw0rd123,12345678-1234-1234-1234-123456789abc,2025-07-25 21:30:45
```

**Segunda execução (acrescenta):**
```csv
NOME_COMPLETO,NOME_USUARIO,EMAIL,SENHA,ID_USUARIO,DATA_GERACAO
Davi Miguel Garcês Jr.,davi.miguel.garces,davi.miguel.garces@example.com,P@ssw0rd123,12345678-1234-1234-1234-123456789abc,2025-07-25 21:30:45
João Silva Santos,joao.silva.santos,joao.silva.santos@example.com,P@ssw0rd456,87654321-4321-4321-4321-cba987654321,2025-07-26 10:15:30
```

**Benefícios:**
- ✅ **Histórico completo**: Todos os registros em um só arquivo
- ✅ **Identificação por data**: Cada registro tem timestamp único
- ✅ **Não perde dados**: Execuções anteriores são preservadas
- ✅ **Fácil importação**: Um arquivo com toda a massa gerada

### **Adicionar Novas Colunas**
Se precisar adicionar novas colunas, modifique:
1. **Linha 35**: Adicione a coluna no cabeçalho
2. **Linha 75**: Adicione o campo na formatação do CSV
3. **Comentários**: Veja as posições marcadas no código

## 🎯 **Benefícios**

- ✅ **Dados Reais**: IDs extraídos diretamente da API
- ✅ **Compatibilidade**: Dados 100% compatíveis com o banco
- ✅ **Cadastro Real**: Usuários realmente criados na API
- ✅ **Caracteres Corretos**: Acentos e caracteres especiais preservados
- ✅ **CSV Simplificado**: Sem colunas desnecessárias
- ✅ **Configurável**: Quantidade de registros ajustável
- ✅ **Rastreabilidade**: Mantém histórico de geração

## 🚀 **Próximos Passos**

1. **Execute os testes** que fazem exclusão
2. **Quando aparecer a mensagem**, execute o gerador
3. **Verifique o arquivo CSV** gerado com dados reais
4. **Atualize a planilha** com os novos dados
5. **Continue os testes** com dados autênticos

O sistema agora gera **dados reais e autênticos** da API com **caracteres especiais corretos**! 🎉✨ 

## 🔧 **Como Usar**

### **1. Executar Testes Normalmente**
```bash
mvn test -Dcucumber.filter.tags="@CT-1013"
```

### **2. Quando Aparecer Falha de Login**
O sistema avisará sobre credenciais inválidas:
```
❌ Login falhou - Credenciais inválidas
📋 ATENÇÃO: Usuário não encontrado ou credenciais incorretas
```

### **3. Gerar Nova Massa (Quando Necessário)**
Execute o gerador manualmente:
```bash
mvn exec:java -Dexec.mainClass="org.br.com.test.utils.massas.GeradorMassaRunner"
```

### **4. Validar Massas Geradas**
Teste se as massas estão funcionando:
```bash
mvn exec:java -Dexec.mainClass="org.br.com.test.utils.massas.ValidadorMassaRunner"
```

### **5. Verificar Arquivo CSV Gerado**
Verifique o arquivo `output/fakerCsv.csv`

### **6. Atualizar Planilha**
Copie os dados do CSV para a planilha Excel nas tabelas:
- **TBL_CENARIOS**: Atualizar EMAIL e SENHA
- **TBL_CADASTRO**: Adicionar nova linha com dados completos 