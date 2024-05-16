package my.novikov.core.architecture.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import my.novikov.core.architecture.mvi.models.ActionModel
import my.novikov.core.architecture.mvi.models.EventModel
import my.novikov.core.architecture.mvi.models.MVIConfiguration
import my.novikov.core.architecture.mvi.models.StateModel
import my.novikov.core.architecture.mvi.models.actionModel
import my.novikov.core.architecture.mvi.models.eventModel
import my.novikov.core.architecture.mvi.models.stateModel
import kotlinx.coroutines.launch


/**
 * Inspired by FlowMVI 2.0
 *
 * Базовый класс для view-model, реализующей паттерн MVI/MVVM+
 * @see <a href="https://developer.android.com/jetpack/compose/text/user-input#state-practices">
 * respawn-app/FlowMVI GitHub
 * </a>
 * @param E базовый класс для внешнего события экрана (клик пользователя, получение пермишна); рекомендуется использовать sealed-класс или enum
 * @param S базовый класс для стейта экрана; рекомендуется использовать sealed-класс или data-класс, если экран простой
 * @param A базовый класс для одноразовых действий, которые нужно воспроизвести (показать попап, вызвать системный picker); рекомендуется использовать sealed-класс или enum
 */
abstract class MVIScreenModel<E, S, A>(
    val config: MVIConfiguration<S>
) : ViewModel(),
    StateModel<S> by stateModel(config.initial),
    ActionModel<A> by actionModel(config.actionShareBehavior),
    EventModel<E> by eventModel(
        config.parallelEventProcess,
        config.eventCapacity,
        config.onEventOverflow
    ) {

    init {
        viewModelScope.launch {
            awaitEvents(::onEvent)
        }
    }

    /**
     * Event handler. Has to be overwritten at place
     */
    protected abstract suspend fun onEvent(event: E)
}
