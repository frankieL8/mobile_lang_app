package my.novikov.feature.mainscreen.impl.presentation

import android.util.Log
import kotlinx.coroutines.CancellationException
import my.novikov.core.architecture.mvi.MVIScreenModel
import my.novikov.core.architecture.mvi.models.MVIConfiguration
import my.novikov.feature.learning.api.domain.Exercise
import my.novikov.feature.mainscreen.impl.domain.model.User
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenAction
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenEvent
import my.novikov.feature.mainscreen.impl.presentation.model.MainScreenState
import my.novikov.feature.userinfo.api.UserRepository

internal class MainScreenViewModel(private val userRepository: UserRepository) :
    MVIScreenModel<MainScreenEvent, MainScreenState, MainScreenAction>(
        config = MVIConfiguration(MainScreenState.Loading)
    ) {
    override suspend fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.ExerciseClick -> onExerciseClick(event.type)
            MainScreenEvent.Start -> onStart()
        }
    }

    private suspend fun onStart() {
        try {
            val currentUserModel = userRepository.getCurrentUser() ?: return
            val currentUser = User(
                emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C",
                name = with(currentUserModel) { "$firstName $lastName" },
                points = userRepository.getCurrentUserScore()
            )
            val scoreBoardUsers = listOf(
                User(
                    emoji = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDFA8",
                    name = "Vincent van Gogh",
                    points = 12
                ),
                User(
                    emoji = "\uD83D\uDC68\uDFFB\u200D\uD83C\uDFA8",
                    name = "Napoleon Bonaparte",
                    points = 7
                ),
                User(
                    emoji = "\uD83C\uDFFB\u200D\uD83C\uDFA8",
                    name = "Julius Caesar",
                    points = 2
                )
            ).take(3).reversed()
            val place = scoreBoardUsers.binarySearchBy(currentUser.points) { it.points }
            val insertionPoint = if (place >= 0) {
                place - 1
            } else {
                val ip = -place - 1
                if (
                    scoreBoardUsers[ip].points - currentUser.points <
                    currentUser.points - scoreBoardUsers[(ip - 1).coerceAtLeast(0)].points
                ) {
                    ip
                } else {
                    ip - 1
                }
            }.coerceAtLeast(0)
            updateState {
                MainScreenState.Content(
                    currentUser = currentUser,
                    scoreBoardUsers = scoreBoardUsers.toMutableList().apply {
                        add(insertionPoint, currentUser)
                    }.toList().reversed()
                )
            }
        } catch (ex: Exception) {
            if (ex is CancellationException) throw ex
            Log.e(this@MainScreenViewModel::class.simpleName, ex.message.orEmpty())
            updateState { MainScreenState.Error }
        }
    }

    private suspend fun onExerciseClick(type: Exercise) {
        action(MainScreenAction.NavigateToExercise(type))
    }
}