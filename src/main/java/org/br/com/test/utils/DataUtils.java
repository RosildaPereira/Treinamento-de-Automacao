package org.br.com.test.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {
    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // >>> NOVO FORMATADOR COM MILISSEGUNDOS <<<
    private static final DateTimeFormatter HORA_COM_MILIS_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static String getHoraAtual() {
        return LocalDateTime.now().format(HORA_FORMATTER);
    }

    // >>> NOVO MÉTODO PARA OBTER A HORA COM MILISSEGUNDOS <<<
    public static String getHoraAtualComMilis() {
        return LocalDateTime.now().format(HORA_COM_MILIS_FORMATTER);
    }

    public static String getDataHoraAtual() {
        return LocalDateTime.now().format(DATA_HORA_FORMATTER);
    }

    public static String getTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    public static String getDataHoraParaArquivo() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}