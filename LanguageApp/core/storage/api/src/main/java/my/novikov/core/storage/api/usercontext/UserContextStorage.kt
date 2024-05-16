package my.novikov.core.storage.api.usercontext

import my.novikov.core.storage.api.keyvalue.KeyValueStorage

/**
 * Key-Value хранилище типа [KeyValueStorage], которое связано с текущим пользователем
 * Очищается при разлогине текущего пользователя вызовом метода [clear]
 * Предназначен для хранения флагов и не критичных данных, которые допустимо "обнулить"
 */
interface UserContextStorage : KeyValueStorage {

    /**
     * Очищает все сохраненные данные
     */
    fun clear()
}