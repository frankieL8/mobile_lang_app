package my.novikov.core.storage.impl.secret.util

import java.security.MessageDigest

/**
 * Утилиты для получения хэш-функций
 */
internal object HashUtils {

    private const val HEX_CHARS = "0123456789ABCDEF"

    fun sha256(input: String) = hashString("SHA-256", input)

    fun sha1(input: String) = hashString("SHA-1", input)

    /**
     * Поддерживаемые алгоритмы на Android:
     * Algorithm	Supported API Levels
     * MD5          1+
     * SHA-1	    1+
     * SHA-256	    1+
     * SHA-384	    1+
     * SHA-512	    1+
     */
    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}
