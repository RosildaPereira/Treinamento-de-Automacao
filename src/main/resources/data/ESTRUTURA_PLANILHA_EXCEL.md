# Estrutura da Planilha MassaDadosCMS.xlsx

## Tabelas Criadas

### 1. TBL_CENARIOS (PRINCIPAL)
Tabela principal que contém todos os dados necessários através de PROCV da TBL_CADASTRO.

| SEQ | ID_CENARIO | NOME | FEATURE | ID_USUARIO | EMAIL | SENHA |
|-----|------------|------|---------|------------|-------|-------|
| 1   | CT-1001    | Criar um novo usuário CMS com sucesso | usuario | 0001 | Dagmar.Mitchell25@hotmail.com | nWJzWZhqf5ZooiA |
| 2   | CT-1002    | Tentar criar um usuário com e-mail já existente | usuario | 0002 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF |
| 3   | CT-1003    | Tentar criar um usuário com nome de usuário já existente | usuario | 0003 | Ashly.Rath@gmail.com | Vp6mdcGBLMJ6P3Y |
| 4   | CT-1004    | Tentar criar um usuário com dados inválidos | usuario | 0004 | Jamel52@hotmail.com | wcSFOMJNXAgb7Mv |
| 5   | CT-1005    | Realizar login com sucesso | usuario | 0005 | Meaghan48@hotmail.com | ryRCq04bQs4PLpq |
| 6   | CT-1006    | Realizar login com dados invalidos | usuario | 0006 | Imelda.Oberbrunner98@yahoo.com | yuxZmbMvp8XGjZV |
| 7   | CT-1007    | Buscar a lista de usuários CMS com autenticação | usuario | 0007 | Landen_Greenholt@yahoo.com | ioRa43yC2pMVewi |

### 2. TBL_CADASTRO (FONTE DOS DADOS)
Tabela fonte com dados completos dos usuários (usada pelo PROCV).

| SEQ | ID_USUARIO | NOME_COMPLETO | NOME_USUARIO | EMAIL | ID | SENHA |
|-----|------------|---------------|--------------|-------|----|-------|
| 1   | 0001       | Glenda Mertz  | Ceasar.Ward71 | Dagmar.Mitchell25@hotmail.com | b3ee84a9-8789-42ad-a108-8dbd00609c22 | nWJzWZhqf5ZooiA |
| 2   | 0002       | Mr. Candace Lueilwitz | Mozelle54 | Fredrick_Quitzon3@hotmail.com | 3da73e8f-4790-4cc8-ae67-11c87d6544d3 | dFhGC3kHlqcywKF |
| 3   | 0003       | Aaron Fahey   | Irma.Farrell | Ashly.Rath@gmail.com | (UUID) | Vp6mdcGBLMJ6P3Y |
| 4   | 0004       | (Nome)        | (Username)   | Jamel52@hotmail.com | (UUID) | wcSFOMJNXAgb7Mv |
| 5   | 0005       | (Nome)        | (Username)   | Meaghan48@hotmail.com | (UUID) | ryRCq04bQs4PLpq |
| 6   | 0006       | (Nome)        | (Username)   | Imelda.Oberbrunner98@yahoo.com | (UUID) | yuxZmbMvp8XGjZV |
| 7   | 0007       | (Nome)        | (Username)   | Landen_Greenholt@yahoo.com | (UUID) | ioRa43yC2pMVewi |

### 3. TBL_DADOS_ID (OPCIONAL)
Dados adicionais de identificação (se necessário).

## Cenários de Teste

### ❌ CT-1001: Comentado
- **Motivo**: Não precisamos mais testar cadastro de usuário
- **Status**: Comentado na feature

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

## Como Funciona (SIMPLIFICADO)

1. **Cenário executa** com tag `@CT-1002` (ou superior)
2. **Hook captura** a tag e extrai `CT-1002`
3. **Context.setIdUsuario("CT-1002")**
4. **LoginDataSheet** busca diretamente na TBL_CENARIOS:
   - WHERE ID_CENARIO = 'CT-1002' → retorna todos os dados
5. **Context.setData(loginModel)**
6. **Steps/Controllers** usam os dados para login e outras operações

## Vantagens da Nova Estrutura

- ✅ **Simplicidade**: Uma única consulta na TBL_CENARIOS
- ✅ **Performance**: Sem joins ou múltiplas consultas
- ✅ **Manutenção**: Dados centralizados via PROCV
- ✅ **Flexibilidade**: Fácil adicionar novos campos
- ✅ **Foco**: Apenas testes de login e operações com usuários existentes

## Classes Ajustadas

### Para Login e Operações:
- ✅ **LoginDataSheet**: Lê diretamente da TBL_CENARIOS
- ✅ **Hook**: Usa LoginDataSheet para todos os cenários
- ✅ **Controller**: Usa dados da planilha para todas as operações

## Exemplo de Uso nos Testes

### Login de Usuário
```java
// Os dados vêm diretamente da TBL_CENARIOS
LoginModel dados = (LoginModel) Context.getData();
String email = dados.getEmail();  // "Fredrick_Quitzon3@hotmail.com"
String senha = dados.getSenha();  // "dFhGC3kHlqcywKF"
```

### Operações com Usuário Logado
```java
// Usar dados do usuário logado para outras operações
LoginModel usuario = (LoginModel) Context.getData();
String email = usuario.getEmail();
// Fazer requisições de listagem, busca, atualização, etc.
```

## Observações Importantes

- ✅ **CT-1001**: Comentado - não precisamos mais de cadastro
- ✅ **CT-1002+**: Ativos - usam usuários já cadastrados na planilha
- ✅ **TBL_CENARIOS**: Tabela principal com todos os dados via PROCV
- ✅ **TBL_CADASTRO**: Fonte dos dados (usada pelo PROCV)
- ✅ **Senhas**: Já preenchidas na planilha
- ✅ **Estrutura simplificada**: Uma consulta única por cenário 