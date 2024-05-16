package my.novikov.core.architecture.mvi.models

import my.novikov.core.architecture.mvi.util.DelicateMVIViewModelApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow

// import androidx.compose.runtime.Immutable

/**
 * An class representing how Actions sharing will be handled in the [ActionModel].
 * There are 4 possible behaviors, which will be different depending on your use-case.
 * When in doubt, use the default one, and change if you have issues.
 */
// @Immutable
sealed interface ActionShareBehavior {

    /**
     * Actions will be distributed to all subscribers equally. Each subscriber will receive a reference to a single
     * instance of the action that was sent from any viewModel. Use when you want to have multiple subscribers
     * that each consume actions. Be aware that, however, if there's at least one subscriber, they will consume an
     * action entirely (i.e. other subscribers won't receive it when they "return" if they weren't present at the
     * time of emission).
     * @param buffer How many actions will be buffered when consumer processes them slower than they are emitted
     * @param replay How many actions will be replayed to each new subscriber
     * @param overflow How actions that overflow the [buffer] are handled.
     * @see [kotlinx.coroutines.channels.BufferOverflow]
     */
    data class Share(
        val buffer: Int = DefaultBufferSize,
        val replay: Int = 0,
        val overflow: BufferOverflow = BufferOverflow.SUSPEND,
    ) : ActionShareBehavior

    /**
     * Fan-out (distribute) behavior means that multiple subscribers are allowed,
     * and each action will be distributed to one subscriber.
     * If there are multiple subscribers, only one of them will handle an instance of an action,
     * and **the order is unspecified**.
     *
     * @param buffer How many actions will be buffered when consumer processes them slower than they are emitted
     * @param overflow How actions that overflow the [buffer] are handled.
     * @see [kotlinx.coroutines.channels.BufferOverflow]
     */
    data class Distribute(
        val buffer: Int = DefaultBufferSize,
        val overflow: BufferOverflow = BufferOverflow.SUSPEND
    ) : ActionShareBehavior

    /**
     * Restricts the count of subscription events to 1.
     * Attempting to subscribe to a store that has already been subscribed to will result in an exception.
     * In other words, you will be required to create a new store for each invocation of [Flow.collect].
     *
     * **Repeated subscriptions are not allowed, including lifecycle-aware collection**.
     *
     * @param buffer How many actions will be buffered when consumer processes them slower than they are emitted
     * @param overflow How actions that overflow the [buffer] are handled.
     * @see [kotlinx.coroutines.channels.BufferOverflow]
     */
    data class Restrict(
        val buffer: Int = DefaultBufferSize,
        val overflow: BufferOverflow = BufferOverflow.SUSPEND
    ) : ActionShareBehavior

    /**
     * Designates that Actions are disabled entirely.
     * Attempting to consume or send an action will throw.
     */
    object Disabled : ActionShareBehavior

    companion object {

        /**
         * The default action buffer size
         * @see kotlinx.coroutines.channels.Channel.BUFFERED
         */
        const val DefaultBufferSize: Int = 64
    }
}

internal interface ActionModel<A> {
    /**
     * Send a new side-effect to be processed by subscribers, only once.
     * How actions will be distributed and handled depends on [ActionShareBehavior].
     * Actions that make the capacity overflow may be dropped or the function may suspend until the buffer is freed.
     */
    @DelicateMVIViewModelApi
    fun sendAction(action: A)

    /**
     * Alias for [sendAction] for parity with [EventModel.sendEvent]
     */
    suspend fun emitAction(action: A)

    /**
     * Alias for [sendAction]
     */
    suspend fun action(action: A): Unit = emitAction(action)

    /**
     * A flow of Actionss to be handled, usually resulting in one-shot events.
     * How actions are distributed depends on [ActionShareBehavior].
     */
    val actions: Flow<A>
}

internal abstract class ChannelActionModel<A>(
    bufferSize: Int,
    overflow: BufferOverflow,
) : ActionModel<A> {

    protected val delegate = Channel<A>(bufferSize, overflow)

    @DelicateMVIViewModelApi
    override fun sendAction(action: A) {
        delegate.trySend(action)
    }

    override suspend fun emitAction(action: A) = delegate.send(action)
}

internal class DistributingModel<A>(
    bufferSize: Int,
    overflow: BufferOverflow,
) : ChannelActionModel<A>(bufferSize, overflow) {

    override val actions = delegate.receiveAsFlow()
}

internal class ConsumingModel<A>(
    bufferSize: Int,
    overflow: BufferOverflow,
) : ChannelActionModel<A>(bufferSize, overflow) {

    override val actions = delegate.consumeAsFlow()
}

internal class SharedModel<A>(
    replay: Int,
    bufferSize: Int,
    overflow: BufferOverflow,
) : ActionModel<A> {

    private val _actions = MutableSharedFlow<A>(
        replay = replay,
        extraBufferCapacity = bufferSize,
        onBufferOverflow = overflow
    )

    override val actions = _actions.asSharedFlow()

    @DelicateMVIViewModelApi
    override fun sendAction(action: A) {
        _actions.tryEmit(action)
    }

    override suspend fun emitAction(action: A) = _actions.emit(action)
}

internal class ThrowingModel<A> : ActionModel<A> {

    override val actions get() = error(ActionsDisabledMessage)

    @DelicateMVIViewModelApi
    override fun sendAction(action: A) = error(ActionsDisabledMessage)
    override suspend fun emitAction(action: A) = error(ActionsDisabledMessage)

    private companion object {

        private const val ActionsDisabledMessage = "Actions are disabled for this store"
    }
}

internal fun <A> actionModel(
    behavior: ActionShareBehavior,
): ActionModel<A> = when (behavior) {
    is ActionShareBehavior.Distribute -> DistributingModel(behavior.buffer, behavior.overflow)
    is ActionShareBehavior.Restrict -> ConsumingModel(behavior.buffer, behavior.overflow)
    is ActionShareBehavior.Share -> SharedModel(behavior.replay, behavior.buffer, behavior.overflow)
    is ActionShareBehavior.Disabled -> ThrowingModel()
}