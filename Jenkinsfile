pipeline {
    agent any

    environment {
        SONARQUBE = 'sonarqube' 
        SONAR_TOKEN = credentials('sonar-token') // SonarQube credential ID in Jenkins
        HADOOP_CLUSTER = 'mycluster'
        REGION = 'us-central1'
        BUCKET_NAME = '14848_qianhuil1'
        GOOGLE_APPLICATION_CREDENTIALS = '/tmp/credential.json'
    }

    stages {
        stage('Pull') {
            steps {
                git branch: 'main', url: 'https://github.com/QianhuiL1/jenkins_sonarqube_repo.git'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube') { 
                        sh """
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        sonar-scanner \
                            -Dsonar.projectKey=my_project \
                            -Dsonar.sources=mapper.py,reducer.py \
                            -Dsonar.language=py \
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
