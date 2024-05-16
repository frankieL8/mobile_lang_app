plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.jetBrainsKotlin.get().pluginId)
}

android {
    compileSdk = 34
    namespace = "my.novikov.feature.userinfo.impl"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
}

dependencies {
    implementation(project(":core:storage:api"))
    implementation(project(":feature:user-info:api"))
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}