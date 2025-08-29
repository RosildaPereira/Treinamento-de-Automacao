# Capítulo 2: Estrutura de uma API Node.js com TypeScript

Bem-vindo ao segundo capítulo! Agora vamos mudar nosso foco para o backend: a API construída com Node.js e TypeScript. Uma API bem estruturada é crucial para a escalabilidade, manutenção e, claro, para facilitar os testes.

Neste capítulo, vamos dissecar a arquitetura da `cms-for-qas-api`, incluindo o código-fonte real dos arquivos do projeto para que você tenha um tutorial prático e completo.

---

### Página 1: O Ponto de Entrada - `server.ts`

Toda aplicação de backend precisa de um ponto de partida, um arquivo que inicializa o servidor, conecta-se ao banco de dados e configura as rotas. Em nosso projeto, esse arquivo é o `server.ts`. Ele é o coração que bombeia vida para a API.

Vamos analisar o código-fonte dele e entender cada parte.

**Código-Fonte: `cms-for-qas-api/src/server.ts`**

```typescript
import "reflect-metadata";
import express from "express";
import cors from "cors";
import swaggerUi from "swagger-ui-express";
import YAML from "yamljs";
import path from "path";
import { AppDataSource } from "./database/data-source";
import userRoutes from "./routes/userRoutes";
import categoryRoutes from "./routes/categoryRoutes";
import articleRoutes from "./routes/articleRoutes";
import authRoutes from "./routes/authRoutes";
import { authMiddleware } from "./middleware/authMiddleware";
import { Router } from "express";
import { DatabaseCleanupService } from "./services/DatabaseCleanupService";
import { LoggerService } from "./services/LoggerService";

const app = express();

app.use(cors());
app.use(express.json());

const swaggerDocument = YAML.load(path.resolve(__dirname, "../swagger.yaml"));
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

app.use("/auth", authRoutes);

const userRouter = Router();
app.use("/usuarios", userRouter);

userRouter.post("/", userRoutes);

userRouter.use(authMiddleware);
userRouter.get("/", userRoutes);
userRouter.get("/:id", userRoutes);
userRouter.put("/:id", userRoutes);
userRouter.delete("/:id", userRoutes);

app.use("/categorias", authMiddleware, categoryRoutes);
app.use("/artigos", authMiddleware, articleRoutes);

const PORT = process.env.PORT || 3000;

AppDataSource.initialize()
    .then(async () => {
        const cleanupService = new DatabaseCleanupService();
        await cleanupService.verificarEstadoAtual();
        LoggerService.info("Banco de dados inicializado com sucesso");

        app.listen(PORT, () => {
            LoggerService.info(`Servidor iniciado na porta ${PORT}`);
            LoggerService.info(`Documentação disponível em http://localhost:${PORT}/api-docs`);
        });
    })
    .catch((error) => {
        LoggerService.error("Erro ao inicializar o servidor", error);
        process.exit(1);
    });
