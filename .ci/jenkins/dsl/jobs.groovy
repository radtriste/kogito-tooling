
/////////////////////////////////////////////////////////////////
// This file is used for generating Jenkins jobs
// Its placement and structure is a "convention" in kiegroup
// For kie-tools we currently generate just pullrequest job,
// to run sonar with each pull request, see: .ci/jenkins/Jenkinsfile
//
// For more details see:
//  - https://github.com/apache/incubator-kie-kogito-pipelines/blob/main/docs/jenkins.md
// Or the same file in other repositories like:
//  - https://github.com/apache/incubator-kie-kogito-runtimes/tree/main/.ci/jenkins/dsl
/////////////////////////////////////////////////////////////////

import org.kie.jenkins.jobdsl.KogitoJobTemplate
import org.kie.jenkins.jobdsl.Utils

Map getMultijobPRConfig() {
    return [
        parallel: true,
        jobs : [
            [
                id: 'kie-tools-stunner-editors',
                primary: true,
                env : [
                    // Sonarcloud analysis only on main branch
                    // As we have only Community edition
                    ENABLE_SONARCLOUD: Utils.isMainBranch(this),
                ]
            ]
        ]
    ]
}

// PR checks
// Deactivated due to ghprb not available on Apache Jenkins
// TODO create PR job with branch source plugin
// setupMultijobPrDefaultChecks()

/////////////////////////////////////////////////////////////////
// Methods
/////////////////////////////////////////////////////////////////

void setupMultijobPrDefaultChecks() {
    KogitoJobTemplate.createPerRepoPRJobs(this, '') { jobFolder -> return getMultijobPRConfig() }
}
