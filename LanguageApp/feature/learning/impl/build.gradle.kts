plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetBrainsKotlin)
}

android {
    compileSdk = 34
    namespace = "my.novikov.feature.learning.impl"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
    buildFeatures {
        compose = true
        buildConfig = false
        resValues = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    implementation(project(":core:ui-kit"))
    implementation(project(":core:architecture"))
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":feature:user-info:api"))
}