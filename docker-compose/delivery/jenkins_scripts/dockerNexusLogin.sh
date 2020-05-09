#!/bin/bash

docker login -u "$NEXUS_USER" -p "$NEXUS_PASSWORD" "$1"