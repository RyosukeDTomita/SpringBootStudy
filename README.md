# Spring Boot Study

Tutorial repository for Java 21 + Spring Boot 3.x.

## INDEX

- [TECH STACK](#tech-stack)
- [ABOUT](#about)
- [ENVIRONMENT](#environment)
- [For Developer](#for-developer)

---

## TECH STACK

| Category | Technology |
| -------- | ---------- |
| Language | Java |
| Framework | Spring Boot |
| Build | Gradle (Kotlin DSL) |
| ORM | MyBatis |
| Mapping | MapStruct |
| Template | Thymeleaf |
| DB | PostgreSQL |
| Security | Spring Security |
| Validation | Jakarta Bean Validation |
| Test | JUnit 5 / Spring Security Test |
| E2E Test | Playwright |
| E2E Runtime | Bun + TypeScript |
| Formatter | google-java-format / mdformat |
| Dev Environment | Nix (flakes) |
| Built in AP Server | Tomcat |
| Container | Podman + Nix `dockerTools` |

---

## ABOUT

A study project exploring Spring Boot 3 with Java 21, packaged as an OCI container image via Nix.

Implements a user profile search feature using MVC + Hexagonal Architecture (Controller / Service / Domain / DAO layers), with MyBatis XML Mapper for SQL and PostgreSQL as the database.

### Spring Features

| カテゴリ | 機能 |
| --- | --- |
| DI | コンストラクタインジェクション (`@Autowired` 省略) |
| DI | `@Configuration` + `@Bean` による Bean 定義 |
| DI | ステレオタイプ (`@Service`, `@Repository`, `@Controller`, `@RestController`) |
| MVC | `@Controller` + `@RequestMapping` (画面遷移) |
| MVC | `@RestController` (JSON API) |
| MVC | `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` |
| MVC | `@PathVariable` / `@RequestParam` |
| MVC | `@RequestBody` / `@ResponseStatus` による JSON 送受信 |
| MVC | `@ModelAttribute` + `Model` によるフォームバインディング |
| MVC | リダイレクト (`redirect:`) |
| MVC | 静的リソース配信 (`static/`) |
| Validation | Bean Validation (`@Valid`, `@NotBlank`, `BindingResult`) |
| テンプレート | Thymeleaf (`th:action`, `th:object`, `th:field`, `th:text`, `th:if`, `th:errors`) |
| トランザクション | `@Transactional` による宣言的トランザクション管理 |
| データアクセス | MyBatis (`@Mapper` + XML Mapper) |
| データアクセス | MyBatis `useGeneratedKeys` による自動採番 |
| アーキテクチャ | ヘキサゴナル (依存性逆転): domain にインターフェース、dao に `@Repository` 実装 |
| アーキテクチャ | サービス層 (`@Service`) によるユースケース集約 |
| マッピング | MapStruct (`@Mapper(componentModel = "spring")`, `@Mapping`) |
| Security | `@EnableWebSecurity` + `SecurityFilterChain` によるロールベースアクセス制御 |
| Security | フォームログイン + `HttpSession` によるセッション管理 |
| Security | `InMemoryUserDetailsManager` によるユーザ定義 |
| Security | `CookieCsrfTokenRepository` による CSRF 対策 |
| Security | `thymeleaf-extras-springsecurity6` (`sec:authentication`) による認証情報の画面表示 |
| テスト | `@WebMvcTest` + `MockMvc` によるコントローラー単体テスト |
| テスト | Mockito (`@ExtendWith(MockitoExtension.class)`, `@Mock`, `when`/`verify`) によるサービス単体テスト |
| テスト | JUnit 5 (`@Test`, `@BeforeEach`) ベースのテスト |
| 設定 | `application.yaml` による外部設定 (datasource, mybatis) |
| レコード | Java `record` を DTO として活用 |

---

## ENVIRONMENT

**Requirements**

- [Nix](https://nixos.org/) (flakes enabled)
- [Podman](https://podman.io/)

**Enter the dev shell** (provides Java 21 + Gradle):

```bash
nix develop
```

---

## For Developer

### Run locally

`deploy.sh`がDB起動→アプリのコンテナービルド→起動までを一括で行います。

```bash
# Start DB + build & run app container
./deploy.sh

# Stop (DB + app コンテナを停止)
./stop.sh
```

開発中にアプリだけ手動で起動したい場合（DBはコンテナー、アプリは`gradle bootRun`）:

```bash
# 1. Start DB
podman run -d \
    --name userdb \
    -e POSTGRES_DB=userdb \
    -e POSTGRES_USER=user \
    -e POSTGRES_PASSWORD=password \
    -p 5432:5432 \
    -v "$(pwd)/db/init.sql:/docker-entrypoint-initdb.d/init.sql:ro" \
    docker.io/postgres:16

# 2. Start app
gradle bootRun

# 3. Stop everything
./stop.sh
```

### Test

```bash
gradle test
```

### E2E Test (Playwright)

```bash
# アプリ起動 → テスト実行 → 停止 を一括で行う
./e2e.sh

# アプリが起動済みの状態で手動実行する場合
cd e2e
bun run test          # headless
bun run test:headed   # ブラウザ表示あり
```

### Format

Format all `.md` and `.java` files at once:

```bash
nix fmt
```

- Markdown: [mdformat](https://github.com/hukkin/mdformat)
- Java: [google-java-format](https://github.com/google/google-java-format)

### Run as container

```bash
# All-in-one (starts DB + builds and runs app container)
./deploy.sh

# Stop
./stop.sh
```

### Architecture

```
controller/   HTTP layer (Spring MVC @Controller / @RestController)
service/      Use case layer (@Service) — orchestrates domain and DAO
domain/       Entities and repository interface (port)
dao/          MyBatis @Mapper implementing the repository (adapter)
```

- View: Thymeleaf templates (`src/main/resources/templates/`)
- SQL: MyBatis XML Mapper (`src/main/resources/mapper/UserMapper.xml`)

### API / Endpoints

| Method | Path                      | Description              | Auth        |
| ------ | ------------------------- | ------------------------ | ----------- |
| GET    | `/hello`                  | Greeting API (JSON)      | -           |
| GET    | `/users`                  | User search form         | -           |
| GET    | `/users/{userId}`         | Show user profile        | -           |
| GET    | `/shop`                   | Shop page                | USER        |
| POST   | `/shop/purchase`          | Purchase product         | USER        |
| GET    | `/register/item`          | List all items (JSON)    | ADMIN       |
| GET    | `/register/item/{itemId}` | Get item by ID (JSON)    | ADMIN       |
| POST   | `/register/item`          | Create item (JSON)       | ADMIN       |
| PUT    | `/register/item/{itemId}` | Update item (JSON)       | ADMIN       |
| DELETE | `/register/item/{itemId}` | Delete item              | ADMIN       |
| GET    | `/api/me`                 | Current user info (JSON) | USER        |

**Hello API example:**

```bash
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Spring"
```

**User search:** open `http://localhost:8080/users` in a browser.

Available dummy users: `user001`, `user002`, `user003`, `alice`, `bob`
