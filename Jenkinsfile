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
          sh 'mvn clean install -DskipTests'
        }

        dir(path: 'zcore-blocking') {
          sh 'mvn clean install -DskipTests'
        }

      }
    }

    stage('Build Backend') {
      parallel {
        stage('Build Backend') {
          steps {
            echo 'Build backend'
          }
        }

        stage('Eureka & Gateway') {
          steps {
            dir(path: 'eureka-service') {
              sh 'mvn clean package -DskipTests'
            }

            dir(path: 'gateway-service') {
              sh 'mvn clean package -DskipTests'
            }

          }
        }

        stage('Microservices') {
          steps {
            dir(path: 'user-service') {
              sh 'mvn clean package'
            }

          }
        }

      }
    }

    stage('Clean') {
      steps {
        cleanWs(cleanWhenAborted: true, cleanWhenFailure: true, cleanWhenNotBuilt: true, cleanWhenSuccess: true, cleanWhenUnstable: true, cleanupMatrixParent: true, deleteDirs: true)
      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}