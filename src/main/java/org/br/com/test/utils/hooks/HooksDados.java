package org.br.com.test.utils.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;
import org.br.com.core.support.Context;
import org.br.com.core.support.logger.LogFormatter;
import org.br.com.test.sheets.cadastro.CadastroDataSheet;
import org.br.com.test.sheets.login.LoginDataSheet;
import org.br.com.test.sheets.login.LoginModel;
import org.junit.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.br.com.test.manager.TokenManager;
import org.br.com.test.manager.UsuarioManager;

public class HooksDados {

    @Getter
    private static Map<String, String> tagsCenario = new HashMap<>();

    @Before
    public void pegarTagsCenario(Scenario cenario) {
        tagsCenario.clear();
        tagsCenario.putAll(extrairTagsDoCenario(cenario.getSourceTagNames()));

        // Carregar massa de dados do Excel
        carregarMassaUsuario(cenario);
    }

    private void carregarMassaUsuario(Scenario cenario) {
        String tagCenario = cenario.getSourceTagNames().stream()
                .filter(t -> t.startsWith("@CT-"))
                .findFirst()
                .orElse(null);

        if (tagCenario == null) {
            System.out.println("ℹ️  Nenhuma tag @CT- encontrada no cenário: " + cenario.getName());
            return; // Sai do método se não houver tag de cenário
        }

        String idCenario = tagCenario.replace("@CT-", "");
        Context.setIdUsuario(idCenario); // Consider renaming this to setIdCenario for clarity

        try {
            System.out.println("🆔 ID Cenário: " + idCenario);
            LoginDataSheet sheet = new LoginDataSheet(idCenario);
            LoginModel usuario = sheet.getData();
            Context.setData(usuario);

            System.out.println("✅ Massa de Dados Carregada:");
            System.out.println("   📧 Email: " + usuario.getEmail());
            System.out.println("   🔑 Senha: " + usuario.getSenha());

            // Tentar carregar dados de cadastro se houver um ID_MASSA
            String idMassa = sheet.getIdMassa();
            if (idMassa != null && !idMassa.isEmpty()) {
                CadastroDataSheet cadastroSheet = new CadastroDataSheet(idMassa);
                // Store the sheet itself for more flexible access in steps
                Context.put("cadastroSheet", cadastroSheet);
                System.out.println("   👤 Nome: " + cadastroSheet.getNomeCompleto());
                System.out.println("   🔖 Nome Usuário: " + cadastroSheet.getNomeUsuario());
                System.out.println("   🆔 ID Usuário: " + cadastroSheet.getIdUsuario());
            }

        } catch (Exception e) {
            // IMPROVEMENT: Fail the test immediately if data cannot be loaded.
            // This prevents misleading failures later on.
            String errorMessage = "❌ FALHA CRÍTICA: Não foi possível carregar a massa de dados para o cenário " + idCenario + ". Causa: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            Assert.fail(errorMessage); // This will stop the test with a clear message.
        }
    }

    // ... (o resto da classe permanece o mesmo)
    private Map<String, String> extrairTagsDoCenario(Collection<String> sourceTagNames) {
        Map<String, String> tags = new HashMap<>();
        String ctTag = null;
        String specificTag = null;

        for (String tagName : sourceTagNames) {
            if (tagName.startsWith("@CT-")) {
                ctTag = tagName.substring(1); // Remove o "@"
            } else if (tagName.startsWith("@") && !tagName.equals("@all") && !tagName.equals("@Home")) {
                specificTag = tagName.substring(1); // Remove o "@"
            }
        }
        if (ctTag != null && specificTag != null) {
            tags.put(ctTag, specificTag);
        }
        return tags;
    }

    public static String getTagCenario() {
        return tagsCenario.keySet().stream().findFirst().orElse(null);
    }

    private String mascararSenha(String senha) {
        if (senha == null || senha.length() < 4) {
            return senha;
        }
        return "***" + senha.substring(senha.length() - 4);
    }

    private String mascararIdParaLog(String id) {
        if (id == null || id.length() < 4) {
            return id;
        }
        return "***" + id.substring(id.length() - 4);
    }

    @Before("@LimparEstadoAntes")
    public void beforeExclusionScenario() {
        LogFormatter.logStep("HOOK (@LimparEstadoAntes): Limpando o estado do TokenManager e UsuarioManager especificamente para o cenário de exclusão.");
        TokenManager.remove();
        UsuarioManager.remove();
    }
}