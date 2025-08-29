package org.br.com.core.filter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.MultiPartSpecification;
import lombok.extern.log4j.Log4j2;
import org.br.com.core.support.logger.LogFormatter;

/**
 * <p>
 * Classe {@code PDFLoggerFilter} implementa a interface {@link Filter} do RestAssured
 * para interceptar requisições e respostas HTTP e logá-las em um documento PDF.
 * </p>
 * <p>
 * Esta classe é responsável por capturar detalhes da requisição (método, URI,
 * parâmetros, cabeçalhos, corpo) e da resposta (status, corpo) e organizá-los
 * em tabelas dentro de um arquivo PDF, servindo como evidência de testes.
 * </p>
 * <p>
 * Utiliza a biblioteca iText para a geração do PDF e Lombok para anotações de log.
 * </p>
 *
 * @see Filter
 * @see Log4j2
 * @see PdfDocument
 * @see Document
 */
@Log4j2
public class PDFLoggerFilter implements Filter {

	private final String scenarioName;
	private final String scenarioId;
	private final List<Table> tables;
	// TODO: A classe PDFLoggerFilter é instanciada em classes de teste que utilizam RestAssured para logar as interações HTTP.

	/**
	 * Construtor para inicializar o filtro com o nome e ID do cenário de teste.
	 *
	 * @param scenarioName O nome do cenário de teste.
	 * @param scenarioId O ID único do cenário de teste.
	 */
	public PDFLoggerFilter(String scenarioName, String scenarioId) {
		this.scenarioName = scenarioName;
		this.scenarioId = scenarioId;
		this.tables = new ArrayList<Table>();
	}

