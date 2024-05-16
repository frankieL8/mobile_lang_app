package my.novikov.languageapp.route

import my.novikov.languageapp.route.model.RouterScreenAction
import my.novikov.languageapp.route.model.RouterScreenEvent
import my.novikov.feature.onboarding.api.domain.OnboardingInteractor
import my.novikov.feature.userinfo.api.UserRepository
import my.novikov.core.architecture.mvi.MVIScreenModel
import my.novikov.core.architecture.mvi.models.ActionShareBehavior
import my.novikov.core.architecture.mvi.models.MVIConfiguration

/**
 * VM стартового экрана, содержит логику навигации
 */
internal class RouteScreenViewModel(
    private val onboardingScreenInteractor: OnboardingInteractor,
    private val userRepository: UserRepository,
) : MVIScreenModel<RouterScreenEvent, Unit, RouterScreenAction>(
    MVIConfiguration(
        initial = Unit,
        actionShareBehavior = ActionShareBehavior.Distribute()
    )
) {


    private val isCompletedOnboarding: Boolean
        get() = onboardingScreenInteractor.isOnboardingCompleted


    override suspend fun onEvent(event: RouterScreenEvent) {
        when (event) {
            RouterScreenEvent.ScreenShown -> reroute()
        }
    }

    private suspend fun reroute() {

        when {
            !isCompletedOnboarding -> {
                action(RouterScreenAction.ShowOnboarding)
            }
            userRepository.authentication.isAuthenticated -> {
                action(RouterScreenAction.NavigateToMain)
            }

            else -> {
                action(RouterScreenAction.NavigateToEntrance)

            }
        }
    }
}