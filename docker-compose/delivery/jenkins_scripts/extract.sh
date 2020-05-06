#!bin/bash

EXTRACT_FOLDER=${2:./}
tar -xzvf "$1" -C "$EXTRACT_FOLDER"