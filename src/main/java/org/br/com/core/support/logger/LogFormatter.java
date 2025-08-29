package org.br.com.core.support.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.br.com.test.utils.DataUtils;


public class LogFormatter {
    private static final Logger logger = LogManager.getLogger(LogFormatter.class);
    
    public static void logStep(String message) {
        String formattedMessage = formatStepMessage(message);
        logger.info(formattedMessage);
    }
    
    public static void logError(String message) {
        String formattedMessage = formatStepMessage("❌ ERRO: " + message);
        logger.error(formattedMessage);
    }
    
    public static void logWarning(String message) {
        String formattedMessage = formatStepMessage("⚠️ AVISO: " + message);
        logger.warn(formattedMessage);
    }
    
    public static void logSuccess(String message) {
        String formattedMessage = formatStepMessage("✅ " + message);
        logger.info(formattedMessage);
    }
    
    public static void logRequest(String method, String url, String body) {
        logStep("Enviando solicitacao de " + method + " para: " + url);
        if (body != null && !body.trim().isEmpty()) {
            logStep("Body: " + body);
        }
    }
    
    public static void logResponse(int statusCode, String responseBody) {
        logStep("Status Code: " + statusCode);
        if (responseBody != null && !responseBody.trim().isEmpty()) {
            logStep("Response: " + responseBody);
        }
    }
    
    private static String formatStepMessage(String message) {
        return "[INFO] " + DataUtils.getHoraAtual() + " | '-- " + message;
    }
    
    public static void logScenarioStart(String scenarioName) {
        logStep("+-- " + scenarioName);
    }
    
    public static void logScenarioEnd(String result) {
        String icon = "PASSED".equalsIgnoreCase(result) ? "✅" : "❌";
        // Garante que não haja duplicação de '--' e adiciona quebra de linha após o status
        String formatted = formatStepMessage(icon + result);
        logger.info(formatted);
        logger.info(""); // quebra de linha para separar cenários
    }
    
    public static void logExecutionHeader(String timestamp) {
        logger.info("==================================== Execucao: " + timestamp + " ====================================");
    }
    
    public static void logFeature(String featureName) {
        logger.info("Feature: " + featureName);
    }
    
    public static void logScenarioId(String scenarioId) {
        logger.info(scenarioId);
        logger.info("");
    }
    
    public static void logSummaryHeader() {
        logger.info("========================================== Resumo da Execucao ==========================================");
    }

    public static void logSummary(String message) {

    }
    
    public static void logValidation(String message) {
        logStep("Validando " + message);
    }

    public static void logInfo(String format) {
    }
}