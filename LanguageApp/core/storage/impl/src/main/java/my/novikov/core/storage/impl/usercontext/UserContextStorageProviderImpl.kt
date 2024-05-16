package my.novikov.core.storage.impl.usercontext

import my.novikov.core.storage.api.keyvalue.KeyValueStorageProvider
import my.novikov.core.storage.api.usercontext.UserContextStorage

object UserContextStorageProvider{
    fun get(kvsProvider: KeyValueStorageProvider): UserContextStorage {
        return UserContextStorageImpl(kvsProvider)
    }
}