pipeline {
  agent any
  stages {
    stage('Clean & Install Libraries') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      when {
        expression {
          params.INSTALL_LIBRARIES
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'jwt') {
          sh 'ls /root/.m2/'
          sh 'cat /root/.m2/settings.xml'
          sh '/jenkins_scripts/mavenInstall.sh ./pom.xml'
        }

        dir(path: 'zcore-blocking') {
          sh '/jenkins_scripts/mavenInstall.sh ./pom.xml'
        }

      }
    }

    stage('Build Infrastructure services') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      when {
        expression {
          params.BUILD_INFRA
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'eureka-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
        }

        dir(path: 'gateway-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
        }

      }
    }

    stage('Build Backend microservices') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      when {
        expression {
          params.BUILD_BACKEND
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'user-service') {
          sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
          stash(name: 'build-user-service', includes: 'target/**')
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
      when {
        expression {
          params.BUILD_BACKEND
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'user-service') {
          unstash 'build-user-service'
          sh '/jenkins_scripts/mavenTest.sh ./pom.xml'
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
      when {
        expression {
          params.BUILD_FRONTEND
        }

      }
      steps {
        dir(path: 'z-dash') {
          sh '''cat ./package.json
/jenkins_scripts/reactBuild.sh'''
          stash(name: 'build-z-dash', includes: 'build/**')
        }

      }
    }

    stage('Deploy Libraries to Nexus') {
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
      when {
        expression {
          params.INSTALL_LIBRARIES
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'jwt') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'zcore-blocking') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

      }
    }

    stage('Deploy Infrastructure services to Nexus') {
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
      when {
        expression {
          params.BUILD_INFRA
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'eureka-service') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

        dir(path: 'gateway-service') {
          sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
        }

      }
    }

    stage('Deploy microservices to Nexus') {
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
      when {
        expression {
          params.BUILD_BACKEND
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'user-service') {
          unstash 'build-user-service'
          sh '/jenkins_scripts/dockerBuild.sh user-service'
          sh '/jenkins_scripts/dockerNexusLogin.sh'
          sh '/jenkins_scripts/dockerPush.sh'
        }

      }
    }

    stage('Deploy frontend to Nexus') {
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '''-v ${NPM_CACHE}:/root/.npm
-e NEXUS_PASSWORD=${NEXUS_PASSWORD}
-e NEXUS_USER=${NEXUS_USER}
-e NEXUS_HOST=${NEXUS_HOST}
-e NEXUS_PORT=${NEXUS_PORT}
--network=delivery_delivery'''
        }

      }
      when {
        expression {
          params.BUILD_FRONTEND
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'z-dash') {
          unstash 'build-z-dash'
          sh '''ls
cat ./package.json
/jenkins_scripts/npmPublish.sh npm-snapshots'''
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
    /* This path is from the host (not the jenkins container) */
    M2_HOME = '/home/jenkins/.m2'
    NPM_CACHE = '/home/jenkins/.npm'
    NEXUS_DOCKER_PORT = 8082
  }
  parameters {
    booleanParam(name: 'INSTALL_LIBRARIES', defaultValue: false, description: 'Whether or not run mvn install for libraries')
    booleanParam(name: 'BUILD_INFRA', defaultValue: false, description: 'Whether or not build Eureka & Gateway services')
    booleanParam(name: 'BUILD_BACKEND', defaultValue: true, description: 'Whether or not build microservices')
    booleanParam(name: 'BUILD_FRONTEND', defaultValue: true, description: 'Whether or not build frontend')
  }
}