#!/bin/sh
REGISTRY="http://${NEXUS_HOST}:${NEXUS_PORT}/repository/$1/"

npm install npm-cli-login

npm config set registry "${REGISTRY}"

npm-cli-login -u "${NEXUS_USER}" -p "${NEXUS_PASSWORD}" -e jenkins@test.com -r "${REGISTRY}"

npm publish