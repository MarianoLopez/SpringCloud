pipeline {
  agent none
  stages {
    stage('Clean & Install Libraries') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      steps {
        sh 'cat /root/.m2/settings.xml'
        dir(path: 'jwt') {
          sh 'mvn clean install -DskipTests'
        }

        dir(path: 'zcore-blocking') {
          sh 'mvn clean install -DskipTests'
        }

      }
    }

    stage('Clean & Build Backend') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
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
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      steps {
        dir(path: 'user-service') {
          sh 'mvn test'
        }

      }
    }

    stage('Frontend build') {
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '-v ${NPM_CACHE}:/root/.npm'
        }

      }
      steps {
        dir(path: 'z-dash') {
          sh '''npm ci --silent

npm run build'''
          stash(name: 'build-z-dash', includes: 'build/**')
        }

      }
    }

    stage('Deploy to Nexus') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '''-v ${M2_HOME}:/root/.m2
                -e NEXUS_PASSWORD=${NEXUS_PASSWORD}
                -e NEXUS_USER=${NEXUS_USER}
                -e NEXUS_HOST=${NEXUS_HOST}
                -e NEXUS_PORT=${NEXUS_PORT}
                --network=delivery_delivery'''
        }

      }
      steps {
        dir(path: 'z-dash') {
          unstash 'build-z-dash'
          sh '''echo $NEXUS_USER
echo $NEXUS_PASSWORD

pwd
ls -la

ls -la /root/.m2/

cat /root/.m2/settings.xml'''
        }

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

    stage('Clean workspace') {
      steps {
        cleanWs(cleanWhenSuccess: true, cleanupMatrixParent: true, deleteDirs: true, skipWhenFailed: true)
      }
    }

  }
  environment {
    M2_HOME = '/root/jenkins/.m2'
    NPM_CACHE = '/root/jenkins/.npm'
  }
}