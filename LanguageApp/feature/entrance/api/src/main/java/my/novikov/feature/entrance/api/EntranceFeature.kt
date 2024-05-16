package my.novikov.feature.entrance.api

import my.novikov.core.navigation.FeatureNavigationApi

interface EntranceFeature: FeatureNavigationApi {
    val route: String
}