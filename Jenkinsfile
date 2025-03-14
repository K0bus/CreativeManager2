pipeline {
    agent any
    tools {
        maven "3.9.9"
        jdk "openjdk-23.0.2"
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Analysis') {
            steps {
                script {
                    sh "mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd spotBugs:spotBugs"

                    def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
                    publishIssues issues: [checkstyle]

                    def pmd = scanForIssues tool: pmdParser(pattern: '**/target/pmd.xml')
                    publishIssues issues: [pmd]

                    def cpd = scanForIssues tool: cpd(pattern: '**/target/cpd.xml')
                    publishIssues issues: [cpd]

                    def spotbugs = scanForIssues tool: spotBugs(pattern: '**/target/spotbugsXml.xml')
                    publishIssues issues: [spotbugs]

                    def maven = scanForIssues tool: mavenConsole()
                    publishIssues issues: [maven]

                    publishIssues id: 'analysis', name: 'All Issues',
                        issues: [checkstyle, pmd, spotbugs],
                        filters: [includePackage('io.jenkins.plugins.analysis.*')]

                    recordIssues(tools: [checkStyle(reportEncoding: 'UTF-8')])
                }
            }
        }
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