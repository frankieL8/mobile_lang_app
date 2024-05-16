package my.novikov.core.storage.impl.keyvalue.di

import my.novikov.core.storage.api.keyvalue.KeyValueStorageProvider
import my.novikov.core.storage.impl.keyvalue.SharedPrefKeyValueStorageProvider
import org.koin.dsl.module

/**
 * A module for KeyValueStorage feature
 */
val KeyValueStorageModule = module(createdAtStart = false) {
    factory<KeyValueStorageProvider> {
        SharedPrefKeyValueStorageProvider(
            context = get()
        )
    }
}