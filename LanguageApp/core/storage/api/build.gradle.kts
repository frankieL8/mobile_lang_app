plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.jetBrainsKotlin.get().pluginId)
}

android {
    compileSdk = 34
    namespace = "my.novikov.core.storage.api"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}