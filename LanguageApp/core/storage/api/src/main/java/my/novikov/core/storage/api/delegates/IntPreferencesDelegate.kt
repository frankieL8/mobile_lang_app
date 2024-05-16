package my.novikov.core.storage.api.delegates

import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Расширение, создающее делегат для работы с хранимой в [KeyValueStorage] Int переменной
 */
fun KeyValueStorage.intPreference(key: String, defValue: Int = 0) =
    IntPreferencesDelegate(this, key, defValue)

/**
 * Делегат для работы с boolean из [KeyValueStorage] как с обычной Int-переменной
 * @param storage хранилище
 * @param key ключ, под которым значение хранится в [storage]
 * @param defValue значение по-умолчанию
 */
class IntPreferencesDelegate(
    private val storage: KeyValueStorage,
    private val key: String,
    private val defValue: Int
) : ReadWriteProperty<Any?, Int?> {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
        storage.writer.update { if (value != null) it.setInt(key, value) else it.remove(key) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return storage.reader.getInt(key) ?: defValue
    }
}