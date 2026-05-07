pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "marlonxavier4/skillswap-app"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                // Descarga el código fuente desde tu rama en GitHub
                checkout scm
            }
        }

        stage('Construcción y Pruebas (Maven)') {
            steps {

                sh './mvnw clean test package'
            }
        }

        stage('Construir y Subir Imagen Docker') {
            steps {
                script {
                    // Jenkins usa el archivo .war generado en el paso anterior
                    // y el Dockerfile para construir la imagen.
                    // Requiere el ID exacto de las credenciales configuradas en Jenkins.
                    withDockerRegistry([credentialsId: 'dockerhub-credentials', url: '']) {
                        def customImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        customImage.push()
                    }
                }
            }
        }
    }

    post {
        always {
            // Este bloque limpia el entorno de trabajo después de terminar
            cleanWs()
        }
        success {
            echo "¡Integración exitosa! La imagen fue subida a Docker Hub."
        }
        failure {
            echo "La integración falló. Revisa si algún test (HU11, HU12 o HU13) no pasó."
        }
    }
}