# Spring Boot Study

Tutorial repository for Java 21 + Spring Boot 3.x.

## INDEX

- [ABOUT](#about)
- [ENVIRONMENT](#environment)
- [For Developer](#for-developer)

## ABOUT

A study project exploring Spring Boot 3 with Java 21, packaged as an OCI container image via Nix.

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
# Start
gradle bootRun

# Verify in another terminal
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Spring"
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

# 4. Start
podman run --rm -p 8080:8080 spring-boot-study:latest

# 5. Verify
curl http://localhost:8080/hello

# 6. Stop
podman stop $(podman ps -q --filter ancestor=spring-boot-study:latest)
```

### API

| Method | Path     | Parameter                          | Response example                                    |
| ------ | -------- | ---------------------------------- | --------------------------------------------------- |
| GET    | `/hello` | `name` (optional, default: `World`) | `{"message":"Hello, World!","name":"World"}` |
