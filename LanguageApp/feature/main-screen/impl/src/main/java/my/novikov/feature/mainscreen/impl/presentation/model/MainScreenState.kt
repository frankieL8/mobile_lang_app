package my.novikov.feature.mainscreen.impl.presentation.model

import my.novikov.feature.learning.api.domain.Exercise
import my.novikov.feature.mainscreen.impl.domain.model.User

internal sealed interface MainScreenState {
    data object Loading : MainScreenState
    data class Content(
        val currentUser: User,
        val scoreBoardUsers: List<User>,
        val availableExercises: List<Exercise> = Exercise.entries
    ) : MainScreenState

    data object Error : MainScreenState
}

internal sealed interface MainScreenEvent {
    data object Start : MainScreenEvent
    data class ExerciseClick(val type: Exercise) : MainScreenEvent
}

internal sealed interface MainScreenAction {
    data class NavigateToExercise(val type: Exercise) : MainScreenAction
}