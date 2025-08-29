package org.br.com.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FormatUtils {
    public static String prettyJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "";
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json;
        }
    }

    public static String formatHeaders(String headersRaw) {
        if (headersRaw == null || headersRaw.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([A-Za-z0-9\\-]+)=([^=]+)(?=\\s+[A-Za-z0-9\\-]+=|$)");
        java.util.regex.Matcher matcher = pattern.matcher(headersRaw);

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();

            if (key.equalsIgnoreCase("Accept")) {
                String[] values = value.split(",\\s*");
                sb.append(key).append("=").append(values[0]).append("\n");
                for (int i = 1; i < values.length; i++) {
                    sb.append(values[i]).append("\n");
                }
            } else if (key.equalsIgnoreCase("Authorization")) {
                if (value.length() > 20) {
                    value = value.substring(0, 20) + "...";
                }
                sb.append(key).append("=").append(value).append("\n");
            } else {
                sb.append(key).append("=").append(value).append("\n");
            }
        }
        return sb.toString().trim();
    }

    /**
     * Mascara dados sensíveis em uma string JSON.
     * Atualmente, mascara os campos "authorization" e "token".
     * @param json A string JSON a ser processada.
     * @return A string JSON com os dados mascarados.
     */
    public static String maskSensitiveDataInJson(String json) {
        if (json == null) return null;

        String maskedJson = json;

        // >>>>>>>> LÓGICA DE MASCARAMENTO ATUALIZADA <<<<<<<<<<

        // Regra 1: Mascara o campo "authorization" (padrão antigo)
        maskedJson = maskedJson.replaceAll(
                "(\"authorization\"\\s*:\\s*\"Bearer\\s+)([^\"]{20})[^\"]*",
                "$1$2...\""
        );

        // Regra 2: Mascara o campo "token" (novo padrão)
        // A regex procura por "token" : "valor", captura os primeiros 15 caracteres do valor e substitui o resto.
        maskedJson = maskedJson.replaceAll(
                "(\"token\"\\s*:\\s*\")([^\"\\s]{15})[^\"\\s]*",
                "$1$2...\""
        );

        return maskedJson;
    }
}