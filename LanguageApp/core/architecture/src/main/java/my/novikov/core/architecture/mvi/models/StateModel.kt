package my.novikov.core.architecture.mvi.models

import my.novikov.core.architecture.mvi.util.DelicateMVIViewModelApi
import my.novikov.core.architecture.mvi.util.MVIScreenModelDSL
import my.novikov.core.architecture.mvi.util.withReentrantLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex

/**
 * An entity that handles State updates.
 */
interface StateModel<S> {

    /**
     * Obtain the current state and update it with the result of [transform]
     * atomically and in a suspending manner.
     *
     * **This function will suspend until all previous [withState] or [updateState] invocations are finished.**
     * **This function is reentrant, for more info, see [withState].**
     *
     * If you want to operate on a state of particular subtype, use the typed version of this function.
     *
     * @see [withState]
     */
    @MVIScreenModelDSL
    suspend fun updateState(transform: suspend S.() -> S)

    /**
     * Obtain the current state and operate on it.
     *
     * This function does NOT update the state, for that, use [updateState].
     * ViewModel allows only one state update at a time, and because of that,
     * **every coroutine that will invoke [withState] or [updateState]
     * will be suspended until the previous state handler is finished.**
     *
     * This function uses locks under the hood.
     * For a version that runs when the state is of particular subtype, see other overloads of this function.
     *
     * This function is reentrant, which means, if you call:
     * ```kotlin
     * withState {
     *   withState { }
     * }
     * ```
     * you should not get a deadlock, but overriding coroutine contexts can still cause problems.
     * This function has lower performance than [useState] and allows plugins to intercept the state change.
     * If you really need the additional performance or wish to avoid plugins, use [useState].
     */
    @MVIScreenModelDSL
    suspend fun withState(block: suspend S.() -> Unit)

    /**
     * A function that obtains current state and updates it atomically (in the thread context), and non-atomically in
     * the coroutine context, which means it can cause races when you want to update states in parallel.
     * This function is performant, but is **not thread-safe**.
     * It should only be used for the most critical state updates happening very often.
     */
    @MVIScreenModelDSL
    fun useState(block: S.() -> S)


    /**
     * A flow of states to be handled by the subscriber.
     */
    val state: StateFlow<S>

    /**
     * Obtain the current state in an unsafe manner.
     * This property is not thread-safe and parallel state updates will introduce a race condition when not
     * handled properly.
     * Such race conditions arise when using multiple data streams such as [kotlinx.coroutines.flow.Flow]s.
     *
     * Accessing and modifying the state this way will **circumvent ALL plugins** and will not make state updates atomic.
     */
    @DelicateMVIViewModelApi
    val currentState: S get() = state.value
}

internal fun <S> stateModel(initial: S): StateModel<S> = StateModelImpl(initial)

private class StateModelImpl<S>(initial: S) : StateModel<S> {

    private val _state = MutableStateFlow(initial)
    private val stateMutex = Mutex()

    @DelicateMVIViewModelApi
    override val currentState by _state::value

    override fun useState(block: S.() -> S) = _state.update(block)

    override val state: StateFlow<S> = _state.asStateFlow()

    override suspend fun withState(block: suspend S.() -> Unit) =
        stateMutex.withReentrantLock { block(state.value) }

    override suspend fun updateState(transform: suspend S.() -> S) =
        stateMutex.withReentrantLock { _state.update { transform(it) } }
}