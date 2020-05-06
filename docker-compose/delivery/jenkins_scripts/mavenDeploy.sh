#!/bin/bash

mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port="$NEXUS_PORT" -Dnexus.host="$NEXUS_HOST" -f "$1"