	/**
	 * <p>
	 * Intercepta a requisição e a resposta HTTP para logar seus detalhes.
	 * Este método é chamado automaticamente pelo RestAssured quando o filtro é aplicado.
	 * </p>
	 *
	 * @param requestSpec A especificação da requisição filtrável.
	 * @param responseSpec A especificação da resposta filtrável.
	 * @param ctx O contexto do filtro.
	 * @return A resposta da requisição.
	 * @see Filter#filter(FilterableRequestSpecification, FilterableResponseSpecification, FilterContext)
	 */
	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
						   FilterContext ctx) {
		Response response = ctx.next(requestSpec, responseSpec);
		logRequest(requestSpec, response);
		return response;
	}
	// TODO: O método filter é o ponto de entrada principal do filtro, sendo invocado pelo RestAssured.

	/**
	 * <p>
	 * Coleta os detalhes da requisição e da resposta e os adiciona a uma nova tabela.
	 * Esta tabela será posteriormente adicionada ao documento PDF.
	 * </p>
	 *
	 * @param requestSpec A especificação da requisição.
	 * @param response A resposta da requisição.
	 */
	private void logRequest(FilterableRequestSpecification requestSpec, Response response) {

		// request
		String method = requestSpec.getMethod();
		String url = requestSpec.getURI();
		String proxy = "";
		if (requestSpec.getProxySpecification() != null) {
			proxy = requestSpec.getProxySpecification().getHost() + ":" + requestSpec.getProxySpecification().getPort();
		}

		StringBuilder requestParamsSb = new StringBuilder();
		for (Entry<String, String> entry : requestSpec.getRequestParams().entrySet()) {
			requestParamsSb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		StringBuilder queryParamsSb = new StringBuilder();
		for (Entry<String, String> entry : requestSpec.getQueryParams().entrySet()) {
			queryParamsSb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		StringBuilder formParamsSb = new StringBuilder();
		for (Entry<String, String> entry : requestSpec.getFormParams().entrySet()) {
			formParamsSb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		StringBuilder pathParamsSb = new StringBuilder();
		for (Entry<String, String> entry : requestSpec.getPathParams().entrySet()) {
			pathParamsSb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		StringBuilder headers = new StringBuilder();
		for (Header header : requestSpec.getHeaders().asList()) {
			headers.append(header.getName()).append("=").append(header.getValue()).append("\n");
		}

		StringBuilder cookies = new StringBuilder();
		for (Cookie cookie : requestSpec.getCookies().asList()) {
			cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append("\n");
		}

		StringBuilder multipartParams = new StringBuilder();
		for (MultiPartSpecification m : requestSpec.getMultiPartParams()) {
			multipartParams.append(m.getFileName()).append("\n");
			multipartParams.append(m.getMimeType()).append("\n");
			multipartParams.append(m.getContent()).append("\n");
		}

		String requestBody = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "";

		// response
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();

		Table table = new Table(UnitValue.createPercentArray(new float[] { 20, 80 }));
		table.setWidth(UnitValue.createPercentValue(100));
		table.setFixedLayout();

		table.addCell("Request method");
		table.addCell(method);
		table.addCell("Request URI");
		table.addCell(url);

		if (!proxy.isEmpty()) {
			table.addCell("Proxy");
			table.addCell(proxy);
		}
		if (!requestParamsSb.toString().isEmpty()) {
			table.addCell("Request params");
			table.addCell(requestParamsSb.toString());
		}
		if (!queryParamsSb.toString().isEmpty()) {
			table.addCell("Query params");
			table.addCell(queryParamsSb.toString());
		}
		if (!formParamsSb.toString().isEmpty()) {
			table.addCell("Form params");
			table.addCell(formParamsSb.toString());
		}

		if (!pathParamsSb.toString().isEmpty()) {
			table.addCell("Path params");
			table.addCell(pathParamsSb.toString());
		}

		table.addCell("Headers");
		table.addCell(headers.toString());

		if (!cookies.toString().isEmpty()) {
			table.addCell("Cookies");
			table.addCell(cookies.toString());
		}

		if (!multipartParams.toString().isEmpty()) {
			table.addCell("Multiparts");
			table.addCell(multipartParams.toString());
		}

		table.addCell("Request Body");
		table.addCell(requestBody);
		table.addCell("Status Code");
		table.addCell(Integer.toString(statusCode));
		table.addCell("Response Body");
		table.addCell(responseBody);

		tables.add(table);

	}
	// TODO: O método logRequest é uma função auxiliar que organiza os dados da requisição e resposta em uma estrutura de tabela.

	/**
	 * <p>
	 * Fecha o documento PDF, salvando todas as requisições e respostas logadas.
	 * </p>
	 * @param passed Um booleano indicando se o cenário de teste passou (true) ou falhou (false).
	 * @return O caminho completo do arquivo PDF gerado.
	 */
	public String closeDocument(Boolean passed) {
		log.info("gerando a evidencia");
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss_SSS");
		String date = sdf.format(d);
		String scenarioNameRemovedChar = this.scenarioName.replaceAll("[\\\\/:*?\"<>|]", "");
		String pdfPath = "evidence/" + this.scenarioId + "_" + scenarioNameRemovedChar + "_" + date + ".pdf";
		File evidenceDir = new File("evidence");
		if (!evidenceDir.exists()) {
			evidenceDir.mkdirs();
		}

		try (PdfWriter writer = new PdfWriter(pdfPath)) {
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc);

			Paragraph titulo = new Paragraph(scenarioId + ": " + scenarioName).setBold()
					.setTextAlignment(TextAlignment.CENTER);

			document.add(titulo);

			String passedStr = passed ? "PASSOU" : "FALHOU";
			Color statusColor = passed ? new DeviceRgb(0, 128, 0) : new DeviceRgb(255, 0, 0);

			Paragraph status = new Paragraph("Status: ").add(new Text(passedStr).setFontColor(statusColor))
					.setTextAlignment(TextAlignment.RIGHT);

			document.add(status);

			int count = 1;
			for (Table table : tables) {
				Paragraph tableCount = new Paragraph("Requisição: " + count).setBold()
						.setTextAlignment(TextAlignment.CENTER);

				document.add(tableCount);
				document.add(table);

				if (count < tables.size()) {
					document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
				}
				count++;
			}

			document.close();
		} catch (Exception e) {
			LogFormatter.logError("falha ao gerar a evidencia: " + e.getMessage());
		}

		return pdfPath;

	}

	// TODO: O método closeDocument é chamado ao final de um cenário de teste para finalizar e salvar o arquivo PDF de evidências.
}
