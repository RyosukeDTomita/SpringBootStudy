#!/bin/bash
set -e

# Start DB
CONTAINER_NAME="userdb"
if podman ps -a --format "{{.Names}}" | grep -q "^${CONTAINER_NAME}$"; then
    podman rm -f "${CONTAINER_NAME}"
fi
podman run -d \
    --name "${CONTAINER_NAME}" \
    -e POSTGRES_DB=userdb \
    -e POSTGRES_USER=user \
    -e POSTGRES_PASSWORD=password \
    -p 5432:5432 \
    -v "$(pwd)/db/init.sql:/docker-entrypoint-initdb.d/init.sql:ro" \
    docker.io/postgres:16

echo "Waiting for DB..."
until podman exec "${CONTAINER_NAME}" pg_isready -U user -d userdb > /dev/null 2>&1; do
    sleep 1
done

# Build and run app
gradle bootJar
nix build path:.#container
podman load < result
podman run --rm -p 8080:8080 spring-boot-study:latest
