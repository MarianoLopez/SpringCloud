pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-14'
      args '''-v /home/jenkins/.m2:/root/.m2
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}
--network=delivery_delivery'''
    }

  }
  stages {
    stage('Install Libraries') {
      steps {
        sh 'echo $NEXUS_HOST'
        dir(path: 'jwt') {
          sh 'mvn clean install -DskipTests'
        }

        dir(path: 'zcore-blocking') {
          sh 'mvn clean install -DskipTests'
        }

      }
    }

    stage('Build Backend') {
      steps {
        dir(path: 'user-service') {
          sh 'mvn clean package'
        }

        dir(path: 'eureka-service') {
          sh 'mvn clean package -DskipTests'
        }

        dir(path: 'gateway-service') {
          sh 'mvn clean package -DskipTests'
        }

      }
    }

    stage('Deploy to Nexus') {
      steps {
        sh 'curl -v -u "admin:${NEXUS_PASSWORD}" --upload-file "${M2_HOME}/repository/com/z/jwt/0.0.1-SNAPSHOT/jwt-0.0.1-SNAPSHOT.jar" "http://${NEXUS_HOST}:${NEXUS_PORT}/repository/maven-releases/com/z/jwt/0.0.1-SNAPSHOT/jwt-0.0.1-SNAPSHOT.jar"'
      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}