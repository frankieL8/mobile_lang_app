package my.novikov.languageapp.route.model

sealed interface RouterScreenEvent {
    data object ScreenShown : RouterScreenEvent
}