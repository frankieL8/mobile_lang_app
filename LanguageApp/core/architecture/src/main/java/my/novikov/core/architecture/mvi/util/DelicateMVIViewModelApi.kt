package my.novikov.core.architecture.mvi.util

/**
 * Marker annotation for store apis that are not thread-safe
 */
@RequiresOptIn(
    message = """
This API is low-level and ignores ALL validations and thread synchronization. 
Use it for performance-critical operations only.
If you use it, make sure to not introduce races to your state management.
"""
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class DelicateMVIViewModelApi
