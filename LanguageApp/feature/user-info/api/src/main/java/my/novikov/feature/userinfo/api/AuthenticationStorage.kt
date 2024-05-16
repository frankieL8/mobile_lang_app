package my.novikov.feature.userinfo.api

interface AuthenticationStorage {
    val isAuthenticated: Boolean
    fun savePassword(password: String)
    fun clear()
    fun checkPassword(password: String): Boolean
}