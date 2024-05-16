package my.novikov.languageapp.route.model

internal sealed interface RouterScreenAction {

    data object ShowOnboarding : RouterScreenAction

    data object NavigateToMain : RouterScreenAction

    data object NavigateToEntrance : RouterScreenAction
}