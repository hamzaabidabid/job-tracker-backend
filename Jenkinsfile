// Jenkinsfile (VERSION FINALE - Authentification par Token)

pipeline {
	agent any // Simplifions en utilisant un agent global

	environment {
		DOCKER_REGISTRY = 'abidhamza'
		IMAGE_NAME = "${DOCKER_REGISTRY}/job-tracker-backend"

		// --- CONFIGURATION POUR LE TOKEN ---
		// Remplacez l'IP par celle de votre Minikube (obtenue avec 'minikube ip')
		KUBERNETES_SERVER_URL = 'https://192.168.49.2:8443'
		KUBERNETES_TOKEN_CREDENTIAL_ID = 'kubernetes-token'
		// ------------------------------------
	}

	stages {
		stage('Checkout') {
			steps {
				cleanWs()
				git url: 'https://github.com/hamzaabidabid/job-tracker-backend.git', branch: 'master'
			}
		}

		stage('Build & Test') {
			// On exécute Maven dans un conteneur Docker pour la propreté
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
		// Étape de Déploiement avec Token
		// ======================================================
		stage('Deploy to Kubernetes') {
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"

					withCredentials([string(credentialsId: KUBERNETES_TOKEN_CREDENTIAL_ID, variable: 'KUBERNETES_TOKEN')]) {
						sh '''
                    # Configuration de kubectl (ne change pas)
                    kubectl config set-cluster minikube --server=${KUBERNETES_SERVER_URL} --insecure-skip-tls-verify=true
                    kubectl config set-credentials jenkins-agent --token=${KUBERNETES_TOKEN}
                    kubectl config set-context jenkins-context --cluster=minikube --user=jenkins-agent
                    kubectl config use-context jenkins-context

                    # --- CORRECTION ICI : Étape de nettoyage ---
                    # On supprime les anciennes ressources avant de les recréer.
                    # --ignore-not-found=true évite les erreurs si c'est le premier déploiement.
                    echo "--- Cleaning up old resources ---"
                    kubectl delete -f k8s/backend.yml --ignore-not-found=true
                    # Attendre un peu pour que la suppression soit effective
                    sleep 5
                    # -----------------------------------------

                    # On exécute les commandes de déploiement
                    echo "--- Applying new resources ---"
                    kubectl apply -f k8s/backend.yml
                    kubectl set image deployment/backend-deployment backend-app=${IMAGE_NAME}:${imageTag}
                    kubectl rollout status deployment/backend-deployment
                '''
					}
				}
			}
		}
	}
}