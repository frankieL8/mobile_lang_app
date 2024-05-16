plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.jetBrainsKotlin.get().pluginId)
}

android {
    compileSdk = 34
    namespace = "my.novikov.feature.mainscreen.impl"
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
    implementation(project(":feature:main-screen:api"))
    implementation(project(":feature:user-info:api"))
    implementation(project(":feature:learning:api"))
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}