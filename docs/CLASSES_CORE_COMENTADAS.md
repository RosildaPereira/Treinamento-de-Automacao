# 📚 **CLASSES DO PACOTE CORE/ - COMENTÁRIOS DETALHADOS**

## 🎯 **VISÃO GERAL DO PACOTE CORE/**

O pacote `org.br.com.core` é o **núcleo fundamental** do framework de automação. Contém classes essenciais para **criptografia**, **tratamento de exceções**, **filtros de captura**, **processamento de tags**, **suporte geral** e **gerenciamento de tokens**. É a base que sustenta todo o funcionamento do framework.

---

## 🔐 **1. ENCRYPTION - Criptografia de Dados**

### **Encryption.java - Utilitário de Criptografia AES**

**📍 Localização**: `src/main/java/org/br/com/core/encryption/Encryption.java`

```java
package org.br.com.core.encryption;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * ============================================================================
 * CLASSE UTILITÁRIA: Encryption
 * ============================================================================
 * 
 * PROPÓSITO:
 * Fornece métodos estáticos para criptografia e descriptografia de dados
 * usando o algoritmo AES (Advanced Encryption Standard). Utilizada para
 * proteger informações sensíveis como senhas e tokens nos testes.
 * 
 * QUANDO USAR:
 * - Criptografar senhas antes de armazenar em planilhas
 * - Proteger tokens de acesso em arquivos de configuração
 * - Mascarar dados sensíveis em logs
 * - Segurança em dados de teste compartilhados
 * 
 * COMO INSTANCIAR:
 * Não é instanciada - todos os métodos são estáticos:
 * 
 * // Criptografar dados
 * String dadosCriptografados = Encryption.criptografar("senha123", "minhaChave");
 * 
 * // Descriptografar dados
 * String dadosOriginais = Encryption.descriptografar(dadosCriptografados, "minhaChave");
 * 
 * MOTIVOS DE USO:
 * 1. Segurança: Protege dados sensíveis em arquivos de teste
 * 2. Compliance: Atende requisitos de segurança corporativa
 * 3. Padronização: Centraliza lógica de criptografia
 * 4. Simplicidade: Interface fácil de usar
 * ============================================================================
 */
public class Encryption {

    /**
     * MÉTODO: criptografar(String texto, String chaveSecreta)
     * 
     * PROPÓSITO:
     * Criptografa um texto usando algoritmo AES e retorna o resultado
     * codificado em Base64. Transforma dados legíveis em formato seguro.
     * 
     * PARÂMETROS:
     * @param texto - Texto a ser criptografado (ex: "senha123")
     * @param chaveSecreta - Chave para criptografia (deve ter tamanho adequado)
     * 
     * RETORNO:
     * @return String - Texto criptografado em Base64
     * 
     * EXCEÇÕES:
     * @throws Exception - Em caso de erro na criptografia
     * 
     * QUANDO USAR:
     * - Antes de salvar senhas em planilhas Excel
     * - Para proteger tokens em arquivos de configuração
     * - Ao armazenar dados sensíveis temporariamente
     * - Em logs que não devem expor informações reais
     * 
     * COMO USAR:
     * try {
     *     String senha = "minhasenha123";
     *     String chave = "1234567890123456"; // 16 bytes para AES
     *     String senhaCriptografada = Encryption.criptografar(senha, chave);
     *     
     *     System.out.println("Senha original: " + senha);
     *     System.out.println("Senha criptografada: " + senhaCriptografada);
     *     
     *     // Salvar senhaCriptografada na planilha
     *     
     * } catch (Exception e) {
     *     System.err.println("Erro na criptografia: " + e.getMessage());
     * }
     * 
     * MOTIVOS:
     * 1. Segurança: Protege dados contra acesso não autorizado
     * 2. Reversibilidade: Permite recuperar dados originais
     * 3. Padrão: Usa AES, algoritmo padrão da indústria
     * 4. Portabilidade: Base64 facilita armazenamento
     * 
     * FLUXO INTERNO:
     * 1. Cria SecretKeySpec com a chave fornecida e algoritmo AES
     * 2. Obtém instância do Cipher para AES
     * 3. Inicializa cipher em modo ENCRYPT_MODE
     * 4. Executa criptografia no texto fornecido
     * 5. Codifica resultado em Base64 para facilitar armazenamento
     * 
     * IMPORTANTES:
     * - Chave deve ter tamanho adequado (16, 24 ou 32 bytes)
     * - Mesmo texto + mesma chave = mesmo resultado
     * - Base64 torna resultado seguro para texto/XML
     * 
     * EXEMPLO PRÁTICO:
     * // Criptografar senha para planilha
     * String senhaPlana = "Admin@2024";
     * String chaveSegura = "MySecretKey12345"; // 16 caracteres
     * String senhaSegura = Encryption.criptografar(senhaPlana, chaveSegura);
     * // Resultado: algo como "kQtPnX8JzMQr/4vF3+2xzA=="
     */
    public static String criptografar(String texto, String chaveSecreta) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }

    /**
     * MÉTODO: descriptografar(String textoCriptografado, String chaveSecreta)
     * 
     * PROPÓSITO:
     * Descriptografa um texto que foi criptografado anteriormente,
     * retornando o conteúdo original. Processo reverso da criptografia.
     * 
     * PARÂMETROS:
     * @param textoCriptografado - Texto em Base64 que foi criptografado
     * @param chaveSecreta - Mesma chave usada na criptografia
     * 
     * RETORNO:
     * @return String - Texto original descriptografado
     * 
     * EXCEÇÕES:
     * @throws Exception - Em caso de erro na descriptografia
     * 
     * QUANDO USAR:
     * - Ao ler senhas criptografadas de planilhas
     * - Para recuperar tokens protegidos
     * - Durante validação de dados criptografados
     * - Em logs que precisam dos dados originais
     * 
     * COMO USAR:
     * try {
     *     String senhaCriptografada = "kQtPnX8JzMQr/4vF3+2xzA=="; // Do banco/planilha
     *     String chave = "1234567890123456"; // Mesma chave da criptografia
     *     String senhaOriginal = Encryption.descriptografar(senhaCriptografada, chave);
     *     
     *     System.out.println("Senha criptografada: " + senhaCriptografada);
     *     System.out.println("Senha original: " + senhaOriginal);
     *     
     *     // Usar senhaOriginal no teste
     *     
     * } catch (Exception e) {
     *     System.err.println("Erro na descriptografia: " + e.getMessage());
     * }
     * 
     * MOTIVOS:
     * 1. Recuperação: Permite acessar dados originais
     * 2. Flexibilidade: Trabalha com dados criptografados
     * 3. Segurança: Requer chave correta para funcionar
     * 4. Integração: Facilita uso em testes automatizados
     * 
     * FLUXO INTERNO:
     * 1. Cria SecretKeySpec com a chave fornecida e algoritmo AES
     * 2. Obtém instância do Cipher para AES
     * 3. Inicializa cipher em modo DECRYPT_MODE
     * 4. Decodifica texto Base64 para bytes
     * 5. Executa descriptografia nos bytes
     * 6. Converte resultado para String
     * 
     * IMPORTANTES:
     * - Chave DEVE ser exatamente a mesma da criptografia
     * - Texto deve estar em formato Base64 válido
     * - Qualquer alteração no texto criptografado causa erro
     * 
     * TRATAMENTO DE ERROS COMUNS:
     * - Chave incorreta: javax.crypto.BadPaddingException
     * - Base64 inválido: IllegalArgumentException
     * - Texto corrompido: AEADBadTagException
     * 
     * EXEMPLO PRÁTICO:
     * // Ler senha da planilha e descriptografar para uso
     * String senhaExcel = lerCampoPlanilha("senha_criptografada");
     * String chaveConfig = lerChaveDoArquivoConfig();
     * 
     * try {
     *     String senhaReal = Encryption.descriptografar(senhaExcel, chaveConfig);
     *     // Usar senhaReal no login do teste
     *     fazerLogin(usuario, senhaReal);
     * } catch (Exception e) {
     *     throw new RuntimeException("Falha ao descriptografar senha: " + e.getMessage());
     * }
     */
    public static String descriptografar(String textoCriptografado, String chaveSecreta) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(chaveSecreta.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] textoDecodificado = Base64.getDecoder().decode(textoCriptografado);
        byte[] textoDescriptografado = cipher.doFinal(textoDecodificado);
        return new String(textoDescriptografado);
    }
}
```

