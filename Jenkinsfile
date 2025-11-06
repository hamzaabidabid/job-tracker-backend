// Jenkinsfile pour le backend (version finale simplifiée et robuste)

pipeline {
	agent none

	environment {
		DOCKER_REGISTRY = 'abidhamza'
		IMAGE_NAME = "${DOCKER_REGISTRY}/job-tracker-backend"
		KUBECONFIG_CREDENTIAL_ID = 'kubeconfig-credentials'
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
		// Étape de Déploiement sur Kubernetes (SIMPLIFIÉE)
		// ======================================================
		stage('Deploy to Kubernetes') {
			agent any
			steps {
				script {
					def imageTag = "v1.${BUILD_NUMBER}"

					withCredentials([string(credentialsId: KUBECONFIG_CREDENTIAL_ID, variable: 'KUBECONFIG_CONTENT')]) {
						sh 'echo "$KUBECONFIG_CONTENT" > ./kubeconfig.tmp'

						// On utilise un seul bloc 'sh' pour la cohérence
						sh '''
                            export KUBECONFIG=./kubeconfig.tmp

                            # 1. On applique la configuration de base depuis le fichier
                            #    (Il appliquera l'image qui est dans le fichier, ce n'est pas grave)
                            kubectl apply -f k8s/backend.yml

                            # 2. On utilise 'kubectl set image' pour mettre à jour l'image du déploiement
                            #    C'est la méthode recommandée, pas besoin de 'sed'.
                            #    Syntaxe: kubectl set image deployment/<nom-du-deployment> <nom-du-conteneur>=<nouvelle-image>
                            kubectl set image deployment/backend-deployment backend-app=${IMAGE_NAME}:${imageTag}

                            # 3. On attend que le déploiement soit terminé
                            kubectl rollout status deployment/backend-deployment
                        '''

						sh 'rm ./kubeconfig.tmp'
					}
				}
			}
		}
	}
}