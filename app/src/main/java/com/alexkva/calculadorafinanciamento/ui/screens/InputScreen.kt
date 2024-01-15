package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.ui.components.CurrencyOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.PercentOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.SegmentedButton
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenUserEvents
import com.alexkva.calculadorafinanciamento.utils.classes.SegmentedButtonBuilder

@Composable
fun InputScreen(viewModel: InputScreenViewModel = hiltViewModel()) {

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val inputState = viewModel.inputState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        SegmentedButton(
            buttons = SegmentedButtonBuilder.buildByFinancingTypes(FinancingTypes.values()),
            selectedButton = inputState.value.financingType.label,
            onButtonClick = {
                val userEvent = InputScreenUserEvents.FinancingTypeChanged(it)
                viewModel.onUserEvent(userEvent)
            })

        CurrencyOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            inputValue = inputState.value.amountFinanced,
            onValueChanged = {
                val userEvent = InputScreenUserEvents.AmountFinancedChanged(it)
                viewModel.onUserEvent(userEvent)
            },
            onDoneAction = { focusManager.clearFocus() },
            label = { Text(text = "Valor Financiado") }
        )

        PercentOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            inputValue = inputState.value.annualInterest,
            onValueChanged = {
                val userEvent = InputScreenUserEvents.AnnualInterestChanged(it)
                viewModel.onUserEvent(userEvent)
            },
            onDoneAction = { focusManager.clearFocus() },
            label = { Text(text = "Juros Anual") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputState.value.term,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            onValueChange = {
                val userEvent = InputScreenUserEvents.TermChanged(it)
                viewModel.onUserEvent(userEvent)
            },
            label = { Text(text = "Prazo") },
            trailingIcon = {
                TextButton(onClick = {
                    val userEvent = InputScreenUserEvents.TermOptionChanged
                    viewModel.onUserEvent(userEvent)
                }) {
                    Text(text = inputState.value.termOption.label)
                }
            },
            supportingText = {
                Text(text = "Toque no tipo de prazo escolhido para alternar entre anos ou meses ")
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewInoutScreen() {

}

