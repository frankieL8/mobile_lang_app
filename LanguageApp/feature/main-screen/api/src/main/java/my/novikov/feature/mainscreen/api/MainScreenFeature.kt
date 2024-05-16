package my.novikov.feature.mainscreen.api

import my.novikov.core.navigation.FeatureNavigationApi

interface MainScreenFeature: FeatureNavigationApi {
    val route: String
}