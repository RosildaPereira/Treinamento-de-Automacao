package org.br.com.test.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagConcatenada {

	public void tagsExcel(String abaExcel, String execution) {
		String caminhoArquivo = "src/test/resources/data/MassaDadosCMS.xlsx"; // Atualize com o caminho do seu arquivo Excel
		String nomePlanilhaAlvo = abaExcel; // Nome da planilha onde será realizada a busca
		String caminhoArquivoJson = "src/main/resources/config/execution.json"; // Caminho para o arquivo JSON

		try {
			// Lista para armazenar as tags encontradas
			List<String> tags = new ArrayList<>();

			// Criar FileInputStream para ler o arquivo
			FileInputStream arquivoExcel = new FileInputStream(new File(caminhoArquivo));

			// Criar um objeto Workbook baseado na extensão do arquivo
			Workbook workbook;
			if (caminhoArquivo.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(arquivoExcel);
			} else if (caminhoArquivo.endsWith(".xls")) {
				workbook = new HSSFWorkbook(arquivoExcel);
			} else {
				throw new IOException("Formato de arquivo não suportado. Use .xlsx ou .xls");
			}

			// Padrão regex para encontrar tags no formato @TEST_QAR-XXXX
			Pattern padrao = Pattern.compile("@TEST_QAR-\\d+");

			// Buscar apenas na planilha especificada
			Sheet planilha = workbook.getSheet(nomePlanilhaAlvo);

			if (planilha == null) {
				System.err.println("Planilha '" + nomePlanilhaAlvo + "' não encontrada!");
			} else {
				// Iterar por todas as linhas da planilha
				for (Row linha : planilha) {
					// Iterar por todas as células da linha
					for (Cell celula : linha) {
						// Verificar se a célula contém texto - forma compatível com todas as versões do POI
						if (celula != null) {
							String valorCelula = "";

							// Obter o valor como String independentemente do tipo de célula
							try {
								switch (celula.getCellType()) {
									case STRING:
										valorCelula = celula.getStringCellValue();
										break;
									case NUMERIC:
										valorCelula = String.valueOf(celula.getNumericCellValue());
										break;
									case BOOLEAN:
										valorCelula = String.valueOf(celula.getBooleanCellValue());
										break;
									case FORMULA:
										try {
											valorCelula = celula.getStringCellValue();
										} catch (Exception e) {
											try {
												valorCelula = String.valueOf(celula.getNumericCellValue());
											} catch (Exception ex) {
												// Ignorar erro se não conseguir obter valor
											}
										}
										break;
									default:
										// Ignorar outros tipos de célula
										break;
								}
							} catch (Exception e) {
								// Ignorar erros ao obter valor da célula
								continue;
							}

							// Procurar tags no texto da célula
							if (!valorCelula.isEmpty()) {
								Matcher matcher = padrao.matcher(valorCelula);
								while (matcher.find()) {
									String tag = matcher.group();
									if (!tags.contains(tag)) {
										tags.add(tag);
									}
								}
							}
						}
					}
				}

				// Fechar recursos
				workbook.close();
				arquivoExcel.close();

				// Concatenar as tags encontradas com "or" entre elas
				String resultado = concatenarTags(tags);

				// Salvar o resultado no arquivo JSON
				if (!tags.isEmpty()) {
					atualizarTagsNoJson(caminhoArquivoJson, resultado, execution);
					System.out.println("Tags atualizadas com sucesso no arquivo JSON: " + caminhoArquivoJson);
				}
			}

		} catch (IOException e) {
			System.err.println("Erro ao processar o arquivo Excel: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Erro ao processar o arquivo JSON: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Concatena as tags encontradas com "or" entre elas
	 * @param tags Lista de tags encontradas
	 * @return String concatenada
	 */
	private static String concatenarTags(List<String> tags) {
		if (tags.isEmpty()) {
			return "Nenhuma tag encontrada.";
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < tags.size(); i++) {
			stringBuilder.append(tags.get(i));

			// Adicionar "or" apenas se não for o último elemento
			if (i < tags.size() - 1) {
				stringBuilder.append(" or ");
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Atualiza o campo "tags" existente dentro de runner.cucumber no arquivo JSON
	 * usando manipulação direta de string, sem depender de bibliotecas JSON.
	 * @param caminhoArquivo Caminho para o arquivo JSON
	 * @param tagsString String com as tags concatenadas
	 * @throws IOException Se ocorrer um erro de I/O
	 */
	private static void atualizarTagsNoJson(String caminhoArquivo, String tagsString, String execution) throws IOException {
		// Ler todo o conteúdo do arquivo como string
		Path path = Paths.get(caminhoArquivo);

		// Verificar se o arquivo existe
		if (!Files.exists(path)) {
			// Verificar se o diretório existe, se não, criar
			Files.createDirectories(path.getParent());
			throw new IOException("Arquivo JSON não encontrado: " + caminhoArquivo);
		}

		// Ler todo o conteúdo do arquivo
		String conteudoJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

		// Usar regex para localizar o campo "tags" dentro de runner.cucumber
		// Esta expressão regular busca o padrão "tags":\s*"[^"]*"
		// que representa um campo "tags" seguido por : e um valor entre aspas
		String padrao = "\"tags\"\\s*:\\s*\"[^\"]*\"";
		Pattern pattern = Pattern.compile(padrao);
		Matcher matcher = pattern.matcher(conteudoJson);

		String conteudoModificado;
		if (matcher.find()) {
			// Substituir apenas o valor atual de "tags" pelo novo valor
			conteudoModificado = matcher.replaceFirst("\"tags\": \"" + execution+" and "+"("+tagsString+")" + "\"");
		} else {
			throw new IOException("Campo 'tags' não encontrado dentro de runner.cucumber no JSON");
		}

		// Escrever o conteúdo modificado de volta para o arquivo
		Files.write(path, conteudoModificado.getBytes(StandardCharsets.UTF_8));
	}
}