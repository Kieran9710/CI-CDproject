pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    environment {
        SONARQUBE = 'SonarQube'
        SONAR_TOKEN = credentials('QubeToken') 
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
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run SonarQube analysis
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=CI-CD \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }
    }
}
