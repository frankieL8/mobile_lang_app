package my.novikov.feature.entrance.impl.presentation.model

internal sealed interface EntranceScreenState {
    data class Login(
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
    ) : EntranceScreenState

    data class Creation(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
    ) : EntranceScreenState

    data class Password(
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val confirm: String = "",
        val isConfirmVisible: Boolean = false,
    ) : EntranceScreenState {
        val arePasswordsSame = password == confirm
    }
}

internal sealed interface EntranceScreenEvent {
    data object MainButtonClick : EntranceScreenEvent
    data class FirstNameChange(val value: String) : EntranceScreenEvent
    data class LastNameChange(val value: String) : EntranceScreenEvent
    data object ForgotClick : EntranceScreenEvent
    data object LoginClick : EntranceScreenEvent
    data object SignupClick : EntranceScreenEvent
    data class EmailChange(val value: String) : EntranceScreenEvent
    data class LoginPasswordChange(val value: String) : EntranceScreenEvent
    data class SignupPasswordChange(val value: String) : EntranceScreenEvent
    data class ConfirmChange(val value: String) : EntranceScreenEvent
    data object LoginPasswordVisibilityChange : EntranceScreenEvent
    data object SignupPasswordVisibilityChange : EntranceScreenEvent
    data object ConfirmPasswordVisibilityChange : EntranceScreenEvent

}

internal sealed interface EntranceScreenAction {
    data object NavigateToMain : EntranceScreenAction
}