package my.novikov.feature.userinfo.impl.di

import my.novikov.core.storage.api.secret.SecretsStorageProvider
import my.novikov.feature.userinfo.api.AuthenticationStorage
import my.novikov.feature.userinfo.api.UserRepository
import my.novikov.feature.userinfo.impl.AuthenticationStorageImpl
import my.novikov.feature.userinfo.impl.UserRepositoryImpl
import org.koin.dsl.module

private const val AUTH_ALIAS = "auth"

val UserInfoModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            authentication = get(),
            userContextStorage = get()
        )
    }
    single<AuthenticationStorage>{
        AuthenticationStorageImpl(
            secretStorage = get<SecretsStorageProvider>().get(AUTH_ALIAS)
        )
    }
}
