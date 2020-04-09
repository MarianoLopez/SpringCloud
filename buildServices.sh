#!/bin/bash

cdToServiceFolder() {
  parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" || exit ; pwd -P )
  cd "$parent_path/$1" || exit
}

runMaven() {
  chmod 755 ./mvnw
  ./mvnw clean "$1" -Dmaven.test.skip=true

  #if maven build successfuly
  STATUS=$?
  [ $STATUS -eq 0 ]
}

isInstalled() {
  [ -x "$(command -v "$1")" ]
}

printError() {
  echo -e "\e[31m[ERROR] $1\e[0m" >&2
}
printErrorAndStop() {
  printError "$1"
  exit 1
}

buildDockerImage() {
  docker build -t "$1":"$2" .
}

buildOrInstallService() {
  SERVICE_NAME=$1
  MAVEN_COMMAND=${2:-package}
  DOCKER_IMAGE_TAG=${3:-snapshot}

  cdToServiceFolder "$SERVICE_NAME"

  if runMaven "$MAVEN_COMMAND"; then
    #if maven command was not install then run docker build
    if ! [ "$MAVEN_COMMAND" == "install" ]; then
      if isInstalled docker; then
        buildDockerImage "$SERVICE_NAME" "$DOCKER_IMAGE_TAG"
      else
        printError "docker is not installed."
      fi
    fi
  else
    printErrorAndStop "Maven $MAVEN_COMMAND failed."
  fi

  cd ..
}

printJavaInfo() {
  JAVA_VERSION=$(java -version 2>&1)
  echo -e "\e[33mJAVA_HOME= $JAVA_HOME\e[0m"
  echo -e "\e[33m$JAVA_VERSION\e[0m"
}

installLibraries() {
  libraries=("jwt" "zcore-blocking")
  for i in "${libraries[@]}"
  do
     buildOrInstallService "$i" "install"
  done
}

buildServices() {
  services=("eureka-service" "user-service" "gateway-service")
  for i in "${services[@]}"
  do
     buildOrInstallService "$i"
  done
}

######################### MAIN #########################
if isInstalled java; then
  printJavaInfo
  installLibraries
  buildServices
else
  printErrorAndStop "java is not installed."
fi