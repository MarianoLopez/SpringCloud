pipeline {
  agent any
   parameters {
      booleanParam(name: 'INSTALL_LIBRARIES', defaultValue: false, description: 'Whether or not run mvn install for libraries')
      booleanParam(name: 'BUILD_INFRA', defaultValue: false, description: 'Whether or not build Eureka & Gateway services')
      booleanParam(name: 'BUILD_BACKEND', defaultValue: true, description: 'Whether or not build microservices')
      booleanParam(name: 'BUILD_FRONTEND', defaultValue: true, description: 'Whether or not build frontend')
   }

  stages {
    stage('Clean & Install Libraries') {
      when {
        expression { params.INSTALL_LIBRARIES }
      }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
        }

      }
      options {
        skipDefaultCheckout()
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

    stage('Build Infrastructure services') {
      when {
          expression { params.BUILD_INFRA }
        }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
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
      when {
        expression { params.BUILD_BACKEND }
      }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
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
      when {
        expression { params.BUILD_BACKEND }
      }
      agent {
        docker {
          image 'maven:3.6.3-jdk-14'
          args '-v ${M2_HOME}:/root/.m2'
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
      when {
        expression { params.BUILD_FRONTEND }
      }
      agent {
        docker {
          image 'node:13.12.0-alpine'
          args '-v ${NPM_CACHE}:/root/.npm'
        }

      }
      options {
        skipDefaultCheckout()
      }
      steps {
        dir(path: 'z-dash') {
          sh '''ls /jenkins_scripts
/jenkins_scripts/reactBuild.sh'''
          stash(name: 'build-z-dash', includes: 'build/**')
        }

      }
    }

    stage('Deploy Libraries to Nexus') {
    when {
        expression { params.INSTALL_LIBRARIES }
      }
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
      when {
          expression { params.BUILD_INFRA }
        }
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
          when {
              expression { params.BUILD_BACKEND }
            }
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
          options {
            skipDefaultCheckout()
          }
          steps {


            dir(path: 'user-service') {
              sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
            }
          }
        }


        stage('Deploy frontend to Nexus') {
          when {
              expression { params.BUILD_FRONTEND }
            }
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
          options {
            skipDefaultCheckout()
          }
          steps {
            dir(path: 'z-dash') {
            unstash 'build-z-dash'
              sh '/jenkins_scripts/npmPublish.sh npm-snapshots'
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