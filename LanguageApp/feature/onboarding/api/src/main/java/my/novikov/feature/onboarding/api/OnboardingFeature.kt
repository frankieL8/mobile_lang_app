package my.novikov.feature.onboarding.api

import my.novikov.core.navigation.FeatureNavigationApi

interface OnboardingFeature : FeatureNavigationApi {
    val route: String
}