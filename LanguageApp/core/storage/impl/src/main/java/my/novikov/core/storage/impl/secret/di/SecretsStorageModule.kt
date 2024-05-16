package my.novikov.core.storage.impl.secret.di

import my.novikov.core.storage.api.secret.SecretsStorageProvider
import my.novikov.core.storage.impl.secret.SecretsStorageProviderImpl
import org.koin.dsl.module

/**
 * Модуль реализации функционала хранения секретов на устройстве
 */
val SecretsStorageModule = module(createdAtStart = false) {
    factory<SecretsStorageProvider> {
        SecretsStorageProviderImpl(
            keyValueStorageFactory = get()
        )
    }
}
