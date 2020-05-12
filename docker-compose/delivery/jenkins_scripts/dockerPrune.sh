#!/bin/sh

#docker image prune -f
docker rmi $(docker images -f "dangling=true" -q) --force