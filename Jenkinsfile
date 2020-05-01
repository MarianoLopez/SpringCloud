pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-14'
      args '-v /home/jenkins/.m2:/root/.m2'
    }

  }
  stages {
    stage('Initialize') {
      steps {
        sh '''echo PATH = ${PATH}
echo M2_HOME = ${M2_HOME}'''
      }
    }

  }
}