package my.novikov.core.storage.impl.keyvalue

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import my.novikov.core.storage.api.keyvalue.KeyValueStorageReader

/**
 * An implementation of [KeyValueStorageReader] for Shared Preferences
 * @property sharedPreferences an entity for accessing to [SharedPreferences]
 */
internal class SharedPrefKeyValueStorageReader(
    private val sharedPreferences: SharedPreferences
) : KeyValueStorageReader {

    override fun getStringSet(key: String): Set<String>? = sharedPreferences.getStringSet(key, null)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun getString(key: String, defaultValue: String?): String? =
        sharedPreferences.getString(key, defaultValue)

    override fun getLong(key: String): Long? {
        return if (contains(key)) sharedPreferences.getLong(key, 0) else null
    }

    override fun getInt(key: String): Int? {
        return if (contains(key)) sharedPreferences.getInt(key, 0) else null
    }

    override fun getFloat(key: String): Float? {
        return if (contains(key)) sharedPreferences.getFloat(key, 0f) else null
    }

    override fun contains(key: String): Boolean = sharedPreferences.contains(key)

    override fun observeStringValue(key: String, defaultValue: String?): Flow<String?> {
        return sharedPreferences.observeValue(
            key = key,
            valueSupplier = { getString(key, defaultValue) }
        )
    }

    override fun observeLongValue(key: String): Flow<Long?> {
        return sharedPreferences.observeValue(
            key = key,
            valueSupplier = { getLong(key) }
        )
    }

    override fun observeBooleanValue(key: String, defaultValue: Boolean): Flow<Boolean> {
        return sharedPreferences.observeValue(
            key = key,
            valueSupplier = { getBoolean(key, defaultValue) }
        )
    }

    override fun observeIntValue(key: String): Flow<Int?> {
        return sharedPreferences.observeValue(
            key = key,
            valueSupplier = { getInt(key) }
        )
    }
}

private fun <T> SharedPreferences.observeValue(key: String, valueSupplier: () -> T): Flow<T> {
    return callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(valueSupplier())
            }
        }
        registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}
