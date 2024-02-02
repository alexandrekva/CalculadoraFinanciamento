package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.utils.classes.DecimalValueInputVisualTransformation

@Composable
fun CurrencyOutlinedTextField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    inputValue: String,
    onValueChanged: (String) -> Unit,
    inputState: InputStates,
    supportingText: @Composable (() -> Unit)? = null,
) {
    val currencySymbol = stringResource(id = R.string.default_currency_symbol)
    val currencyIcon: @Composable (() -> Unit) = { Text(text = currencySymbol) }

    DecimalOutlinedTextField(
        modifier = modifier,
        label = label,
        inputValue = inputValue,
        onValueChanged = onValueChanged,
        leadingIcon = currencyIcon,
        decimalValueInputVisualTransformation = DecimalValueInputVisualTransformation(),
        inputState = inputState,
        supportingText = supportingText
    )
}