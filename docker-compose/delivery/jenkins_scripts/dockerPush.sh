#!/bin/bash

DOCKER_IMAGE_TAG=${2:-SNAPSHOT}

docker push "$1:$DOCKER_IMAGE_TAG"