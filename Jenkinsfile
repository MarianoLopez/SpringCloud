pipeline {
  agent none
  stages {
    stage('Build Frontend') {
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '''-v ${PWD}:/app  
-v /app/node_modules
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_USER=${NEXUS_USER}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}
--network=delivery_delivery'''
        }

      }
      steps {
        dir(path: 'z-dash') {
          sh '''npm ci

npm install react-scripts@3.4.1 -g

npm run build'''
        }

      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}