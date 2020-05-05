pipeline {
  agent none
  stages {
    stage('Build Frontend') {
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '''-v ${NODE_MODULES}:/var/jenkins_home/workspace/gCloud_feature_jenkins-blueocean/z-dash/node_modules
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_USER=${NEXUS_USER}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}
--network=delivery_delivery'''
        }

      }
      steps {
        dir(path: 'z-dash') {
          sh '''ls /
ls -la
pwd
npm ci --silent

npm install react-scripts@3.4.1 -g --silent

ls -la

npm run build'''
        }

      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
    NODE_MODULES = '/root/node_modules'
  }
}