package my.novikov.core.storage.impl.secret.util

import android.security.KeyStoreException
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import java.security.UnrecoverableEntryException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * Утилитный класс для шифровки/дешифровки произвольного текста с помощью алгоритма AES
 * и хранения ключа шифрования в Android Keystore
 * Исчерпывающее описание на русском https://habr.com/ru/company/rambler_and_co/blog/279835/
 */
object AESCryptographer {

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private const val IV_SIZE = 16
    private const val AES_ALGORITHM = "AES"

    /**
     * Генерирует секретный ключ [SecretKey] с помощью [KeyGenParameterSpec.Builder] c псевдонимом [keystoreAlias]
     */
    @Throws(KeyStoreException::class, IllegalStateException::class)
    fun generateSecretKey(
        keystoreAlias: String,
    ): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )
        val spec = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setRandomizedEncryptionRequired(false)
            .setUserAuthenticationRequired(false)
            .setInvalidatedByBiometricEnrollment(false)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    /**
     * Возвращает секретный ключ [SecretKey] из [KeyStore.SecretKeyEntry] c псевдонимом [keystoreAlias]
     */
    @Throws(GeneralSecurityException::class, KeyStoreException::class, IllegalStateException::class)
    fun getOrCreateSecretKey(
        keystoreAlias: String,
    ): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        val secretKeyEntry = try {
            keyStore.getEntry(keystoreAlias, null) as? KeyStore.SecretKeyEntry
        } catch (ex: UnrecoverableEntryException) {
            null
        }
        return secretKeyEntry?.secretKey ?: generateSecretKey(keystoreAlias)
    }

    /**
     * Генерирует произвольно-сгенерированный вектор инициализации IV
     * IV не является приватным параметром и может храниться в открытом виде!
     * Однако не рекомендуется использовать один и тот же вектор для разных сессий
     */
    fun generateIv(): ByteArray {
        val result = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(result)
        return result
    }

    /**
     * Шифрует текст [data] c помощью секрета из хранилища секретов с псевдонимом [keystoreAlias]
     * и вектора инициализации [iv]
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(keystoreAlias: String, data: String, iv: ByteArray): String {
        val cipher = aesCipher(Cipher.ENCRYPT_MODE, getOrCreateSecretKey(keystoreAlias), iv)
        return encrypt(cipher, data)
    }

    /**
     * Дешифрует текст [data] c помощью секрета из хранилища секретов с псевдонимом [keystoreAlias]
     * и вектора инициализации [iv]
     */
    @Throws(GeneralSecurityException::class)
    fun decrypt(keystoreAlias: String, data: String, iv: ByteArray): String {
        val cipher = aesCipher(Cipher.DECRYPT_MODE, getOrCreateSecretKey(keystoreAlias), iv)
        return decrypt(cipher, data)
    }

    private fun aesCipher(mode: Int, key: Key, iv: ByteArray): Cipher {
        return Cipher.getInstance(AES_MODE).apply {
            init(mode, key, IvParameterSpec(iv))
        }
    }

    private fun encrypt(cipher: Cipher, data: String): String {
        val encrypt = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypt, Base64.NO_WRAP)
    }

    private fun decrypt(cipher: Cipher, data: String): String {
        val decodedBytes = Base64.decode(data, Base64.NO_WRAP)
        val decoded = cipher.doFinal(decodedBytes)
        return String(decoded, Charsets.UTF_8)
    }
}
