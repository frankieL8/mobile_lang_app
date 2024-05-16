plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetBrainsKotlin)
}

android {
    compileSdk = 34
    namespace = "my.novikov.feature.learning.api"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
}