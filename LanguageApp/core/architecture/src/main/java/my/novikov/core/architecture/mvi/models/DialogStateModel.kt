package my.novikov.core.architecture.mvi.models

import my.novikov.core.architecture.mvi.util.DelicateMVIViewModelApi
import my.novikov.core.architecture.mvi.util.withReentrantLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex

/**
 * Same as StateModel but contains nullable value in state for dialog specifically
 */
interface DialogStateModel<D> {

    suspend fun updateDialogState(transform: suspend D?.() -> D?)

    suspend fun withDialogState(block: suspend D?.() -> Unit)

    fun useDialogState(block: D?.() -> D?)

    suspend fun closeDialog() = updateDialogState { null }

    val dialogState: StateFlow<D?>

    @DelicateMVIViewModelApi
    val currentDialogState: D? get() = dialogState.value
}

internal fun <D> dialogStateModel(): DialogStateModel<D> = DialogStateModelImpl()

internal class DialogStateModelImpl<D> : DialogStateModel<D> {

    private val _state = MutableStateFlow<D?>(null)
    private val stateMutex = Mutex()

    @DelicateMVIViewModelApi
    override val currentDialogState by _state::value

    override fun useDialogState(block: D?.() -> D?) = _state.update(block)

    override val dialogState: StateFlow<D?> = _state.asStateFlow()

    override suspend fun withDialogState(block: suspend D?.() -> Unit) =
        stateMutex.withReentrantLock { block(dialogState.value) }

    override suspend fun updateDialogState(transform: suspend D?.() -> D?) =
        stateMutex.withReentrantLock { _state.update { transform(it) } }
}