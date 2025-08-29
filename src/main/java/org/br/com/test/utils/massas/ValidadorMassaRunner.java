package org.br.com.test.utils.massas;

import com.github.javafaker.Faker;
import io.restassured.response.Response;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;

public class ValidadorMassaRunner {

	private static final Faker faker = new Faker(Locale.forLanguageTag("pt-BR"));
	private static final String BASE_URL = "http://localhost:3000";
	private static final String ENDPOINT_USUARIOS = "/usuarios";
	private static final String ENDPOINT_LOGIN = "/auth/login";

	public static void main(String[] args) {
		System.out.println("🔍 Validador de Massa de Dados");
		System.out.println("===============================");

		String fileName = "output/fakerCsv.csv";
		File file = new File(fileName);

		if (!file.exists()) {
			System.out.println("❌ Arquivo " + fileName + " não encontrado!");
			System.out.println("💡 Execute primeiro: mvn exec:java -Dexec.mainClass=\"org.br.com.test.utils.massas.GeradorMassaRunner\"");
			return;
		}

		List<MassaData> massas = carregarMassasDoCSV(fileName);

		if (massas.isEmpty()) {
			System.out.println("❌ Nenhuma massa encontrada no arquivo CSV!");
			return;
		}

		System.out.println("📊 Total de massas encontradas: " + massas.size());
		System.out.println("🔍 Iniciando validação...");
		System.out.println("================================================");

		int sucessos = 0;
		int falhas = 0;

		// Validar cada massa e atualizar o status
		for (int i = 0; i < massas.size(); i++) {
			MassaData massa = massas.get(i);
			System.out.println("📋 Validando massa " + (i + 1) + "/" + massas.size() + ":");
			System.out.println("   👤 Nome: " + massa.nomeCompleto);
			System.out.println("   🔖 Usuário: " + massa.nomeUsuario);
			System.out.println("   📧 Email: " + massa.email);
			System.out.println("   🔑 Senha: " + massa.senha);
			System.out.println("   🆔 ID: " + massa.idUsuario);

			// Testar se o usuário existe fazendo login
			boolean loginSucesso = testarLogin(massa.email, massa.senha);

			// Atualizar status da massa
			massa.loginValidado = loginSucesso;
			massa.dataValidacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			if (loginSucesso) {
				System.out.println("   ✅ LOGIN SUCESSO - Usuário válido!");
				sucessos++;
			} else {
				System.out.println("   ❌ LOGIN FALHOU - Usuário inválido ou não existe!");
				falhas++;
			}

			System.out.println("   ---");

			// Pequena pausa entre testes
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		// Salvar CSV atualizado com validação
		salvarCSVAtualizado(fileName, massas);

		System.out.println("================================================");
		System.out.println("📊 RESULTADO DA VALIDAÇÃO:");
		System.out.println("   ✅ Sucessos: " + sucessos);
		System.out.println("   ❌ Falhas: " + falhas);
		System.out.println("   📈 Taxa de sucesso: " + String.format("%.1f", (double) sucessos / massas.size() * 100) + "%");
		System.out.println("📁 CSV atualizado com status de validação!");

		if (falhas > 0) {
			System.out.println("⚠️ ATENÇÃO: " + falhas + " massas falharam na validação!");
			System.out.println("💡 Recomendação: Gerar nova massa com dados frescos");
		} else {
			System.out.println("🎉 TODAS as massas estão válidas!");
		}
	}

	private static boolean testarLogin(String email, String senha) {
		try {
			LoginRequest loginRequest = new LoginRequest(email, senha);

			Response response = given()
				.header("Content-Type", "application/json")
				.baseUri(BASE_URL)
				.body(loginRequest)
				.when()
				.post(ENDPOINT_LOGIN);

			return response.getStatusCode() == 200;

		} catch (Exception e) {
			return false;
		}
	}

	private static List<MassaData> carregarMassasDoCSV(String fileName) {
		List<MassaData> massas = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			boolean primeiraLinha = true;

			while ((line = reader.readLine()) != null) {
				if (primeiraLinha) {
					primeiraLinha = false;
					continue; // Pular cabeçalho
				}

				String[] campos = line.split(",");
				if (campos.length >= 6) {
					MassaData massa = new MassaData();
					massa.nomeCompleto = campos[0];
					massa.nomeUsuario = campos[1];
					massa.email = campos[2];
					massa.idUsuario = campos[3];  // ID_USUARIO está na posição 3
					massa.senha = campos[4];      // SENHA está na posição 4
					massa.dataGeracao = campos[5];

					// Verificar se já tem dados de validação
					if (campos.length >= 8) {
						massa.loginValidado = Boolean.parseBoolean(campos[6]);
						massa.dataValidacao = campos[7];
					}

					massas.add(massa);
				}
			}
		} catch (IOException e) {
			System.err.println("❌ Erro ao ler arquivo CSV: " + e.getMessage());
		}

		return massas;
	}

	private static void salvarCSVAtualizado(String fileName, List<MassaData> massas) {
		try (FileWriter writer = new FileWriter(fileName, false)) {
			// Escrever cabeçalho atualizado (mantendo ordem original + validação)
			writer.write("NOME_COMPLETO,NOME_USUARIO,EMAIL,ID_USUARIO,SENHA,DATA_GERACAO,LOGIN_VALIDADO,DATA_VALIDACAO\n");

			// Escrever dados atualizados
			for (MassaData massa : massas) {
				String linha = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
					massa.nomeCompleto,
					massa.nomeUsuario,
					massa.email,
					massa.idUsuario,
					massa.senha,
					massa.dataGeracao,
					massa.loginValidado,
					massa.dataValidacao != null ? massa.dataValidacao : ""
				);
				writer.write(linha);
			}

		} catch (IOException e) {
			System.err.println("❌ Erro ao salvar CSV atualizado: " + e.getMessage());
		}
	}

	private static class MassaData {
		String nomeCompleto;
		String nomeUsuario;
		String email;
		String senha;
		String idUsuario;
		String dataGeracao;
		boolean loginValidado = false;
		String dataValidacao;
	}

	private static class LoginRequest {
		private String email;
		private String senha;

		public LoginRequest(String email, String senha) {
			this.email = email;
			this.senha = senha;
		}

		// Getters para serialização JSON
		public String getEmail() { return email; }
		public String getSenha() { return senha; }
	}
}