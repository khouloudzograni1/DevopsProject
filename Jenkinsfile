pipeline {
    agent any
    stages {
        stage('Récupération du projet de Git') {
            steps {
                git branch: 'khouloudZograni_5DS4',
                    url: 'https://github.com/khouloudzograni1/DevopsProject.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package install'
            }
        }
         stage('Compile') {
              steps {
                  echo 'Compilation du projet ...'
                  sh 'mvn compile'
              }
            }
       /* stage('Unit Tests Mockito') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'sonar-api-key') {
                        sh 'mvn clean package sonar:sonar'
                    }
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/eventsProject-1.0.0.jar', fingerprint: true
            }
        }

        stage('Publish to Nexus') {
            steps {
                nexusArtifactUploader artifacts: [
                    [artifactId: 'eventsProject', classifier: '', file: 'target/eventsProject-1.0.0.jar', type: 'jar']
                ],
                credentialsId: 'nexus-auth',
                groupId: 'tn.esprit.eventsProject',
                nexusUrl: '192.168.50.4:8081',
                nexusVersion: 'nexus3',
                protocol: 'http',
                repository: 'projet-spring',
                version: "1.0"
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t eventsProject:v1.${BUILD_ID} ."
                sh "docker tag eventsProject:v1.${BUILD_ID} khouloudzograni/eventsProject:v1.${BUILD_ID}"
                sh "docker tag eventsProject:v1.${BUILD_ID} khouloudzograni/eventsProject:latest"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'git_creds', variable: 'docker_hub_cred')]) {
                    sh 'docker login -u khouloudzograni -p ${docker_hub_cred}'
                    sh 'docker push khouloudzograni/eventsProject:v1.${BUILD_ID}'
                    sh 'docker push khouloudzograni/eventsProject:latest'
                }
            }
        }

        stage('Deploy using Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker rm -f mysql || true'
                sh 'docker-compose up -d'
            }
        }
    }
    post {
        success {
            // Notify on successful build
            mail to: 'khouloud.zograni@esprit.tn',
                 subject: "Pipeline Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build was successful. Check it out here: ${env.BUILD_URL}"
        }
        failure {
            // Notify on failed build
            mail to: 'khouloud.zograni@esprit.tn',
                 subject: "Pipeline Failure: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build has failed. Check it out here: ${env.BUILD_URL}"
        }*/
    }
}