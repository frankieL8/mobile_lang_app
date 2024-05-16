package my.novikov.core.storage.api.delegates

import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Расширение, создающее делегат для работы с хранимой в [KeyValueStorage] String переменной
 */
fun KeyValueStorage.stringPreference(key: String, defValue: String? = null) =
    StringPreferencesDelegate(this, key, defValue)

/**
 * Делегат для работы со строкой из [KeyValueStorage] как с обычной String-переменной
 * @param storage хранилище
 * @param key ключ, под которым значение хранится в [storage]
 * @param defValue значение по умолчанию
 */
class StringPreferencesDelegate(
    private val storage: KeyValueStorage,
    private val key: String,
    private val defValue: String?
) : ReadWriteProperty<Any?, String?> {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        storage.writer.update { if (value != null) it.setString(key, value) else it.remove(key) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return storage.reader.getString(key, defValue)
    }
}