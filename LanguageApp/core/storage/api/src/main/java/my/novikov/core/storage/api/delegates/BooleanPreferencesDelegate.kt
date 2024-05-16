package my.novikov.core.storage.api.delegates

import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Расширение, создающее делегат для работы с хранимой в [KeyValueStorage] Boolean переменной
 */
fun KeyValueStorage.booleanPreference(key: String, defValue: Boolean = false) =
    BooleanPreferencesDelegate(this, key, defValue)

/**
 * Делегат для работы с boolean из [KeyValueStorage] как с обычной Boolean-переменной
 * @param storage хранилище
 * @param key ключ, под которым значение хранится в [storage]
 * @param defValue значение по умолчанию
 */
class BooleanPreferencesDelegate(
    private val storage: KeyValueStorage,
    private val key: String,
    private val defValue: Boolean
) : ReadWriteProperty<Any?, Boolean?> {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean?) {
        storage.writer.update { if (value != null) it.setBoolean(key, value) else it.remove(key) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return storage.reader.getBoolean(key, defValue)
    }
}