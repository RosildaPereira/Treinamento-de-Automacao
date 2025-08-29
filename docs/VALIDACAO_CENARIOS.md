# 🔍 **Validação Dinâmica de Nomes de Cenários**

## 🎯 **Objetivo**
Garantir que os nomes dos cenários na planilha Excel correspondam exatamente aos nomes definidos na feature, evitando execução de testes errados que podem "matar" a massa de outros cenários.

## ⚠️ **Problema**
Se o nome do cenário na planilha estiver diferente do nome na feature, o teste pode executar com dados errados e:
- ❌ Usar massa de outro cenário
- ❌ Executar teste diferente do esperado
- ❌ Causar falhas em cascata

## ✅ **Solução Dinâmica**
O sistema agora **lê automaticamente** os nomes dos cenários da feature e valida dinamicamente, sem necessidade de mapeamento manual.

## 🔄 **Como Funciona (Dinâmico)**

### **1. Leitura Automática da Feature**
O sistema lê o arquivo `1.usuario.feature` e extrai automaticamente:
- IDs dos cenários (`@CT-1001`, `@CT-1002`, etc.)
- Nomes dos cenários (`Scenario: Nome do cenário`)

### **2. Validação Automática**
Quando um teste é executado:
- Sistema carrega dados da `TBL_CENARIOS`
- Compara `NOME_CENARIO` da planilha com o nome da feature
- **FALHA** se não corresponder

### **3. Suporte a Novos Cenários**
Para adicionar novos cenários (ex: `CT-1016`):
1. **Adicione na feature:**
   ```gherkin
   @CT-1016 @api
   Scenario: Validar exclusão com token ok
     Given que envio uma solicitação 'POST' de registro de usuario CMS
     When eu realizo o login com as credenciais válidas do usuário criado
     And envio uma solicitação de DELETE para o ID
     Then a resposta da API Delete deve retornar o código de status 204
   ```

2. **Adicione na planilha:**
   ```csv
   ID_CENARIO,NOME_CENARIO,ID_MASSA,EMAIL,SENHA
   CT-1016,Validar exclusão com token ok,16,usuario16@exemplo.com,senha123
   ```

3. **Sistema valida automaticamente!** ✅

## 📋 **Estrutura da Planilha**

### **TBL_CENARIOS** (Obrigatório)
| Campo | Descrição | Exemplo |
|-------|-----------|---------|
| ID_CENARIO | ID do cenário | CT-1001 |
| NOME_CENARIO | **Nome exato da feature** | Criar um novo usuário CMS com sucesso |
| ID_MASSA | ID da massa | 1 |
| EMAIL | Email para teste | usuario@exemplo.com |
| SENHA | Senha para teste | senha123 |

### **TBL_CADASTRO** (Complementar)
| Campo | Descrição | Exemplo |
|-------|-----------|---------|
| ID_MASSA | ID da massa | 1 |
| NOME_COMPLETO | Nome completo | João Silva Santos |
| NOME_USUARIO | Nome de usuário | joao.silva.santos |
| EMAIL | Email | joao.silva.santos@exemplo.com |
| SENHA | Senha | P@ssw0rd123 |
| ID_USUARIO | ID real da API | 12345678-1234-1234-1234-123456789abc |

## 🚨 **Exemplo de Erro**

Se na planilha estiver:
```
ID_CENARIO: CT-1001
NOME_CENARIO: Criar usuario CMS  ← ❌ Nome errado
```

O sistema vai retornar:
```
❌ ERRO DE VALIDAÇÃO: Nome do cenário na planilha não corresponde ao esperado!
   🆔 ID Cenário: CT-1001
   📋 Nome na Planilha: 'Criar usuario CMS'
   ✅ Nome Esperado: 'Criar um novo usuário CMS com sucesso'
   💡 Corrija o nome na planilha para evitar execução de teste errado!
```

## ✅ **Exemplo de Sucesso**

Se na planilha estiver:
```
ID_CENARIO: CT-1001
NOME_CENARIO: Criar um novo usuário CMS com sucesso  ← ✅ Nome correto
```

O sistema vai retornar:
```
✅ Validação de cenário: Nome correto para CT-1001
```

## 🛠️ **Como Funciona**

1. **Carregamento**: Quando um cenário é executado, o sistema carrega os dados da `TBL_CENARIOS`
2. **Leitura Dinâmica**: Sistema lê automaticamente os nomes da feature
3. **Validação**: O sistema verifica se `NOME_CENARIO` corresponde ao nome esperado
4. **Erro**: Se não corresponder, o teste **FALHA** com mensagem clara
5. **Sucesso**: Se corresponder, o teste continua normalmente

## 💡 **Benefícios**

- ✅ **Dinâmico**: Não precisa atualizar código para novos cenários
- ✅ **Automático**: Lê nomes diretamente da feature
- ✅ **Segurança**: Evita execução de testes errados
- ✅ **Rastreabilidade**: Identifica problemas na planilha
- ✅ **Manutenibilidade**: Facilita correção de dados
- ✅ **Confiabilidade**: Garante que o teste certo seja executado

## 🔧 **Implementação**

A validação é feita automaticamente em:
- `LoginDataSheet.java` - Ao carregar dados da `TBL_CENARIOS`
- `CenarioValidator.java` - Classe utilitária de validação dinâmica
- `TesteCenarioValidator.java` - Runner para testar a validação

## 🧪 **Testar Validação**

Para testar a validação dinâmica:
```bash
mvn exec:java -Dexec.mainClass="org.br.com.test.utils.TesteCenarioValidator"
```

**Não é necessário fazer nada manualmente!** O sistema valida automaticamente e suporta novos cenários dinamicamente. 🎉 


Efetue a documentação com TODO: e como comentario /**/ para cada metodo das classes e das lihha mais importantes da classes e de que classes ele esta sendo instanciado vai ser, para ficar como lembrete. E tudo isso escrito em portugues Brasil

Efetue a documentação como JavaDoc e como comentario /**/ para cada metodo das classes e das lihha mais importantes da classes e de que classes ele esta sendo instanciado vai ser, para ficar como lembrete. E tudo isso escrito em portugues Brasil

Efetue a documentação como JavaDoc com TODO: para cada metodo das classes e das lihha mais importantes da classes e de que classes ele esta sendo instanciado vai ser, para ficar como lembrete. E tudo isso escrito em portugues Brasil