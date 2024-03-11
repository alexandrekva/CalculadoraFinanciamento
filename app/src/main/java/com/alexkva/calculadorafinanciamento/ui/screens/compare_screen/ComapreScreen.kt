package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.ui.components.CompareTable
import com.alexkva.calculadorafinanciamento.ui.components.CustomTopBar
import com.alexkva.calculadorafinanciamento.ui.components.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.models.CompareTableCollection
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme
import com.alexkva.calculadorafinanciamento.utils.classes.SimulationComparator
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedString
import kotlin.math.max

@Composable
fun CompareScreenRoute(
    viewModel: CompareScreenViewModel = hiltViewModel(),
    onNavigationCommand: (NavigationCommand) -> Unit
) {
    val compareState: CompareScreenState by viewModel.compareState.collectAsStateWithLifecycle()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.Navigate -> onNavigationCommand(uiEvent.navigationCommand)
        }
    }

    CompareScreen(compareScreenState = compareState, onUserEvent = viewModel::onUserEvent)
}

@Composable
fun CompareScreen(
    compareScreenState: CompareScreenState, onUserEvent: (CompareScreenUserEvent) -> Unit
) {
    with(compareScreenState) {
        val pairedList = remember(currentSimulationResult, compareSimulationResult) {
            val maxIndex = max(
                currentSimulationResult.monthlyInstallmentCollection.monthlyInstallments.lastIndex,
                compareSimulationResult.monthlyInstallmentCollection.monthlyInstallments.lastIndex
            )

            if (maxIndex > 0) {
                List(maxIndex) {
                    currentSimulationResult.monthlyInstallmentCollection.monthlyInstallments.getOrNull(
                        it
                    ) to compareSimulationResult.monthlyInstallmentCollection.monthlyInstallments.getOrNull(
                        it
                    )
                }
            } else emptyList()
        }

        Scaffold(topBar = {
            CustomTopBar(title = { Text(text = stringResource(id = R.string.compare_label)) },
                leadingIcon = {
                    IconButton(onClick = { onUserEvent(CompareScreenUserEvent.BackButtonClicked) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = null
                        )
                    }
                })
        }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(paddingValues), contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            AmountFinancedBanner(
                                modifier = Modifier.padding(24.dp), stringResource(
                                    id = R.string.financing_with_currency_label,
                                    amountFinanced.toFormattedString()
                                )
                            )
                        }

                        item {
                            CompareTable(
                                modifier = Modifier.padding(24.dp),
                                compareTableCollection = CompareTableCollection(
                                    rowLabelList = mutableListOf<String>().apply {
                                        add(stringResource(id = R.string.total_paid_label))
                                        add(stringResource(id = R.string.total_paid_interests_label))
                                        if (currentSimulationResult.getTotalMonetaryUpdate() != null || compareSimulationResult.getTotalMonetaryUpdate() != null) {
                                            add(stringResource(id = R.string.total_monetary_update_label))
                                        }
                                        add(stringResource(id = R.string.first_installment_label))
                                        add(stringResource(id = R.string.last_installment_label))
                                    },
                                    compareItemCollection = SimulationComparator.buildCompareItemCollection(
                                        currencySymbol = stringResource(
                                            id = R.string.default_currency_symbol,
                                        ), currentSimulationResult, compareSimulationResult
                                    )
                                )
                            )
                        }

                        items(pairedList) {
                            Text(text = "${it.first.toString()} ${it.second.toString()}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AmountFinancedBanner(modifier: Modifier = Modifier, string: String) {
    Text(
        modifier = modifier,
        text = string,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )
}


@Preview
@Composable
private fun PreviewCompareScreen() {
    CalculadoraFinanciamentoTheme {
        CompareScreen(compareScreenState = CompareScreenState(
            isLoading = false
        ), onUserEvent = {})
    }
}