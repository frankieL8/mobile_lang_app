package my.novikov.core.architecture.mvi.dsl

import my.novikov.core.architecture.mvi.models.StateModel
import my.novikov.core.architecture.mvi.util.MVIScreenModelDSL
import my.novikov.core.architecture.mvi.util.typed
import my.novikov.core.architecture.mvi.util.withType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

/**
 * Run [block] if current [StateModel.currentState] is of type [T], otherwise do nothing.
 *
 * **This function will suspend until all previous [StateModel.withState] invocations are finished.**
 * @see StateModel.withState
 * @see StateModel.useState
 * @see StateModel.updateState
 */
@OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
@MVIScreenModelDSL
suspend inline fun <reified T : S, S> StateModel<S>.withState(
    @BuilderInference crossinline block: suspend T.() -> Unit
) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    withState { typed<T>()?.block() }
}

/**
 * Obtain the current [StateModel.currentState] and update it with
 * the result of [transform] if it is of type [T], otherwise do nothing.
 *
 * **This function will suspend until all previous [StateModel.withState] invocations are finished.**
 * @see StateModel.withState
 * @see StateModel.useState
 * @see StateModel.updateState
 */
@OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
@MVIScreenModelDSL
suspend inline fun <reified T : S, S> StateModel<S>.updateState(
    @BuilderInference crossinline transform: suspend T.() -> S
) {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return updateState { withType<T, _> { transform() } }
}

/**
 * Obtain the current [StateModel.currentState] and update it with
 * the result of [transform] if it is of type [T], otherwise do nothing.
 *
 * * **This function may be executed multiple times**
 * * **This function will not trigger any plugins. It is intended for performance-critical operations only**
 * * **This function does lock the state. Watch out for races**
 *
 * @see StateModel.withState
 * @see StateModel.useState
 * @see StateModel.updateState
 */
@OptIn(ExperimentalTypeInference::class)
@MVIScreenModelDSL
inline fun <reified T : S, S> StateModel<S>.useState(
    @BuilderInference crossinline transform: T.() -> S
): Unit = useState { withType<T, _> { transform() } }