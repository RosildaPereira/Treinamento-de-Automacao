package org.br.com.test.utils;

import org.br.com.core.support.logger.LogFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CenarioValidator {
    
    private static final Map<String, String> SCENARIO_NAMES = new HashMap<>();
    private static boolean initialized = false;
    
    // Mapeamento dinâmico dos IDs de cenário para os nomes esperados na feature
    private static final Map<String, String> EXPECTED_SCENARIO_NAMES = new HashMap<>();
    
    public static void validateScenarioName(String idCenario, String nomeCenarioPlanilha) {
        if (!initialized) {
            loadScenarioNamesFromFeature();
        }
        
        String expectedName = EXPECTED_SCENARIO_NAMES.get(idCenario);
        if (expectedName == null) {
            LogFormatter.logStep("⚠️ AVISO: ID de cenário não encontrado na feature: " + idCenario);
            LogFormatter.logStep("💡 Cenários disponíveis: " + EXPECTED_SCENARIO_NAMES.keySet());
            return;
        }
        
        if (!expectedName.equals(nomeCenarioPlanilha)) {
            String errorMsg = String.format(
                "❌ ERRO DE VALIDAÇÃO: Nome do cenário na planilha não corresponde ao esperado!\n" +
                "   🆔 ID Cenário: %s\n" +
                "   📋 Nome na Planilha: '%s'\n" +
                "   ✅ Nome Esperado: '%s'\n" +
                "   💡 Corrija o nome na planilha para evitar execução de teste errado!",
                idCenario, nomeCenarioPlanilha, expectedName
            );
            
            LogFormatter.logStep(errorMsg);
            throw new RuntimeException("Validação de cenário falhou: " + errorMsg);
        }
        
        LogFormatter.logStep("✅ Validação de cenário: Nome correto para " + idCenario);
    }
    
    private static void loadScenarioNamesFromFeature() {
        String[] possiblePaths = {
            "src/main/resources/features/1.usuario.feature",
            "features/1.usuario.feature",
            "resources/features/1.usuario.feature"
        };
        
        File featureFile = null;
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                featureFile = file;
                break;
            }
        }
        
        if (featureFile == null) {
            LogFormatter.logStep("⚠️ AVISO: Arquivo de feature não encontrado para validação de cenários");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(featureFile))) {
            String line;
            String currentScenario = null;
            StringBuilder scenarioContent = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                // Procurar por linhas de cenário (@CT-XXXX)
                if (line.trim().startsWith("@CT-")) {
                    // Se já temos um cenário anterior, salvar
                    if (currentScenario != null && scenarioContent.length() > 0) {
                        String scenarioName = extractScenarioName(scenarioContent.toString());
                        if (scenarioName != null) {
                            EXPECTED_SCENARIO_NAMES.put(currentScenario, scenarioName);
                            LogFormatter.logStep("📋 Cenário carregado: " + currentScenario + " -> " + scenarioName);
                        }
                    }
                    
                    // Extrair ID do novo cenário
                    Pattern pattern = Pattern.compile("@(CT-\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        currentScenario = matcher.group(1);
                        scenarioContent = new StringBuilder();
                    }
                }
                // Adicionar linha ao conteúdo do cenário atual
                else if (currentScenario != null) {
                    scenarioContent.append(line).append("\n");
                }
            }
            
            // Salvar o último cenário
            if (currentScenario != null && scenarioContent.length() > 0) {
                String scenarioName = extractScenarioName(scenarioContent.toString());
                if (scenarioName != null) {
                    EXPECTED_SCENARIO_NAMES.put(currentScenario, scenarioName);
                    LogFormatter.logStep("📋 Cenário carregado: " + currentScenario + " -> " + scenarioName);
                }
            }
            
        } catch (IOException e) {
            LogFormatter.logStep("⚠️ AVISO: Erro ao ler arquivo de feature: " + e.getMessage());
        }
        
        initialized = true;
        LogFormatter.logStep("✅ Total de cenários carregados: " + EXPECTED_SCENARIO_NAMES.size());
    }
    
    private static String extractScenarioName(String scenarioContent) {
        // Procurar por "Scenario:" seguido do nome
        Pattern pattern = Pattern.compile("Scenario:\\s*(.+)");
        Matcher matcher = pattern.matcher(scenarioContent);
        
        if (matcher.find()) {
            String scenarioName = matcher.group(1).trim();
            // Remover comentários se houver
            if (scenarioName.contains("#")) {
                scenarioName = scenarioName.substring(0, scenarioName.indexOf("#")).trim();
            }
            return scenarioName;
        }
        
        return null;
    }
    
    public static String getExpectedScenarioName(String idCenario) {
        if (!initialized) {
            loadScenarioNamesFromFeature();
        }
        return EXPECTED_SCENARIO_NAMES.get(idCenario);
    }
    
    public static boolean isScenarioMapped(String idCenario) {
        if (!initialized) {
            loadScenarioNamesFromFeature();
        }
        return EXPECTED_SCENARIO_NAMES.containsKey(idCenario);
    }
    
    public static Map<String, String> getAllScenarios() {
        if (!initialized) {
            loadScenarioNamesFromFeature();
        }
        return new HashMap<>(EXPECTED_SCENARIO_NAMES);
    }
} 