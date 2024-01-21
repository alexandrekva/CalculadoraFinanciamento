package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexkva.calculadorafinanciamento.business.entities.InputStates

@Composable
fun PercentOutlinedTextField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    inputValue: String,
    inputState: InputStates,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    val currencyIcon: @Composable (() -> Unit) = { Text(text = "%") }

    DecimalOutlinedTextField(
        modifier = modifier,
        label = label,
        inputValue = inputValue,
        onValueChanged = onValueChanged,
        leadingIcon = currencyIcon,
        maxLength = 4,
        supportingText = supportingText,
        inputState = inputState
    )
}