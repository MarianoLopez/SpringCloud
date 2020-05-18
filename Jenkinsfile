pipeline {
    agent any
    stages {
        stage('Install Libraries') {
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
            steps {
                dir(path: 'jwt') {
                    sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
                }

                dir(path: 'zcore-blocking') {
                    sh '/jenkins_scripts/mavenDeploy.sh ./pom.xml'
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
            steps {
                dir(path: 'eureka-service') {
                    sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
                    stash(name: 'build-eureka-service', includes: 'target/**')
                }

                dir(path: 'gateway-service') {
                    sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
                    stash(name: 'build-gateway-service', includes: 'target/**')
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
            steps {
                dir(path: 'user-service') {
                    sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
                    stash(name: 'build-user-service', includes: 'target/**')
                }

                dir(path: 'mail-service') {
                    sh '/jenkins_scripts/mavenBuild.sh ./pom.xml'
                    stash(name: 'build-mail-service', includes: 'target/**')
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
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Frontend build') {
            agent {
                docker {
                    image 'node:13.12.0-alpine'
                    args '-v ${NPM_CACHE}:/root/.npm -e GATEWAY_URL=${GATEWAY_URL}'
                }

            }
            when {
                expression {
                    params.BUILD_FRONTEND
                }

            }
            steps {
                dir(path: 'z-dash') {
                    sh '/jenkins_scripts/npmBuild.sh'
                    stash(name: 'build-z-dash', includes: 'build/**')
                }

            }
        }

        stage('Deploy Infrastructure services to Nexus') {
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
                    unstash 'build-eureka-service'
                    sh '/jenkins_scripts/dockerBuildAndPublish.sh eureka-service'
                }

                dir(path: 'gateway-service') {
                    unstash 'build-gateway-service'
                    sh '/jenkins_scripts/dockerBuildAndPublish.sh gateway-service'
                }

            }
        }

        stage('Deploy microservices to Nexus') {
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
                    sh '/jenkins_scripts/dockerBuildAndPublish.sh user-service'
                }

                dir(path: 'mail-service') {
                    unstash 'build-mail-service'
                    sh '/jenkins_scripts/dockerBuildAndPublish.sh mail-service'
                }

            }
        }

        stage('Deploy frontend to Nexus') {
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
                    sh '/jenkins_scripts/dockerBuildAndPublish.sh z-dash'
                }

            }
        }

        stage('Clean workspace') {
            steps {
                sh "/jenkins_scripts/dockerPrune.sh"
                cleanWs()
            }
        }

    }
    environment {
        M2_HOME = '/home/jenkins/.m2'
        NPM_CACHE = '/home/jenkins/.npm'
        GATEWAY_URL="$params.GATEWAY_URL"
        //used by dockerBuildAndPublish
        NEXUS_URL = 'localhost'
        NEXUS_DOCKER_PORT = 8082
    }
    parameters {
        booleanParam(name: 'INSTALL_LIBRARIES', defaultValue: false, description: 'Whether or not run mvn install for libraries')
        booleanParam(name: 'BUILD_INFRA', defaultValue: false, description: 'Whether or not build Eureka & Gateway services')
        booleanParam(name: 'BUILD_BACKEND', defaultValue: true, description: 'Whether or not build microservices')
        booleanParam(name: 'BUILD_FRONTEND', defaultValue: true, description: 'Whether or not build frontend')
        string(name: 'GATEWAY_URL', defaultValue: 'http://localhost:9000', description: 'Gateway service URL to be use by the frontend')
    }
}