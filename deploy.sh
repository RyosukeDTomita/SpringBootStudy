#!/bin/bash
set -e

podman compose up -d

gradle bootJar
nix build path:.#container
podman load < result
podman run --rm -p 8080:8080 spring-boot-study:latest
