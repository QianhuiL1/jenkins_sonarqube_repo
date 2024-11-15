pipeline {
    agent any

    environment {
        HADOOP_CLUSTER = 'hadoop-cluster'
        REGION = 'us-central1'
        BUCKET_NAME = '14848_qianhuil1'
        GOOGLE_APPLICATION_CREDENTIALS = '/tmp/credential.json'
        SONAR_SCANNER_HOME = '/opt/sonar-scanner'
        SONARQUBE_URL = 'http://34.172.20.95:9000'
        SONAR_USER = 'admin'
        SONAR_PASSWORD = 'Course14848-'
        PATH = "${env.PATH}:${env.SONAR_SCANNER_HOME}/bin"
    }

    stages {
        stage('Pull') {
            steps {
                git branch: 'main', url: 'https://github.com/QianhuiL1/jenkins_sonarqube_repo.git'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                sh """
                sonar-scanner \
                    -Dsonar.projectKey=my_project \
                    -Dsonar.host.url=${SONARQUBE_URL} \
                    -Dsonar.sources=mapper.py,reducer.py \
                    -Dsonar.login=${SONAR_USER} \
                    -Dsonar.password=${SONAR_PASSWORD}
                """
            }
        }
        
        stage('Quality Gate Check') {
            steps {
                script {
                    def qualityGateStatus = ''
                    timeout(time: 2, unit: 'MINUTES') {
                        waitUntil {
                            sleep(time: 10, unit: 'SECONDS')
                            qualityGateStatus = sh(
                                script: """
                                curl -u ${SONAR_USER}:${SONAR_PASSWORD} -s '${SONARQUBE_URL}/api/qualitygates/project_status?projectKey=my_project' | grep -o '"status":"[^"]*' | sed 's/"status":"//' | head -n1
                                """,
                                returnStdout: true
                            ).trim()
                            echo "Current Quality Gate Status: ${qualityGateStatus}"
                            return qualityGateStatus == 'OK' || qualityGateStatus == 'ERROR'
                        }
                    }

                    if (qualityGateStatus == 'ERROR') {
                        error "Quality Gate failed: ${qualityGateStatus}"
                    } else {
                        echo "Quality Gate passed: ${qualityGateStatus}"
                    }
                }
            }
        }

        stage('Download credentials') {
            steps {
                script {
                    sh "gsutil cp gs://${BUCKET_NAME}/credential.json ${GOOGLE_APPLICATION_CREDENTIALS}"
                }
            }
        }
        
        stage('Configure gcloud') {
            steps {
                script {
                    sh '''
                    gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}
                    gcloud config set project course-project-436720
                    gcloud config set compute/region ${REGION}
                    '''
                }
            }
        }

        stage('Upload Data and Scripts to Cloud Storage') {
            steps {
                script {
                    sh '''
                    gsutil cp data.txt gs://${BUCKET_NAME}/wordcount/input/
                    gsutil cp mapper.py gs://${BUCKET_NAME}/wordcount/
                    gsutil cp reducer.py gs://${BUCKET_NAME}/wordcount/
                    '''
                }
            }
        }

        stage('Submit Hadoop Job to Dataproc') {
            steps {
                script {
                    sh '''
                    gcloud dataproc jobs submit hadoop \
                        --cluster=${HADOOP_CLUSTER} \
                        --region=${REGION} \
                        --jar file:///usr/lib/hadoop/hadoop-streaming.jar \
                        -- -files gs://${BUCKET_NAME}/wordcount/mapper.py,gs://${BUCKET_NAME}/wordcount/reducer.py \
                        -mapper "python3 mapper.py" \
                        -reducer "python3 reducer.py" \
                        -input gs://${BUCKET_NAME}/wordcount/input/ \
                        -output gs://${BUCKET_NAME}/wordcount/output
                    '''
                }
            }
        }

        stage('Display Results') {
            steps {
                script {
                    sh '''
                    echo "MapReduce Job Output:"
                    gsutil cat gs://${BUCKET_NAME}/wordcount/output/part-00000
                    '''
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
