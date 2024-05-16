package my.novikov.core.storage.api.keyvalue

/**
 * A set of operations for updating data in a key-value storage
 */
interface KeyValueStorageWriteOperations {
    /**
     * Puts a value with given key to a storage. If the value exists, it'll be overwritten
     * @param key value's key
     * @param value to put
     */
    fun setSetString(key: String, value: Set<String>)

    /**
     * Puts a value with given key to a storage. If the value exists, it'll be overwritten
     * @param key value's key
     * @param value to put
     */
    fun setBoolean(key: String, value: Boolean)

    /**
     * Puts a value with given key to a storage. If the value exists, it'll be overwritten
     * @param key value's key
     * @param value to put
     */
    fun setString(key: String, value: String)

    /**
     * Помещает значение [value] с заданным ключом [key] в хранилище.
     * Если значение существует, оно будет перезаписано
     */
    fun setLong(key: String, value: Long)

    /**
     * Помещает значение [value] с заданным ключом [key] в хранилище.
     * Если значение существует, оно будет перезаписано
     */
    fun setInt(key: String, value: Int)

    /**
     * Помещает значение [value] с заданным ключом [key] в хранилище.
     * Если значение существует, оно будет перезаписано
     */
    fun setFloat(key: String, value: Float)

    /**
     * Removes a value with given key from a storage.
     */
    fun remove(key: String)

    /**
     * Remove all data from a storage
     */
    fun clear()
}
