# 📋 Ajustes Necessários na Planilha Excel

## 🚨 Problema Identificado

Os testes estão falhando porque:
1. **CT-1003**: Está tentando testar nome de usuário duplicado, mas a API está retornando erro de email duplicado
2. **CT-1004-A**: Está tentando testar email inválido, mas a API está retornando erros de duplicação
3. **CT-1004-B**: Precisa de dados específicos para senha inválida

## ✅ Solução Implementada

### **Métodos Atualizados no Controller:**

1. **CT-1003**: Agora cria um usuário original e depois tenta criar outro com o mesmo nome de usuário
2. **CT-1004-A**: Usa email com formato inválido (`email_invalido`)
3. **CT-1004-B**: Usa senha muito curta (`123`)

## 📊 Estrutura da Planilha Atual

### **TBL_CENARIOS - Linhas Existentes:**

| ID_CENARIO | ID_MASSA | EMAIL | SENHA | FEATURE | NOME |
|------------|----------|-------|-------|---------|------|
| CT-1002 | 0002 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF | usuario | Tentar criar um usuário com e-mail já existente |
| CT-1003 | 0003 | Ashly.Rath@gmail.com | dFhGC3kHlqcywKF | usuario | Tentar criar um usuário com nome de usuário já existente |

### **TBL_CADASTRO - Linhas Existentes:**

| ID_MASSA | NOME_COMPLETO | NOME_USUARIO | EMAIL | SENHA | ID_USUARIO |
|----------|---------------|--------------|-------|-------|------------|
| 0002 | Mr. Candace Lueilwitz | Mozelle54 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF | 3da73e8f-4790-4cc8-ae67-11c87d6544d3 |
| 0003 | Aaron Fahey | Irma.Farrell | Ashly.Rath@gmail.com | dFhGC3kHlqcywKF | [ID_EXISTENTE] |

## 🔧 Ajustes Recomendados

### **Opção 1: Manter Estrutura Atual (Recomendado)**

Os métodos do controller foram atualizados para não depender dos dados da planilha para estes testes específicos. Isso é mais confiável porque:

- ✅ **Isolamento**: Cada teste usa dados únicos
- ✅ **Confiabilidade**: Não depende de dados pré-existentes
- ✅ **Manutenibilidade**: Fácil de entender e manter

### **Opção 2: Ajustar Planilha (Se Preferir)**

Se você quiser manter o uso da planilha, adicione estas linhas:

#### **TBL_CENARIOS - Novas Linhas:**

| ID_CENARIO | ID_MASSA | EMAIL | SENHA | FEATURE | NOME |
|------------|----------|-------|-------|---------|------|
| CT-1004-A | 0004A | email_invalido | senha123 | usuario | Tentar criar usuário com email inválido |
| CT-1004-B | 0004B | usuario.teste@exemplo.com | 123 | usuario | Tentar criar usuário com senha inválida |

#### **TBL_CADASTRO - Novas Linhas:**

| ID_MASSA | NOME_COMPLETO | NOME_USUARIO | EMAIL | SENHA | ID_USUARIO |
|----------|---------------|--------------|-------|-------|------------|
| 0004A | Usuario Email Invalido | usuario.email.invalido | email_invalido | senha123 | 00000000-0000-0000-0000-000000000000 |
| 0004B | Usuario Senha Invalida | usuario.senha.invalida | usuario.teste@exemplo.com | 123 | 00000000-0000-0000-0000-000000000000 |

## 🎯 Resultado Esperado

### **CT-1003 (Nome de Usuário Duplicado):**
```
AÇÃO: Tentativa de cadastro com nome de usuário duplicado
🔖 Nome Usuário duplicado: usuario.original
📧 Email novo: usuario.duplicado@exemplo.com

RESPOSTA:
{
  "erros": [
    {
      "campo": "nomeUsuario",
      "mensagem": "Nome de usuário já está em uso"
    }
  ]
}
```

### **CT-1004-A (Email Inválido):**
```
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
```

### **CT-1004-B (Senha Inválida):**
```
AÇÃO: Tentativa de cadastro com senha inválida
🔑 Senha inválida: ***

RESPOSTA:
{
  "erros": [
    {
      "campo": "senha",
      "mensagem": "Senha deve ter no mínimo 6 caracteres"
    }
  ]
}
```

## ✅ Recomendação Final

**Use a Opção 1** - Os métodos já foram atualizados para funcionar independentemente da planilha. Isso garante que os testes sejam mais confiáveis e isolados. 