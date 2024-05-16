package my.novikov.feature.entrance.impl.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.novikov.core.architecture.mvi.dsl.collectActionsWithLifeсycle
import my.novikov.core.uikit.composable.button.MainButton
import my.novikov.core.uikit.composable.text.MainTextField
import my.novikov.core.uikit.theme.AppTheme
import my.novikov.feature.entrance.impl.R
import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenAction
import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenEvent
import my.novikov.feature.entrance.impl.presentation.model.EntranceScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun EntranceScreen(
    modifier: Modifier = Modifier,
    viewModel: EntranceScreenViewModel = koinViewModel(),
    onNavigateToMain: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    when (val stateValue = state.value) {
        is EntranceScreenState.Creation -> CreateAccount(
            modifier = modifier,
            firstName = stateValue.firstName,
            lastName = stateValue.lastName,
            emailAddress = stateValue.email,
            onContinueClick = { viewModel.event(EntranceScreenEvent.MainButtonClick) },
            onFirstNameChange = { viewModel.event(EntranceScreenEvent.FirstNameChange(it)) },
            onLastNameChange = { viewModel.event(EntranceScreenEvent.LastNameChange(it)) },
            onEmailAddressChange = { viewModel.event(EntranceScreenEvent.EmailChange(it)) }
        )

        is EntranceScreenState.Login -> LoginScreen(
            modifier = modifier,
            email = stateValue.email,
            password = stateValue.password,
            isPasswordVisible = stateValue.isPasswordVisible,
            onForgotClick = { viewModel.event(EntranceScreenEvent.ForgotClick) },
            onMainButtonClick = { viewModel.event(EntranceScreenEvent.MainButtonClick) },
            onVisibilityChange = { viewModel.event(EntranceScreenEvent.LoginPasswordVisibilityChange) },
            onEmailChange = { viewModel.event(EntranceScreenEvent.EmailChange(it)) },
            onPasswordChange = { viewModel.event(EntranceScreenEvent.LoginPasswordChange(it)) },
            onSignupClick = { viewModel.event(EntranceScreenEvent.SignupClick) }
        )

        is EntranceScreenState.Password -> ChoosePassword(
            modifier = modifier,
            password = stateValue.password,
            passwordConfirm = stateValue.confirm,
            onMainButtonClick = { viewModel.event(EntranceScreenEvent.MainButtonClick) },
            onVisibilityPasswordChange = { viewModel.event(EntranceScreenEvent.SignupPasswordVisibilityChange) },
            onVisibilityPasswordConfirmChange = { viewModel.event(EntranceScreenEvent.ConfirmPasswordVisibilityChange) },
            isPasswordVisible = stateValue.isPasswordVisible,
            isPasswordConfirmVisible = stateValue.isConfirmVisible,
            onPasswordChange = { viewModel.event(EntranceScreenEvent.SignupPasswordChange(it)) },
            onPasswordConfirmChange = { viewModel.event(EntranceScreenEvent.ConfirmChange(it)) },
            arePasswordsSame = stateValue.arePasswordsSame
        )
    }

    viewModel.collectActionsWithLifeсycle { action ->
        when (action) {
            EntranceScreenAction.NavigateToMain -> onNavigateToMain()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onForgotClick: () -> Unit,
    onMainButtonClick: () -> Unit,
    onSignupClick: () -> Unit,
    onVisibilityChange: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.login_app_bar),
                        style = AppTheme.typography.bodyLarge,
                        color = AppTheme.colors.textStatic
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.purple,
                    scrolledContainerColor = AppTheme.colors.purple,
                    navigationIconContentColor = AppTheme.colors.textStatic,
                    titleContentColor = AppTheme.colors.textStatic,
                    actionIconContentColor = AppTheme.colors.textStatic,
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.login_photo),
                modifier = Modifier
                    .width(105.dp)
                    .align(Alignment.CenterHorizontally),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.login_title),
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .fillMaxWidth(),
                style = AppTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            MainTextField(
                value = email,
                label = stringResource(id = R.string.login_email),
                placeholder = stringResource(id = R.string.login_email_box),
                onValueChange = onEmailChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainTextField(
                value = password,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation('\u2022')
                },
                label = stringResource(id = R.string.login_password),
                placeholder = stringResource(id = R.string.login_password_box),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 10.dp),
                                onClick = onVisibilityChange
                            ),
                        painter = painterResource(if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                        contentDescription = null
                    )
                },
                onValueChange = onPasswordChange
            )
            Spacer(modifier = Modifier.height(12.dp))
            ClickableText(
                modifier = Modifier.padding(start = 24.dp),
                text = AnnotatedString(stringResource(id = R.string.login_forgot_password)),
                onClick = { onForgotClick() },
                style = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.pink)

            )
            Spacer(modifier = Modifier.height(32.dp))
            MainButton(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(id = R.string.login_button),
                onClick = onMainButtonClick
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login_signup1),
                    color = AppTheme.colors.textDynamic,
                    style = AppTheme.typography.bodyMedium
                )
                ClickableText(
                    text = AnnotatedString(" " + stringResource(id = R.string.login_signup2)),
                    style = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.blue),
                    onClick = { onSignupClick() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login_google1),
                    color = AppTheme.colors.textDynamic,
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = " " + stringResource(id = R.string.login_google2),
                    color = AppTheme.colors.blue,
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = " " + stringResource(id = R.string.login_google3),
                    color = AppTheme.colors.textDynamic,
                    style = AppTheme.typography.bodyMedium
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAccount(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    emailAddress: String,
    onContinueClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailAddressChange: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.signup1_app_bar),
                        style = AppTheme.typography.bodyLarge,
                        color = AppTheme.colors.textStatic
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.purple,
                    scrolledContainerColor = AppTheme.colors.purple,
                    navigationIconContentColor = AppTheme.colors.textStatic,
                    titleContentColor = AppTheme.colors.textStatic,
                    actionIconContentColor = AppTheme.colors.textStatic,
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.signup1_title),
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .fillMaxWidth(),
                style = AppTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            MainTextField(
                value = firstName,
                label = stringResource(id = R.string.signup1_first_name),
                placeholder = stringResource(id = R.string.signup1_first_name_box),
                onValueChange = onFirstNameChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainTextField(
                value = lastName,
                label = stringResource(id = R.string.signup1_last_name),
                placeholder = stringResource(id = R.string.signup1_last_name_box),
                onValueChange = onLastNameChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainTextField(
                value = emailAddress,
                label = stringResource(id = R.string.signup1_address_name),
                placeholder = stringResource(id = R.string.signup1_address_name_box),
                onValueChange = onEmailAddressChange
            )
            Spacer(modifier = Modifier.height(32.dp))
            MainButton(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(id = R.string.signup1_button),
                onClick = onContinueClick
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.signup1_bottom_text1),
                    color = AppTheme.colors.textDynamic,
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = " " + stringResource(id = R.string.signup1_bottom_text2),
                    color = AppTheme.colors.blue,
                    style = AppTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoosePassword(
    modifier: Modifier = Modifier,
    password: String,
    passwordConfirm: String,
    onMainButtonClick: () -> Unit,
    onVisibilityPasswordChange: () -> Unit,
    onVisibilityPasswordConfirmChange: () -> Unit,
    isPasswordVisible: Boolean,
    isPasswordConfirmVisible: Boolean,
    arePasswordsSame: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.signup2_app_bar),
                        style = AppTheme.typography.bodyLarge,
                        color = AppTheme.colors.textStatic
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.purple,
                    scrolledContainerColor = AppTheme.colors.purple,
                    navigationIconContentColor = AppTheme.colors.textStatic,
                    titleContentColor = AppTheme.colors.textStatic,
                    actionIconContentColor = AppTheme.colors.textStatic,
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.signup2_title),
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .fillMaxWidth(),
                style = AppTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            MainTextField(
                value = password,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation('\u2022')
                },
                label = stringResource(id = R.string.signup2_password),
                placeholder = stringResource(id = R.string.signup2_password_box),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 10.dp),
                                onClick = onVisibilityPasswordChange
                            ),
                        painter = painterResource(if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                        contentDescription = null
                    )
                },
                onValueChange = onPasswordChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainTextField(
                value = passwordConfirm,
                visualTransformation = if (isPasswordConfirmVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation('\u2022')
                },
                label = stringResource(id = R.string.signup2_password_confirm),
                placeholder = stringResource(id = R.string.signup2_password_confirm_box),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 10.dp),
                                onClick = onVisibilityPasswordConfirmChange
                            ),
                        painter = painterResource(if (isPasswordConfirmVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                        contentDescription = null
                    )
                },
                onValueChange = onPasswordConfirmChange
            )
            Spacer(modifier = Modifier.height(142.dp))
            MainButton(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(id = R.string.signup2_button),
                onClick = onMainButtonClick,
                enabled = arePasswordsSame
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.signup2_bottom_text1),
                    color = AppTheme.colors.textDynamic,
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = " " + stringResource(id = R.string.signup2_bottom_text2),
                    color = AppTheme.colors.blue,
                    style = AppTheme.typography.bodyMedium
                )
            }
        }
    }

}


private fun String.changeVisibility(isVisible: Boolean): String = if (isVisible) {
    this
} else {
    buildString { repeat(this.length) { append("•") } }
}



