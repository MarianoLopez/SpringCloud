#!/bin/sh

DOCKER_IMAGE_TAG=${2:-latest}

docker push "$1:$DOCKER_IMAGE_TAG"