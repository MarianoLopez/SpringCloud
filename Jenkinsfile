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

    stage('Deploy to Nexus') {
      steps {
        dir(path: 'jwt') {
          sh 'mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port=$NEXUS_PORT -Dnexus.host=$NEXUS_HOST'
        }

        dir(path: 'zcore-blocking') {
          sh 'mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port=$NEXUS_PORT -Dnexus.host=$NEXUS_HOST'
        }

        dir(path: 'user-service') {
          sh 'mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port=$NEXUS_PORT -Dnexus.host=$NEXUS_HOST'
        }

        dir(path: 'eureka-service') {
          sh 'mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port=$NEXUS_PORT -Dnexus.host=$NEXUS_HOST'
        }

        dir(path: 'gateway-service') {
          sh 'mvn deploy -DskipTests -Dmaven.install.skip=true -Dnexus.port=$NEXUS_PORT -Dnexus.host=$NEXUS_HOST'
        }

      }
    }

  }
  environment {
    M2_HOME = '/root/.m2'
  }
}