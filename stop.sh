#!/bin/bash
APP=$(podman ps -q --filter ancestor=spring-boot-study:latest)
if [ -n "$APP" ]; then
    podman stop "$APP"
fi
podman rm -f userdb