---

## ⚠️ **2. EXCEPTIONS - Tratamento de Erros**

### **DataException.java - Exceção Personalizada para Dados**

**📍 Localização**: `src/main/java/org/br/com/core/exceptions/DataException.java`

```java
package org.br.com.core.exceptions;

/**
 * ============================================================================
 * EXCEÇÃO PERSONALIZADA: DataException
 * ============================================================================
 * 
 * PROPÓSITO:
 * Exceção RuntimeException personalizada para tratar especificamente
 * erros relacionados ao acesso e manipulação de dados no framework.
 * Facilita o diagnóstico de problemas com planilhas, queries e conexões.
 * 
 * QUANDO USAR:
 * - Erros ao conectar com arquivos Excel
 * - Falhas na execução de queries SQL nas planilhas
 * - Problemas de formato de dados
 * - Campos não encontrados ou inválidos
 * 
 * COMO INSTANCIAR:
 * throw new DataException("Erro específico de dados");
 * throw new DataException(exception);
 * throw new DataException("Contexto do erro", exception);
 * 
 * MOTIVOS DE USO:
 * 1. Especificidade: Identifica claramente erros de dados
 * 2. Rastreabilidade: Facilita debug de problemas
 * 3. Padronização: Centraliza tratamento de erros de dados
 * 4. Contexto: Fornece informações específicas do erro
 * ============================================================================
 */
public class DataException extends RuntimeException {

    /**
     * ATRIBUTO: serialVersionUID
     * 
     * PROPÓSITO: Identificador único para serialização da classe
     * TIPO: long (static final)
     * MOTIVO: Garantia de compatibilidade em serialização
     * USO: Requerido por todas as classes Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * CONSTRUTOR: DataException(String message)
     * 
     * PROPÓSITO:
     * Cria uma exceção com apenas uma mensagem descritiva.
     * Usado quando o erro é conhecido e não há exceção subjacente.
     * 
     * PARÂMETROS:
     * @param message - Mensagem descritiva do erro
     * 
     * QUANDO USAR:
     * - Validações que falham (ex: campo obrigatório vazio)
     * - Regras de negócio violadas
     * - Estados inconsistentes detectados
     * - Erros lógicos específicos do framework
     * 
     * COMO USAR:
     * if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
     *     throw new DataException("Email do usuário é obrigatório para o teste");
     * }
     * 
     * if (!arquivo.exists()) {
     *     throw new DataException("Arquivo de dados não encontrado: " + arquivo.getPath());
     * }
     * 
     * MOTIVOS:
     * 1. Clareza: Mensagem específica sobre o problema
     * 2. Simplicidade: Para erros sem causa subjacente
     * 3. Controle: Erros detectados pela própria aplicação
     * 
     * EXEMPLO PRÁTICO:
     * // Validação de dados de entrada
     * public void validarDadosUsuario(UsuarioModel usuario) {
     *     if (usuario == null) {
     *         throw new DataException("Modelo de usuário não pode ser nulo");
     *     }
     *     if (usuario.getEmail() == null) {
     *         throw new DataException("Email é obrigatório para criar usuário");
     *     }
     *     if (!usuario.getEmail().contains("@")) {
     *         throw new DataException("Email deve ter formato válido: " + usuario.getEmail());
     *     }
     * }
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * CONSTRUTOR: DataException(Throwable cause)
     * 
     * PROPÓSITO:
     * Cria uma exceção encapsulando outra exceção como causa.
     * Usado quando se quer transformar uma exceção técnica
     * em uma exceção de domínio mais específica.
     * 
     * PARÂMETROS:
     * @param cause - Exceção original que causou o problema
     * 
     * QUANDO USAR:
     * - Capturar exceções de baixo nível (SQLException, FilloException)
     * - Transformar exceções técnicas em erros de domínio
     * - Manter stack trace original para debug
     * - Simplificar tratamento de erros para quem chama
     * 
     * COMO USAR:
     * try {
     *     conexao.executeQuery("SELECT * FROM planilha");
     * } catch (FilloException e) {
     *     throw new DataException(e); // Encapsula exceção técnica
     * }
     * 
     * try {
     *     File arquivo = new File(caminho);
     *     // operações com arquivo
     * } catch (IOException e) {
     *     throw new DataException(e); // Transforma IOException em DataException
     * }
     * 
     * MOTIVOS:
     * 1. Preservação: Mantém stack trace completo
     * 2. Simplificação: Unifica diferentes tipos de erro
     * 3. Abstração: Esconde complexidade técnica
     * 4. Rastreabilidade: Permite debug até a causa raiz
     * 
     * EXEMPLO PRÁTICO:
     * // Encapsular erros de conexão com planilha
     * public Recordset executarQuery(String query) {
     *     try {
     *         return dataReader.executeQuery(query);
     *     } catch (FilloException e) {
     *         // Transforma erro técnico em erro de domínio
     *         throw new DataException(e);
     *     } catch (SQLException e) {
     *         // Unifica diferentes tipos de erro de dados
     *         throw new DataException(e);
     *     }
     * }
     */
    public DataException(Throwable cause) {
        super(cause);
    }

    /**
     * CONSTRUTOR: DataException(String message, Throwable e)
     * 
     * PROPÓSITO:
     * Cria uma exceção com mensagem personalizada E a exceção original.
     * Combina contexto específico com preservação do erro técnico.
     * Mais informativo e útil para debug.
     * 
     * PARÂMETROS:
     * @param message - Mensagem contextual sobre o erro
     * @param e - Exceção original que causou o problema
     * 
     * QUANDO USAR:
     * - Adicionar contexto específico a erros técnicos
     * - Explicar onde/quando o erro ocorreu
     * - Fornecer informações de debug mais ricas
     * - Casos onde tanto a mensagem quanto a causa são importantes
     * 
     * COMO USAR:
     * try {
     *     recordset = dataReader.executeQuery(query);
     * } catch (FilloException e) {
     *     String mensagem = "Erro ao executar query na planilha: " + nomeArquivo;
     *     throw new DataException(mensagem, e);
     * }
     * 
     * try {
     *     campo = recordset.getField(nomeCampo);
     * } catch (Exception e) {
     *     String contexto = "Campo '" + nomeCampo + "' não encontrado na planilha";
     *     throw new DataException(contexto, e);
     * }
     * 
     * MOTIVOS:
     * 1. Contexto: Explica onde/quando o erro ocorreu
     * 2. Preservação: Mantém exceção original para debug
     * 3. Informação: Combina mensagem custom + stack trace
     * 4. Debug: Facilita identificação e correção do problema
     * 
     * VANTAGENS SOBRE OUTROS CONSTRUTORES:
     * - Mais informativo que só message
     * - Mais contextual que só cause
     * - Combina o melhor dos dois mundos
     * 
     * EXEMPLO PRÁTICO:
     * // Adicionar contexto rico a erros de dados
     * public String lerCampoObrigatorio(String nomeCampo) {
     *     try {
     *         setData(); // Carrega dados da planilha
     *         String valor = getField(nomeCampo);
     *         
     *         if (valor == null || valor.trim().isEmpty()) {
     *             throw new DataException("Campo obrigatório está vazio: " + nomeCampo);
     *         }
     *         
     *         return valor;
     *         
     *     } catch (FilloException e) {
     *         String mensagem = String.format(
     *             "Falha ao ler campo '%s' da planilha '%s'", 
     *             nomeCampo, nomeArquivo
     *         );
     *         throw new DataException(mensagem, e);
     *     } catch (Exception e) {
     *         String contexto = String.format(
     *             "Erro inesperado ao processar campo '%s'", 
     *             nomeCampo
     *         );
     *         throw new DataException(contexto, e);
     *     }
     * }
     * 
     * FORMATAÇÃO DE MENSAGENS ÚTEIS:
     * // Incluir informações relevantes na mensagem
     * String mensagem = String.format(
     *     "Erro na query '%s' do arquivo '%s' na linha %d", 
     *     query, arquivo, numeroLinha
     * );
     * throw new DataException(mensagem, e);
     * 
     * // Mencionar operação que estava sendo executada
     * String contexto = "Falha ao validar dados do usuário " + usuario.getId() + 
     *                  " durante execução do cenário " + nomeScenario;
     * throw new DataException(contexto, e);
     */
    public DataException(String message, Throwable e) {
        super(message, e);
    }
}
```

