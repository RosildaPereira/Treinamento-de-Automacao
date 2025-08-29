# 📋 Estrutura Final da Planilha Excel

## ✅ **Sistema Atualizado - APENAS Dados da Planilha**

Todos os métodos do controller foram atualizados para usar **APENAS** dados da planilha, sem nenhum mock ou fallback com dados hardcoded.

## 📊 **Estrutura Obrigatória da Planilha**

### **TBL_CENARIOS - Estrutura Base**

| ID_CENARIO | ID_MASSA | EMAIL | SENHA | FEATURE | NOME | SEQ |
|------------|----------|-------|-------|---------|------|-----|
| CT-1002 | 0002 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF | usuario | Tentar criar um usuário com e-mail já existente | 2 |
| CT-1003 | 0003 | Ashly.Rath@gmail.com | dFhGC3kHlqcywKF | usuario | Tentar criar um usuário com nome de usuário já existente | 3 |
| CT-1004-A | 0004A | email_invalido | senha123 | usuario | Tentar criar usuário com email inválido | 4A |
| CT-1004-B | 0004B | usuario.teste@exemplo.com | 123 | usuario | Tentar criar usuário com senha inválida | 4B |

### **TBL_CADASTRO - Dados Completos**

| ID_MASSA | NOME_COMPLETO | NOME_USUARIO | EMAIL | SENHA | ID_USUARIO | SEQ |
|----------|---------------|--------------|-------|-------|------------|-----|
| 0002 | Mr. Candace Lueilwitz | Mozelle54 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF | 3da73e8f-4790-4cc8-ae67-11c87d6544d3 | 2 |
| 0003 | Aaron Fahey | Irma.Farrell | Ashly.Rath@gmail.com | dFhGC3kHlqcywKF | [ID_EXISTENTE] | 3 |
| 0004A | Usuario Email Invalido | usuario.email.invalido | email_invalido | senha123 | 00000000-0000-0000-0000-000000000000 | 4A |
| 0004B | Usuario Senha Invalida | usuario.senha.invalida | usuario.teste@exemplo.com | 123 | 00000000-0000-0000-0000-000000000000 | 4B |

## 🔄 **Como o Sistema Funciona**

### **1. Carregamento de Dados**
```
TBL_CENARIOS (ID_CENARIO) → ID_MASSA → TBL_CADASTRO (ID_MASSA)
```

### **2. Prioridade de Dados**
1. **TBL_CADASTRO**: Dados completos (nome, nome usuário, email, senha)
2. **TBL_CENARIOS**: Dados básicos (email, senha) - fallback

### **3. Cenários Específicos**

#### **CT-1002 (Email Duplicado)**
- **Dados**: Usa TBL_CADASTRO com email que já existe
- **Teste**: Tenta criar usuário com email duplicado
- **Resultado**: Status 400 - "E-mail já está em uso"

#### **CT-1003 (Nome Usuário Duplicado)**
- **Dados**: Usa TBL_CADASTRO com nome de usuário que já existe
- **Teste**: Tenta criar usuário com nome de usuário duplicado
- **Resultado**: Status 400 - "Nome de usuário já está em uso"

#### **CT-1004-A (Email Inválido)**
- **Dados**: Usa TBL_CADASTRO com email inválido (`email_invalido`)
- **Teste**: Tenta criar usuário com formato de email inválido
- **Resultado**: Status 400 - "Email inválido"

#### **CT-1004-B (Senha Inválida)**
- **Dados**: Usa TBL_CADASTRO com senha muito curta (`123`)
- **Teste**: Tenta criar usuário com senha inválida
- **Resultado**: Status 400 - "Senha deve ter no mínimo 6 caracteres"

## 📝 **Logs Esperados**

### **CT-1002 (Email Duplicado)**
```
🆔 ID Cenário: 1002
✅ Massa de Dados:
   📧 Email: Fredrick_Quitzon3@hotmail.com
   🔑 Senha: ***ywKF
   👤 Nome: Mr. Candace Lueilwitz
   🔖 Nome Usuário: Mozelle54

AÇÃO: Tentativa de cadastro com e-mail duplicado
👤 Nome: Mr. Candace Lueilwitz
🔖 Nome Usuário: Mozelle54
📧 Email: Fredrick_Quitzon3@hotmail.com

RESPOSTA:
{
  "erros": [
    {
      "campo": "email",
      "mensagem": "E-mail já está em uso"
    }
  ]
}
✅ PASSED
```

### **CT-1004-A (Email Inválido)**
```
🆔 ID Cenário: 1004-A
✅ Massa de Dados:
   📧 Email: email_invalido
   🔑 Senha: ***123
   👤 Nome: Usuario Email Invalido
   🔖 Nome Usuário: usuario.email.invalido

AÇÃO: Tentativa de cadastro com email inválido
📧 Email inválido: email_invalido

RESPOSTA:
{
  "erros": [
    {
      "campo": "email",
      "mensagem": "Email inválido"
    }
  ]
}
✅ PASSED
```

## ✅ **Benefícios da Nova Estrutura**

- ✅ **100% Dados da Planilha**: Nenhum mock ou hardcode
- ✅ **Centralização**: Todos os dados em um local
- ✅ **Manutenibilidade**: Fácil alteração de dados
- ✅ **Rastreabilidade**: Cada cenário tem seu ID único
- ✅ **Flexibilidade**: Pode adicionar novos cenários facilmente
- ✅ **Confiabilidade**: Dados consistentes entre testes

## 🚀 **Próximos Passos**

1. **Atualizar a planilha** com a estrutura acima
2. **Executar os testes** para verificar funcionamento
3. **Ajustar dados** conforme necessário
4. **Adicionar novos cenários** seguindo o mesmo padrão

Agora o sistema está **100% dependente da planilha**! 🎉✨ 