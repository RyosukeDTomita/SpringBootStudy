#!/bin/bash
podman stop $(podman ps -q --filter ancestor=spring-boot-study:latest)
