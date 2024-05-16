package my.novikov.core.storage.impl.secret

import my.novikov.core.storage.api.keyvalue.KeyValueStorageProvider
import my.novikov.core.storage.api.secret.SecretStorage
import my.novikov.core.storage.api.secret.SecretsStorageProvider

internal class SecretsStorageProviderImpl(
    private val keyValueStorageFactory: KeyValueStorageProvider
) : SecretsStorageProvider {

    override fun get(alias: String): SecretStorage {
        return SecretStorageImpl(
            alias = alias,
            keyValueStorageFactory["alias$SHARED_PREF_KEY_SUFFIX"]
        )
    }

    private companion object {
        private const val SHARED_PREF_KEY_SUFFIX = "_sp"
    }
}
