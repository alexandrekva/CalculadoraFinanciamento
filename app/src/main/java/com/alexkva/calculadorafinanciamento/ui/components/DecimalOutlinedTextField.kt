package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.utils.classes.DecimalValueInputVisualTransformation
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters

@Composable
fun DecimalOutlinedTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    isError: Boolean = false,
    decimalValueInputVisualTransformation: DecimalValueInputVisualTransformation = DecimalValueInputVisualTransformation(),
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLength: Int = 12,
    onValueChanged: (String) -> Unit
) {
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
        singleLine = true,
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewDecimalOutlinedInputField() {
    Column(modifier = Modifier.padding(24.dp)) {
        var input by remember { mutableStateOf("") }
        DecimalOutlinedTextField(inputValue = input) {
            input = it
        }
    }
}