---

## 🎬 **3. FILTER - Filtros de Captura**

### **EvidenceFilter.java - Captura de Evidências HTTP** (já documentado anteriormente)

### **PDFLoggerFilter.java - Geração de Evidências em PDF**

**📍 Localização**: `src/main/java/org/br/com/core/filter/PDFLoggerFilter.java`

```java
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
 * ============================================================================
 * FILTRO AVANÇADO: PDFLoggerFilter
 * ============================================================================
 * 
 * PROPÓSITO:
 * Filtro REST Assured especializado em capturar TODAS as informações de
 * requisições HTTP e gerar evidências completas em formato PDF. Produz
 * documentação técnica detalhada para auditoria e análise de testes.
 * 
 * QUANDO USAR:
 * - Testes que requerem evidências formais
 * - Auditoria técnica de requisições
 * - Documentação detalhada de APIs
 * - Análise de problemas de integração
 * 
 * COMO INSTANCIAR:
 * PDFLoggerFilter filter = new PDFLoggerFilter("Cenário de Login", "CT-001");
 * 
 * Response response = given()
 *     .filter(filter)
 *     .body(loginRequest)
 *     .when()
 *     .post("/login");
 * 
 * String pathPDF = filter.closeDocument(response.statusCode() == 200);
 * 
 * MOTIVOS DE USO:
 * 1. Evidências: Documenta todas as requisições em PDF
 * 2. Auditoria: Rastreabilidade completa das chamadas
 * 3. Debug: Informações técnicas detalhadas
 * 4. Compliance: Atende requisitos de documentação
 * ============================================================================
 */
@Log4j2
public class PDFLoggerFilter implements Filter {

    // ========================================================================
    // ATRIBUTOS DA CLASSE
    // ========================================================================

    /**
     * ATRIBUTO: scenarioName
     * 
     * PROPÓSITO: Nome do cenário de teste sendo executado
     * TIPO: String (final - não muda após construção)
     * USO: Incluído no título do PDF gerado
     * EXEMPLO: "Login com credenciais válidas"
     */
    private final String scenarioName;

    /**
     * ATRIBUTO: scenarioId
     * 
     * PROPÓSITO: Identificador único do cenário (geralmente tag)
     * TIPO: String (final - não muda após construção)
     * USO: Usado no nome do arquivo PDF
     * EXEMPLO: "CT-001", "REQ-123"
     */
    private final String scenarioId;

    /**
     * ATRIBUTO: tables
     * 
     * PROPÓSITO: Lista de tabelas com dados de cada requisição
     * TIPO: List<Table> (iText PDF tables)
     * USO: Armazena informações de todas as requisições do cenário
     * CRESCIMENTO: Uma tabela por requisição HTTP
     */
    private final List<Table> tables;

    // ========================================================================
    // CONSTRUTOR
    // ========================================================================

    /**
     * CONSTRUTOR: PDFLoggerFilter(String scenarioName, String scenarioId)
     * 
     * PROPÓSITO:
     * Inicializa o filtro com informações do cenário de teste.
     * Prepara estruturas internas para capturar dados das requisições.
     * 
     * PARÂMETROS:
     * @param scenarioName - Nome descritivo do cenário
     * @param scenarioId - Identificador único (tag) do cenário
     * 
     * QUANDO USAR:
     * - No início de cada cenário que precisa de evidências PDF
     * - Para testes críticos que requerem documentação
     * - Em ambientes que exigem auditoria técnica
     * 
     * COMO USAR:
     * // No início do teste
     * PDFLoggerFilter pdfFilter = new PDFLoggerFilter(
     *     "Cadastro de usuário com dados válidos", 
     *     "CT-USER-001"
     * );
     * 
     * // Usar em todas as requisições do cenário
     * Response response1 = given().filter(pdfFilter).when().post("/usuarios");
     * Response response2 = given().filter(pdfFilter).when().get("/usuarios/" + id);
     * 
     * // Finalizar e gerar PDF
     * String caminhoArquivo = pdfFilter.closeDocument(todosSucessos);
     * 
     * MOTIVOS:
     * 1. Identificação: Nomeia claramente o PDF gerado
     * 2. Organização: Agrupa requisições do mesmo cenário
     * 3. Rastreabilidade: Liga PDF ao cenário específico
     * 4. Preparação: Inicializa estruturas de dados necessárias
     */
    public PDFLoggerFilter(String scenarioName, String scenarioId) {
        this.scenarioName = scenarioName;
        this.scenarioId = scenarioId;
        this.tables = new ArrayList<Table>();
    }

    // ========================================================================
    // MÉTODO DO INTERFACE FILTER
    // ========================================================================

    /**
     * MÉTODO: filter() - Interceptação de Requisições
     * 
     * PROPÓSITO:
     * Método principal do interface Filter. Intercepta cada requisição
     * HTTP, executa a chamada e captura todos os dados para o PDF.
     * 
     * PARÂMETROS:
     * @param requestSpec - Especificação da requisição
     * @param responseSpec - Especificação da resposta
     * @param ctx - Contexto de execução do filtro
     * 
     * RETORNO:
     * @return Response - Resposta da requisição HTTP
     * 
     * QUANDO É CHAMADO:
     * Automaticamente pelo REST Assured para cada requisição que
     * usa este filtro.
     * 
     * FLUXO INTERNO:
     * 1. Executa a requisição através do contexto
     * 2. Captura dados da requisição e resposta
     * 3. Processa e formata informações
     * 4. Adiciona tabela com dados à lista interna
     * 5. Retorna a resposta para o teste
     * 
     * MOTIVOS:
     * 1. Transparência: Não interfere na execução do teste
     * 2. Captura: Coleta todos os dados necessários
     * 3. Formatação: Organiza dados para o PDF
     * 4. Acúmulo: Adiciona dados à coleção do cenário
     */
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        logRequest(requestSpec, response);
        return response;
    }

    // ========================================================================
    // MÉTODO DE PROCESSAMENTO DE DADOS
    // ========================================================================

    /**
     * MÉTODO: logRequest() - Processamento de Dados da Requisição
     * 
     * PROPÓSITO:
     * Extrai e organiza TODAS as informações técnicas da requisição
     * e resposta, formatando-as em uma tabela PDF estruturada.
     * 
     * PARÂMETROS:
     * @param requestSpec - Dados da requisição HTTP
     * @param response - Dados da resposta HTTP
     * 
     * VISIBILIDADE: private (método interno)
     * 
     * DADOS CAPTURADOS DA REQUISIÇÃO:
     * - Método HTTP (GET, POST, PUT, DELETE)
     * - URL completa
     * - Configuração de proxy (se houver)
     * - Request parameters
     * - Query parameters
     * - Form parameters
     * - Path parameters
     * - Headers HTTP
     * - Cookies
     * - Multipart data
     * - Request body
     * 
     * DADOS CAPTURADOS DA RESPOSTA:
     * - Status code
     * - Response body
     * 
     * FORMATAÇÃO:
     * - Cria tabela com 2 colunas (campo/valor)
     * - Largura 20%/80% respectivamente
     * - Inclui apenas campos que contêm dados
     * - Formata strings multi-linha adequadamente
     * 
     * FLUXO DETALHADO:
     * 1. Extrai método e URL da requisição
     * 2. Verifica e captura configuração de proxy
     * 3. Processa todos os tipos de parâmetros
     * 4. Formata headers em string legível
     * 5. Processa cookies se existirem
     * 6. Trata dados multipart para uploads
     * 7. Captura body da requisição
     * 8. Obtém status code e body da resposta
     * 9. Monta tabela PDF com todos os dados
     * 10. Adiciona tabela à lista do cenário
     */
    private void logRequest(FilterableRequestSpecification requestSpec, Response response) {
        
        // EXTRAÇÃO DE DADOS DA REQUISIÇÃO
        String method = requestSpec.getMethod();
        String url = requestSpec.getURI();
        String proxy = "";
        if (requestSpec.getProxySpecification() != null) {
            proxy = requestSpec.getProxySpecification().getHost() + ":" + requestSpec.getProxySpecification().getPort();
        }

        // PROCESSAMENTO DE PARÂMETROS
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

        // PROCESSAMENTO DE HEADERS
        StringBuilder headers = new StringBuilder();
        for (Header header : requestSpec.getHeaders().asList()) {
            headers.append(header.getName()).append("=").append(header.getValue()).append("\n");
        }

        // PROCESSAMENTO DE COOKIES
        StringBuilder cookies = new StringBuilder();
        for (Cookie cookie : requestSpec.getCookies().asList()) {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append("\n");
        }

        // PROCESSAMENTO DE MULTIPART
        StringBuilder multipartParams = new StringBuilder();
        for (MultiPartSpecification m : requestSpec.getMultiPartParams()) {
            multipartParams.append(m.getFileName()).append("\n");
            multipartParams.append(m.getMimeType()).append("\n");
            multipartParams.append(m.getContent()).append("\n");
        }

        String requestBody = requestSpec.getBody() != null ? requestSpec.getBody().toString() : "";

        // EXTRAÇÃO DE DADOS DA RESPOSTA
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        // CRIAÇÃO DA TABELA PDF
        Table table = new Table(UnitValue.createPercentArray(new float[] { 20, 80 }));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setFixedLayout();

        // ADIÇÃO DE DADOS À TABELA (apenas campos não vazios)
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

    // ========================================================================
    // MÉTODO DE FINALIZAÇÃO
    // ========================================================================

    /**
     * MÉTODO: closeDocument() - Geração Final do PDF
     * 
     * PROPÓSITO:
     * Finaliza a captura de dados e gera o arquivo PDF completo
     * com todas as requisições do cenário. Inclui formatação
     * profissional e informações de status.
     * 
     * PARÂMETROS:
     * @param passed - Indica se o cenário passou (true) ou falhou (false)
     * 
     * RETORNO:
     * @return String - Caminho completo do arquivo PDF gerado
     * 
     * QUANDO CHAMAR:
     * - Ao final de cada cenário de teste
     * - Após todas as requisições terem sido capturadas
     * - Preferencialmente no hook @After
     * 
     * COMO USAR:
     * // No final do teste
     * boolean cenarioPassou = !scenario.isFailed();
     * String arquivoPDF = pdfFilter.closeDocument(cenarioPassou);
     * 
     * // Anexar ao relatório se necessário
     * if (scenario.isFailed()) {
     *     anexarEvidencia(arquivoPDF);
     * }
     * 
     * ESTRUTURA DO PDF GERADO:
     * 1. Título com ID e nome do cenário
     * 2. Status (PASSOU/FALHOU) com cor verde/vermelha
     * 3. Uma página por requisição HTTP
     * 4. Tabela detalhada para cada requisição
     * 5. Quebra de página entre requisições
     * 
     * FORMATAÇÃO:
     * - Título centralizado e em negrito
     * - Status alinhado à direita com cor
     * - Numeração de requisições
     * - Tabelas com largura fixa otimizada
     * - Quebras de página adequadas
     * 
     * NOME DO ARQUIVO:
     * Formato: [scenarioId]_[scenarioName]_[timestamp].pdf
     * Exemplo: CT-001_Login_usuario_valido_25-12-2024 14_30_15_123.pdf
     * 
     * TRATAMENTO DE ERROS:
     * - Cria diretório 'evidence' se não existir
     * - Remove caracteres especiais do nome do cenário
     * - Loga erros de geração com LogFormatter
     * - Retorna caminho mesmo se houver erros
     * 
     * FLUXO DETALHADO:
     * 1. Loga início da geração
     * 2. Gera timestamp único
     * 3. Remove caracteres problemáticos do nome
     * 4. Define caminho do arquivo PDF
     * 5. Cria diretório de evidências
     * 6. Inicializa documento PDF
     * 7. Adiciona título e status
     * 8. Itera por todas as tabelas capturadas
     * 9. Adiciona contador de requisições
     * 10. Insere quebras de página
     * 11. Fecha documento
     * 12. Trata exceções se houver
     * 13. Retorna caminho do arquivo
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

            // TÍTULO DO DOCUMENTO
            Paragraph titulo = new Paragraph(scenarioId + ": " + scenarioName).setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            // STATUS COM COR
            String passedStr = passed ? "PASSOU" : "FALHOU";
            Color statusColor = passed ? new DeviceRgb(0, 128, 0) : new DeviceRgb(255, 0, 0);
            Paragraph status = new Paragraph("Status: ").add(new Text(passedStr).setFontColor(statusColor))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(status);

            // ADIÇÃO DAS REQUISIÇÕES
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
}
```

