package my.novikov.languageapp

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import my.novikov.feature.entrance.api.EntranceFeature
import my.novikov.feature.mainscreen.api.MainScreenFeature
import my.novikov.feature.onboarding.api.OnboardingFeature
import my.novikov.languageapp.route.ROUTE_SCREEN_ROUTE
import my.novikov.languageapp.route.RouteScreen
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val onBoardingFeature = koinInject<OnboardingFeature>()
    val mainScreenFeature = koinInject<MainScreenFeature>()
    val entranceFeature = koinInject<EntranceFeature>()
    NavHost(
        navController = navController,
        startDestination = ROUTE_SCREEN_ROUTE,
        modifier = modifier,
        enterTransition =
        { fadeIn(animationSpec = tween(300)) },
        exitTransition =
        { fadeOut(animationSpec = tween(300)) },
    ) {
        composable(ROUTE_SCREEN_ROUTE) {
            RouteScreen(
                onShowOnboarding = { navController.navigate(onBoardingFeature.route) },
                onNavigateToEntrance = { navController.navigate(entranceFeature.route) },
                onNavigateToMain = {navController.navigate(mainScreenFeature.route)}
            )
        }
        onBoardingFeature.registerGraph(this, navController, modifier)
        mainScreenFeature.registerGraph(this, navController, modifier)
        entranceFeature.registerGraph(this, navController, modifier)
    }
}