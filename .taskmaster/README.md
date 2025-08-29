# Task Master - Configuração e Estrutura

Este diretório contém toda a configuração e dados do Task Master para o projeto de automação de testes.

## 📁 Estrutura de Diretórios

### **📄 Arquivos de Configuração**

#### **`config.json`**
- **Descrição**: Configuração principal do Task Master
- **Conteúdo**:
  - Configuração dos modelos de IA (Google Gemini)
  - Configurações globais do projeto
  - Parâmetros de geração de tarefas
  - URLs de serviços externos

#### **`state.json`**
- **Descrição**: Estado atual do Task Master
- **Conteúdo**:
  - Tag atual ativa (`master`)
  - Data da última mudança de tag
  - Mapeamento de branches para tags
  - Status de migração

### **📂 Diretórios de Dados**

#### **`tasks/`**
- **Descrição**: Tarefas do projeto organizadas por tag
- **Conteúdo**:
  - `tasks.json` - Arquivo principal com todas as tarefas
  - `task_001.txt` até `task_015.txt` - Arquivos individuais de cada tarefa

#### **`docs/`**
- **Descrição**: Documentação do Task Master
- **Conteúdo**:
  - PRD (Product Requirements Document)
  - Templates de documentação

#### **`templates/`**
- **Descrição**: Templates para criação de tarefas
- **Conteúdo**:
  - `example_prd.txt` - Exemplo de PRD

#### **`reports/`**
- **Descrição**: Relatórios gerados pelo Task Master
- **Conteúdo**:
  - Relatórios de complexidade
  - Análises de projeto

## 🔧 Configuração dos Modelos de IA

### **Modelo Principal (Main)**
- **Provedor**: Google
- **Modelo**: `gemini-2.0-flash`
- **Função**: Geração de tarefas e operações gerais
- **Temperatura**: 0.2 (focado)

### **Modelo de Pesquisa (Research)**
- **Provedor**: Google
- **Modelo**: `gemini-2.0-flash`
- **Função**: Pesquisas e análises complexas
- **Temperatura**: 0.1 (muito focado)

### **Modelo de Reserva (Fallback)**
- **Provedor**: Google
- **Modelo**: `gemini-2.0-flash`
- **Função**: Backup caso o principal falhe
- **Temperatura**: 0.2 (padrão)

## 📊 Configurações Globais

### **Geração de Tarefas**
- **Tarefas padrão**: 10
- **Subtarefas padrão**: 5
- **Prioridade padrão**: média

### **Logs e Debug**
- **Nível de log**: info
- **Modo debug**: desabilitado

### **Projeto**
- **Nome**: Taskmaster
- **Tag padrão**: master
- **Idioma de resposta**: Inglês (configurável)

## 🚀 Como Usar

### **Comandos Principais**
```bash
# Ver tarefas
task-master list

# Próxima tarefa
task-master next

# Expandir tarefa
task-master expand --id=1

# Adicionar tarefa
task-master add-task --prompt="Nova tarefa"

# Atualizar status
task-master set-status --id=1 --status=done
```

### **Configuração de Modelos**
```bash
# Ver configuração atual
task-master models

# Configurar modelo principal
task-master models --set-main=gemini-2.0-flash

# Configurar modelo de pesquisa
task-master models --set-research=gemini-2.0-flash
```

## 📝 Estrutura das Tarefas

### **Campos Principais**
- **id**: Identificador único da tarefa
- **title**: Título da tarefa
- **description**: Descrição resumida
- **details**: Detalhes de implementação
- **status**: Status atual (pending, in-progress, done)
- **priority**: Prioridade (high, medium, low)
- **dependencies**: IDs das tarefas dependentes
- **complexity**: Complexidade (1-10)
- **testStrategy**: Estratégia de teste

### **Subtarefas**
- Cada tarefa pode ter subtarefas
- Subtarefas seguem a mesma estrutura
- IDs das subtarefas: `tarefa.subtarefa` (ex: 1.2)

## 🔍 Monitoramento

### **Telemetria**
- Tokens utilizados
- Custo por operação
- Provedor utilizado
- Tempo de resposta

### **Relatórios**
- Análise de complexidade
- Cobertura de tarefas
- Progresso do projeto

## ⚠️ Observações Importantes

1. **Chaves de API**: Configuradas em `.cursor/mcp.json` e `.env`
2. **Backup**: Sempre fazer backup antes de alterações
3. **Versionamento**: As tarefas são versionadas no Git
4. **Tags**: Sistema de tags para organizar contextos diferentes

## 🛠️ Manutenção

### **Atualizações**
- Verificar configuração dos modelos periodicamente
- Atualizar dependências quando necessário
- Revisar estrutura de tarefas

### **Backup**
- Fazer backup do diretório `.taskmaster/`
- Versionar alterações importantes
- Documentar mudanças de configuração
