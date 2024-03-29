package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.utils.classes.DecimalValueInputVisualTransformation
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters

@Composable
fun DecimalOutlinedTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    decimalValueInputVisualTransformation: DecimalValueInputVisualTransformation = DecimalValueInputVisualTransformation(),
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    inputState: InputStates,
    maxLength: Int = 12,
    onValueChanged: (String) -> Unit
) {
    val errorText: @Composable (() -> Unit)? = when (inputState) {
        InputStates.VALID -> null
        InputStates.EMPTY -> { { Text(text = stringResource(id = R.string.empty_input_error_text)) } }
        InputStates.INVALID_CHARACTERS -> { { Text(text = stringResource(id = R.string.invalid_chars_input_error_text)) } }
        InputStates.INVALID_INPUT -> { { Text(text = stringResource(id = R.string.invalid_input_error_text)) } }
    }

    OutlinedTextField(
        modifier = modifier,
        value = inputValue,
        onValueChange = {
            onValueChanged(
                it.formatToNumericString()
                    .limitedCharacters(maxLength)
            )
        },
        label = label,
        trailingIcon = trailingIcon,
        leadingIcon = if (inputValue.isNotEmpty()) leadingIcon else null,
        visualTransformation = decimalValueInputVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        isError = inputState != InputStates.VALID,
        supportingText = errorText ?: supportingText
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewDecimalOutlinedInputField() {
    Column(modifier = Modifier.padding(24.dp)) {
        var input by remember { mutableStateOf("") }
        DecimalOutlinedTextField(inputValue = input, inputState = InputStates.VALID) {
            input = it
        }
    }
}