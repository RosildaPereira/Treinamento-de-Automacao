package org.br.com.test.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ScenarioCountExtract {

	public static void main(String[] args) throws IOException {
		String featuresPath = "src/main/resources/features"; // Caminho para a pasta de features
		String outputFilePath = "ScenarioCountExtract.txt"; // Caminho para o arquivo de saída

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
			Files.walk(Paths.get(featuresPath))
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".feature"))
					.forEach(path -> analyzeFeatureFile(path, writer));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analyzeFeatureFile(Path featureFile, BufferedWriter writer) {
		try {
			List<String> lines = Files.readAllLines(featureFile, StandardCharsets.UTF_8);
			String currentScenario = null;
			int stepCount = 0;

			writer.write("Arquivo: " + featureFile.getFileName() + "\n");

			for (String line : lines) {
				line = line.trim();

				// Verifica se é um novo cenário
				if (line.startsWith("Scenario") || line.startsWith("Scenario Outline")) {
					if (currentScenario != null) {
						// Escreve o cenário anterior com a contagem de steps
						writer.write("  Cenário: " + currentScenario + "\n");
						writer.write("    Steps: " + stepCount + "\n");
					}
					// Define o novo cenário e reseta a contagem de steps
					currentScenario = line;
					stepCount = 0;
				} else if (line.startsWith("Given") || line.startsWith("When") || line.startsWith("Then") ||
						line.startsWith("And") || line.startsWith("But")) {
					// Conta o step
					stepCount++;
				}
			}

			// Escreve o último cenário após o loop
			if (currentScenario != null) {
				writer.write("  Cenário: " + currentScenario + "\n");
				writer.write("    Steps: " + stepCount + "\n");
			}

			writer.write("\n"); // Adiciona uma linha em branco entre os arquivos
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
