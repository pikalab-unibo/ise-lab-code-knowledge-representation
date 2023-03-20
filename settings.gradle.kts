plugins {
    id("com.gradle.enterprise") version "3.12.5"
}

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

rootProject.name = "ise-lab-code-knowledge-representation"

val n = 5

for (i in 1 .. n) {
    include("exercise-$i")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}
