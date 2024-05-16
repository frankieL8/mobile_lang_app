package my.novikov.core.storage.api.secret

/**
 * Хранилище секретных строковых значений по ключу
 * Не рекомендуется для хранения данных не имеющих повышенного требования к приватности!
 * Для хранения таких данных используйте [KeyValueStorage]
 */
interface SecretStorage {

    /**
     * Сохраняет данные [value] в зашифрованном виде по ключу [key]
     * Не предъявляет требований к имплементации в части сохранения секретности ключа [key]
     * @param blocking блокировать ли выполнение записи
     */
    fun save(key: String, value: String, blocking: Boolean = false)

    /**
     * Возвращает дешифрованные данные по ключу [key]
     */
    fun load(key: String): String?

    /**
     * Удаляет даныне с ключом [key]
     * @param blocking блокировать ли выполнение записи
     */
    fun remove(key: String, blocking: Boolean = false)
}
