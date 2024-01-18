package com.alexkva.calculadorafinanciamento.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun InputScreen(
    inputState: InputScreenState,
    onUserEvent: (InputScreenUserEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val termOptionsCharLimit = inputState.termOption.charLimit

    val buttons = remember {
        SegmentedButtonBuilder.buildByFinancingTypes(
            FinancingTypes.values()
        )
    }

    Scaffold { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
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
                label = { Text(text = stringResource(id = R.string.financed_value_label)) }
            )

            PercentOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                inputValue = inputState.annualInterest,
                onValueChanged = {
                    onUserEvent(InputScreenUserEvents.AnnualInterestChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.annual_interest_label)) }
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
                supportingText = {
                    Text(text = stringResource(id = R.string.term_support_text))
                }
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.more_details),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

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
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
private fun PreviewInoutScreen() {
    InputScreen(inputState = InputScreenState(), onUserEvent = {})
}


