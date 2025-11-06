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

		stage('Checkout') {
			// Cette étape s'exécute sur l'agent Jenkins de base
			agent any
			steps {
				// On nettoie l'espace de travail et on récupère le code
				cleanWs()
				git url: 'https://github.com/hamzaabidabid/job-tracker-backend.git', branch: 'master'
			}
		}

		stage('Build & Test') {
			// --- CORRECTION MAJEURE ---
			// Cette étape s'exécutera ENTIÈREMENT à l'intérieur d'un conteneur Maven.
			agent {
				docker {
					image 'maven:3.8.5-openjdk-17'
					// On partage le cache Maven pour accélérer les builds futurs
					args '-v $HOME/.m2:/root/.m2'
				}
			}
			steps {
				// La commande est maintenant exécutée directement, car nous sommes DANS le conteneur.
				sh 'mvn clean package -DskipTests'
			}
		}

		// ======================================================
		// Étape de Build & Push de l'Image Docker
		// ======================================================
		stage('Build & Push Docker Image') {
			// Cette étape s'exécute sur l'agent Jenkins de base, qui a accès à Docker.
			agent any
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"
					def dockerImage = docker.build("${IMAGE_NAME}:${imageTag}", '.')

					docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
						dockerImage.push()
					}
				}
			}
		}

		// ======================================================
		// Étape de Déploiement sur Kubernetes
		// ======================================================
		stage('Deploy to Kubernetes') {
			// Cette étape s'exécute sur l'agent Jenkins de base.
			agent any
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"

					// On change file() en string() et le nom de la variable
					withCredentials([string(credentialsId: KUBECONFIG_CREDENTIAL_ID, variable: 'KUBECONFIG_CONTENT')]) {
						// On écrit le contenu du texte secret dans un fichier temporaire
						sh 'echo "$KUBECONFIG_CONTENT" > ./kubeconfig.tmp'

						// On dit à kubectl d'utiliser ce fichier temporaire pour toutes les commandes suivantes
						// en exportant la variable d'environnement KUBECONFIG
						sh """
							export KUBECONFIG=./kubeconfig.tmp

							docker run --rm -v \$(pwd):/workspace -w /workspace alpine:latest \\
								sed -i 's|image: .*|image: ${IMAGE_NAME}:${imageTag}|' k8s/backend.yml

							kubectl apply -f k8s/backend.yml
							kubectl rollout status deployment/backend-deployment
						"""

						// On nettoie le fichier temporaire
						sh 'rm ./kubeconfig.tmp'
					}
				}
			}
		}
	}
}
