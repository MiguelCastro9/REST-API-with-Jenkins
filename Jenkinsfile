pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    sh 'test -f mvnw || curl -O https://raw.githubusercontent.com/apache/maven/master/mvnw'
                    sh 'chmod +x mvnw'
                    git branch: 'main', url: 'https://github.com/MiguelCastro9/REST-API-with-Jenkins.git'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Comando para construir o projeto usando Maven Wrapper
                    sh './mvnw clean install'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Comando para executar testes usando Maven Wrapper
                    sh './mvnw test'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Comando para implantar a aplicação
                    sh 'cp target/api-service.jar /opt/homebrew/Cellar/tomcat/10.1.16/libexec/webapps/'
                }
            }
        }
    }
}
