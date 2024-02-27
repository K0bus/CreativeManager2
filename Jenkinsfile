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
            discordSend webhookURL: $params.DISCORD_HOOK,
                title: JOB_NAME,
                thumbnail: "https://i.imgur.com/wMJWATd.png",
                description: "Jenkins Pipeline Build",
                footer: "by K0bus",
                successful: true,
                link: env.BUILD_URL,
                showChangeset: true,
                result: currentBuild.currentResult
        }
    }
}