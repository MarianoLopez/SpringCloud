#!/bin/bash

curl -v -u "${NEXUS_USER}:${NEXUS_PASSWORD}" \
--upload-file "$1" \
"http://${NEXUS_HOST}:${NEXUS_PORT}/repository/$2/$3"