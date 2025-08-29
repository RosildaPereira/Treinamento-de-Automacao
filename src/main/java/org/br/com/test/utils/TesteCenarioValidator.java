package org.br.com.test.utils;

import org.br.com.core.support.logger.LogFormatter;

public class TesteCenarioValidator {
    
    public static void main(String[] args) {
        System.out.println("🧪 Teste de Validação Dinâmica de Cenários");
        System.out.println("==========================================");
        
        // Testar carregamento dinâmico
        System.out.println("📋 Carregando cenários da feature...");
        var cenarios = CenarioValidator.getAllScenarios();
        
        System.out.println("✅ Cenários encontrados:");
        cenarios.forEach((id, nome) -> {
            System.out.println("   " + id + " -> " + nome);
        });
        
        System.out.println("\n🧪 Testando validações:");
        
        // Teste 1: Nome correto
        System.out.println("\n1️⃣ Teste com nome correto:");
        try {
            CenarioValidator.validateScenarioName("CT-1001", "Criar um novo usuário CMS com sucesso");
            System.out.println("   ✅ Teste passou!");
        } catch (Exception e) {
            System.out.println("   ❌ Teste falhou: " + e.getMessage());
        }
        
        // Teste 2: Nome errado
        System.out.println("\n2️⃣ Teste com nome errado:");
        try {
            CenarioValidator.validateScenarioName("CT-1001", "Criar usuario CMS");
            System.out.println("   ❌ Teste deveria ter falhado!");
        } catch (Exception e) {
            System.out.println("   ✅ Teste falhou corretamente: " + e.getMessage());
        }
        
        // Teste 3: Cenário inexistente
        System.out.println("\n3️⃣ Teste com cenário inexistente:");
        try {
            CenarioValidator.validateScenarioName("CT-9999", "Nome qualquer");
            System.out.println("   ✅ Teste passou (cenário não encontrado)");
        } catch (Exception e) {
            System.out.println("   ❌ Teste falhou: " + e.getMessage());
        }
        
        // Teste 4: Simular novo cenário CT-1016
        System.out.println("\n4️⃣ Simulando novo cenário CT-1016:");
        System.out.println("   💡 Para adicionar CT-1016, adicione na feature:");
        System.out.println("   @CT-1016 @api");
        System.out.println("   Scenario: Validar exclusão com token ok");
        System.out.println("   💡 E na planilha:");
        System.out.println("   ID_CENARIO: CT-1016");
        System.out.println("   NOME_CENARIO: Validar exclusão com token ok");
        
        System.out.println("\n🎉 Teste de validação dinâmica concluído!");
    }
} 