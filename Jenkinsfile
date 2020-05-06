pipeline {
  agent any
  stages {
    stage('Clean & Install Libraries') {
      options { skipDefaultCheckout() }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      steps {
        sh 'cat /root/.m2/settings.xml'
      }

      steps {
        dir(path: 'jwt') {
          sh '/jenkins_scripts/mavenInstall.sh ./pom.xml'
        }

        dir(path: 'zcore-blocking') {
          sh '/jenkins_scripts/mavenInstall.sh ./pom.xml'
        }

      }
    }

   stage('Clean & Build Backend') {
      options { skipDefaultCheckout() }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      steps {
        dir(path: 'user-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
          stash(name: 'build-user-service', includes: 'build/**')
        }

        dir(path: 'eureka-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
        }

        dir(path: 'gateway-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
        }

      }
    }

    stage('Backend Tests') {
      options { skipDefaultCheckout() }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      steps {
        dir(path: 'user-service') {
        unstash 'build-user-service'
          sh '/jenkins_scripts/mavenTest.sh ./pom.xml'
        }

      }
    }

    stage('Frontend build') {
      options { skipDefaultCheckout() }
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '-v ${NPM_CACHE}:/root/.npm'
        }

      }
      steps {
        dir(path: 'z-dash') {
          sh '/jenkins_scripts/npmBuild.sh'
          stash(name: 'build-z-dash', includes: 'build/**')
        }

      }
    }

    stage('Deploy to Nexus') {
      options { skipDefaultCheckout() }
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
          sh '/jenkins_scripts/compress.sh z-dash-0.0.1-SNAPSHOT ./build'
          sh '/jenkins_scripts/nexusHttpDeploy.sh z-dash-0.0.1-SNAPSHOT.tar.gz npm-snapshots com.z.z-dash'
        }

        dir(path: 'jwt') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'zcore-blocking') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'user-service') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'eureka-service') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'gateway-service') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

      }
    }

    stage('Clean workspace') {
      steps {
        cleanWs()
      }
    }

  }
  environment {
    M2_HOME = '/root/jenkins/.m2'
    NPM_CACHE = '/root/jenkins/.npm'
  }
}