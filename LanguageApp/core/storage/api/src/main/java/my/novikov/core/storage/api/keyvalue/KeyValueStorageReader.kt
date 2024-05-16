package my.novikov.core.storage.api.keyvalue

import kotlinx.coroutines.flow.Flow

/**
 * An interface for reading data from a key-value storage
 */
interface KeyValueStorageReader {
    /**
     * Get a set of strings from the storage by its key
     * @param key value's key
     * @return resulted value or null if the value has not been found
     */
    fun getStringSet(key: String): Set<String>?

    /**
     * Get a boolean value from the storage by its key
     * @param key value's key
     * @return resulted value or [defaultValue] if the value has not been found
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /**
     * Get a string value from the storage by its key
     * @param key value's key
     * @return resulted value or [defaultValue] if the value has not been found
     */
    fun getString(key: String, defaultValue: String? = null): String?

    /**
     * Получить  значение из хранилища по его ключу [key]
     */
    fun getLong(key: String): Long?

    /**
     * Получить  значение из хранилища по его ключу [key]
     */
    fun getInt(key: String): Int?

    /**
     * Получить  значение из хранилища по его ключу [key]
     */
    fun getFloat(key: String): Float?

    /**
     * Checks for the existence of a value in the store
     * @param key value's key
     * @return checking result
     */
    fun contains(key: String): Boolean

    /** Получение Flow изменений String значения хранилища по ключу. */
    fun observeStringValue(key: String, defaultValue: String? = null): Flow<String?>

    /** Получение Flow изменений Long значения хранилища по ключу. */
    fun observeLongValue(key: String): Flow<Long?>

    /** Получение Flow изменений Int значения хранилища по ключу. */
    fun observeIntValue(key: String): Flow<Int?>

    /** Получение Flow изменений Boolean значения хранилища по ключу. */
    fun observeBooleanValue(key: String, defaultValue: Boolean = false): Flow<Boolean>
}
