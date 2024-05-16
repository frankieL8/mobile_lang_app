package my.novikov.feature.mainscreen.impl.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import my.novikov.feature.mainscreen.api.MainScreenFeature
import my.novikov.feature.learning.api.domain.Exercise
import my.novikov.feature.mainscreen.impl.domain.model.User
import my.novikov.feature.mainscreen.impl.presentation.MainScreen

internal class MainScreenFeatureImpl : MainScreenFeature {
    override val route: String = "languageapp://main"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route) {
            MainScreen(
                modifier = modifier
            )
        }
    }
}