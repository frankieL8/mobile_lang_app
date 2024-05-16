package my.novikov.core.uikit.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun LanguageAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColors()
        else -> lightColors()
    }


    val rippleIndication = androidx.compose.material.ripple.rememberRipple()
    val selectionColors = remember {
        TextSelectionColors(
            handleColor = colorScheme.textDynamic,
            backgroundColor = colorScheme.textDynamic.copy(alpha = .4f),
        )
    }
    CompositionLocalProvider(
        LocalIndication provides rippleIndication,
        LocalTextSelectionColors provides selectionColors,
        LocalAppColors provides colorScheme,
        LocalAppTypography provides Typography,
    ) {
        ProvideTextStyle(value = Typography.titleMedium, content = content)
    }
}

object AppTheme {
    val colors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current

    val typography: Typography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTypography.current
}