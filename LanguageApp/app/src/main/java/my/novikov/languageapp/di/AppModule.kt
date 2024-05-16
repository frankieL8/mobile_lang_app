package my.novikov.languageapp.di

import my.novikov.core.storage.api.keyvalue.KeyValueStorageProvider
import my.novikov.core.storage.impl.usercontext.UserContextStorageProvider
import my.novikov.languageapp.route.RouteScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private const val COMMON_KVS_NAME = "cmn"


val AppModule = module {
    viewModel{
        RouteScreenViewModel(onboardingScreenInteractor = get(), userRepository = get())
    }
    /**
     * Общее для всех модулей хранилище [KeyValueStorage], доступное из графа
     * Не очищается при выходе из профиля!
     */
    single { get<KeyValueStorageProvider>()[COMMON_KVS_NAME] }
    /**
     * Общее для всех модулей хранилище [UserContextStorage], доступное из графа
     * Очищается при выходе из профиля!
     */
    single { UserContextStorageProvider.get(get()) }
}