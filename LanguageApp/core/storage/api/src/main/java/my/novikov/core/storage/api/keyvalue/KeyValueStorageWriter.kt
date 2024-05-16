package my.novikov.core.storage.api.keyvalue

/**
 * Интерфейс для обнавление данные в k-v хранилище
 */
interface KeyValueStorageWriter {

    /**
     * Выполнет операции внутри [updateAction] по обновлению данных в хранилище с возможностью чтения
     * @param blocking блокировать ли выполнение записи
     */
    fun updateWithRead(
        blocking: Boolean = false,
        updateAction: (KeyValueStorageReader, KeyValueStorageWriteOperations) -> Unit
    )

    /**
     * Выполнет операции внутри [updateAction] по обновлению данных в хранилище
     * @param blocking блокировать ли выполнение записи
     */
    fun update(blocking: Boolean = false, updateAction: (KeyValueStorageWriteOperations) -> Unit)
}
