pluginManagement {
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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Manifest"

include(":app")

// Core modules
include(":core:model")
include(":core:database")
include(":core:datastore")
include(":core:common")
include(":core:ui")

// Feature modules
include(":feature:transactions")
include(":feature:addedit")
include(":feature:stats")
include(":feature:accounts")
include(":feature:budgets")
include(":feature:goals")
include(":feature:subscriptions")
include(":feature:settings")
include(":feature:sms")
