package my.novikov.core.storage.impl.keyvalue

import android.content.SharedPreferences
import my.novikov.core.storage.api.keyvalue.KeyValueStorageWriteOperations

/**
 * An implementation of [KeyValueStorageWriteOperations] for Shared Preferences
 * @property editor an entity for updating [SharedPreferences]
 */
internal class SharedPrefKeyValueStorageWriteOperations(
    private val editor: SharedPreferences.Editor
) : KeyValueStorageWriteOperations {

    override fun setSetString(key: String, value: Set<String>) {
        editor.putStringSet(key, value)
    }

    override fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
    }

    override fun setString(key: String, value: String) {
        editor.putString(key, value)
    }

    override fun setLong(key: String, value: Long) {
        editor.putLong(key, value)
    }

    override fun setInt(key: String, value: Int) {
        editor.putInt(key, value)
    }

    override fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value)
    }

    override fun remove(key: String) {
        editor.remove(key)
    }

    override fun clear() {
        editor.clear()
    }
}