---

## ⚙️ **4. PROCESSOR - Processamento de Tags**

### **TagProcessor.java - Processador de Tags de Teste**

**📍 Localização**: `src/main/java/org/br/com/core/processor/TagProcessor.java`

```java
package org.br.com.core.processor;

import org.br.com.test.model.builder.TagBuilder;
import org.br.com.test.utils.TagConcatenada;

/**
 * ============================================================================
 * PROCESSADOR: TagProcessor
 * ============================================================================
 * 
 * PROPÓSITO:
 * Processa e concatena tags de teste baseadas em dados de planilhas Excel.
 * Facilita a execução seletiva de testes através de tags dinâmicas
 * geradas a partir de critérios definidos por analistas.
 * 
 * QUANDO USAR:
 * - Executar testes baseados em critérios de planilhas
 * - Gerar tags dinamicamente para execução seletiva
 * - Integrar dados de analistas com execução de testes
 * - Automatizar seleção de cenários baseada em Excel
 * 
 * COMO INSTANCIAR:
 * TagProcessor processor = new TagProcessor();
 * TagBuilder tag = TagBuilder.builder()
 *     .abaAnalista("TBL_EXECUCAO")
 *     .execution("S")
 *     .build();
 * processor.start(tag);
 * 
 * MOTIVOS DE USO:
 * 1. Flexibilidade: Controle dinâmico de execução
 * 2. Integração: Liga planilhas com execução
 * 3. Seletividade: Executa apenas testes relevantes
 * 4. Automação: Reduz intervenção manual
 * ============================================================================
 */
public class TagProcessor {

    /**
     * MÉTODO: start(TagBuilder tag)
     * 
     * PROPÓSITO:
     * Inicia o processamento de tags baseado nos critérios fornecidos.
     * Delega para TagConcatenada que faz o trabalho de leitura da planilha
     * e geração das tags de execução.
     * 
     * PARÂMETROS:
     * @param tag - Objeto TagBuilder com critérios de processamento
     *              - abaAnalista: nome da aba na planilha Excel
     *              - execution: critério de execução (ex: "S" para sim)
     * 
     * QUANDO USAR:
     * - Antes de executar uma suíte de testes
     * - Para processar critérios definidos em planilhas
     * - Quando precisa de execução seletiva baseada em dados
     * - Em pipelines CI/CD que usam critérios de Excel
     * 
     * COMO USAR:
     * // Preparar critérios
     * TagBuilder criterios = TagBuilder.builder()
     *     .abaAnalista("TBL_CRITERIOS_EXECUCAO")  // Aba da planilha
     *     .execution("EXECUTAR")                   // Valor para filtro
     *     .build();
     * 
     * // Processar tags
     * TagProcessor processor = new TagProcessor();
     * processor.start(criterios);
     * 
     * // Após este ponto, TagConcatenada terá processado
     * // a planilha e gerado as tags apropriadas
     * 
     * MOTIVOS:
     * 1. Delegação: Separa responsabilidade de coordenação
     * 2. Simplicidade: Interface limpa para processamento
     * 3. Flexibilidade: TagBuilder permite diferentes critérios
     * 4. Manutenibilidade: Lógica complexa fica em TagConcatenada
     * 
     * FLUXO INTERNO:
     * 1. Recebe TagBuilder com critérios
     * 2. Cria instância de TagConcatenada
     * 3. Chama tagsExcel() passando os critérios
     * 4. TagConcatenada processa a planilha
     * 5. Tags são geradas e disponibilizadas
     * 
     * EXEMPLO PRÁTICO:
     * // Cenário: executar apenas testes marcados como "REGRESSAO"
     * TagBuilder regressao = TagBuilder.builder()
     *     .abaAnalista("TBL_SUITE_REGRESSAO")
     *     .execution("S")
     *     .build();
     * 
     * TagProcessor processor = new TagProcessor();
     * processor.start(regressao);
     * 
     * // Agora pode executar com as tags geradas:
     * // mvn test -Dcucumber.filter.tags="@tag1 or @tag2 or @tag3"
     * 
     * INTEGRAÇÃO COM PLANILHA:
     * A planilha deve ter estrutura como:
     * | cenario     | tag       | executar |
     * |-------------|-----------|----------|
     * | Login       | @login    | S        |
     * | Cadastro    | @cadastro | N        |
     * | Relatório   | @relat    | S        |
     * 
     * Com execution="S", processará apenas Login e Relatório
     */
    public void start(TagBuilder tag) {
        TagConcatenada tagConcatenada = new TagConcatenada();
        tagConcatenada.tagsExcel(tag.getAbaAnalista(), tag.getExecution());
    }
}
```

