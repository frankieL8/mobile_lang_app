package my.novikov.feature.mainscreen.impl.di

import my.novikov.feature.mainscreen.api.MainScreenFeature
import my.novikov.feature.mainscreen.impl.navigation.MainScreenFeatureImpl
import my.novikov.feature.mainscreen.impl.presentation.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainScreenModule = module {
    factory<MainScreenFeature> {
        MainScreenFeatureImpl()
    }

    viewModel {
        MainScreenViewModel(
            userRepository = get()
        )
    }
}

