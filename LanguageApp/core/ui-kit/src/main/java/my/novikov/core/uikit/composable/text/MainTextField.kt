package my.novikov.core.uikit.composable.text

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.novikov.core.uikit.theme.AppTheme
import my.novikov.core.uikit.theme.LanguageAppTheme

@Composable
fun MainTextField(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    uppercase: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {}
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    MainTextField(
        value = textFieldValue,
        modifier = modifier,
        placeholder = placeholder,
        isError = isError,
        isSuccess = isSuccess,
        uppercase = uppercase,
        label = label,
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = { newText ->
            textFieldValueState = newText
            if (value != newText.text) {
                onValueChange(newText.text)
            }
        }
    )
}

@Composable
fun MainTextField(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    uppercase: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit = {}
) {
    val textColor = textColor(enabled, isError, isSuccess).value

    val mergedTextStyle = AppTheme.typography.bodyMedium.merge(
        TextStyle(color = textColor)
    )

    BasicTextField(
        modifier = modifier
            .wrapContentHeight(),
        value = if (uppercase) value.copy(annotatedString = value.annotatedString.toUpperCase()) else value,
        onValueChange = {
            if (it.text.length <= maxLength) {
                onValueChange(
                    it.copy(
                        text = it.text
                    )
                )
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(Color.Black),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        decorationBox = @Composable { innerTextField ->
            val transformedText = remember(value, visualTransformation) {
                visualTransformation.filter(AnnotatedString(value.text))
            }.text.text
            Column() {
                label?.let {
                    Text(
                        text = it,
                        style = AppTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = AppTheme.colors.textDynamic
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFf3f3f4),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    leadingIcon?.invoke()
                    Box (modifier = Modifier.weight(1f)){
                        if (placeholder != null && transformedText.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AppTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth(),
                                color = textColor
                            )
                        } else {
                            innerTextField()
                        }
                    }
                    trailingIcon?.invoke()
                }
            }
        }
    )
}

@Composable
internal fun textColor(
    enabled: Boolean,
    isError: Boolean,
    isSuccess: Boolean,
): State<Color> {
    val targetValue = when {
        !enabled -> AppTheme.colors.grey.copy(alpha = 0.33f)
        isError -> AppTheme.colors.pink
        isSuccess -> AppTheme.colors.sewers
        else -> AppTheme.colors.grey.copy(alpha = 0.5f)
    }
    return rememberUpdatedState(targetValue)
}

@Preview(showBackground = true)
@Composable
private fun MainTextFieldPreview() {
    LanguageAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            MainTextField(
                value = "",
                modifier = Modifier.fillMaxWidth(),
                label = "qweqweqwe",
                placeholder = "qweqweqwe"
            )
        }
    }
}