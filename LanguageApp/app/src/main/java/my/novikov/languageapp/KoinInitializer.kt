package my.novikov.languageapp

import android.content.Context
import androidx.startup.Initializer
import my.novikov.core.storage.impl.keyvalue.di.KeyValueStorageModule
import my.novikov.core.storage.impl.secret.di.SecretsStorageModule
import my.novikov.core.storage.impl.usercontext.di.UserContextStorageModule
import my.novikov.feature.entrance.impl.di.EntranceModule
import my.novikov.feature.mainscreen.impl.di.MainScreenModule
import my.novikov.feature.onboarding.impl.di.OnboardingModule
import my.novikov.feature.userinfo.impl.di.UserInfoModule
import my.novikov.languageapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer: Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication =
        startKoin {
            androidContext(context)
            modules(
                OnboardingModule,
                KeyValueStorageModule,
                UserContextStorageModule,
                SecretsStorageModule,
                MainScreenModule,
                EntranceModule,
                AppModule,
                UserInfoModule
            )
        }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

}