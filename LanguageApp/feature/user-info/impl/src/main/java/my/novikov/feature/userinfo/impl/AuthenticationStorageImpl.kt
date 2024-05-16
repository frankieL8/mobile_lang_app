package my.novikov.feature.userinfo.impl

import my.novikov.core.storage.api.secret.SecretStorage
import my.novikov.feature.userinfo.api.AuthenticationStorage

private const val PASSWORD_KEY = "passk"

internal class AuthenticationStorageImpl(
    private val secretStorage: SecretStorage
): AuthenticationStorage {
    override val isAuthenticated: Boolean
        get() = getPassword() != null

    override fun savePassword(password: String) {
        secretStorage.save(PASSWORD_KEY, password, blocking = true)
    }

    override fun clear() {
        secretStorage.remove(PASSWORD_KEY, blocking = true)
    }

    override fun checkPassword(password: String): Boolean = getPassword() == password

    private fun getPassword():String? = secretStorage.load(PASSWORD_KEY)

}