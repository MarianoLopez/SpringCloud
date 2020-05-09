#!/bin/bash

DOCKER_IMAGE_TAG=${2:-SNAPSHOT}

docker build -t "$1:$DOCKER_IMAGE_TAG" .