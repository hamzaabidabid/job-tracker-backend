// Jenkinsfile pour le backend

pipeline {
	agent any // Exécute ce pipeline sur n'importe quel agent Jenkins disponible

	// Variables d'environnement utilisées dans le pipeline
	environment {
		DOCKER_REGISTRY = 'abidhamza' // Votre nom d'utilisateur Docker Hub
		IMAGE_NAME = "${DOCKER_REGISTRY}/job-tracker-backend"
		KUBECONFIG_CREDENTIAL_ID = 'kubeconfig-credentials' // L'ID des credentials Kubernetes dans Jenkins
	}

	stages {
		// ======================================================
		// Étape 1 : Récupérer le code source
		// ======================================================
		stage('Checkout') {
			steps {
				git 'https://github.com/hamzaabidabid/job-tracker-backend.git' // Remplacez par l'URL de votre dépôt Git
			}
		}

		// ======================================================
		// Étape 2 : Construire et Tester (Phase CI)
		// ======================================================
		stage('Build & Test') {
			steps {
				// Jenkins utilise Docker pour lancer un conteneur Maven et construire le projet
				script {
					def mavenImage = docker.image('maven:3.8.5-openjdk-17')
					mavenImage.inside {
						sh 'mvn clean package -DskipTests' // On exécute la commande de build
					}
				}
			}
		}

		// ======================================================
		// Étape 3 : Construire et Pousser l'Image Docker (Fin de la CI)
		// ======================================================
		stage('Build & Push Docker Image') {
			steps {
				script {
					// On génère un tag unique pour l'image (ex: 'v1.123')
					def imageTag = "v1.${BUILD_NUMBER}"
					def dockerImage = docker.build(IMAGE_NAME + ":${imageTag}", '.')

					// On se connecte à Docker Hub avec les credentials stockés dans Jenkins
					docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
						dockerImage.push()
					}
				}
			}
		}

		// ======================================================
		// Étape 4 : Déployer sur Kubernetes (Phase CD)
		// ======================================================
		stage('Deploy to Kubernetes') {
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"
					// On utilise les credentials kubeconfig pour se connecter au cluster
					withCredentials([file(credentialsId: KUBECONFIG_CREDENTIAL_ID, variable: 'KUBECONFIG')]) {
						// On met à jour le fichier YAML avec le nouveau tag d'image
						sh "sed -i 's|image: .*|image: ${IMAGE_NAME}:${imageTag}|' k8s/backend.yml"

						// On applique la mise à jour sur Kubernetes
						sh "kubectl apply -f k8s/backend.yml"

						// On vérifie que le déploiement se passe bien
						sh "kubectl rollout status deployment/backend-deployment"
					}
				}
			}
		}
	}
}