```

**Análise Detalhada:**

1.  **Imports:**
    *   `import "reflect-metadata";`: Essencial para o TypeORM. Deve ser a primeira importação. Permite que o TypeORM leia os metadados dos decoradores das entidades.
    *   `express`: O framework web que usamos para construir a API.
    *   `cors`: Middleware que habilita o Cross-Origin Resource Sharing, permitindo que um frontend em outro domínio acesse a API.
    *   `swagger-ui-express` & `yamljs`: Usados para gerar a documentação interativa da API a partir do arquivo `swagger.yaml`.
    *   `./...`: Importações dos nossos próprios módulos: rotas, middlewares, serviços e configuração do banco de dados.

2.  **Configuração do Express (`app.use(...)`):**
    *   `app.use(cors())`: Habilita o CORS para todas as requisições.
    *   `app.use(express.json())`: Middleware que interpreta o corpo de requisições `Content-Type: application/json` e o torna acessível em `req.body`.

3.  **Configuração das Rotas:**
    *   `app.use("/api-docs", ...)`: Define o endpoint `/api-docs` para servir a documentação do Swagger.
    *   `app.use("/auth", authRoutes)`: Associa as rotas de autenticação (como `/login`) ao prefixo `/auth`.
    *   **Rotas de Usuário Protegidas:** O código demonstra de forma inteligente como proteger apenas algumas rotas de um mesmo grupo. A rota para criar usuário (`POST /usuarios`) é pública, mas as rotas de `GET`, `PUT` e `DELETE` são protegidas pelo `authMiddleware`, que é aplicado a todas as rotas declaradas após `userRouter.use(authMiddleware)`.

4.  **Inicialização do Servidor:**
    *   `AppDataSource.initialize()`: Tenta conectar ao banco de dados. É uma operação assíncrona que retorna uma `Promise`.
    *   `.then(...)`: Este bloco só executa se a conexão com o banco for bem-sucedida.
    *   `app.listen(PORT, ...)`: **Somente após** o banco de dados estar conectado, o servidor começa a "ouvir" por requisições HTTP. Isso garante que a API não aceite requisições antes de estar totalmente pronta.
    *   `.catch(...)`: Se a conexão com o banco falhar, o erro é logado e a aplicação é encerrada (`process.exit(1)`), uma prática recomendada para evitar que a API funcione em um estado inconsistente.

---

### Página 2: A Conexão com o Mundo - `database/data-source.ts`

Uma API sem um banco de dados é como um cérebro sem memória. A camada de persistência de dados é fundamental. Em nosso projeto, o **TypeORM** é a ferramenta que faz a ponte entre nosso código TypeScript e o banco de dados. O arquivo `data-source.ts` é onde configuramos essa conexão.

**Código-Fonte: `cms-for-qas-api/src/database/data-source.ts`**

```typescript
import "reflect-metadata";
import { DataSource } from "typeorm";
import { User } from "../entities/User";
import { Category } from "../entities/Category";
import { Article } from "../entities/Article";

export const AppDataSource = new DataSource({
    type: "sqlite",
    database: "database.sqlite",
    synchronize: true,
    logging: false,
    entities: [User, Category, Article],
    migrations: [],
    subscribers: [],
});
```

**Análise Detalhada:**

Este arquivo é curto, mas cada linha tem um significado importante para o funcionamento do TypeORM.

1.  **`import { DataSource } from "typeorm";`**: Importa a classe principal do TypeORM que será usada para criar a nossa fonte de dados.

2.  **`import { ... } from "../entities/..."`**: Importamos todas as classes que definimos como **Entidades**. Uma entidade é uma classe que o TypeORM mapeia para uma tabela no banco de dados. Precisamos listar todas elas aqui para que o TypeORM saiba quais tabelas ele deve gerenciar.

3.  **`export const AppDataSource = new DataSource({...});`**: Aqui criamos e exportamos a instância do `DataSource`. É esta instância que usamos no `server.ts` para inicializar a conexão (`AppDataSource.initialize()`). O objeto de configuração dentro do `new DataSource` é o mais importante:

    *   **`type: "sqlite"`**: Especifica qual driver de banco de dados o TypeORM deve usar. Poderia ser `"postgres"`, `"mysql"`, etc. Nós usamos `"sqlite"` por sua simplicidade: ele cria um arquivo de banco de dados (`database.sqlite`) no diretório do projeto e não requer um servidor de banco de dados separado.

    *   **`database: "database.sqlite"`**: Para o driver `sqlite`, este é o nome do arquivo que será usado para armazenar os dados.

    *   **`synchronize: true`**: Esta é uma das funcionalidades mais poderosas e perigosas do TypeORM. Quando `true`, a cada vez que a aplicação inicia, o TypeORM compara o estado das suas entidades (`User`, `Category`, etc.) com as tabelas no banco de dados. Se houver alguma diferença (ex: você adicionou uma nova coluna na entidade `User`), o TypeORM **altera automaticamente** a tabela no banco para corresponder à entidade.
        *   **Vantagem:** Fantástico para desenvolvimento e prototipagem rápida, pois você não precisa escrever migrações de banco de dados manualmente.
        *   **Cuidado:** **NUNCA** use `synchronize: true` em produção! Uma mudança acidental no código da entidade poderia levar à perda de dados em uma tabela de produção. Em produção, o correto é usar `migrations`.

    *   **`logging: false`**: Se definido como `true`, o TypeORM imprimirá no console todas as queries SQL que ele executa. É extremamente útil para debugar, mas pode poluir os logs em produção ou durante os testes.

    *   **`entities: [User, Category, Article]`**: Um array que lista todas as classes de entidade que o TypeORM deve carregar e gerenciar. Se você criar uma nova entidade, precisa adicioná-la aqui.

    *   **`migrations: []`** e **`subscribers: []`**: Seções para configurar migrações de banco de dados e "subscribers" (event listeners do TypeORM), que são funcionalidades mais avançadas que não estamos usando neste projeto.

---

### Página 3: Os "Modelos" do Banco de Dados - `entities/User.ts`

As entidades são o coração do TypeORM. Uma entidade é uma classe que mapeia diretamente para uma tabela no banco de dados. Cada instância de uma entidade corresponde a uma linha nessa tabela, e as propriedades da classe correspondem às colunas.

Vamos analisar a entidade `User.ts` para ver como essa "mágica" acontece usando **Decorators** (os `@`s).

**Código-Fonte: `cms-for-qas-api/src/entities/User.ts`**

```typescript
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, OneToMany, Index } from "typeorm";
import { Article } from "./Article";

