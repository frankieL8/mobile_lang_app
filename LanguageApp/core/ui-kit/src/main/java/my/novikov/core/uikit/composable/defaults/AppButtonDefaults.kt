package my.novikov.core.uikit.composable.defaults

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.moex.finuslugi.core.uikit.composable.defaults.ElevationLevels
import my.novikov.core.uikit.theme.AppColors
import my.novikov.core.uikit.theme.AppTheme

object AppButtonDefaults {
    private val AppColors.defaultButtonColors: ButtonColors
        get() {
            return ButtonColors(
                containerColor = blue,
                contentColor = textStatic,
                disabledContainerColor = blue.copy(.50f),
                disabledContentColor = textStatic.copy(.50f)
            )
        }

    /**
     * Creates a [ButtonColors] that represents the default container and content colors used in a
     * [Button].
     */
    @Composable
    fun buttonColors() = AppTheme.colors.defaultButtonColors

    /**
     * Creates a [ButtonColors] that represents the default container and content colors used in a
     * [Button].
     *
     * @param containerColor the container color of this [Button] when enabled.
     * @param contentColor the content color of this [Button] when enabled.
     * @param disabledContainerColor the container color of this [Button] when not enabled.
     * @param disabledContentColor the content color of this [Button] when not enabled.
     */
    @Composable
    fun buttonColors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
    ): ButtonColors = AppTheme.colors.defaultButtonColors.copy(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    /**
     * Creates a [ButtonElevation] that will animate between the provided values according to the
     * Material specification for a [Button].
     *
     * @param defaultElevation the elevation used when the [Button] is enabled, and has no other
     * [Interaction]s.
     * @param pressedElevation the elevation used when this [Button] is enabled and pressed.
     * @param focusedElevation the elevation used when the [Button] is enabled and focused.
     * @param hoveredElevation the elevation used when the [Button] is enabled and hovered.
     * @param disabledElevation the elevation used when the [Button] is not enabled.
     */
    @Composable
    fun buttonElevation(
        defaultElevation: Dp = ElevationLevels.Level1,
        pressedElevation: Dp = ElevationLevels.Level4,
        focusedElevation: Dp = ElevationLevels.Level2,
        hoveredElevation: Dp = ElevationLevels.Level2,
        disabledElevation: Dp = ElevationLevels.Level0,
    ): ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        disabledElevation = disabledElevation,
    )
}