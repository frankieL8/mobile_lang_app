package my.novikov.core.architecture.mvi.models

import my.novikov.core.architecture.mvi.util.MVIScreenModelDSL
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

@MVIScreenModelDSL
data class MVIConfiguration<S>(
    val initial: S,
    /**
     * Provide the [ActionShareBehavior] for the viewmodel.
     * For viewmodels where actions are of type [Nothing] this must be set to [ActionShareBehavior.Disabled].
     *
     * [ActionShareBehavior.Share] by default.
     */
    val actionShareBehavior: ActionShareBehavior = ActionShareBehavior.Share(),
    /**
     * Declare that intents must be processed in parallel.
     * All guarantees on the order of Events will be lost.
     * Events may still be dropped according to [onEventOverflow].
     * Events are not **obtained** in parallel, just processed.
     *
     * false by default.
     */
    val parallelEventProcess: Boolean = false,
    /**
     * Designate the maximum capacity of Events waiting for processing in the events queue.
     * Intents that overflow this capacity will be processed according to [onEventOverflow].
     * This should be either a positive value, or one of:
     *  * [Channel.UNLIMITED]
     *  * [Channel.CONFLATED]
     *  * [Channel.RENDEZVOUS]
     *  * [Channel.BUFFERED]
     *
     *  [Channel.UNLIMITED] by default.
     */
    val eventCapacity: Int = Channel.UNLIMITED,
    /**
     * Designate behavior for when event channel pool overflows.
     *
     * [BufferOverflow.DROP_OLDEST] by default
     */
    val onEventOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
)