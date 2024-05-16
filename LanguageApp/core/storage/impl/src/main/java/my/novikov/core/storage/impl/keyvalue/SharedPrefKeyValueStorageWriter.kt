package my.novikov.core.storage.impl.keyvalue

import android.annotation.SuppressLint
import android.content.SharedPreferences
import my.novikov.core.storage.api.keyvalue.KeyValueStorageReader
import my.novikov.core.storage.api.keyvalue.KeyValueStorageWriteOperations
import my.novikov.core.storage.api.keyvalue.KeyValueStorageWriter

/**
 * An implementation of [KeyValueStorageWriter] for Shared Preferences
 * @property sharedPreferences an entity for accessing to [SharedPreferences]
 * @property reader an interface for reading data from a key-value storage
 */
class SharedPrefKeyValueStorageWriter(
    private val reader: KeyValueStorageReader,
    private val sharedPreferences: SharedPreferences
) : KeyValueStorageWriter {


    override fun updateWithRead(
        blocking: Boolean,
        updateAction: (KeyValueStorageReader, KeyValueStorageWriteOperations) -> Unit
    ) = updateInternal(blocking = blocking) {
        updateAction(reader, SharedPrefKeyValueStorageWriteOperations(it))
    }

    override fun update(blocking: Boolean, updateAction: (KeyValueStorageWriteOperations) -> Unit) =
        updateInternal(blocking = blocking) {
            updateAction(SharedPrefKeyValueStorageWriteOperations(it))
        }

    @SuppressLint("ApplySharedPref")
    private fun updateInternal(
        blocking: Boolean,
        updateAction: (SharedPreferences.Editor) -> Unit
    ) {
        sharedPreferences.edit().apply {
            updateAction(this)
            if (blocking) commit() else apply()
        }
    }
}
