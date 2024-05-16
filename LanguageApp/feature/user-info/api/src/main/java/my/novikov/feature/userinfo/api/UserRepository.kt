package my.novikov.feature.userinfo.api

import my.novikov.feature.userinfo.api.model.UserInfo

interface UserRepository {
    fun getCurrentUser(): UserInfo?
    fun getCurrentUserScore(): Int
    fun saveUser(user: UserInfo)
    val authentication: AuthenticationStorage
    fun clearAll()
}