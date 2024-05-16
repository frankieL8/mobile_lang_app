package my.novikov.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Контракт api фичи. Все api фичи должны наследоваться от него
 *
 * API фичей дополняются методами, собирающими routes (ссылки) до destinations (composable - экранов)
 * с учетом параметров навигации
 */
interface FeatureNavigationApi {

    /**
     * Регистрирует граф навигации фичи либо как отдельные экраны через navGraphBuilder.composable(...),
     * либо как вложенный граф через navGraphBuilder.navigation(..)
     * @param navGraphBuilder служит для регистрации nested graph или отдельных destinations
     * @param navController служит для навигации внутри фичи, в том числе на другие фичи
     * @param modifier модификатор с проставленными paddings от Bottom Nav Bar
     */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier = Modifier
    )
}