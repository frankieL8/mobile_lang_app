plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.jetBrainsKotlin.get().pluginId)
}

android {
    compileSdk = 34
    namespace = "my.novikov.feature.entrance.api"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
}

dependencies {
    api(project(":core:navigation"))
}