pipeline {
    agent any
    tools {
        maven "3.9.9"
        jdk "openjdk-23.0.2"
    }
    options {
        disableConcurrentBuilds()
    }
    stage('CheckStyle') {
        steps {
            sh 'mvn checkstyle:check'
            def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
            publishIssues issues: [checkstyle]
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -B -Dbuild.number=${BUILD_NUMBER}'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/CreativeManager2*.jar', fingerprint: true
            withCredentials([string(credentialsId: 'DISCORD_URL_CREATIVEMANAGER', variable: 'DISCORD_URL')]) {
                        discordSend webhookURL: DISCORD_URL,
                            title: "$JOB_NAME #$BUILD_NUMBER",
                            thumbnail: "https://i.imgur.com/wMJWATd.png",
                            description: "CI Automated in Jenkins",
                            footer: "by K0bus",
                            successful: true,
                            link: env.BUILD_URL,
                            showChangeset: true,
                            result: currentBuild.currentResult
            }
        }
    }
}