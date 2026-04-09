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
- [Podman](https://podman.io/) (with podman-compose)

**Enter the dev shell** (provides Java 21 + Gradle):

```bash
nix develop
```

## For Developer

### Start the database

```bash
podman compose up -d
```

This starts a PostgreSQL container and loads `db/init.sql` (creates the `users` table with dummy data).

### Run locally

```bash
# Start app (DB must be running first)
gradle bootRun

# Stop DB
podman compose down
# Or stop everything at once
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
# All-in-one
./deploy.sh
```

Or step by step:

```bash
# 1. Build the jar
gradle bootJar

# 2. Build OCI image via Nix
nix build path:.#container

# 3. Load into Podman
podman load < result

# 4. Start DB and app
podman compose up -d
podman run --rm -p 8080:8080 spring-boot-study:latest

# 5. Stop
./stop.sh
```

### Architecture

```
controller/   HTTP layer (Spring MVC @Controller)
domain/       Entities and repository interface (port)
dao/          MyBatis @Mapper implementing the repository (adapter)
```

SQL is defined in `src/main/resources/mapper/UserMapper.xml`.

### API / Endpoints

| Method | Path             | Description                    |
| ------ | ---------------- | ------------------------------ |
| GET    | `/hello`         | Greeting API (JSON)            |
| GET    | `/users`         | User search form               |
| GET    | `/users/{userId}`| Show user profile              |

**Hello API example:**

```bash
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Spring"
```

**User search:** open `http://localhost:8080/users` in a browser.

Available dummy users: `user001`, `user002`, `user003`, `alice`, `bob`
