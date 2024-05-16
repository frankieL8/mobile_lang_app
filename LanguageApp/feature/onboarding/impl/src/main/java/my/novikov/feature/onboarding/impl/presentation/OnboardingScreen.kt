package my.novikov.feature.onboarding.impl.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.novikov.core.uikit.composable.button.MainButton
import my.novikov.core.uikit.theme.AppTheme
import my.novikov.core.uikit.theme.LanguageAppTheme
import my.novikov.feature.onboarding.impl.R
import my.novikov.feature.onboarding.impl.presentation.model.OnboardingScreenAction
import my.novikov.feature.onboarding.impl.presentation.model.OnboardingScreenEvent
import my.novikov.feature.onboarding.impl.presentation.model.OnboardingStep
import my.novikov.feature.onboarding.impl.presentation.ui.HorizontalPagerIndicator
import my.novikov.core.architecture.mvi.dsl.collectActionsWithLifeсycle
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun OnBoardingScreen(
    modifier: Modifier,
    viewModel: OnboardingScreenViewModel = koinViewModel(),
    onNavigateToEntrance: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    OnBoardingScreenContent(modifier = modifier,
        currentStep = state.value.currentStep,
        onButtonClick = {viewModel.event(OnboardingScreenEvent.ClickNext)},
        onSkipOnboarding = {viewModel.event(OnboardingScreenEvent.ClickClose)}
    )

    viewModel.collectActionsWithLifeсycle { action ->
        when (action) {
            OnboardingScreenAction.NavigateToEntrance -> onNavigateToEntrance()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OnBoardingScreenContent(
    modifier: Modifier = Modifier,
    currentStep: OnboardingStep,
    onButtonClick: () -> Unit,
    onSkipOnboarding: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { OnboardingStep.entries.size })

    LaunchedEffect(currentStep) {
        pagerState.scrollToPage(currentStep.ordinal)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(.30f))
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false,
            modifier = Modifier.weight(.46f)
        ) { stepIndex ->
            Image(
                painter = OnboardingStep.entries[stepIndex].imagePainter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.weight(.24f))
        HorizontalPagerIndicator(pagerState = pagerState)
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = currentStep.title,
            style = AppTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 56.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentStep.description,
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 56.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        MainButton(
            text = currentStep.buttonText,
            onClick = onButtonClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.skip_onboarding)),
            onClick = { onSkipOnboarding() })
        Spacer(modifier = Modifier.height(64.dp))
    }
}

private val OnboardingStep.imagePainter: Painter
    @Composable
    get() = painterResource(
        when (this) {
            OnboardingStep.STEP_1 -> R.drawable.img_onboarding_step_1
            OnboardingStep.STEP_2 -> R.drawable.img_onboarding_step_2
            OnboardingStep.STEP_3 -> R.drawable.img_onboarding_step_3
        }
    )

private val OnboardingStep.title: String
    @Composable
    @ReadOnlyComposable
    get() = stringResource(
        when (this) {
            OnboardingStep.STEP_1 -> R.string.title1
            OnboardingStep.STEP_2 -> R.string.title2
            OnboardingStep.STEP_3 -> R.string.title3
        }
    )

private val OnboardingStep.description: String
    @Composable
    @ReadOnlyComposable
    get() = stringResource(
        when (this) {
            OnboardingStep.STEP_1 -> R.string.description1
            OnboardingStep.STEP_2 -> R.string.description2
            OnboardingStep.STEP_3 -> R.string.description3
        }
    )

private val OnboardingStep.buttonText: String
    @Composable
    @ReadOnlyComposable
    get() = stringResource(
        when (this) {
            OnboardingStep.STEP_1 -> R.string.button1
            OnboardingStep.STEP_2 -> R.string.button2
            OnboardingStep.STEP_3 -> R.string.button3
        }
    )

@Preview
@Composable
private fun OnboardingContentPreview() {
    LanguageAppTheme {
        OnBoardingScreenContent(
            currentStep = OnboardingStep.STEP_1,
            onButtonClick = {},
            onSkipOnboarding = {})
    }
}