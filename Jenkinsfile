pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from GitHub repository
                git 'https://github.com/Kieran9710/CI-CDproject.git'
            }
        }
        
        stage('Build') {
            steps {
                // Run Maven build
                sh 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // If Docker is used, build your app's Docker image
                    sh 'docker build -t springapp .'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Deploy the application (for example, using Docker)
                    sh 'docker run -d -p 8090:8090 springapp'
                }
            }
        }
    }
}
