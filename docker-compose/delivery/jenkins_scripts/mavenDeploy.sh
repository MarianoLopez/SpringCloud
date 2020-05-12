#!/bin/bash

mvn deploy -DskipTests -Dnexus.port="$NEXUS_PORT" -Dnexus.host="$NEXUS_HOST" -f "$1"