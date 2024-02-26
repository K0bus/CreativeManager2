pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean install'
            }
        }
        stage('Archive artifacts') {
            steps {
                sh 'rm -rf artifacts'
                sh 'mkdir artifacts'
                sh 'cp build/libs/CreativeManager2*.jar artifacts/'
                archiveArtifacts artifacts: 'artifacts/*.jar', followSymlinks: false
            }
        }
    }
}