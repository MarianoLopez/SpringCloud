#!/bin/sh

DOCKER_IMAGE_TAG=${2:-latest}

docker build -t "$1:$DOCKER_IMAGE_TAG" .