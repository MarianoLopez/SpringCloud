pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-14'
      args '-v /home/jenkins/.m2:/root/.m2'
    }

  }
  stages {
    stage('Initialize') {
      parallel {
        stage('Initialize') {
          steps {
            sh '''echo PATH = ${PATH}
echo M2_HOME = ${M2_HOME}'''
          }
        }

        stage('Install Libraries') {
          steps {
            dir(path: 'jwt') {
              sh 'mvn clean install -Dmaven.test.skip=true'
            }

          }
        }

        stage('Just test') {
          steps {
            sh '''echo $PATH
echo $NEXUS_HOST
echo $NEXUS_PORT
echo $NEXUS_PASSWORD
echo $M2_HOME'''
          }
        }

      }
    }

    stage('Publish to Nexus') {
      steps {
        sh 'curl -v -u "admin:${NEXUS_PASSWORD}" --upload-file "${M2_HOME}/repository/com/z/jwt/0.0.1-SNAPSHOT/jwt-0.0.1-SNAPSHOT.jar" "http://${NEXUS_HOST}:${NEXUS_PORT}/repository/maven-releases/com/z/jwt/0.0.1-SNAPSHOT/jwt-0.0.1-SNAPSHOT.jar"'
      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}