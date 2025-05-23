pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }
    environment {
        SONARQUBE = 'SonarQube'
        SONAR_TOKEN = credentials('SonarToken') 
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

        stage('Test') {
            steps {
                sh 'mvn test'
                junit '**/target/surefire-reports/*.xml'
            }
        }
                
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run SonarQube analysis
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=CI-CD \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        
        stage('Build Docker Image') {
            steps {
                sh ' docker build -f Dockerfile -t my-java-app .'
            }
        }
        
        stage('Run Docker Container') {
            steps {
                script {
                    // Run a container from the built image
                    // You can customize the docker run command with necessary options
                    sh 'docker run -d -p 8090:8090 --network jenkins-setup_cicd my-java-app'
                }
            }
        }

        //stage('Deploy with Ansible') {
        //    steps {
        //        script {
        //            sh '''
        //            ansible-playbook -i inventory.ini Deploy.yml
        //            '''
        //        }
        //    }
        //}
        
        stage('Verify Container') {
            steps {
                script {
                    // Verify that the container is running
                    sh 'docker ps'
                }
            }
        }
    }
}
