package org.br.com.test.utils.massas;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.br.com.test.model.request.UsuarioRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class GeradorMassaRunner {

	private static final String BASE_URL = "http://localhost:3000";
	private static final String ENDPOINT_USUARIOS = "/usuarios";

	// ========================================
	// CONFIGURAÇÕES - MODIFIQUE AQUI
	// ========================================
	private static final String ORIGINAL_INPUT_PATH = "output/massaDeTeste.csv";
	private static final String SYNC_FILE_PATH = "output/massaDeTeste_com_ids.csv";
	private static final String EXCEL_PATH = "src/main/resources/data/MassaDadosCMS.xlsx";

	// *** NOVA CONFIGURAÇÃO: Edite o nome base da aba aqui ***
	private static final String EXCEL_SHEET_NAME_PREFIX = "TBL_MASSA_CADASTRADA";
	// ========================================

	public static void main(String[] args) {
		System.out.println("🔄 Sincronizador de Massa de Dados a partir de CSV");
		System.out.println("=================================================");

		File sourceFile = new File(ORIGINAL_INPUT_PATH);
		File syncFile = new File(SYNC_FILE_PATH);

		if (syncFile.exists()) {
			try (FileWriter fw = new FileWriter(syncFile, true)) {
				// Teste de bloqueio
			} catch (IOException e) {
				System.err.println("❌ ERRO DE PERMISSÃO: O arquivo CSV de saída está bloqueado.");
				System.err.println("   Arquivo: " + syncFile.getAbsolutePath());
				System.err.println("   Por favor, feche o arquivo e tente novamente.");
				return;
			}
		}

		if (!sourceFile.exists() || sourceFile.length() == 0) {
			System.err.println("❌ ERRO FATAL: Arquivo de entrada original não encontrado ou vazio: " + ORIGINAL_INPUT_PATH);
			return;
		}

		try {
			// 1. Lê o arquivo de origem
			List<String[]> sourceRows = readAllCsvRows(sourceFile);
			if (sourceRows.size() < 2) {
				System.out.println("⚠️ Arquivo de origem não contém dados para processar.");
				return;
			}
			String[] sourceHeader = sourceRows.get(0);
			List<String[]> sourceData = sourceRows.subList(1, sourceRows.size());
			final int sourceEmailIdx = findColumnIndex(sourceHeader, "Email");
			if (sourceEmailIdx == -1) {
				throw new IllegalArgumentException("A coluna 'Email' é obrigatória no arquivo de origem.");
			}

			// 2. Lê o arquivo de sincronização para saber o estado atual
			Map<String, String> syncRawLineMap = readCsvToRawLineMap(syncFile, sourceEmailIdx);

			// 3. Prepara a lista de DADOS FINAIS para a saída
			List<String[]> finalOutputData = new ArrayList<>(); // Usaremos esta lista para ambos os writers
			List<String> finalHeaderList = new ArrayList<>(Arrays.asList(sourceHeader));
			if (findColumnIndex(sourceHeader, "ID_USUARIO") == -1) finalHeaderList.add("ID_USUARIO");
			if (findColumnIndex(sourceHeader, "STATUS_CADASTRO") == -1) finalHeaderList.add("STATUS_CADASTRO");
			finalOutputData.add(finalHeaderList.toArray(new String[0]));

			final String[] finalHeader = finalHeaderList.toArray(new String[0]);
			final int outIdUsuarioIdx = findColumnIndex(finalHeader, "ID_USUARIO");
			final int outStatusIdx = findColumnIndex(finalHeader, "STATUS_CADASTRO");
			final int outNomeUsuarioIdx = findColumnIndex(finalHeader, "NomeUsuario");

			// 4. Itera sobre a fonte da verdade e decide o que fazer
			for (String[] sourceRow : sourceData) {
				String email = cleanCsvField(sourceRow[sourceEmailIdx]);
				String nomeUsuario = cleanCsvField(sourceRow[outNomeUsuarioIdx]);
				String syncedRawLine = syncRawLineMap.get(email);

				if (syncedRawLine != null) {
					System.out.println("🔵 Pulando usuário '" + nomeUsuario + "', pois já foi processado anteriormente.");
					// Adiciona a linha como um array de strings para ser usado por ambos os writers
					finalOutputData.add(syncedRawLine.split(";", -1));
					continue;
				}

				System.out.println("📋 Processando registro para o usuário: " + nomeUsuario);
				String nomeCompleto = cleanCsvField(sourceRow[findColumnIndex(sourceHeader, "NomeCompleto")]);
				String senha = cleanCsvField(sourceRow[findColumnIndex(sourceHeader, "Senha")]);

				UsuarioRequest usuarioRequest = new UsuarioRequest(nomeCompleto, nomeUsuario, email, senha);

				Response response = given()
						.header("Content-Type", "application/json")
						.baseUri(BASE_URL)
						.body(usuarioRequest)
						.when()
						.post(ENDPOINT_USUARIOS);

				List<String> outputRowFields = new ArrayList<>(Arrays.asList(sourceRow));
				while (outputRowFields.size() < finalHeader.length) {
					outputRowFields.add("");
				}

				if (response.getStatusCode() == 201) {
					String novoId = response.jsonPath().getString("id");
					System.out.println("✅ Usuário '" + nomeUsuario + "' cadastrado com sucesso - ID: " + novoId);
					outputRowFields.set(outIdUsuarioIdx, formatForCsvOutput(novoId));
					outputRowFields.set(outStatusIdx, formatForCsvOutput("Cadastrado com sucesso"));
				} else if (response.getBody().asString().contains("já está em uso")) {
					System.out.println("🟡 Usuário '" + nomeUsuario + "' já existe na API.");
					outputRowFields.set(outStatusIdx, formatForCsvOutput("Usuário já existe na API"));
				} else {
					System.err.println("⚠️ Falha no cadastro para o usuário '" + nomeUsuario + "'.");
					outputRowFields.set(outStatusIdx, formatForCsvOutput("Falha: " + response.getStatusCode()));
				}
				finalOutputData.add(outputRowFields.toArray(new String[0]));
				Thread.sleep(200);
			}

			// 5. Escreve os resultados nos dois formatos
			writeToCsv(finalOutputData);
			writeToExcel(finalOutputData);

		} catch (IOException | CsvException e) {
			if (e.getMessage() != null && e.getMessage().contains("The process cannot access the file")) {
				System.err.println("❌ ERRO DE GRAVAÇÃO: Um dos arquivos de saída está aberto em outro programa (Excel?).");
				System.err.println("   Por favor, feche os arquivos e tente novamente.");
			} else {
				System.err.println("❌ Erro ao ler, processar o arquivo CSV ou conectar à API: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			System.err.println("❌ Processo interrompido.");
			Thread.currentThread().interrupt();
		} catch (IllegalArgumentException e) {
			System.err.println("❌ Erro de configuração: " + e.getMessage());
		}
	}

	/**
	 * Escreve os dados em um arquivo CSV, preservando a formatação.
	 */
	private static void writeToCsv(List<String[]> data) throws IOException {
		try (FileWriter fw = new FileWriter(SYNC_FILE_PATH, StandardCharsets.UTF_8)) {
			for (int i = 0; i < data.size(); i++) {
				fw.write(String.join(";", data.get(i)));
				if (i < data.size() - 1) {
					fw.write(System.lineSeparator());
				}
			}
		}
		System.out.println("\n🎉 Arquivo CSV de sincronização gerado com sucesso!");
		System.out.println("   -> " + new File(SYNC_FILE_PATH).getAbsolutePath());
	}

	/**
	 * Escreve os dados em uma nova aba de um arquivo Excel, formatando como uma tabela profissional.
	 */
	private static void writeToExcel(List<String[]> data) throws IOException {
		File excelFile = new File(EXCEL_PATH);
		// Usamos XSSFWorkbook diretamente para ter acesso a recursos avançados como tabelas
		XSSFWorkbook workbook;

		if (excelFile.exists()) {
			try (FileInputStream fis = new FileInputStream(excelFile)) {
				workbook = new XSSFWorkbook(fis);
			}
		} else {
			workbook = new XSSFWorkbook();
		}

		String sheetName = EXCEL_SHEET_NAME_PREFIX;

		int existingSheetIndex = workbook.getSheetIndex(sheetName);
		if (existingSheetIndex != -1) {
			workbook.removeSheetAt(existingSheetIndex);
			System.out.println("ℹ️ Aba '" + sheetName + "' existente foi removida para atualização.");
		}

		XSSFSheet sheet = workbook.createSheet(sheetName);

		// Escreve os dados primeiro
		int rowNum = 0;
		for (String[] rowData : data) {
			Row row = sheet.createRow(rowNum++);
			int cellNum = 0;
			for (String cellData : rowData) {
				Cell cell = row.createCell(cellNum++);
				cell.setCellValue(cleanCsvField(cellData));
			}
		}

		// --- FORMATAÇÃO DA TABELA (LÓGICA DO SEU SCRIPT) ---

		// Garante que temos dados para formatar
		if (data.isEmpty() || data.get(0).length == 0) {
			// Salva e sai se não houver dados
			try (FileOutputStream fos = new FileOutputStream(excelFile)) {
				workbook.write(fos);
			}
			workbook.close();
			return;
		}

		// 1. Define a área da tabela
		int numRows = data.size();
		int numCols = data.get(0).length;
		AreaReference tableArea = workbook.getCreationHelper().createAreaReference(
				new CellReference(0, 0), // Canto superior esquerdo (A1)
				new CellReference(numRows - 1, numCols - 1) // Canto inferior direito
		);

		// 2. Cria a tabela na área definida
		XSSFTable table = sheet.createTable(tableArea);
		table.setDisplayName(sheetName.replaceAll("\\s", "")); // Nome da tabela sem espaços

		// 3. Define o estilo da tabela (igual ao seu script) - MÉTODO SEGURO
		table.setStyleName("TableStyleMedium8");

		// 4. Autoajusta a largura das colunas
		for (int i = 0; i < numCols; i++) {
			sheet.autoSizeColumn(i);
		}

		// Salva o arquivo Excel
		try (FileOutputStream fos = new FileOutputStream(excelFile)) {
			workbook.write(fos);
		}
		workbook.close();

		System.out.println("🎉 Planilha Excel atualizada e formatada com sucesso!");
		System.out.println("   -> " + excelFile.getAbsolutePath() + " (Aba: " + sheetName + ")");
	}


	/**
	 * CORREÇÃO FINAL: Lógica de limpeza que lida com os dois tipos de formatação.
	 */
	private static String cleanCsvField(String field) {
		if (field == null) {
			return "";
		}
		String trimmedField = field.trim();

		// 1. Lida com o formato vindo do split de linha crua: "=""valor"""
		if (trimmedField.startsWith("\"=\"\"") && trimmedField.endsWith("\"\"\"")) {
			return trimmedField.substring(4, trimmedField.length() - 3);
		}
		// 2. Lida com o formato vindo do parser OpenCSV: ="valor"
		if (trimmedField.startsWith("=\"") && trimmedField.endsWith("\"")) {
			return trimmedField.substring(2, trimmedField.length() - 1);
		}
		// 3. Se não tiver formato especial, retorna o valor como está.
		return trimmedField;
	}

	private static String formatForCsvOutput(String cleanField) {
		if (cleanField == null || cleanField.isEmpty()) return "";
		return "\"=\"\"" + cleanField + "\"\"\"";
	}

	private static int findColumnIndex(String[] header, String columnName) {
		if (header == null) return -1;
		for (int i = 0; i < header.length; i++) {
			if (header[i].equalsIgnoreCase(columnName)) {
				return i;
			}
		}
		return -1;
	}

	private static List<String[]> readAllCsvRows(File file) throws IOException, CsvException {
		// <<< CORREÇÃO APLICADA AQUI >>>
		// Desativamos o processamento de aspas para que o parser leia o campo literalmente.
		CSVParser parser = new CSVParserBuilder().withSeparator(';')
				.withQuoteChar('\0') // Trata as aspas como um caractere normal
				.build();
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(file, StandardCharsets.UTF_8))
				.withCSVParser(parser)
				.build()) {
			return reader.readAll();
		}
	}

	private static Map<String, String> readCsvToRawLineMap(File file, int keyColumnIndex) throws IOException {
		if (!file.exists() || keyColumnIndex == -1) {
			return Map.of();
		}

		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}

		if (lines.size() < 2) {
			return Map.of();
		}

		return lines.subList(1, lines.size()).stream()
				.filter(line -> !line.trim().isEmpty())
				.collect(Collectors.toMap(
						line -> {
							String[] fields = line.split(";", -1);
							return (fields.length > keyColumnIndex) ? cleanCsvField(fields[keyColumnIndex]) : "";
						},
						line -> line,
						(existing, replacement) -> existing
				));
	}
}