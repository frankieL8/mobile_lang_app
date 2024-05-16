package my.novikov.core.architecture.mvi.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

/**
 * Do the operation on [this] if the type of [this] is [T], and return [R], otherwise return [this]
 */
@OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
@MVIScreenModelDSL
inline fun <reified T, R> R.withType(@BuilderInference block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return typed<T>()?.let(block) ?: this
}

/**
 * Cast this to type [T] or return null.
 *
 * Just an alias for `(this as? T)`
 */
@MVIScreenModelDSL
inline fun <reified T> Any?.typed(): T? = this as? T