package org.br.com.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LogConfig {
	
	private static final Properties properties = new Properties();
	private static boolean initialized = false;
	
	static {
		loadProperties();
	}
	
	private static void loadProperties() {
		try (InputStream input = LogConfig.class.getClassLoader().getResourceAsStream("test.properties")) {
			if (input != null) {
				properties.load(input);
				initialized = true;
			} else {
				System.out.println("⚠️ Arquivo test.properties não encontrado, usando configurações padrão");
				setDefaultProperties();
			}
		} catch (IOException e) {
			System.out.println("⚠️ Erro ao carregar test.properties: " + e.getMessage());
			setDefaultProperties();
		}
	}
	
	private static void setDefaultProperties() {
		properties.setProperty("log.mascarar.dados.sensiveis", "true");
		properties.setProperty("log.mascarar.senha", "true");
		properties.setProperty("log.mascarar.id", "true");
		properties.setProperty("log.mascarar.token", "true");
		properties.setProperty("log.mascarar.email", "false");
		properties.setProperty("log.caracteres.visiveis", "3");
		initialized = true;
	}
	
	public static boolean isMascararDadosSensiveis() {
		return Boolean.parseBoolean(properties.getProperty("log.mascarar.dados.sensiveis", "true"));
	}
	
	public static boolean isMascararSenha() {
		return Boolean.parseBoolean(properties.getProperty("log.mascarar.senha", "true"));
	}
	
	public static boolean isMascararId() {
		return Boolean.parseBoolean(properties.getProperty("log.mascarar.id", "true"));
	}
	
	public static boolean isMascararToken() {
		return Boolean.parseBoolean(properties.getProperty("log.mascarar.token", "true"));
	}
	
	public static boolean isMascararEmail() {
		return Boolean.parseBoolean(properties.getProperty("log.mascarar.email", "false"));
	}
	
	public static int getCaracteresVisiveis() {
		try {
			String valor = properties.getProperty("log.caracteres.visiveis", "3");
			return Integer.parseInt(valor.trim());
		} catch (NumberFormatException e) {
			System.out.println("⚠️ Erro ao parsear log.caracteres.visiveis, usando valor padrão: 3");
			return 3;
		}
	}
	
	public static boolean isInitialized() {
		return initialized;
	}
} 