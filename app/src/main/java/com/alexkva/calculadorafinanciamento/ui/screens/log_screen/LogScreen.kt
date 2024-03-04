package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.ui.components.CustomTopBar
import com.alexkva.calculadorafinanciamento.ui.components.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.models.LogItemCollection
import com.alexkva.calculadorafinanciamento.ui.models.LogItemModel
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedString
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedStringFromPercent
import java.math.BigDecimal

@Composable
fun LogScreenRoute(
    viewModel: LogScreenViewModel = hiltViewModel(),
    onNavigationCommand: (NavigationCommand) -> Unit
) {
    val logScreenState: LogScreenState by viewModel.logState.collectAsStateWithLifecycle()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.Navigate -> onNavigationCommand(uiEvent.navigationCommand)
        }
    }

    LogScreen(logScreenState = logScreenState, onUserEvent = viewModel::onUserEvent)
}

@Composable
fun LogScreen(logScreenState: LogScreenState, onUserEvent: (LogScreenUserEvent) -> Unit) {
    with(logScreenState) {
        Scaffold(
            topBar = {
                CustomTopBar(
                    title = { Text(text = stringResource(id = R.string.log_label)) },
                    leadingIcon = {
                        IconButton(onClick = { onUserEvent(LogScreenUserEvent.BackButtonClicked) }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                } else if (!logItemCollection.hasVisibleItem()) {
                    Text(
                        text = stringResource(id = R.string.empty_log_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)

                    ) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                TextButton(
                                    onClick = { onUserEvent(LogScreenUserEvent.DeleteAllLogsButtonClicked) }) {
                                    Text(text = stringResource(id = R.string.clear_label))
                                }
                            }
                        }

                        itemsIndexed(logItemCollection.logItems) { index, logItem ->
                            if (logItem.isVisible) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            LogItem(
                                item = logItem,
                                onUserEvent = onUserEvent
                            )

                            if (index == logItemCollection.logItems.lastIndex && logItem.isVisible) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogItem(
    item: LogItemModel,
    onUserEvent: (LogScreenUserEvent) -> Unit
) {
    AnimatedVisibility(
        visible = item.isVisible,
        enter = fadeIn(),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        with(item.simulationParameters) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(
                                id = R.string.months_label,
                                "${financingType.label} $termInMonths"
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(
                                id = R.string.amount_financed_currency_value,
                                amountFinanced.toFormattedString()
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
                        )

                        Text(
                            text = stringResource(
                                id = R.string.annual_interest_with_value,
                                annualInterest.toFormattedStringFromPercent()
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
                        )

                        insurance?.let {
                            Text(
                                text = stringResource(
                                    id = R.string.insurance_currency_value,
                                    it.toFormattedString()
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
                            )
                        }

                        administrationTax?.let {
                            Text(
                                text = stringResource(
                                    id = R.string.administration_tax_currency_value,
                                    it.toFormattedString()
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
                            )
                        }

                        referenceRate?.let {
                            Text(
                                text = stringResource(
                                    id = R.string.monetary_update_value,
                                    it.toFormattedStringFromPercent()
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.8f)
                            )
                        }
                    }

                    IconButton(onClick = {
                        onUserEvent(
                            LogScreenUserEvent.DeleteLogButtonClicked(
                                simulationParametersId
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        onUserEvent(
                            LogScreenUserEvent.EditButtonClicked(
                                simulationParametersId
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.edit_label))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        onUserEvent(
                            LogScreenUserEvent.SimulateButtonClicked(
                                simulationParametersId
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.simulate_label))
                    }
                }
            }

        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewLogScreen() {
    CalculadoraFinanciamentoTheme {
        LogScreen(
            logScreenState = LogScreenState(
                isLoading = false, logItemCollection = LogItemCollection(
                    listOf(
                        LogItemModel(
                            SimulationParameters(
                                simulationParametersId = 1L,
                                FinancingTypes.SAC,
                                amountFinanced = BigDecimal(100),
                                annualInterest = BigDecimal.TEN,
                                termInMonths = 360
                            )
                        ),
                        LogItemModel(
                            SimulationParameters(
                                simulationParametersId = 2L,
                                FinancingTypes.SAC,
                                amountFinanced = BigDecimal(1000),
                                annualInterest = BigDecimal(11),
                                termInMonths = 300,
                                insurance = BigDecimal(90),
                                administrationTax = BigDecimal(10),
                                referenceRate = BigDecimal(30)
                            )
                        ),
                        LogItemModel(
                            SimulationParameters(
                                simulationParametersId = 3L,
                                FinancingTypes.PRICE,
                                amountFinanced = BigDecimal(56000),
                                annualInterest = BigDecimal(990),
                                termInMonths = 250
                            )
                        )
                    )
                )
            ),
            onUserEvent = {})
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewLogScreenEmpty() {
    CalculadoraFinanciamentoTheme {
        LogScreen(
            logScreenState = LogScreenState(
                isLoading = false, logItemCollection = LogItemCollection(
                    listOf(
                        LogItemModel(
                            simulationParameters = SimulationParameters(
                                simulationParametersId = 3L,
                                FinancingTypes.PRICE,
                                amountFinanced = BigDecimal(56000),
                                annualInterest = BigDecimal(990),
                                termInMonths = 250
                            ), isVisible = false
                        )
                    )
                )
            ),
            onUserEvent = {})
    }
}