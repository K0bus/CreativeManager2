pipeline {
    agent any
    environment {
        DISCORD_URL = credentials('discord-hook-cm2')
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -B -Dbuild.number=${BUILD_NUMBER}'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/CreativeManager2*.jar', fingerprint: true
            discordSend webhookURL: '$DISCORD_URL',
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