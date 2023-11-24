pipeline {
    agent any
    tools { 
      maven 'MAVEN_HOME' 
      jdk 'JAVA_HOME' 
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/MiguelCastro9/REST-API-with-Jenkins.git'
                }
            }
        }


        stage('Build') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'cp target/api-service.jar /opt/homebrew/Cellar/tomcat/10.1.16/libexec/webapps/'
                }
            }
        }

    }
}