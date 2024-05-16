package my.novikov.feature.onboarding.impl.domain

import my.novikov.core.storage.api.delegates.booleanPreference
import my.novikov.core.storage.api.keyvalue.KeyValueStorage
import my.novikov.feature.onboarding.api.domain.OnboardingInteractor

private const val COMPLETED_ONBOARDING = "cplonb"

internal class OnboardingInteractorImpl(kvStorage: KeyValueStorage): OnboardingInteractor {
    override var isOnboardingCompleted: Boolean by kvStorage.booleanPreference(COMPLETED_ONBOARDING)
}