package my.novikov.core.storage.api.secret

/**
 * Провайдер [SecretStorage]
 */
interface SecretsStorageProvider {

    /**
     * Возвращает [SecretStorage] с псевдонимом [alias]
     */
    fun get(alias: String): SecretStorage
}
