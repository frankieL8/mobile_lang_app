pluginManagement {
    repositories {
        google()
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

rootProject.name = "Language App"
include(":app")
include(
    ":core",

    ":core:architecture",
    ":core:navigation",
    ":core:storage",
    ":core:storage:api",
    ":core:storage:impl",
    ":core:ui-kit"
)
include(
    ":feature",

    ":feature:entrance",
    ":feature:entrance:api",
    ":feature:entrance:impl",

    ":feature:onboarding",
    ":feature:onboarding:api",
    ":feature:onboarding:impl",
    ":feature:main-screen",
    ":feature:main-screen:api",
    ":feature:main-screen:impl",
    ":feature:user-info",
    ":feature:user-info:api",
    ":feature:user-info:impl",
    ":feature:learning",
    ":feature:learning:api",
    ":feature:learning:impl"
)
