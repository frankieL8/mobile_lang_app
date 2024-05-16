package my.novikov.core.storage.impl.secret

import android.util.Base64
import my.novikov.core.storage.impl.secret.util.AESCryptographer
import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import my.novikov.core.storage.api.secret.SecretStorage
import my.novikov.core.storage.impl.secret.util.HashUtils

internal class SecretStorageImpl(
    alias: String,
    private val storage: KeyValueStorage
) : SecretStorage {

    private val ivKey = alias + IV_KEY_SUFFIX
    private val keystoreAlias = alias + KEYSTORE_SUFFIX
    private val iv by lazy { AESCryptographer.generateIv() }

    @Synchronized
    override fun save(key: String, value: String, blocking: Boolean) {
        val encrypted = AESCryptographer.encrypt(keystoreAlias, value, iv)
        val keyHash = HashUtils.sha1(key)
        storage.writer.update(blocking = blocking) {
            it.setString(ivKey, Base64.encodeToString(iv, Base64.NO_WRAP))
            it.setString(keyHash, encrypted)
        }
    }

    @Synchronized
    override fun load(key: String): String? {
        try {
            val keyHash = HashUtils.sha1(key)
            val encrypted = storage.reader.getString(keyHash, null) ?: return null
            val ivString = storage.reader.getString(ivKey, null) ?: return null

            val iv: ByteArray = Base64.decode(ivString, Base64.NO_WRAP)
            return AESCryptographer.decrypt(keystoreAlias, encrypted, iv)
        } catch (e: Exception) {
            return null
        }
    }

    @Synchronized
    override fun remove(key: String, blocking: Boolean) {
        val keyHash = HashUtils.sha1(key)
        storage.writer.update(blocking = blocking) {
            it.remove(keyHash)
        }
    }

    private companion object {
        private const val IV_KEY_SUFFIX = "_iv"
        private const val KEYSTORE_SUFFIX = "_ks"
    }
}
