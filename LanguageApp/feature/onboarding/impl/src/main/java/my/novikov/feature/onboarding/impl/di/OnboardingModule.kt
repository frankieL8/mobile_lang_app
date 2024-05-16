package my.novikov.feature.onboarding.impl.di

import my.novikov.feature.onboarding.api.OnboardingFeature
import my.novikov.feature.onboarding.api.domain.OnboardingInteractor
import my.novikov.feature.onboarding.impl.domain.OnboardingInteractorImpl
import my.novikov.feature.onboarding.impl.navigation.OnboardingFeatureImpl
import my.novikov.feature.onboarding.impl.presentation.OnboardingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val OnboardingModule = module {
    single<OnboardingInteractor> { OnboardingInteractorImpl(kvStorage = get()) }
    factory<OnboardingFeature> { OnboardingFeatureImpl() }
    viewModel {
        OnboardingScreenViewModel(interactor = get())
    }
}