pipeline {
    // Где выполнять: на любом доступном исполнителе (у нас это сам Jenkins)
    agent any

    // Переменные, доступные во всех этапах
    environment {
        REGISTRY = '192.168.56.10:8082'              // адрес Nexus registry
        IMAGE    = "${REGISTRY}/demo-service"        // полное имя образа
        TAG      = "${env.BUILD_NUMBER}"             // номер сборки Jenkins: 1, 2, 3...
    }

    options {
        // Хранить только последние 10 сборок, чтобы не забивать диск
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Добавлять время к каждой строке лога
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                // Скачать код из репозитория, указанного в настройках джобы
                checkout scm
            }
        }

        stage('Build image') {
            steps {
                // Собрать образ и сразу дать ему два тега: номер сборки и latest
                sh 'docker build -t $IMAGE:$TAG -t $IMAGE:latest .'
            }
        }

        stage('Push to Nexus') {
            steps {
                // Достать логин/пароль из Credentials в переменные — только внутри этого блока
                withCredentials([usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS')]) {
                    sh '''
                        echo "$NEXUS_PASS" | docker login $REGISTRY -u "$NEXUS_USER" --password-stdin
                        docker push $IMAGE:$TAG
                        docker push $IMAGE:latest
                    '''
                }
            }
        }
    }

    post {
        always {
            // Выйти из registry, чтобы не оставлять сохранённый логин
            sh 'docker logout $REGISTRY || true'
        }
        success {
            echo "Образ запушен: ${IMAGE}:${TAG}"
        }
    }
}