@Entity("users")
export class User {
    @PrimaryGeneratedColumn("uuid")
    id: string;

    @Column({ name: "nome_completo" })
    nomeCompleto: string;

    @Column({ name: "nome_usuario" })
    @Index({ unique: true })
    nomeUsuario: string;

    @Column()
    @Index({ unique: true })
    email: string;

    @Column({ select: false })
    senha: string;

    @CreateDateColumn({ name: "data_criacao" })
    dataCriacao: Date;

    @OneToMany(() => Article, article => article.autor)
    artigos: Article[];
}
```

**Análise Detalhada dos Decorators:**

Decorators são uma funcionalidade do TypeScript que nos permite anotar ou modificar classes e suas propriedades. O TypeORM os utiliza extensivamente.

1.  **`@Entity("users")`**:
    *   Este decorator marca a classe `User` como uma entidade do TypeORM.
    *   O argumento opcional `"users"` define o nome da tabela que será criada no banco de dados. Se omitido, o TypeORM usaria o nome da classe em minúsculas (`"user"`).

2.  **`@PrimaryGeneratedColumn("uuid")`**:
    *   Marca a propriedade `id` como a **chave primária** da tabela.
    *   O argumento `"uuid"` especifica que o valor deve ser um UUID (Universally Unique Identifier) gerado automaticamente.

3.  **`@Column({...})`**:
    *   Marca uma propriedade da classe como uma **coluna** na tabela do banco de dados.
    *   **`{ name: "nome_completo" }`**: Permite definir um nome para a coluna no banco (`snake_case`) que é diferente do nome da propriedade no código (`camelCase`).
    *   **`{ select: false }`**: Opção de segurança importante usada na `senha`. Diz ao TypeORM para **NÃO** incluir esta coluna em queries de seleção (`SELECT`), evitando que a senha (mesmo hasheada) seja exposta desnecessariamente.

4.  **`@Index({ unique: true })`**:
    *   Cria um **índice** na coluna, o que acelera buscas.
    *   A opção `{ unique: true }` adiciona uma restrição que garante que não pode haver dois registros com o mesmo valor nesta coluna (usado em `nomeUsuario` e `email`).

5.  **`@CreateDateColumn({...})`**:
    *   Decorator especial que diz ao TypeORM para inserir automaticamente a data e a hora atuais quando um novo registro é criado.

6.  **`@OneToMany(...)`**:
    *   Define um **relacionamento** entre tabelas. `@OneToMany(() => Article, article => article.autor)` significa: "Um `User` pode ter muitos (`One`) `Article`s (`ToMany`)".
    *   Isso não cria uma coluna na tabela `users`, mas permite que, ao carregar um usuário, você também possa carregar todos os seus artigos associados.

---

### Página 4: As Portas de Entrada da API - `routes/userRoutes.ts`

As rotas são as portas de entrada da nossa API. Elas definem os URLs (endpoints) que os clientes podem acessar e qual código será executado para cada um deles. O Express fornece uma ferramenta poderosa para isso, chamada `Router`.

Neste projeto, o arquivo `userRoutes.ts` combina a definição da rota, a validação e a lógica de negócio (interação com o banco), o que nos dá uma visão completa do fluxo de uma requisição em um único lugar.

**Código-Fonte (Abreviado): `cms-for-qas-api/src/routes/userRoutes.ts`**

```typescript
// ... (imports)
import { Router, Request, Response } from "express";
import { body } from "express-validator";
import { AppDataSource } from "../database/data-source";
import { User } from "../entities/User";
import { validateRequest } from "../middleware/validateRequest";

