package my.novikov.core.storage.impl.usercontext.di

import my.novikov.core.storage.api.usercontext.UserContextStorage
import my.novikov.core.storage.impl.usercontext.UserContextStorageProvider
import org.koin.dsl.module

/**
 * A module for KeyValueStorage feature
 */
val UserContextStorageModule = module() {
    single<UserContextStorage> {
        UserContextStorageProvider.get(kvsProvider = get())
    }
}