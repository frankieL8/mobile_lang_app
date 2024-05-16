package my.novikov.core.architecture.either

/**
 * Монада [Either] создан, чтобы передать состояние, которое может находиться в одном из двух вариантов:
 * [Either.Left] и [Either.Right]. Если Вы передаёте ошибку и результат, то ошибка должна быть [Either.Left],
 * а результат [Either.Right]. Пример: `Either<NetworkError, LoadedUser>`.
 * Концепция из functional мира.
 */
sealed class Either<out A, out B> {

    /** Левая ветвь. */
    data class Left<T>(val value: T) : Either<T, Nothing>()

    /** Правая ветвь. */
    data class Right<T>(val value: T) : Either<Nothing, T>()

    companion object
}

/** Трансформация правой части. */
inline infix fun <A, B, C> Either<A, B>.map(f: (B) -> C): Either<A, C> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(f(value))
}

/** Трансформация левой части. */
inline infix fun <A, B, C> Either<A, C>.mapLeft(f: (A) -> B): Either<B, C> = when (this) {
    is Either.Left -> Either.Left(f(value))
    is Either.Right -> this
}

/** Трансформация левой и правой частей. */
inline fun <A, B, C, D> Either<A, B>.bimap(leftOperation: (A) -> C, rightOperation: (B) -> D): Either<C, D> =
    when (this) {
        is Either.Left -> Either.Left(leftOperation(value))
        is Either.Right -> Either.Right(rightOperation(value))
    }

/** Трансформация правой ветви. В отличие от [map], результом будет новый [Either]. */
inline infix fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> =
    when (this) {
        is Either.Left -> this
        is Either.Right -> f(value)
    }

/** Сведение обеих ветвей к единому результату [C]. */
inline fun <A, B, C> Either<A, B>.fold(ifLeft: (A) -> C, ifRight: (B) -> C): C = when (this) {
    is Either.Left -> ifLeft(value)
    is Either.Right -> ifRight(value)
}

/** Создание [Either] на основе результата [f]. */
@Suppress("InstanceOfCheckForException")
inline fun <reified A : Exception, B> either(f: () -> B): Either<A, B> =
    try {
        Either.Right(f())
    } catch (e: Exception) {
        if (e is A) {
            Either.Left(e)
        } else {
            throw e
        }
    }