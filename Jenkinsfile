pipeline {
  agent any
  stages {
    stage('Build backend') {
      steps {
         build job: 'User-service', propagate: true, wait: true
      }
    }

  }
  environment {
    M2_HOME = '/home/jenkins/.m2'
    NPM_CACHE = '/home/jenkins/.npm'
    NEXUS_URL = 'localhost'
    NEXUS_DOCKER_PORT = 8082
    GATEWAY_URL="$params.GATEWAY_URL"
  }
  parameters {
    booleanParam(name: 'INSTALL_LIBRARIES', defaultValue: false, description: 'Whether or not run mvn install for libraries')
    booleanParam(name: 'BUILD_INFRA', defaultValue: false, description: 'Whether or not build Eureka & Gateway services')
    booleanParam(name: 'BUILD_BACKEND', defaultValue: true, description: 'Whether or not build microservices')
    booleanParam(name: 'BUILD_FRONTEND', defaultValue: true, description: 'Whether or not build frontend')
    string(name: 'GATEWAY_URL', defaultValue: 'http://localhost:9000', description: 'Gateway service URL to be use by the frontend')
  }
}