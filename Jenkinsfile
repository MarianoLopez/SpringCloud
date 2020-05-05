pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-14'
      args '''-v /home/jenkins/.m2:/root/.m2
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_USER=${NEXUS_USER}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}
--network=delivery_delivery'''
    }

  }
  stages {
    stage('Clean & Install Libraries') {
      steps {
        dir(path: 'jwt') {
          sh 'mvn clean install -DskipTests'
        }

        dir(path: 'zcore-blocking') {
          sh 'mvn clean install -DskipTests'
        }

      }
    }

    stage('Clean & Build Backend') {
      steps {
        dir(path: 'user-service') {
          sh 'mvn clean package -DskipTests'
        }

        dir(path: 'eureka-service') {
          sh 'mvn clean package -DskipTests'
        }

        dir(path: 'gateway-service') {
          sh 'mvn clean package -DskipTests'
        }

      }
    }

    stage('Backend Tests') {
      steps {
        dir(path: 'user-service') {
          sh 'mvn surefire:test'
        }

      }
    }

    

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}