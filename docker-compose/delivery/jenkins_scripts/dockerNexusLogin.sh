#!/bin/bash

docker login -u "$NEXUS_USER" -p "$NEXUS_PASSWORD" "$NEXUS_HOST:$NEXUS_DOCKER_PORT"