# Spring Boot Study

Tutorial repository for Java 21 + Spring Boot 3.x.

## INDEX

- [ABOUT](#about)
- [ENVIRONMENT](#environment)
- [For Developer](#for-developer)

## ABOUT

A study project exploring Spring Boot 3 with Java 21, packaged as an OCI container image via Nix.

Implements a user profile search feature using MVC + Hexagonal Architecture (Controller / Domain / DAO layers), with MyBatis XML Mapper for SQL and PostgreSQL as the database.

## ENVIRONMENT

**Requirements**

- [Nix](https://nixos.org/) (flakes enabled)
- [Podman](https://podman.io/)

**Enter the dev shell** (provides Java 21 + Gradle):

```bash
nix develop
```

## For Developer

### Run locally

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
controller/   HTTP layer (Spring MVC @Controller)
domain/       Entities and repository interface (port)
dao/          MyBatis @Mapper implementing the repository (adapter)
```

- View: Thymeleaf templates (`src/main/resources/templates/`)
- SQL: MyBatis XML Mapper (`src/main/resources/mapper/UserMapper.xml`)

### API / Endpoints

| Method | Path              | Description         |
| ------ | ----------------- | ------------------- |
| GET    | `/hello`          | Greeting API (JSON) |
| GET    | `/users`          | User search form    |
| GET    | `/users/{userId}` | Show user profile   |

**Hello API example:**

```bash
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Spring"
```

**User search:** open `http://localhost:8080/users` in a browser.

Available dummy users: `user001`, `user002`, `user003`, `alice`, `bob`
