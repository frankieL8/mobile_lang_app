package my.novikov.feature.onboarding.impl.presentation.model

internal data class OnboardingScreenState (
    val currentStep: OnboardingStep
)

internal enum class OnboardingStep {
    STEP_1,
    STEP_2,
    STEP_3,
}

internal sealed interface OnboardingScreenEvent {
    data object ClickClose: OnboardingScreenEvent
    data object ClickNext: OnboardingScreenEvent
}

internal sealed interface OnboardingScreenAction {
    data object NavigateToEntrance: OnboardingScreenAction
}