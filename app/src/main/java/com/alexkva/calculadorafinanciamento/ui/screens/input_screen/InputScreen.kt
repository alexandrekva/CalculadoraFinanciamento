package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.ui.components.CurrencyOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.LabeledSwitch
import com.alexkva.calculadorafinanciamento.ui.components.PercentOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.SegmentedButton
import com.alexkva.calculadorafinanciamento.ui.models.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters
import kotlinx.coroutines.launch

@Composable
fun InputScreenRoute(
    viewModel: InputScreenViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit
) {
    val inputState: InputScreenState by viewModel.inputState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.NavigationEvent -> onNavigateTo(uiEvent.destination)
            is UiEvent.ShowSnackbar -> scope.launch {
                when (snackbarHostState.showSnackbar(uiEvent.snackbarVisuals)) {
                    SnackbarResult.ActionPerformed -> {
                        viewModel.onUserEvent(InputScreenUserEvents.LastSimulationClicked(uiEvent.simulationParameters))
                    }
                    else -> {}
                }
            }
        }
    }

    InputScreen(
        inputState = inputState,
        onUserEvent = viewModel::onUserEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun InputScreen(
    inputState: InputScreenState,
    snackbarHostState: SnackbarHostState,
    onUserEvent: (InputScreenUserEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val termOptionsCharLimit = inputState.termOption.charLimit
    val scrollState = rememberScrollState()

    with(inputState) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
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
                    buttonCollection = segmentedButtons,
                    selectedButton = selectedSegmentedButton,
                    onButtonClick = {
                        onUserEvent(InputScreenUserEvents.SegmentedButtonChanged(it))
                    })

                CurrencyOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    inputValue = amountFinanced,
                    onValueChanged = {
                        onUserEvent(InputScreenUserEvents.AmountFinancedChanged(it))
                    },
                    label = { Text(text = stringResource(id = R.string.financed_value_label)) },
                    inputState = amountFinancedState
                )

                PercentOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    inputValue = annualInterest,
                    onValueChanged = {
                        onUserEvent(InputScreenUserEvents.AnnualInterestChanged(it))
                    },
                    label = { Text(text = stringResource(id = R.string.annual_interest_label)) },
                    inputState = annualInterestState
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = term,
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
                                    termOption.inverse()
                                )
                            )
                        }) {
                            Text(text = termOption.label)
                        }
                    },
                    isError = termState != InputStates.VALID,
                    supportingText = when (termState) {
                        InputStates.VALID -> {
                            { Text(text = stringResource(id = R.string.term_support_text)) }
                        }

                        InputStates.EMPTY -> {
                            { Text(text = stringResource(id = R.string.empty_input_error_text)) }
                        }

                        InputStates.INVALID_CHARACTERS -> {
                            { Text(text = stringResource(id = R.string.invalid_chars_input_error_text)) }
                        }

                        InputStates.INVALID_INPUT -> {
                            { Text(text = stringResource(id = R.string.term_invalid_input_error_text)) }
                        }
                    }
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurface
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
                    isChecked = hasInsurance,
                    onCheckedChange = { onUserEvent(InputScreenUserEvents.HasInsuranceChanged(it)) })


                LabeledSwitch(
                    label = stringResource(id = R.string.administration_tax_label),
                    description = stringResource(id = R.string.administration_tax_description),
                    isChecked = hasAdministrationTax,
                    onCheckedChange = {
                        onUserEvent(
                            InputScreenUserEvents.HasAdministrationTaxChanged(
                                it
                            )
                        )
                    })

                LabeledSwitch(
                    label = stringResource(id = R.string.simulate_reference_rate_label),
                    description = stringResource(id = R.string.simulate_reference_rate_description),
                    isChecked = hasReferenceRate,
                    onCheckedChange = { onUserEvent(InputScreenUserEvents.HasReferenceRateChanged(it)) })

                AnimatedVisibility(visible = hasOptionalInput()) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurface
                    )
                }

                AnimatedVisibility(visible = hasInsurance) {
                    CurrencyOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(id = R.string.insurance_label)) },
                        inputValue = insurance,
                        onValueChanged = { onUserEvent(InputScreenUserEvents.InsuranceChanged(it)) },
                        inputState = insuranceState
                    )
                }

                AnimatedVisibility(visible = hasAdministrationTax) {

                    CurrencyOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(id = R.string.administration_tax_label)) },
                        inputValue = administrationTax,
                        onValueChanged = {
                            onUserEvent(
                                InputScreenUserEvents.AdministrationTaxChanged(
                                    it
                                )
                            )
                        },
                        inputState = administrationTaxState
                    )
                }

                AnimatedVisibility(visible = hasReferenceRate) {
                    PercentOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(id = R.string.reference_rate_label)) },
                        inputValue = referenceRate,
                        onValueChanged = { onUserEvent(InputScreenUserEvents.ReferenceRateChanged(it)) },
                        inputState = referenceRateState
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
}

@Preview(showSystemUi = true, showBackground = true, name = "Default Preview")
@Composable
private fun PreviewInoutScreen() {
    InputScreen(
        inputState = InputScreenState(),
        onUserEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}


