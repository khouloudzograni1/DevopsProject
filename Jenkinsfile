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
       stage('Unit Tests Mockito') {
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
                archiveArtifacts artifacts: 'target/eventsProject-1.0.0-SNAPSHOT.jar', fingerprint: true
            }
        }
         stage('Deploy to Nexus') {
                    steps {
                        script {
                            // mvExécuter la commande Maven deploy pour envoyer l'artefact vers Nexus
                            sh "mvn clean deploy -X"
                        }
                    }
                }


        stage('Build Docker Image') {
            steps {
                sh "docker build -t eventsproject:v1.${BUILD_ID} ."
                sh "docker tag eventsproject:v1.${BUILD_ID} khouloudzograni/eventsproject:v1.${BUILD_ID}"
                sh "docker tag eventsproject:v1.${BUILD_ID} khouloudzograni/eventsproject:latest"
            }
        }



        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'docker_creds', variable: 'docker_hub_cred')]) {
                    sh 'docker login -u khouloudzograni -p ${docker_hub_cred}'
                    sh 'docker push khouloudzograni/eventsproject:v1.${BUILD_ID}'
                    sh 'docker push khouloudzograni/eventsproject:latest'
                }
            }
        }
        stage('Deploy using Docker Compose') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
        stage('Deploy Prometheus and Grafana') {
                    steps {
                        sh 'docker-compose up -d prometheus grafana'
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
        }
    }
}