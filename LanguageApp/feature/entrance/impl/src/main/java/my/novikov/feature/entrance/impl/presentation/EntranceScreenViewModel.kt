package my.novikov.feature.entrance.impl.presentation

import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenAction
import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenEvent
import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenState
import my.novikov.feature.userinfo.api.UserRepository
import my.novikov.core.architecture.mvi.MVIScreenModel
import my.novikov.core.architecture.mvi.models.MVIConfiguration
import my.novikov.core.architecture.mvi.dsl.updateState
import my.novikov.feature.userinfo.api.model.UserInfo

internal class EntranceScreenViewModel(
    private val userRepository: UserRepository
) : MVIScreenModel<EntranceScreenEvent, EntranceScreenState, EntranceScreenAction>(
    MVIConfiguration(
        initial = EntranceScreenState.Login()
    )
) {
    override suspend fun onEvent(event: EntranceScreenEvent) {
        when (event) {
            is EntranceScreenEvent.ConfirmChange -> updateState<EntranceScreenState.Password, _> {
                copy(confirm = event.value)
            }

            EntranceScreenEvent.ConfirmPasswordVisibilityChange -> updateState<EntranceScreenState.Password, _> {
                copy(isConfirmVisible = !isConfirmVisible)
            }

            is EntranceScreenEvent.EmailChange -> onEmailChange(event.value)
            is EntranceScreenEvent.FirstNameChange -> updateState<EntranceScreenState.Creation, _> {
                copy(firstName = event.value)
            }

            EntranceScreenEvent.ForgotClick -> Unit
            is EntranceScreenEvent.LastNameChange -> updateState<EntranceScreenState.Creation, _> {
                copy(lastName = event.value)
            }

            EntranceScreenEvent.LoginClick -> updateState {
                EntranceScreenState.Login()
            }

            is EntranceScreenEvent.LoginPasswordChange -> updateState<EntranceScreenState.Login, _> {
                copy(password = event.value)
            }

            EntranceScreenEvent.LoginPasswordVisibilityChange -> updateState<EntranceScreenState.Login, _> {
                copy(isPasswordVisible = !isPasswordVisible)
            }

            EntranceScreenEvent.MainButtonClick -> onMainButtonClick()
            EntranceScreenEvent.SignupClick -> updateState {
                EntranceScreenState.Creation()
            }

            is EntranceScreenEvent.SignupPasswordChange -> updateState<EntranceScreenState.Password, _> {
                copy(password = event.value)
            }

            EntranceScreenEvent.SignupPasswordVisibilityChange -> updateState<EntranceScreenState.Password, _> {
                copy(isPasswordVisible = !isPasswordVisible)
            }
        }
    }

    private suspend fun onEmailChange(email: String) {
        withState {
            when (this) {
                is EntranceScreenState.Creation -> updateState<EntranceScreenState.Creation, _> {
                    copy(email = email)
                }

                is EntranceScreenState.Login -> updateState<EntranceScreenState.Login, _> {
                    copy(email = email)
                }

                is EntranceScreenState.Password -> Unit
            }
        }
    }

    private suspend fun onMainButtonClick() {
        withState {
            when (this) {
                is EntranceScreenState.Creation -> {
                    userRepository.saveUser(
                        UserInfo(
                            firstName = firstName,
                            lastName = lastName,
                            email = email
                        )
                    )
                    updateState { EntranceScreenState.Password() }
                }

                is EntranceScreenState.Login -> {
                    Unit
                }

                is EntranceScreenState.Password -> {
                    userRepository.authentication.savePassword(password)
                    action(EntranceScreenAction.NavigateToMain)
                }
            }
        }
    }
}