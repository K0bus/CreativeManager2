pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean install'
            }
        }
    }
    post {
            always {
            archiveArtifacts artifacts: 'target/CreativeManager2*.jar', fingerprint: true
        }
    }
}