---

## 🛠️ **5. SUPPORT - Classes de Suporte**

### **Context.java - Gerenciamento de Contexto** (já documentado anteriormente)

### **ResourceUtils.java - Utilitário de Recursos**

**📍 Localização**: `src/main/java/org/br/com/core/support/resource/ResourceUtils.java`

```java
package org.br.com.core.support.resource;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * ============================================================================
 * UTILITÁRIO: ResourceUtils
 * ============================================================================
 * 
 * PROPÓSITO:
 * Localiza arquivos de recursos (planilhas, configurações) em diferentes
 * ambientes de execução. Torna o framework robusto e independente de
 * configurações específicas de IDE ou servidor de CI/CD.
 * 
 * QUANDO USAR:
 * - Carregar planilhas Excel de dados de teste
 * - Acessar arquivos de configuração
 * - Localizar recursos independente do ambiente
 * - Evitar erros de "arquivo não encontrado"
 * 
 * COMO INSTANCIAR:
 * Não é instanciada - métodos são estáticos:
 * String caminho = ResourceUtils.getPath("MassaDados.xlsx");
 * 
 * MOTIVOS DE USO:
 * 1. Robustez: Funciona em IDE, Maven, JAR
 * 2. Flexibilidade: Procura em múltiplos locais
 * 3. Simplicidade: Uma chamada para localizar
 * 4. Confiabilidade: Reduz erros de ambiente
 * ============================================================================
 */
public class ResourceUtils {

    /**
     * MÉTODO: getPath(String resourceName)
     * 
     * PROPÓSITO:
     * Encontra o caminho absoluto de um arquivo, procurando em vários 
     * locais padrão. Torna o framework resistente a diferentes 
     * configurações de ambiente e estruturas de projeto.
     * 
     * PARÂMETROS:
     * @param resourceName - Nome do arquivo a ser encontrado
     * 
     * RETORNO:
     * @return String - Caminho absoluto do arquivo encontrado
     * 
     * EXCEÇÕES:
     * @throws RuntimeException - Se arquivo não for encontrado
     * 
     * QUANDO USAR:
     * - Carregar planilhas de dados de teste
     * - Acessar arquivos de configuração
     * - Localizar templates ou recursos estáticos
     * - Substituir caminhos hardcoded por busca inteligente
     * 
     * COMO USAR:
     * // Localizar planilha de dados
     * try {
     *     String caminhoExcel = ResourceUtils.getPath("MassaDados.xlsx");
     *     DataReader reader = new DataReader(caminhoExcel);
     *     // usar reader...
     * } catch (RuntimeException e) {
     *     System.err.println("Arquivo não encontrado: " + e.getMessage());
     * }
     * 
     * // Localizar arquivo de configuração
     * String configPath = ResourceUtils.getPath("application.properties");
     * Properties props = new Properties();
     * props.load(new FileInputStream(configPath));
     * 
     * MOTIVOS:
     * 1. Portabilidade: Funciona em qualquer ambiente
     * 2. Robustez: Múltiplas estratégias de localização
     * 3. Debugging: Informa onde encontrou o arquivo
     * 4. Clareza: Erro específico quando não encontra
     * 
     * ESTRATÉGIAS DE BUSCA:
     * 1. ClassLoader (padrão Java/Maven)
     * 2. src/main/resources/ (estrutura Maven)
     * 3. src/test/resources/ (recursos de teste)
     * 4. data/ (diretório comum para dados)
     * 5. Raiz do projeto (fallback)
     * 
     * FLUXO DETALHADO:
     * 1. Tenta ClassLoader.getResource() primeiro
     * 2. Se encontrou, converte URL para caminho absoluto
     * 3. Se não encontrou, testa caminhos relativos
     * 4. Para cada caminho possível:
     *    - Cria File object
     *    - Verifica se existe e não é diretório
     *    - Se válido, loga localização e retorna
     * 5. Se nenhum funcionou, lança exceção descritiva
     * 
     * EXEMPLO PRÁTICO:
     * // Em vez de caminho fixo que pode quebrar:
     * // String arquivo = "C:/projeto/dados/usuarios.xlsx"; // RUIM!
     * 
     * // Use busca inteligente que sempre funciona:
     * String arquivo = ResourceUtils.getPath("usuarios.xlsx"); // BOM!
     * 
     * AMBIENTES SUPORTADOS:
     * - IDE (IntelliJ, Eclipse, VS Code)
     * - Maven command line
     * - JAR executável
     * - Servidor CI/CD (Jenkins, GitHub Actions)
     * - Docker containers
     * 
     * ESTRUTURAS DE PROJETO SUPORTADAS:
     * projeto/
     * ├── src/main/resources/usuarios.xlsx     ← Encontra aqui
     * ├── src/test/resources/usuarios.xlsx     ← Ou aqui
     * ├── data/usuarios.xlsx                   ← Ou aqui
     * └── usuarios.xlsx                        ← Ou aqui
     * 
     * LOGS INFORMATIVOS:
     * Quando encontra arquivo, loga:
     * "INFO: Recurso 'MassaDados.xlsx' encontrado em: /caminho/completo/arquivo.xlsx"
     * 
     * ERRO DETALHADO:
     * Se não encontra, erro contém:
     * "FALHA CRÍTICA AO CARREGAR RECURSO: O arquivo 'nome.xlsx' não foi 
     *  encontrado no classpath ou em diretórios comuns do projeto."
     */
    public static String getPath(String resourceName) {
        // 1. ESTRATÉGIA PRINCIPAL: ClassLoader (padrão Java)
        URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            return new File(resourceUrl.getPath()).getAbsolutePath();
        }

        // 2. ESTRATÉGIA ALTERNATIVA: Caminhos relativos comuns
        String[] possiblePaths = {
                "src/main/resources/",    // Maven/Gradle main resources
                "src/test/resources/",    // Maven/Gradle test resources  
                "data/",                  // Diretório comum para dados
                ""                        // Raiz do projeto (último recurso)
        };

        for (String path : possiblePaths) {
            File file = new File(path + resourceName);
            if (file.exists() && !file.isDirectory()) {
                System.out.println("INFO: Recurso '" + resourceName + "' encontrado em: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        }

        // 3. FALHA: Arquivo não encontrado em lugar nenhum
        throw new RuntimeException("FALHA CRÍTICA AO CARREGAR RECURSO: O arquivo '" + resourceName + "' não foi encontrado no classpath ou em diretórios comuns do projeto.");
    }
}
```

