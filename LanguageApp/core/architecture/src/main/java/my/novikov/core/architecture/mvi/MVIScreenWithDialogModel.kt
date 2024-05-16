package my.novikov.core.architecture.mvi

import my.novikov.core.architecture.mvi.models.DialogStateModel
import my.novikov.core.architecture.mvi.models.MVIConfiguration
import my.novikov.core.architecture.mvi.models.dialogStateModel


@Suppress("UnnecessaryAbstractClass")
abstract class MVIScreenWithDialogModel<E, S, A, D>(
    config: MVIConfiguration<S>
) : MVIScreenModel<E, S, A>(config),
    DialogStateModel<D?> by dialogStateModel()
