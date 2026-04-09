#!/usr/bin/env bash
set -e

CONTAINER_NAME="userdb"

if podman ps -a --format "{{.Names}}" | grep -q "^${CONTAINER_NAME}$"; then
    echo "Removing existing container: ${CONTAINER_NAME}"
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

echo "Waiting for PostgreSQL to be ready..."
until podman exec "${CONTAINER_NAME}" pg_isready -U user -d userdb > /dev/null 2>&1; do
    sleep 1
done

echo "DB is ready. Container: ${CONTAINER_NAME}"
