plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.jetBrainsKotlin.get().pluginId)
}

android {
    compileSdk = 34
    namespace = "my.novikov.core.navigation"
    defaultConfig {
        minSdk = 30
    }
    lint {
        targetSdk = 34
    }
}

dependencies {
    implementation(libs.koin.compose)
    implementation(libs.koin.android)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.navigation.ui.ktx)
}