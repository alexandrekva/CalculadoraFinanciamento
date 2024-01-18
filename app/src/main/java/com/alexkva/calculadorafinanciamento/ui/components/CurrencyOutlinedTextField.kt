package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexkva.calculadorafinanciamento.utils.classes.Currency
import com.alexkva.calculadorafinanciamento.utils.classes.DecimalValueInputVisualTransformation

@Composable
fun CurrencyOutlinedTextField(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    inputValue: String,
    isError: Boolean = false,
    onValueChanged: (String) -> Unit,
    currency: Currency = Currency.BRAZILIAN_REAL
) {
    val currencyIcon: @Composable (() -> Unit) = { Text(text = currency.symbol) }


    DecimalOutlinedTextField(
        modifier = modifier,
        label = label,
        inputValue = inputValue,
        onValueChanged = onValueChanged,
        leadingIcon = currencyIcon,
        decimalValueInputVisualTransformation = DecimalValueInputVisualTransformation(),
        isError = isError
    )
}