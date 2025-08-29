package org.br.com.test.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TagExtractor {

	public static void main(String[] args) {
		// Caminho da pasta principal que contém as subpastas e arquivos .feature
		String mainFeaturePath = "src/main/resources/features";

		// Lista para armazenar as tags extraídas
		List<String> extractedTags = new ArrayList<>();

		// Obter todos os arquivos .feature recursivamente
		List<File> featureFiles = getFeatureFiles(new File(mainFeaturePath));

		if (!featureFiles.isEmpty()) {
			for (File file : featureFiles) {
				log.info("Lendo arquivo: " + file.getAbsolutePath());
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = reader.readLine()) != null) {
						// Procurar tags na linha usando Regex
						Matcher matcher = Pattern.compile("@TEST_QAR-\\d+").matcher(line);
						while (matcher.find()) {
							String tag = matcher.group();
							extractedTags.add(tag);
							log.info("Tag encontrada: " + tag); // Log da tag encontrada
						}
					}
				} catch (IOException e) {
					log.info("Erro ao ler o arquivo: " + file.getName());
					e.printStackTrace();
				}
			}
		} else {
			log.info("Nenhum arquivo .feature encontrado na pasta: " + mainFeaturePath);
		}

		// Verificar se as tags foram extraídas
		if (extractedTags.isEmpty()) {
			log.info("Nenhuma tag foi encontrada.");
		} else {
			// Exibir todas as tags extraídas
			System.out.println("\nTags extraídas:");
			extractedTags.forEach(System.out::println);

			// Salvar as tags em um arquivo
			try (PrintWriter writer = new PrintWriter("tagsExtract.txt")) {
				for (String tag : extractedTags) {
					writer.println(tag);
				}
				log.info("\nTags salvas no arquivo tags_extraidas.txt");
			} catch (IOException e) {
				System.err.println("Erro ao salvar as tags no arquivo.");
				e.printStackTrace();
			}
		}
	}

	// Método para buscar recursivamente todos os arquivos .feature em uma pasta e suas subpastas
	private static List<File> getFeatureFiles(File folder) {
		List<File> featureFiles = new ArrayList<>();
		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					// Chama o método recursivamente para as subpastas
					featureFiles.addAll(getFeatureFiles(file));
				} else if (file.getName().endsWith(".feature")) {
					featureFiles.add(file);
				}
			}
		}

		return featureFiles;
	}
}
