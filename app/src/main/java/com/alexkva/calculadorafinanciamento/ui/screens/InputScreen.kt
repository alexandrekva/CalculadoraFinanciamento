package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.ui.components.CurrencyOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.LabeledSwitch
import com.alexkva.calculadorafinanciamento.ui.components.PercentOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.SegmentedButton
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenState
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenUserEvents
import com.alexkva.calculadorafinanciamento.utils.classes.SegmentedButtonBuilder
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters

@Composable
fun InputScreenRoute(viewModel: InputScreenViewModel = hiltViewModel()) {
    val inputState: InputScreenState by viewModel.inputState.collectAsStateWithLifecycle()

    InputScreen(
        inputState = inputState,
        onUserEvent = viewModel::onUserEvent
    )
}

@Composable
private fun InputScreen(
    inputState: InputScreenState,
    onUserEvent: (InputScreenUserEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val termOptionsCharLimit = inputState.termOption.charLimit
    val scrollState = rememberScrollState()

    val buttons = remember {
        SegmentedButtonBuilder.buildByFinancingTypes(
            FinancingTypes.values()
        )
    }

    Scaffold { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus() }
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SegmentedButton(
                buttonCollection = buttons,
                selectedButton = inputState.financingType.label,
                onButtonClick = {
                    onUserEvent(InputScreenUserEvents.FinancingTypeChanged(it))
                })

            CurrencyOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                inputValue = inputState.amountFinanced,
                onValueChanged = {
                    onUserEvent(InputScreenUserEvents.AmountFinancedChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.financed_value_label)) },
                inputState = inputState.amountFinancedState
            )

            PercentOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                inputValue = inputState.annualInterest,
                onValueChanged = {
                    onUserEvent(InputScreenUserEvents.AnnualInterestChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.annual_interest_label)) },
                inputState = inputState.annualInterestState
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputState.term,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                onValueChange = {
                    onUserEvent(
                        InputScreenUserEvents.TermChanged(
                            it.formatToNumericString().limitedCharacters(
                                termOptionsCharLimit
                            )
                        )
                    )
                },
                label = { Text(text = stringResource(id = R.string.term_label)) },
                trailingIcon = {
                    TextButton(onClick = {
                        onUserEvent(
                            InputScreenUserEvents.TermOptionChanged(
                                inputState.termOption.inverse()
                            )
                        )
                    }) {
                        Text(text = inputState.termOption.label)
                    }
                },
                isError = inputState.termState != InputStates.VALID,
                supportingText = when (inputState.termState) {
                    InputStates.VALID -> null
                    InputStates.EMPTY -> { { Text(text = stringResource(id = R.string.empty_input_error_text)) } }
                    InputStates.INVALID_CHARACTERS -> { { Text(text = stringResource(id = R.string.invalid_chars_input_error_text)) } }
                }
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.more_details),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LabeledSwitch(
                label = stringResource(id = R.string.simulate_insurance_label),
                description = stringResource(id = R.string.simulate_insurance_description),
                isChecked = inputState.hasInsurance,
                onCheckedChange = { onUserEvent(InputScreenUserEvents.HasInsuranceChanged(it)) })


            LabeledSwitch(
                label = stringResource(id = R.string.administration_tax_label),
                description = stringResource(id = R.string.administration_tax_description),
                isChecked = inputState.hasAdministrationTax,
                onCheckedChange = { onUserEvent(InputScreenUserEvents.HasAdministrationTaxChanged(it)) })

            LabeledSwitch(
                label = stringResource(id = R.string.simulate_reference_rate_label),
                description = stringResource(id = R.string.simulate_reference_rate_description),
                isChecked = inputState.hasReferenceRate,
                onCheckedChange = { onUserEvent(InputScreenUserEvents.HasReferenceRateChanged(it)) })

            AnimatedVisibility(visible = inputState.hasOptionalInput()) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            AnimatedVisibility(visible = inputState.hasInsurance) {
                CurrencyOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.insurance_label)) },
                    inputValue = inputState.insurance,
                    onValueChanged = { onUserEvent(InputScreenUserEvents.InsuranceChanged(it)) },
                    inputState = inputState.insuranceState
                )
            }

            AnimatedVisibility(visible = inputState.hasAdministrationTax) {

                CurrencyOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.administration_tax_label)) },
                    inputValue = inputState.administrationTax,
                    onValueChanged = { onUserEvent(InputScreenUserEvents.AdministrationTaxChanged(it)) },
                    inputState = inputState.administrationTaxState
                )
            }

            AnimatedVisibility(visible = inputState.hasReferenceRate) {
                PercentOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.reference_rate_label)) },
                    inputValue = inputState.referenceRate,
                    onValueChanged = { onUserEvent(InputScreenUserEvents.ReferenceRateChanged(it)) },
                    inputState = inputState.referenceRateState
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onUserEvent(InputScreenUserEvents.SimulateButtonClicked) }) {
                Text(text = stringResource(id = R.string.simulate_label))
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Default Preview")
@Composable
private fun PreviewInoutScreen() {
    InputScreen(inputState = InputScreenState(), onUserEvent = {})
}


