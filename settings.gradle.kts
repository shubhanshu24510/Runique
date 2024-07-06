pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Runique"
include(":app")
include(":auth:presentation")
include(":auth:data")
include(":auth:domain")
include(":core:data")
include(":core:domain")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:database")
include(":run:presentation")
include(":run:data")
include(":run:domain")

include(":run:location")
include(":run:network")
include(":analyticss:data")
include(":analyticss:presentation")
include(":analyticss:domain")
include(":analyticss:analytics_features")

include(":wear:app")
include(":wear:run:data")
include(":wear:run:presentation")
include(":wear:run:domain")
include(":core:presentation:designsystem_wear")
include(":core:connectivity:domain")
include(":core:connectivity:data")
