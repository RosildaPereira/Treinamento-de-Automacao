# 🎯 **TUTORIAL COMPLETO - PARTE 1: ESTRUTURA E SETUP**
## **📋 RECEITA PARA RECRIAR O PROJETO @AUTOMACAO-REST-API-CMS-FOR-QAS-API**

---

## 🎯 **ÍNDICE GERAL DO TUTORIAL COMPLETO**

### **📚 PARTE 1 - ESTRUTURA E SETUP** (Este arquivo)
- Criação da estrutura do projeto
- Configuração do Maven (pom.xml)
- Configuração de Resources (properties, XML)
- Setup inicial completo

### **📚 PARTE 2 - CLASSES CORE E DATA**
- Pacote core/ completo
- Pacote data/ completo
- Classes de criptografia, exceções, filtros

### **📚 PARTE 3 - FRAMEWORK E CONTROLLERS**
- Managers (TokenManager, UsuarioManager, etc.)
- Controllers (UsuarioController)
- Models (Request/Response)
- Steps (Cucumber)

### **📚 PARTE 4 - RESOURCES E EXECUÇÃO**
- Features (Gherkin)
- Planilhas Excel (estrutura)
- Runners e execução
- Testes finais

---

# 📁 **1. ESTRUTURA INICIAL DO PROJETO**

## **🏗️ Criar a estrutura de diretórios:**

```
📁 AUTOMACAO-REST-API-cms-for-qas-api/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 org/
│   │   │       └── 📁 br/
│   │   │           └── 📁 com/
│   │   │               ├── 📁 core/
│   │   │               │   ├── 📁 encryption/
│   │   │               │   ├── 📁 exceptions/
│   │   │               │   ├── 📁 filter/
│   │   │               │   ├── 📁 processor/
│   │   │               │   ├── 📁 support/
│   │   │               │   │   ├── 📁 logger/
│   │   │               │   │   └── 📁 resource/
│   │   │               │   └── 📁 token/
│   │   │               ├── 📁 data/
│   │   │               │   ├── 📁 datasheet/
│   │   │               │   ├── 📁 reader/
│   │   │               │   └── 📁 writer/
│   │   │               ├── 📁 test/
│   │   │               │   ├── 📁 controllers/
│   │   │               │   │   └── 📁 usuario/
│   │   │               │   ├── 📁 manager/
│   │   │               │   ├── 📁 model/
│   │   │               │   │   ├── 📁 builder/
│   │   │               │   │   ├── 📁 request/
│   │   │               │   │   └── 📁 response/
│   │   │               │   ├── 📁 sheets/
│   │   │               │   │   ├── 📁 cadastro/
│   │   │               │   │   ├── 📁 dados_usuario/
│   │   │               │   │   └── 📁 login/
│   │   │               │   ├── 📁 steps/
│   │   │               │   └── 📁 utils/
│   │   │               │       └── 📁 hooks/
│   │   │               ├── Main.java
│   │   │               ├── RunnerTagConcatenada.java
│   │   │               └── RunnerTestApi.java
│   │   └── 📁 resources/
│   │       ├── 📁 data/
│   │       ├── 📁 features/
│   │       ├── test.properties
│   │       ├── log4j2.xml
│   │       └── 📄 Templates DOCX
│   └── 📁 test/
│       └── 📁 java/
├── 📁 target/
├── 📁 evidence/
├── 📁 logs/
├── pom.xml
├── README.md
└── .gitignore
```

---

