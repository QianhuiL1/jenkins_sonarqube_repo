pipeline {
    agent any

    environment {
        SONARQUBE = 'sonarqube' 
        SONAR_TOKEN = credentials('sonar-token') // SonarQube credential ID in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/QianhuiL1/jenkins_sonarqube_repo.git'
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // 使用 Maven 编译项目，生成 .class 文件
                    sh 'mvn clean compile'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube') { 
                    // Run SonarQube analysis using the SonarQube Scanner
                        sh """
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        sonar-scanner \
                            -Dsonar.projectKey=my_project \
                            -Dsonar.sources=. \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                    // Wait for SonarQube to finish analysis and get the Quality Gate result
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                
            }
        }
    }

    post {
        always {
            echo "SonarQube analysis completed."
        }
    }
}
