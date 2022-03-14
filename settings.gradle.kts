plugins {
    id("com.gradle.enterprise") version "3.8.1"
}

enableFeaturePreview("VERSION_CATALOGS")

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
