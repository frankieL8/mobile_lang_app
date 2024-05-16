package my.novikov.core.architecture.mvi.dsl

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import my.novikov.core.architecture.mvi.MVIScreenModel
import my.novikov.core.architecture.mvi.util.MVIScreenModelDSL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

/**
 * A function to subscribe to the actions flow that follows the system lifecycle.
 * This function will assign the flow a new subscriber when invoked
 * Provided [consume] parameter will be used to consume actions that come from the viewModel.
 *
 * @param lifecycleState the minimum lifecycle state that should be reached in order to subscribe to the store,
 *   upon leaving that state, the function will unsubscribe.
 * @param consume a lambda to consume actions with.
 */
@SuppressLint("ComposableNaming")
@Suppress("NOTHING_TO_INLINE")
@Composable
@MVIScreenModelDSL
inline fun <E, S, A> MVIScreenModel<E, S, A>.collectActionsWithLifeсycle(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline consume: suspend CoroutineScope.(action: A) -> Unit = {}
) {
    val owner = LocalLifecycleOwner.current
    val block by rememberUpdatedState(consume)
    LaunchedEffect(this, lifecycleState, owner) {
        withContext(Dispatchers.Main.immediate) {
            owner.repeatOnLifecycle(lifecycleState) {
                this@collectActionsWithLifeсycle.actions.collect { block(it) }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun <A> SharedFlow<A>.collectActionsWithLifeсycle(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline consume: suspend CoroutineScope.(action: A) -> Unit = {}
) {
    val owner = LocalLifecycleOwner.current
    val block by rememberUpdatedState(consume)
    LaunchedEffect(this, lifecycleState, owner) {
        withContext(Dispatchers.Main.immediate) {
            owner.repeatOnLifecycle(lifecycleState) {
                this@collectActionsWithLifeсycle.collect { block(it) }
            }
        }
    }
}