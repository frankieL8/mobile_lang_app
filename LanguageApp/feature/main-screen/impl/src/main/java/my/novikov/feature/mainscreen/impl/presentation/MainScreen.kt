package my.novikov.feature.mainscreen.impl.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.novikov.core.architecture.mvi.dsl.collectActionsWithLifeсycle
import my.novikov.core.uikit.theme.AppTheme
import my.novikov.core.uikit.theme.LanguageAppTheme
import my.novikov.feature.learning.api.domain.Exercise
import my.novikov.feature.mainscreen.impl.domain.model.User
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenAction
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenEvent
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenState
import my.novikov.feature.mainscreen.impl.presentation.ui.ExerciseType
import my.novikov.feature.mainscreen.impl.presentation.ui.TopUserItem
import my.novikov.feature.mainscreen.impl.presentation.ui.text
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler { }

    when (val stateValue = state.value) {
        is MainScreenState.Content -> {
            MainScreenContent(
                modifier = modifier,
                state = stateValue,
                onExerciseClick = {
                    viewModel.event(MainScreenEvent.ExerciseClick(it))
                }
            )
        }

        MainScreenState.Error -> Unit
        MainScreenState.Loading -> Unit
    }

    viewModel.collectActionsWithLifeсycle { action ->
        when (action) {
            is MainScreenAction.NavigateToExercise -> {
                Toast.makeText(
                    context,
                    "Exercise \"${action.type.text}\" is not available yet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event(MainScreenEvent.Start)
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    state: MainScreenState.Content,
    onExerciseClick: (Exercise) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.purple)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.grey)
                )
                Text(
                    text = state.currentUser.name,
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colors.textStatic
                )
                Text(
                    text = "Are you ready for learning today?",
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colors.textStatic
                )
                Spacer(modifier = Modifier.height(11.dp))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = "Top users",
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                style = AppTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.scoreBoardUsers.forEach { user ->
                    TopUserItem(
                        user = user,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = "Available exersises",
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textStatic
            )
            Spacer(modifier = Modifier.height(9.dp))
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(21.dp)
            ) {
                repeat(1 + (state.availableExercises.size - 1) / 2) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(21.dp)) {
                        repeat(2) { columnIndex ->
                            val itemIndex = rowIndex * 2 + columnIndex

                            if (itemIndex < state.availableExercises.size) {
                                ExerciseType(
                                    exercise = state.availableExercises[itemIndex],
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        onExerciseClick(state.availableExercises[itemIndex])
                                    }
                                )
                            } else {
                                Spacer(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview
@Composable
private fun EntranceLoginScreen() {
    LanguageAppTheme {
        MainScreenContent(
            state = MainScreenState.Content(
                scoreBoardUsers = listOf(
                    User(
                        emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8",
                        name = "Vincent van Gogh",
                        points = 12
                    ), User(
                        emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8",
                        name = "Vincent van Gogh",
                        points = 12
                    ), User(
                        emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8",
                        name = "Vincent van Gogh",
                        points = 12
                    )
                ),
                currentUser = User(
                    emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8",
                    name = "Vincent van Gogh",
                    points = 12
                )
            ),
            onExerciseClick = {}
        )
    }
}