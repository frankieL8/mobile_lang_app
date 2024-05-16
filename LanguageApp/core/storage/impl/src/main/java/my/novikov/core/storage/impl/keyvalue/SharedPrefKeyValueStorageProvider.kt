package my.novikov.core.storage.impl.keyvalue

import android.content.Context
import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import my.novikov.core.storage.api.keyvalue.KeyValueStorageProvider

/**
 * Фабрика по получению хранилищ [KeyValueStorage] на основе их ключей
 * Реализация для SharedPreferences
 */
class SharedPrefKeyValueStorageProvider(
    private val context: Context
) : KeyValueStorageProvider {

    /**
     * Получение хранилища
     * @param storageKey ключ. Должен содержать только латинские символы и подчеркивания
     */
    override fun get(storageKey: String): KeyValueStorage =
        SharedPrefKeyValueStorage(
            context,
            storageKey
        )
}