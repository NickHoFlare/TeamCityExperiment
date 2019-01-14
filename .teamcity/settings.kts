import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.2"

project {
    description = "An experiment with using Kotlin DSL to create a build pipeline with TeamCity. TEST"

    params {
        param("repo.name", "TeamCityExperiment")
    }

    vcsRoot(TeamCityExperimentGitRepo)

    buildType(HelloWorld)

    template(TestTemplateConfiguration)

    subProject(SubprojectTest)
}

object HelloWorld : BuildType({
    templates(TestTemplateConfiguration)
    name = "HelloWorld"
    description = "First Try"

    vcs {
        root(TeamCityExperimentGitRepo)
    }

    steps {
        powerShell {
            name = "Build"
            id = "RUNNER_2"
            scriptMode = script {
                content = """echo "I am building...""""
            }
        }
        powerShell {
            name = "Test"
            id = "RUNNER_3"
            scriptMode = script {
                content = """echo "I am testing...""""
            }
        }
        powerShell {
            name = "Deploy"
            id = "RUNNER_4"
            scriptMode = script {
                content = """echo "I am deploying...""""
            }
        }
        stepsOrder = arrayListOf("RUNNER_2", "RUNNER_3", "RUNNER_4", "RUNNER_5")
    }
})

object TestTemplateConfiguration : Template({
    name = "TestTemplateConfiguration"
    description = "This is an example of a Template Configuration"

    steps {
        script {
            name = "TestTemplateStep"
            id = "RUNNER_5"
            scriptContent = """echo "This is a test template build step""""
        }
    }
})

object SubprojectTest : Project({
    id("SubprojectTest")
    name = "SubprojectTest"
    description = "An example of a subproject in TeamCity"
})

object TeamCityExperimentGitRepo : GitVcsRoot({
    name = "TeamCityExperimentGitRepo"
    url = "https://github.com/terran324/TeamCityExperiment.git"
    authMethod = password {
        userName = "terran324"
        password = "credentialsJSON:c137106d-4250-4a8c-ba45-09b60635a868"
    }
})
