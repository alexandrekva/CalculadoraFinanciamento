package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.ui.components.CurrencyOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.CustomTopBar
import com.alexkva.calculadorafinanciamento.ui.components.LabeledSwitch
import com.alexkva.calculadorafinanciamento.ui.components.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.components.PercentOutlinedTextField
import com.alexkva.calculadorafinanciamento.ui.components.SegmentedButton
import com.alexkva.calculadorafinanciamento.ui.models.MenuItemCollection
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters
import kotlinx.coroutines.launch

@Composable
fun InputScreenRoute(
    viewModel: InputScreenViewModel = hiltViewModel(),
    onNavigationCommand: (NavigationCommand) -> Unit
) {
    val inputScreenState: InputScreenState by viewModel.inputState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.Navigate -> onNavigationCommand(uiEvent.navigationCommand)
            is UiEvent.ShowSnackbar -> scope.launch {
                when (snackbarHostState.showSnackbar(uiEvent.snackbarVisuals)) {
                    SnackbarResult.ActionPerformed -> uiEvent.onActionPerformed()
                    SnackbarResult.Dismissed -> uiEvent.onDismissed
                }
            }
        }
    }

    InputScreen(
        inputScreenState = inputScreenState,
        onUserEvent = viewModel::onUserEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun InputScreen(
    inputScreenState: InputScreenState,
    snackbarHostState: SnackbarHostState,
    onUserEvent: (InputScreenUserEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val termOptionsCharLimit = inputScreenState.termOption.charLimit
    val scrollState = rememberScrollState()

    with(inputScreenState) {
        Scaffold(snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }, topBar = {
            CustomTopBar(title = { Text(text = stringResource(id = R.string.financing_simulation_label)) },
                trailingIcon = {
                    CustomDropdownMenu(
                        onUserEvent = onUserEvent,
                        isDropdownMenuExpanded = isDropdownMenuExpanded,
                        menuItemCollection = menuItemCollection
                    )
                })
        }) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) { focusManager.clearFocus() }
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SegmentedButton(buttonCollection = segmentedButtons,
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

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
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
                    })

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
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

                LabeledSwitch(label = stringResource(id = R.string.simulate_insurance_label),
                    description = stringResource(id = R.string.simulate_insurance_description),
                    isChecked = hasInsurance,
                    onCheckedChange = { onUserEvent(InputScreenUserEvents.HasInsuranceChanged(it)) })


                LabeledSwitch(label = stringResource(id = R.string.administration_tax_label),
                    description = stringResource(id = R.string.administration_tax_description),
                    isChecked = hasAdministrationTax,
                    onCheckedChange = {
                        onUserEvent(
                            InputScreenUserEvents.HasAdministrationTaxChanged(
                                it
                            )
                        )
                    })

                LabeledSwitch(label = stringResource(id = R.string.simulate_reference_rate_label),
                    description = stringResource(id = R.string.simulate_reference_rate_description),
                    isChecked = hasReferenceRate,
                    onCheckedChange = { onUserEvent(InputScreenUserEvents.HasReferenceRateChanged(it)) })

                AnimatedVisibility(
                    visible = hasOptionalInput(), enter = fadeIn(), exit = fadeOut()
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                AnimatedVisibility(
                    visible = hasInsurance,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    CurrencyOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(id = R.string.insurance_label)) },
                        inputValue = insurance,
                        onValueChanged = { onUserEvent(InputScreenUserEvents.InsuranceChanged(it)) },
                        inputState = insuranceState
                    )
                }

                AnimatedVisibility(
                    visible = hasAdministrationTax,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {

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

                AnimatedVisibility(
                    visible = hasReferenceRate,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    PercentOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(id = R.string.reference_rate_label)) },
                        inputValue = referenceRate,
                        onValueChanged = { onUserEvent(InputScreenUserEvents.ReferenceRateChanged(it)) },
                        inputState = referenceRateState
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(modifier = Modifier.fillMaxWidth(),
                    onClick = { onUserEvent(InputScreenUserEvents.SimulateButtonClicked) }) {
                    Text(text = stringResource(id = R.string.simulate_label))
                }
            }
        }
    }
}

@Composable
private fun CustomDropdownMenu(
    onUserEvent: (InputScreenUserEvents) -> Unit,
    isDropdownMenuExpanded: Boolean,
    menuItemCollection: MenuItemCollection
) {
    Box {
        IconButton(onClick = { onUserEvent(InputScreenUserEvents.DropdownMenuButtonClicked) }) {
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
        }
        DropdownMenu(expanded = isDropdownMenuExpanded,
            onDismissRequest = { onUserEvent(InputScreenUserEvents.DropdownMenuClosed) }) {
            menuItemCollection.menuItems.forEach {
                DropdownMenuItem(label = it.label, onUserEvent = onUserEvent)
            }
        }
    }
}

@Composable
private fun DropdownMenuItem(label: String, onUserEvent: (InputScreenUserEvents) -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 96.dp, height = 42.dp)
            .clickable { onUserEvent(InputScreenUserEvents.LogButtonClicked) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label, style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Default Preview")
@Composable
private fun PreviewInoutScreen() {
    CalculadoraFinanciamentoTheme {
        InputScreen(
            inputScreenState = InputScreenState(),
            onUserEvent = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}