### **Console.java - Constantes de Console**

**📍 Localização**: `src/main/java/org/br/com/core/support/logger/Console.java`

```java
package org.br.com.core.support.logger;

/**
 * ============================================================================
 * CONSTANTES: Console
 * ============================================================================
 * 
 * PROPÓSITO:
 * Define constantes para formatação consistente de saídas no console.
 * Padroniza separadores visuais e mensagens de erro em todo o framework.
 * 
 * QUANDO USAR:
 * - Separar seções nos logs
 * - Indicar ausência de dados ou tags
 * - Formatar saídas de console consistentemente
 * - Melhorar legibilidade dos logs
 * 
 * COMO USAR:
 * System.out.println(Console.SEPARATE);
 * System.out.println("Relatório de Execução");
 * System.out.println(Console.SEPARATE_HYPHEN);
 * 
 * MOTIVOS DE USO:
 * 1. Padronização: Formatação consistente
 * 2. Manutenibilidade: Mudança centralizada
 * 3. Legibilidade: Separação visual clara
 * 4. Profissionalismo: Logs organizados
 * ============================================================================
 */
public class Console {

    /**
     * CONSTANTE: SEPARATE
     * 
     * PROPÓSITO: Separador principal para seções importantes
     * VALOR: Linha com sinais de igual (=)
     * USO: Delimitar seções principais de relatórios e logs
     * 
     * EXEMPLO:
     * System.out.println(Console.SEPARATE);
     * System.out.println("    RELATÓRIO DE EXECUÇÃO DE TESTES");
     * System.out.println(Console.SEPARATE);
     */
    public static final String SEPARATE = "===========================================================================================";

    /**
     * CONSTANTE: SEPARATE_HYPHEN
     * 
     * PROPÓSITO: Separador secundário para subseções
     * VALOR: Linha com hífens (-)
     * USO: Delimitar subseções dentro de relatórios
     * 
     * EXEMPLO:
     * System.out.println("Cenários Executados:");
     * System.out.println(Console.SEPARATE_HYPHEN);
     * System.out.println("CT-001: Login válido - PASSOU");
     * System.out.println("CT-002: Login inválido - PASSOU");
     */
    public static final String SEPARATE_HYPHEN = "------------------------------------------------------------------------------------";

    /**
     * CONSTANTE: ID_TAG_NOT_FOUND
     * 
     * PROPÓSITO: Mensagem padrão quando tag ID não é encontrada
     * VALOR: "[ID tag not found]"
     * USO: Indicar ausência de identificador em cenários
     * 
     * EXEMPLO:
     * String id = scenario.getId();
     * if (id == null) {
     *     id = Console.ID_TAG_NOT_FOUND;
     * }
     * System.out.println("Cenário: " + id);
     */
    public static final String ID_TAG_NOT_FOUND = "[ID tag not found]";

    /**
     * CONSTANTE: DATA_TAG_NOT_FOUND
     * 
     * PROPÓSITO: Mensagem padrão quando tag de dados não é encontrada  
     * VALOR: "[Data tag not found]"
     * USO: Indicar ausência de dados em cenários data-driven
     * 
     * EXEMPLO:
     * String dataTag = extractDataTag(scenario);
     * if (dataTag == null) {
     *     dataTag = Console.DATA_TAG_NOT_FOUND;
     * }
     * System.out.println("Dados: " + dataTag);
     */
    public static final String DATA_TAG_NOT_FOUND = "[Data tag not found]";
}
```

### **ContextTime.java - Utilitário de Tempo**

**📍 Localização**: `src/main/java/org/br/com/core/support/logger/ContextTime.java`