# ⚙️ **2. CONFIGURAÇÃO DO MAVEN (pom.xml)**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <!-- ================================================================ -->
    <!-- INFORMAÇÕES DO PROJETO -->
    <!-- ================================================================ -->
    <groupId>org.br.com</groupId>
    <artifactId>automacao-rest-api-cms-for-qas-api</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <name>Automação REST API - CMS for QAs API</name>
    <description>Framework de automação de testes para API REST com Cucumber BDD</description>
    
    <!-- ================================================================ -->
    <!-- PROPRIEDADES GLOBAIS -->
    <!-- ================================================================ -->
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        
        <!-- VERSÕES DAS DEPENDÊNCIAS -->
        <cucumber.version>7.18.0</cucumber.version>
        <rest-assured.version>5.3.2</rest-assured.version>
        <junit.version>5.10.0</junit.version>
        <lombok.version>1.18.30</lombok.version>
        <jackson.version>2.15.2</jackson.version>
        <log4j.version>2.20.0</log4j.version>
        <poi.version>5.2.4</poi.version>
        <itext.version>8.0.2</itext.version>
        <javafaker.version>1.0.2</javafaker.version>
        <fillo.version>1.21</fillo.version>
        <opencsv.version>5.7.1</opencsv.version>
    </properties>
    
    <!-- ================================================================ -->
    <!-- DEPENDÊNCIAS PRINCIPAIS -->
    <!-- ================================================================ -->
    <dependencies>
        
        <!-- CUCUMBER BDD -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- REST ASSURED -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>
        
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>json-schema-validator</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>
        
        <!-- JUNIT 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
            <version>${junit.version}</version>
        </dependency>
        
        <!-- LOMBOK -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- JACKSON JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- LOG4J 2 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        
        <!-- APACHE POI (Excel) -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>${poi.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>${poi.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-scratchpad</artifactId>
            <version>${poi.version}</version>
        </dependency>
        
        <!-- FILLO (Query Excel) -->
        <dependency>
            <groupId>com.codoid.products</groupId>
            <artifactId>fillo</artifactId>
            <version>${fillo.version}</version>
        </dependency>
        
        <!-- iText PDF -->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext-core</artifactId>
            <version>${itext.version}</version>
            <type>pom</type>
        </dependency>
        
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>kernel</artifactId>
            <version>${itext.version}</version>
        </dependency>
        
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>layout</artifactId>
            <version>${itext.version}</version>
        </dependency>
        
        <!-- JavaFaker -->
        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>${javafaker.version}</version>
        </dependency>
        
        <!-- OpenCSV -->
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>${opencsv.version}</version>
        </dependency>
        
        <!-- HAMCREST (Matchers) -->
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest</artifactId>
            <version>2.2</version>
        </dependency>
        
    </dependencies>
    
    <!-- ================================================================ -->
    <!-- PLUGINS DE BUILD -->
    <!-- ================================================================ -->
    <build>
        <plugins>
            
            <!-- MAVEN COMPILER -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            
            <!-- MAVEN SUREFIRE (Testes) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <includes>
                        <include>**/RunnerTestApi.java</include>
                    </includes>
                    <systemPropertyVariables>
                        <cucumber.publish.quiet>true</cucumber.publish.quiet>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
            
            <!-- MAVEN FAILSAFE (Testes de Integração) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>3.1.2</version>
            </plugin>
            
            <!-- EXEC MAVEN (Execução) -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <mainClass>org.br.com.Main</mainClass>
                </configuration>
            </plugin>
            
        </plugins>
    </build>
    
    <!-- ================================================================ -->
    <!-- PERFIS DE EXECUÇÃO -->
    <!-- ================================================================ -->
    <profiles>
        
        <!-- PERFIL DE DESENVOLVIMENTO -->
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <env>dev</env>
                <log.level>INFO</log.level>
            </properties>
        </profile>
        
        <!-- PERFIL DE TESTES -->
        <profile>
            <id>test</id>
            <properties>
                <env>test</env>
                <log.level>DEBUG</log.level>
            </properties>
        </profile>
        
        <!-- PERFIL DE PRODUÇÃO -->
        <profile>
            <id>prod</id>
            <properties>
                <env>prod</env>
                <log.level>WARN</log.level>
            </properties>
        </profile>
        
    </profiles>
    
</project>
```

---

# ⚙️ **3. CONFIGURAÇÕES DE RESOURCES**

## **📄 test.properties**
**📍 Local**: `src/main/resources/test.properties`

```properties
# ========================================================================
# CONFIGURAÇÕES DE LOG PARA TESTES
# ========================================================================
# Controla comportamento de mascaramento de dados sensíveis nos logs
# Configurar conforme ambiente (dev/test/prod)

# Controla se dados sensíveis são mascarados nos logs
# true = dados mascarados (recomendado para produção)
# false = dados visíveis (útil para debug)
log.mascarar.dados.sensiveis=true

# Controla se senhas são mascaradas
# SEMPRE manter true por segurança
log.mascarar.senha=true

# Controla se IDs são mascarados  
# false facilita debugging
log.mascarar.id=false

# Controla se tokens são mascarados
# true recomendado por segurança
log.mascarar.token=true

# Controla se emails são mascarados
# false para dados de teste fake
log.mascarar.email=false

# Número de caracteres visíveis no início e fim de dados mascarados
# 3 oferece bom equilíbrio entre segurança e debugging
log.caracteres.visiveis=3
```

## **📄 log4j2.xml**
**📍 Local**: `src/main/resources/log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    
    <!-- ================================================================ -->
    <!-- APPENDERS - DESTINOS DOS LOGS -->
    <!-- ================================================================ -->
    <Appenders>
        
        <!-- Console: Saída imediata durante execução -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%msg%n"/>
        </Console>
        
        <!-- Arquivo fixo: Log da sessão atual -->
        <File name="FileAppender" fileName="target/log/execution.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5level - %msg%n"/>
        </File>
        
        <!-- Arquivo rotativo: Histórico com compressão -->
        <RollingFile name="RollingFile"
                     fileName="target/log/automation-tmp.log"
                     filePattern="target/log/automation-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <!-- Nova data = novo arquivo -->
                <TimeBasedTriggeringPolicy />
                <!-- Arquivo > 10MB = rotação -->
                <SizeBasedTriggeringPolicy size="10MB"/>
            </Policies>
            <!-- Manter máximo 10 arquivos -->
            <DefaultRolloverStrategy max="10"/>
        </RollingFile>
        
    </Appenders>
    
    <!-- ================================================================ -->
    <!-- LOGGERS - CONFIGURAÇÃO DE NÍVEIS -->
    <!-- ================================================================ -->
    <Loggers>
        
        <!-- Logger principal: nível INFO para todas as classes -->
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="RollingFile"/>
        </Root>
        
    </Loggers>
    
</Configuration>
```

---

# 📊 **4. ESTRUTURA DE DADOS (Excel)**

## **📄 ESTRUTURA_PLANILHA_EXCEL.md**
**📍 Local**: `src/main/resources/data/ESTRUTURA_PLANILHA_EXCEL.md`

```markdown
# Estrutura da Planilha MassaDadosCMS.xlsx

## ============================================================================
## DOCUMENTAÇÃO: Estrutura de Dados para Testes
## ============================================================================

## TABELAS CRIADAS

### 1. TBL_CENARIOS (PRINCIPAL)
Tabela principal que contém todos os dados necessários através de PROCV da TBL_CADASTRO.

| SEQ | ID_CENARIO | NOME | FEATURE | ID_USUARIO | EMAIL | SENHA |
|-----|------------|------|---------|------------|-------|-------|
| 1   | CT-1001    | Criar um novo usuário CMS com sucesso | usuario | 0001 | Dagmar.Mitchell25@hotmail.com | nWJzWZhqf5ZooiA |
| 2   | CT-1002    | Tentar criar um usuário com e-mail já existente | usuario | 0002 | Fredrick_Quitzon3@hotmail.com | dFhGC3kHlqcywKF |
| 3   | CT-1003    | Tentar criar um usuário com nome de usuário já existente | usuario | 0003 | Ashly.Rath@gmail.com | Vp6mdcGBLMJ6P3Y |

### 2. TBL_CADASTRO (FONTE DOS DADOS)
Tabela fonte com dados completos dos usuários (usada pelo PROCV).

| SEQ | ID_USUARIO | NOME_COMPLETO | NOME_USUARIO | EMAIL | ID | SENHA |
|-----|------------|---------------|--------------|-------|----|-------|
| 1   | 0001       | Glenda Mertz  | Ceasar.Ward71 | Dagmar.Mitchell25@hotmail.com | b3ee84a9-8789-42ad-a108-8dbd00609c22 | nWJzWZhqf5ZooiA |
| 2   | 0002       | Mr. Candace Lueilwitz | Mozelle54 | Fredrick_Quitzon3@hotmail.com | 3da73e8f-4790-4cc8-ae67-11c87d6544d3 | dFhGC3kHlqcywKF |

## FLUXO DE FUNCIONAMENTO

1. **Cenário executa** com tag `@CT-1002`
2. **Hook captura** a tag e extrai `CT-1002`
3. **Context.setIdUsuario("CT-1002")**
4. **LoginDataSheet** busca diretamente na TBL_CENARIOS
5. **Context.setData(loginModel)**
6. **Steps/Controllers** usam os dados

## VANTAGENS

- ✅ **Simplicidade**: Uma única consulta na TBL_CENARIOS
- ✅ **Performance**: Sem joins ou múltiplas consultas
- ✅ **Manutenção**: Dados centralizados via PROCV
- ✅ **Flexibilidade**: Fácil adicionar novos campos
```

---

# 📄 **5. ARQUIVOS AUXILIARES**

## **📄 .gitignore**

```gitignore
# ========================================================================
# .gitignore para Projeto de Automação Java/Maven
# ========================================================================

# Compilados Java
*.class
*.jar
*.war
*.ear
*.nar
hs_err_pid*

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDEs
.idea/
*.iws
*.iml
*.ipr
.vscode/
.classpath
.project
.settings/
*.swp
*.swo

# Logs
logs/
*.log
target/log/

# Evidências
evidence/
reports/
screenshots/
*.pdf

# Dados sensíveis
*.xlsx
*.xls
*.csv
application-local.properties

# Sistema Operacional
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Temporários
tmp/
temp/
*.tmp
*.bak
*.swp
*~.nib
```

## **📄 README.md**

```markdown
# 🚀 AUTOMAÇÃO REST API - CMS for QAs API

## 📋 Descrição
Framework de automação de testes para API REST usando Cucumber BDD, REST Assured e Java 21.

## 🛠️ Tecnologias
- **Java 21**
- **Maven** 
- **Cucumber BDD**
- **REST Assured**
- **JUnit 5**
- **Log4j 2**
- **Apache POI** (Excel)
- **Lombok**

## 🏗️ Estrutura do Projeto
```
src/
├── main/java/org/br/com/
│   ├── core/           # Classes fundamentais
│   ├── data/           # Manipulação de dados
│   ├── test/           # Lógica de testes
│   └── *.java          # Runners principais
└── resources/
    ├── data/           # Planilhas Excel
    ├── features/       # Cenários Gherkin
    └── *.properties    # Configurações
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.8+
- API CMS em execução (localhost:3000)

### Comandos
```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dcucumber.filter.tags="@usuario"

# Gerar relatórios
mvn test && open target/reports/cucumber-html-report.html
```

## 📊 Recursos
- ✅ Data-Driven Testing com Excel
- ✅ Evidências automáticas em PDF
- ✅ Logs estruturados
- ✅ Execução paralela
- ✅ Relatórios HTML/JSON
```

---

# 🎯 **6. COMANDOS DE SETUP INICIAL**

## **📋 Passo a passo para criar o projeto:**

### **1. Criar projeto Maven:**
```bash
mvn archetype:generate \
  -DgroupId=org.br.com \
  -DartifactId=automacao-rest-api-cms-for-qas-api \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### **2. Navegar para o diretório:**
```bash
cd automacao-rest-api-cms-for-qas-api
```

### **3. Substituir pom.xml pelo configurado acima**

### **4. Criar estrutura de diretórios:**
```bash
# No Windows (PowerShell)
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/encryption
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/exceptions
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/filter
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/processor
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/support/logger
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/support/resource
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/core/token
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/data/datasheet
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/data/reader
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/data/writer
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/controllers/usuario
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/manager
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/model/builder
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/model/request
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/model/response
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/sheets/cadastro
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/sheets/dados_usuario
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/sheets/login
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/steps
New-Item -ItemType Directory -Force -Path src/main/java/org/br/com/test/utils/hooks
New-Item -ItemType Directory -Force -Path src/main/resources/data
New-Item -ItemType Directory -Force -Path src/main/resources/features
New-Item -ItemType Directory -Force -Path target/log
New-Item -ItemType Directory -Force -Path evidence

# No Linux/Mac
mkdir -p src/main/java/org/br/com/core/{encryption,exceptions,filter,processor,support/{logger,resource},token}
mkdir -p src/main/java/org/br/com/data/{datasheet,reader,writer}
mkdir -p src/main/java/org/br/com/test/{controllers/usuario,manager,model/{builder,request,response},sheets/{cadastro,dados_usuario,login},steps,utils/hooks}
mkdir -p src/main/resources/{data,features}
mkdir -p target/log evidence
```

### **5. Criar arquivos de configuração:**
```bash
# Criar test.properties
echo "log.mascarar.dados.sensiveis=true" > src/main/resources/test.properties

# Criar log4j2.xml  
# (copiar conteúdo da seção anterior)

# Criar .gitignore
# (copiar conteúdo da seção anterior)
```

### **6. Verificar estrutura:**
```bash
mvn clean compile
```

---

# ✅ **7. CHECKLIST DE SETUP INICIAL**

### **📋 Verificar se foi criado:**

- [ ] **Projeto Maven** com groupId correto
- [ ] **pom.xml** com todas as dependências
- [ ] **Estrutura de diretórios** completa
- [ ] **test.properties** configurado
- [ ] **log4j2.xml** configurado  
- [ ] **.gitignore** criado
- [ ] **README.md** documentado
- [ ] **Compilação** sem erros (`mvn clean compile`)

### **🎯 Próximos passos:**
1. **PARTE 2**: Criar todas as classes do pacote `core/` e `data/`
2. **PARTE 3**: Implementar framework, controllers e managers
3. **PARTE 4**: Configurar resources, features e execução final

---

## 🎓 **RESUMO DA PARTE 1 - ESTRUTURA E SETUP**

✅ **Criada estrutura completa** do projeto Maven  
✅ **Configurado pom.xml** com todas as dependências necessárias  
✅ **Setupados arquivos** de configuração (properties, XML)  
✅ **Documentada estrutura** de dados (Excel)  
✅ **Preparado ambiente** para desenvolvimento  

**🚀 Projeto pronto para receber as classes na PARTE 2!** 📚✨

