package my.novikov.core.architecture.mvi.models

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

internal interface EventModel<E> {
    /**
     * Alias for [sendEvent] with one difference - this function will suspend if
     * [MVIConfiguration.onEventOverflow] permits it.
     */
    suspend fun emitEvent(event: E)

    /**
     * Send an event asynchronously. The intent is sent to the receiver and is placed in a queue.
     * When the events collection is started, the event will be processed.
     * Events that overflow the buffer will be handled according to the
     * behavior specified in [MVIConfiguration.onEventOverflow].
     * If the events collection is not started when an event is sent, it will wait in the buffer, and **will be processed
     * once the store is started**.
     */
    fun sendEvent(event: E)

    /**
     * Alias for [sendEvent]
     */
    fun event(event: E): Unit = sendEvent(event)

    suspend fun awaitEvents(onEvent: suspend (event: E) -> Unit)
}

internal fun <I> eventModel(
    parallel: Boolean,
    capacity: Int,
    overflow: BufferOverflow,
): EventModel<I> = EventModelImpl(parallel, capacity, overflow)


private class EventModelImpl<E>(
    private val parallel: Boolean,
    capacity: Int,
    overflow: BufferOverflow,
) : EventModel<E> {

    private val events = Channel<E>(capacity, overflow)

    override suspend fun emitEvent(event: E) = events.send(event)

    override fun sendEvent(event: E) {
        events.trySend(event)
    }

    override fun event(event: E): Unit = sendEvent(event)

    override suspend fun awaitEvents(onEvent: suspend (event: E) -> Unit) = coroutineScope {
        for (event in events) {
            // must always suspend the current scope to wait for intents
            if (parallel) launch { onEvent(event) } else onEvent(event)
            yield()
        }
    }
}