```java
package org.br.com.core.support.logger;

import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * UTILITÁRIO: ContextTime
 * ============================================================================
 * 
 * PROPÓSITO:
 * Gerencia medição de tempo de execução de testes individuais e suítes
 * completas. Usa ThreadLocal para isolamento entre threads paralelas.
 * Fornece relatórios de performance e duração.
 * 
 * QUANDO USAR:
 * - Medir tempo de execução de testes
 * - Gerar relatórios de performance
 * - Análise de tempo de suítes
 * - Debugging de testes lentos
 * 
 * COMO USAR:
 * // Início do teste
 * ContextTime.printTimeInitial();
 * 
 * // ... execução do teste ...
 * 
 * // Final do teste
 * ContextTime.printTimeFinal();
 * 
 * MOTIVOS DE USO:
 * 1. Performance: Identifica gargalos de tempo
 * 2. Relatórios: Documenta duração de execução
 * 3. Otimização: Baseline para melhorias
 * 4. Thread-Safety: Isolamento em execução paralela
 * ============================================================================
 */
@Log4j2
public class ContextTime {

    // ========================================================================
    // ARMAZENAMENTO THREAD-SAFE DE TEMPOS
    // ========================================================================

    /**
     * ATRIBUTO: timeSuiteInit
     * 
     * PROPÓSITO: Armazena horário de início da suíte completa
     * TIPO: ThreadLocal<LocalDateTime>
     * THREAD-SAFETY: Isolado por thread para execução paralela
     * USO: Calcular tempo total da suíte de testes
     */
    public static final ThreadLocal<LocalDateTime> timeSuiteInit = new ThreadLocal<LocalDateTime>();

    /**
     * ATRIBUTO: timeTestInit
     * 
     * PROPÓSITO: Armazena horário de início do teste atual
     * TIPO: ThreadLocal<LocalDateTime>
     * THREAD-SAFETY: Isolado por thread para execução paralela
     * USO: Calcular tempo do teste individual
     */
    public static final ThreadLocal<LocalDateTime> timeTestInit = new ThreadLocal<LocalDateTime>();

    // ========================================================================
    // MÉTODOS DE MARCAÇÃO DE TEMPO
    // ========================================================================

    /**
     * MÉTODO: printTimeInitial()
     * 
     * PROPÓSITO:
     * Marca o início de um teste e exibe o horário no console.
     * Inicializa também o timer da suíte se for o primeiro teste.
     * 
     * QUANDO CHAMAR:
     * - No início de cada teste (@Before hook)
     * - Antes de executar cenários
     * - Para marcar início de operações críticas
     * 
     * COMO USAR:
     * @Before
     * public void antesDoTeste() {
     *     ContextTime.printTimeInitial();
     *     // resto da preparação...
     * }
     * 
     * COMPORTAMENTO:
     * 1. Captura horário atual
     * 2. Armazena em timeTestInit (thread-local)
     * 3. Exibe horário formatado no console
     * 4. Se é primeiro teste, inicializa timeSuiteInit
     * 
     * SAÍDA ESPERADA:
     * "Initial hour of test...........: 14:30:25"
     */
    public static void printTimeInitial() {
        timeTestInit.set(LocalDateTime.now());
        printHour("Initial hour of test...........", timeTestInit.get());

        if (timeSuiteInit.get() == null)
            timeSuiteInit.set(timeTestInit.get());
    }

    /**
     * MÉTODO: printTimeFinal()
     * 
     * PROPÓSITO:
     * Marca o final de um teste, calcula durações e exibe relatório
     * de tempo no console. Mostra tempo do teste e tempo total.
     * 
     * QUANDO CHAMAR:
     * - No final de cada teste (@After hook)
     * - Após completar cenários
     * - Para finalizar medição de operações
     * 
     * COMO USAR:
     * @After
     * public void depoisDoTeste() {
     *     // limpeza do teste...
     *     ContextTime.printTimeFinal();
     * }
     * 
     * COMPORTAMENTO:
     * 1. Captura horário atual de finalização
     * 2. Exibe horário final formatado
     * 3. Calcula e exibe duração do teste atual
     * 4. Calcula e exibe duração total da suíte
     * 
     * SAÍDA ESPERADA:
     * "Final hour of test.............: 14:31:45"
     * "Time execution of test.........: 0 Hour(s) 1 minute(s) 20 second(s)"
     * "Time execution of tests so far.: 0 Hour(s) 5 minute(s) 35 second(s)"
     */
    public static void printTimeFinal() {
        LocalDateTime timeTestFinal = LocalDateTime.now();
        printHour("Final hour of test.............", timeTestFinal);

        printTime("Time execution of test.........", returnDifferenceBetweenTimes(timeTestInit.get(), timeTestFinal));
        printTime("Time execution of tests so far.", returnDifferenceBetweenTimes(timeSuiteInit.get(), timeTestFinal));
    }

    // ========================================================================
    // MÉTODOS DE FORMATAÇÃO E CÁLCULO
    // ========================================================================

    /**
     * MÉTODO: printHour(String previous, LocalDateTime time)
     * 
     * PROPÓSITO:
     * Formata e exibe um horário específico com prefixo descritivo.
     * Usado internamente para exibir horários de início e fim.
     * 
     * PARÂMETROS:
     * @param previous - Texto descritivo (ex: "Initial hour of test")
     * @param time - Horário a ser formatado e exibido
     * 
     * FORMATO DE SAÍDA:
     * "[previous]: [hora]:[minuto]:[segundo]"
     * Exemplo: "Initial hour of test...........: 14:30:25"
     */
    public static void printHour(String previous, LocalDateTime time) {
        System.out.printf("%s: %d:%d:%d%n", previous, time.getHour(), time.getMinute(), time.getSecond());
    }

    /**
     * MÉTODO: printTime(String previous, LocalDateTime time)
     * 
     * PROPÓSITO:
     * Formata e exibe uma duração calculada com prefixo descritivo.
     * Usado para mostrar tempos decorridos de forma legível.
     * 
     * PARÂMETROS:
     * @param previous - Texto descritivo (ex: "Time execution of test")
     * @param time - Duração calculada a ser exibida
     * 
     * VISIBILIDADE: private (uso interno)
     * 
     * FORMATO DE SAÍDA:
     * "[previous]: [X] Hour(s) [Y] minute(s) [Z] second(s)"
     * Exemplo: "Time execution of test.........: 0 Hour(s) 1 minute(s) 20 second(s)"
     */
    private static void printTime(String previous, LocalDateTime time) {
        System.out.printf(String.format("%s: %d Hour(s) %d minute(s) %d second(s)", previous,
                time.getHour(),
                time.getMinute(),
                time.getSecond()));
    }

    /**
     * MÉTODO: returnDifferenceBetweenTimes(LocalDateTime timeInit, LocalDateTime timeFinal)
     * 
     * PROPÓSITO:
     * Calcula a diferença entre dois horários e retorna como LocalDateTime.
     * Usado para determinar durações de testes e suítes.
     * 
     * PARÂMETROS:
     * @param timeInit - Horário de início
     * @param timeFinal - Horário de fim
     * 
     * RETORNO:
     * @return LocalDateTime - Diferença calculada (duração)
     * 
     * ALGORITMO:
     * Subtrai componentes individuais (horas, minutos, segundos)
     * do horário final para calcular a duração decorrida.
     * 
     * LIMITAÇÕES:
     * - Funciona apenas para durações dentro do mesmo dia
     * - Não trata mudanças de fuso horário
     * - Precisão limitada a segundos
     * 
     * USO INTERNO:
     * Chamado por printTimeFinal() para calcular:
     * - Duração do teste atual
     * - Duração total da suíte
     */
    public static LocalDateTime returnDifferenceBetweenTimes(LocalDateTime timeInit, LocalDateTime timeFinal) {
        return timeFinal
                .minusHours(timeInit.getHour())
                .minusMinutes(timeInit.getMinute())
                .minusSeconds(timeInit.getSecond());
    }
}
```

---

## 🔑 **6. TOKEN - Gerenciamento de Tokens**

### **Token.java - Interface de Token**

**📍 Localização**: `src/main/java/org/br/com/core/token/Token.java`

```java
package org.br.com.core.token;

/**
 * ============================================================================
 * INTERFACE: Token
 * ============================================================================
 * 
 * PROPÓSITO:
 * Define o contrato padrão para diferentes tipos de tokens de autenticação
 * no framework. Garante que todas as implementações de token forneçam
 * informações essenciais de forma consistente.
 * 
 * QUANDO IMPLEMENTAR:
 * - Criar diferentes tipos de token (JWT, OAuth, API Key)
 * - Padronizar acesso a informações de token
 * - Facilitar polimorfismo com tokens
 * - Integrar com diferentes sistemas de autenticação
 * 
 * COMO IMPLEMENTAR:
 * public class JWTToken implements Token {
 *     private String accessToken;
 *     private String productType;
 *     
 *     @Override
 *     public String getAccessToken() { return accessToken; }
 *     
 *     @Override
 *     public String getProductType() { return productType; }
 * }
 * 
 * MOTIVOS DE USO:
 * 1. Padronização: Interface consistente para tokens
 * 2. Flexibilidade: Suporte a diferentes tipos
 * 3. Polimorfismo: Tratamento uniforme de tokens
 * 4. Extensibilidade: Fácil adição de novos tipos
 * ============================================================================
 */
public interface Token {

    /**
     * MÉTODO: getProductType()
     * 
     * PROPÓSITO:
     * Retorna o tipo ou categoria do produto/sistema que o token autentica.
     * Permite identificar para qual sistema o token é válido.
     * 
     * RETORNO:
     * @return String - Tipo do produto (ex: "API", "WEB", "MOBILE")
     * 
     * QUANDO USAR:
     * - Identificar sistema de origem do token
     * - Validar se token é apropriado para operação
     * - Logging e auditoria de uso de tokens
     * - Roteamento baseado em tipo de produto
     * 
     * EXEMPLOS DE IMPLEMENTAÇÃO:
     * // Token para API REST
     * @Override
     * public String getProductType() {
     *     return "REST_API";
     * }
     * 
     * // Token para aplicação web
     * @Override  
     * public String getProductType() {
     *     return "WEB_APP";
     * }
     * 
     * // Token para serviços móveis
     * @Override
     * public String getProductType() {
     *     return "MOBILE_SERVICE";
     * }
     */
    String getProductType();

    /**
     * MÉTODO: getAccessToken()
     * 
     * PROPÓSITO:
     * Retorna o valor atual do token de acesso para autenticação.
     * Fornece o token propriamente dito para uso em requisições.
     * 
     * RETORNO:
     * @return String - Token de acesso (ex: JWT, Bearer token, API key)
     * 
     * QUANDO USAR:
     * - Incluir token em headers de requisições HTTP
     * - Validar se token está presente e válido
     * - Logging de uso de token (mascarado)
     * - Renovação e refresh de tokens
     * 
     * EXEMPLOS DE IMPLEMENTAÇÃO:
     * // Token JWT
     * @Override
     * public String getAccessToken() {
     *     return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
     * }
     * 
     * // API Key
     * @Override
     * public String getAccessToken() {
     *     return "ak_live_5d8f7a9b2c1e3f4g5h6i7j8k9l0m1n2o";
     * }
     * 
     * // Bearer Token
     * @Override
     * public String getAccessToken() {
     *     return "BQD8f7s9dfsdf8s7df6sdf8s7dfs8df7s...";
     * }
     * 
     * IMPORTANTE:
     * - Retornar apenas o valor do token (sem prefixos como "Bearer ")
     * - Garantir que token não seja null quando válido
     * - Considerar mascaramento em logs
     */
    String getAccessToken();
}
```

