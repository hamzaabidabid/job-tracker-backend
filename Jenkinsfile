// Jenkinsfile (version simplifiée utilisant le kubeconfig du dépôt Git)

pipeline {
	agent none

	environment {
		DOCKER_REGISTRY = 'abidhamza'
		IMAGE_NAME = "${DOCKER_REGISTRY}/job-tracker-backend"
	}

	stages {
		stage('Checkout') {
			agent any
			steps {
				cleanWs()
				git url: 'https://github.com/hamzaabidabid/job-tracker-backend.git', branch: 'master'
			}
		}

		stage('Build & Test') {
			agent {
				docker {
					image 'maven:3.8.5-openjdk-17'
					args '-v $HOME/.m2:/root/.m2'
				}
			}
			steps {
				sh 'mvn clean package -DskipTests'
			}
		}

		stage('Build & Push Docker Image') {
			agent any
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"
					def dockerImage = docker.build(IMAGE_NAME + ":${imageTag}", '.')

					docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
						dockerImage.push()
					}
				}
			}
		}

		// ======================================================
		// Étape de Déploiement (ULTRA-SIMPLIFIÉE)
		// ======================================================
		stage('Deploy to Kubernetes') {
			agent any
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"

					// On utilise directement le fichier kubeconfig.yml qui a été cloné
					sh '''
                        export KUBECONFIG=./kubeconfig.yml

                        kubectl apply -f k8s/backend.yml
                        kubectl set image deployment/backend-deployment backend-app=${IMAGE_NAME}:${imageTag}
                        kubectl rollout status deployment/backend-deployment
                    '''
				}
			}
		}
	}
}