pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "souhir98/marketplace-back"
        DOCKER_TAG = "v1.${BUILD_NUMBER}" // Version dynamique basée sur le numéro de build Jenkins
        DOCKER_CREDENTIALS = 'dockerhub-credentials' // ID des credentials Docker dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Clonage du dépôt...'
                checkout scm // Récupère le code du repository configuré dans Jenkins
            }
        }



        stage('Docker Build') {
            steps {
                echo 'Construction de l\'image Docker...'
                sh """
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                """
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Push de l\'image Docker sur Docker Hub...'
                withDockerRegistry([credentialsId: "${DOCKER_CREDENTIALS}", url: '']) {
                    sh """
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Une erreur est survenue pendant l\'exécution du pipeline.'
        }
    }
}