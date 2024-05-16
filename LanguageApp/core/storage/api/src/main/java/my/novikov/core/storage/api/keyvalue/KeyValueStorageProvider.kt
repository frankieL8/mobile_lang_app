package my.novikov.core.storage.api.keyvalue

/**
 * Интерфейс фаблики по получению хранилищ [KeyValueStorage] на основе их ключей
 */
interface KeyValueStorageProvider {
    /**
     * Получение хранилища
     * @param storageKey ключ. Должен содержать только латинские символы и подчеркивания
     */
    operator fun get(storageKey: String): KeyValueStorage
}