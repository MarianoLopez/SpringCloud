#!/bin/sh

SERVICE_NAME=$1
DOCKER_IMAGE_TAG=${2:-latest}

/jenkins_scripts/dockerBuild.sh "$NEXUS_URL:$NEXUS_DOCKER_PORT/$SERVICE_NAME" "$DOCKER_IMAGE_TAG"
/jenkins_scripts/dockerNexusLogin.sh "$NEXUS_URL:$NEXUS_DOCKER_PORT"
/jenkins_scripts/dockerPush.sh "$NEXUS_URL:$NEXUS_DOCKER_PORT/$SERVICE_NAME" "$DOCKER_IMAGE_TAG"