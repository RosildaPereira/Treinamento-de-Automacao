# 📋 Estrutura da Planilha Excel para CT-1004-A e CT-1004-B

## TBL_CENARIOS - Novas Linhas

| ID_CENARIO | ID_MASSA | EMAIL | SENHA | FEATURE | NOME | SEQ |
|------------|----------|-------|-------|---------|------|-----|
| CT-1004-A | 0004A | email_invalido | senha123 | usuario | Tentar criar usuário com email inválido | 4A |
| CT-1004-B | 0004B | usuario.teste@exemplo.com | 123 | usuario | Tentar criar usuário com senha inválida | 4B |

## TBL_CADASTRO - Novas Linhas

| ID_MASSA | NOME_COMPLETO | NOME_USUARIO | EMAIL | SENHA | ID_USUARIO | SEQ |
|----------|---------------|--------------|-------|-------|------------|-----|
| 0004A | Usuario Email Invalido | usuario.email.invalido | email_invalido | senha123 | 00000000-0000-0000-0000-000000000000 | 4A |
| 0004B | Usuario Senha Invalida | usuario.senha.invalida | usuario.teste@exemplo.com | 123 | 00000000-0000-0000-0000-000000000000 | 4B |

## 📝 Explicação

### CT-1004-A (Email Inválido)
- **EMAIL**: `email_invalido` (formato inválido)
- **SENHA**: `senha123` (válida)
- **Resultado esperado**: Status 400 com erro "Email inválido"

### CT-1004-B (Senha Inválida)
- **EMAIL**: `usuario.teste@exemplo.com` (válido)
- **SENHA**: `123` (muito curta - menos de 6 caracteres)
- **Resultado esperado**: Status 400 com erro "Senha deve ter no mínimo 6 caracteres"

## 🔄 Como Funciona

1. **CT-1004-A**: Sistema lê `ID_MASSA = 0004A` da TBL_CENARIOS
2. **Busca na TBL_CADASTRO**: Encontra dados com `ID_MASSA = 0004A`
3. **Usa dados**: Email inválido + senha válida para teste
4. **CT-1004-B**: Mesmo processo com `ID_MASSA = 0004B`

## ✅ Benefícios

- ✅ **Dados centralizados**: Toda a massa de dados na planilha
- ✅ **Fácil manutenção**: Alterar dados sem mexer no código
- ✅ **Rastreabilidade**: Cada cenário tem seu ID único
- ✅ **Flexibilidade**: Pode adicionar mais cenários facilmente 