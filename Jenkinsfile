pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-14'
      args '''-v /home/jenkins/.m2:/root/.m2
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}'''
    }

  }
  stages {
    stage('Install Libraries') {
      steps {
        dir(path: 'jwt') {
          sh 'mvn clean'
          sh 'mvn deploy -DskipTests'
        }

      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}