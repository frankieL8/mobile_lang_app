package my.novikov.core.uikit.composable.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.novikov.core.uikit.composable.defaults.AppButtonDefaults
import my.novikov.core.uikit.theme.AppTheme
import my.novikov.core.uikit.theme.LanguageAppTheme


enum class MainButtonState {
    /**
     * Кнопка кликабельна, отображается текст
     */
    ENABLED,

    /**
     * Кнопка не кликабельна, отображается текст
     *
     */
    DISABLED,

    /**
     * Кнопка не кликабельна, отображается прогресс
     *
     */
    LOADING
}

@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) = MainButton(
    text = text,
    modifier = modifier,
    state = if (enabled) MainButtonState.ENABLED else MainButtonState.DISABLED,
    onClick = onClick
)

@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    state: MainButtonState,
    onClick: () -> Unit
) {
    MainButton(
        modifier = modifier,
        state = state,
        onClick = onClick,
        content = {
            if (state == MainButtonState.LOADING) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                }
            } else {
                val isEnabled = state == MainButtonState.ENABLED

                Text(
                    text = text,
                    color = if (isEnabled) AppButtonDefaults.buttonColors().contentColor else AppButtonDefaults.buttonColors().disabledContentColor,
                    style = AppTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    state: MainButtonState,
    content: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = state == MainButtonState.ENABLED,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.heightIn(min = 56.dp),
        elevation = AppButtonDefaults.buttonElevation(),
        colors = AppButtonDefaults.buttonColors()
    ) {
        content()
    }
}

@Preview
@Composable
private fun MainButtonPreview() {
    LanguageAppTheme{
        MainButton(
            text = "Основная кнопка",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = true
        ) {}
    }
}