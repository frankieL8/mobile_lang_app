package my.novikov.core.uikit.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = staticCompositionLocalOf { lightColors() }

interface AppColors {
    val purple: Color
    val blue: Color
    val orange: Color
    val green: Color
    val pink: Color
    val sewers: Color
    val creamy: Color
    val sky: Color
    val grey: Color
    val background: Color
    val textStatic: Color
    val textDynamic: Color
}

fun lightColors(): AppColors = object:AppColors {
    override val purple: Color = Color(0xFF3b189f)
    override val blue: Color = Color(0xFF617bf8)
    override val orange: Color = Color(0xFFe67124)
    override val green: Color = Color(0xFF6da591)
    override val pink: Color = Color(0xFFc5395e)
    override val sewers: Color = Color(0xFF4fb1ad)
    override val creamy: Color = Color(0xFFfef6ec)
    override val sky: Color = Color(0xFFe0f5fe)
    override val grey: Color = Color(0xFF656871)
    override val background: Color = Color.White
    override val textStatic: Color = Color.White
    override val textDynamic: Color = Color.Black
}

fun darkColors(): AppColors = object:AppColors {
    override val purple: Color = Color(0xFF3b189f)
    override val blue: Color = Color(0xFF617bf8)
    override val orange: Color = Color(0xFFe67124)
    override val green: Color = Color(0xFF6da591)
    override val pink: Color = Color(0xFFc5395e)
    override val sewers: Color = Color(0xFF4fb1ad)
    override val creamy: Color = Color(0xFFfef6ec)
    override val sky: Color = Color(0xFFe0f5fe)
    override val grey: Color = Color(0xFF656871)
    override val background: Color = Color(0xFF090e1d)
    override val textStatic: Color = Color.White
    override val textDynamic: Color = Color.White
}