### **GerarTokenResquest.java - Modelo de Requisição de Token**

**📍 Localização**: `src/main/java/org/br/com/core/token/GerarTokenResquest.java`

```java
package org.br.com.core.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * MODELO: GerarTokenResquest
 * ============================================================================
 * 
 * PROPÓSITO:
 * Modelo de dados para requisições de geração de token de autenticação.
 * Encapsula as credenciais necessárias para obter tokens de acesso
 * de forma padronizada e type-safe.
 * 
 * QUANDO USAR:
 * - Fazer login em APIs que retornam tokens
 * - Solicitar tokens de acesso OAuth
 * - Autenticar com serviços que usam email/senha
 * - Padronizar requisições de autenticação
 * 
 * COMO INSTANCIAR:
 * // Usando Builder (recomendado)
 * GerarTokenResquest request = GerarTokenResquest.builder()
 *     .email("usuario@teste.com")
 *     .password("senha123")
 *     .build();
 * 
 * // Usando construtor
 * GerarTokenResquest request = new GerarTokenResquest("user@test.com", "pass123");
 * 
 * MOTIVOS DE USO:
 * 1. Padronização: Estrutura consistente para login
 * 2. Type Safety: Compilador valida tipos
 * 3. Serialização: Funciona com JSON automaticamente
 * 4. Builder Pattern: Construção fluente e clara
 * ============================================================================
 */
@Data              // Gera getters, setters, toString, equals, hashCode
@Builder           // Permite construção fluente com padrão Builder
@NoArgsConstructor // Construtor sem parâmetros (necessário para JSON)
@AllArgsConstructor // Construtor com todos os parâmetros
public class GerarTokenResquest {

    /**
     * CAMPO: email
     * 
     * PROPÓSITO: Email do usuário para autenticação
     * TIPO: String
     * USO: Identificação do usuário no sistema
     * 
     * VALIDAÇÕES RECOMENDADAS:
     * - Não deve ser null ou vazio
     * - Deve ter formato de email válido
     * - Pode ser sensível a maiúsculas/minúsculas
     * 
     * EXEMPLO:
     * request.setEmail("admin@sistema.com");
     * String email = request.getEmail();
     */
    private String email;

    /**
     * CAMPO: password
     * 
     * PROPÓSITO: Senha do usuário para autenticação
     * TIPO: String
     * USO: Verificação de credenciais do usuário
     * 
     * SEGURANÇA:
     * - NUNCA logar este campo
     * - Considerar mascaramento em toString()
     * - Limpar da memória após uso se possível
     * - Usar HTTPS para transmissão
     * 
     * EXEMPLO:
     * request.setPassword("senhaSegura123");
     * String senha = request.getPassword();
     */
    private String password;

    /**
     * EXEMPLO DE USO COMPLETO:
     * 
     * // 1. Criar requisição de token
     * GerarTokenResquest loginRequest = GerarTokenResquest.builder()
     *     .email("teste@automatizado.com")
     *     .password("senha123")
     *     .build();
     * 
     * // 2. Fazer requisição HTTP
     * Response response = given()
     *     .contentType(ContentType.JSON)
     *     .body(loginRequest)  // Serializado automaticamente para JSON
     *     .when()
     *     .post("/auth/login");
     * 
     * // 3. Extrair token da resposta
     * String token = response.jsonPath().getString("token");
     * 
     * // 4. Usar token em requisições subsequentes
     * Response apiResponse = given()
     *     .header("Authorization", "Bearer " + token)
     *     .when()
     *     .get("/api/usuarios");
     * 
     * JSON GERADO:
     * {
     *     "email": "teste@automatizado.com",
     *     "password": "senha123"
     * }
     * 
     * INTEGRAÇÃO COM DATA-DRIVEN TESTING:
     * // Ler credenciais de planilha Excel
     * String emailPlanilha = dataSheet.getField("email");
     * String senhaPlanilha = dataSheet.getField("senha");
     * 
     * GerarTokenResquest request = GerarTokenResquest.builder()
     *     .email(emailPlanilha)
     *     .password(senhaPlanilha)
     *     .build();
     * 
     * BOAS PRÁTICAS:
     * 1. Sempre usar Builder para construção
     * 2. Validar campos antes de enviar requisição
     * 3. Não logar senhas em lugar nenhum
     * 4. Considerar criptografia para senhas em planilhas
     * 5. Usar constantes para credenciais padrão de teste
     */
}
```

### **GerarToken.java - Gerador de Tokens** (Classe legada comentada)

**📍 Localização**: `src/main/java/org/br/com/core/token/GerarToken.java`

```java
package org.br.com.core.token;

/**
 * ============================================================================
 * CLASSE LEGADA: GerarToken
 * ============================================================================
 * 
 * PROPÓSITO:
 * Classe originalmente destinada a gerar tokens automaticamente para
 * diferentes tipos de usuários. Atualmente está inativa (comentada)
 * mas mantida para referência histórica e possível reativação.
 * 
 * ESTADO ATUAL:
 * - Todos os métodos estão comentados
 * - Não está sendo utilizada no framework atual
 * - Mantida para referência de implementação
 * 
 * FUNCIONALIDADE ORIGINAL:
 * - Geração de tokens Bearer para administradores
 * - Tokens para usuários comuns (carrinhos)
 * - Integração com CadastrarUsuarioController
 * - Uso de FakerApiData para dados de teste
 * 
 * SE REATIVAR:
 * 1. Descomentar métodos necessários
 * 2. Atualizar imports conforme nova estrutura
 * 3. Ajustar para usar classes atuais do framework
 * 4. Implementar interface Token se apropriado
 * 
 * MÉTODOS COMENTADOS:
 * - gerarTokenBearer(): Token para usuário geral
 * - GerarTokenAdm(): Token para administrador  
 * - GerarTokenCarrinho(): Token para operações de carrinho
 * 
 * DECISÃO DE ARQUITETURA:
 * Atualmente, geração de tokens é feita diretamente nos
 * controladores específicos usando UsuarioLoginController
 * ============================================================================
 */
public class GerarToken {
    // Classe mantida para referência histórica
    // Todos os métodos estão comentados
    // Funcionalidade migrada para controladores específicos
}
```

---

## 🎓 **RESUMO COMPARATIVO DAS CLASSES CORE/**

| **Pacote** | **Classe** | **Propósito** | **Tipo** | **Uso Principal** |
|------------|------------|---------------|----------|-------------------|
| **encryption** | Encryption | Criptografia AES | Utilitário | Proteger dados sensíveis |
| **exceptions** | DataException | Exceção personalizada | Exceção | Tratar erros de dados |
| **filter** | EvidenceFilter | Captura básica | Filtro | Evidências simples |
| **filter** | PDFLoggerFilter | Captura avançada | Filtro | Evidências em PDF |
| **processor** | TagProcessor | Processamento tags | Processador | Execução seletiva |
| **support** | Context | Contexto global | Gerenciador | Estado entre threads |
| **support** | ResourceUtils | Localização arquivos | Utilitário | Encontrar recursos |
| **support/logger** | Console | Constantes formatação | Constantes | Logs padronizados |
| **support/logger** | ContextTime | Medição tempo | Utilitário | Performance de testes |
| **token** | Token | Interface token | Interface | Padronização de tokens |
| **token** | GerarTokenResquest | Modelo login | DTO | Requisições de autenticação |
| **token** | GerarToken | Gerador (legado) | Legado | Referência histórica |

---

Esta documentação fornece uma visão completa e detalhada de cada classe do pacote **core/**, com comentários explicativos sobre **propósito**, **quando usar**, **como instanciar** e **motivos** de cada método. Agora você tem material completo para copiar no seu caderno! 📚✨