const router = Router();
const userRepository = AppDataSource.getRepository(User);

// Rota para CRIAR um usuário (POST /usuarios)
router.post("/",
    [
        // 1. Array de Middlewares de Validação
        body("nomeCompleto").notEmpty(),
        body("email").isEmail(),
        body("senha").isLength({ min: 6 }),
        validateRequest // 2. Nosso middleware customizado
    ],
    // 3. O Handler da Rota
    async (req: Request, res: Response) => {
        try {
            // Lógica de negócio: hashear a senha, verificar duplicados...
            // Interação com o banco via TypeORM...
            // Envio da resposta...
        } catch (error) {
            // Tratamento de erro...
        }
    }
);

// ... (outras rotas: GET, PUT, DELETE)

export default router;
```
*(Nota: O código acima foi abreviado. O arquivo completo está no projeto.)*

**Análise Detalhada:**

1.  **`const router = Router();`**: Cria uma instância do `Router` do Express, um "mini-app" que agrupa rotas relacionadas. Este `router` é exportado e usado no `server.ts`.

2.  **`const userRepository = AppDataSource.getRepository(User);`**: Este é o nosso principal ponto de acesso para interagir com a tabela `users`. O método `getRepository(User)` do TypeORM nos dá um objeto com todos os métodos para realizar operações de CRUD (Create, Read, Update, Delete) na entidade `User`.

3.  **A Estrutura de uma Rota:** Cada rota segue o padrão `router.METODO(caminho, [middlewares], handler)`.
    *   **`caminho`**: O URL, como `/` ou `/:id`.
    *   **`[middlewares]`**: Um array de funções executadas *antes* do handler. Nós o usamos para validação.
    *   **`handler`**: A função final que processa a requisição e envia a resposta.

4.  **Validação com `express-validator`:**
    *   A rota `POST` usa um array de validadores como `body("email").isEmail()`. Se a validação falhar, um erro é anexado à requisição.
    *   `validateRequest`: É um middleware customizado que verifica se algum validador anterior falhou. Se sim, ele interrompe o fluxo e envia uma resposta `400 Bad Request`, mantendo os handlers limpos.

5.  **Lógica de Negócio no Handler:**
    *   **`async/await`**: Os handlers são `async` porque as operações de banco de dados são assíncronas. Isso torna o código mais legível.
    *   **Segurança**: O código demonstra duas práticas de segurança cruciais:
        1.  **Hashing de Senha com `bcrypt`**: A senha do cliente é transformada em um hash criptográfico antes de ser salva.
        2.  **Omissão da Senha na Resposta**: Um truque de desestruturação (`const { senha: _, ... }`) é usado para garantir que o hash da senha nunca seja enviado de volta na resposta da API.

6.  **Ponto de Melhoria (Separação de Responsabilidades):**
    *   Em uma aplicação maior, a lógica dentro do handler seria movida para uma classe de **Serviço** (ex: `UserService`). O handler (que seria o **Controller**) apenas receberia a requisição, chamaria o serviço e devolveria a resposta. Isso torna o código mais testável e organizado, seguindo o princípio da responsabilidade única.
