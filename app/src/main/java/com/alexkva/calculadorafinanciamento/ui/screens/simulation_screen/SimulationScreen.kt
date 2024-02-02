package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallment
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallmentCollection
import com.alexkva.calculadorafinanciamento.ui.components.LabeledInfo
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedString
import java.math.BigDecimal

@Composable
fun SimulationScreenRoute(
    viewModel: SimulationScreenViewModel = hiltViewModel()
) {
    val simulationState: SimulationScreenState by viewModel.simulationState.collectAsStateWithLifecycle()
    SimulationScreen(simulationState)
}

@Composable
private fun SimulationScreen(simulationScreenState: SimulationScreenState) {

    if (simulationScreenState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        with(simulationScreenState) {
            Scaffold { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(paddingValues)

                ) {
                    item {
                        SimulationHeader(
                            financingType = financingType,
                            termInMonths = termInMonths,
                            amountFinanced = amountFinanced,
                            totalPaid = totalPaid,
                            totalPaidInInterests = totalPaidInInterests,
                            totalMonetaryUpdate = totalMonetaryUpdate
                        )
                    }
                    itemsIndexed(monthlyInstallmentCollection.monthlyInstallments) { index, monthlyInstallment ->
                        MonthlyInstallmentItem(monthlyInstallment)
                        if (index < monthlyInstallmentCollection.monthlyInstallments.lastIndex) {
                            Divider(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp), color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulationHeader(
    financingType: FinancingTypes,
    termInMonths: BigDecimal,
    amountFinanced: BigDecimal,
    totalPaid: BigDecimal,
    totalPaidInInterests: BigDecimal,
    totalMonetaryUpdate: BigDecimal?
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${financingType.label} $termInMonths",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        LabeledInfo(
            label = stringResource(id = R.string.financed_value_label),
            info = stringResource(id = R.string.currency_label, amountFinanced.toFormattedString())
        )
        LabeledInfo(
            label = stringResource(id = R.string.total_paid_label),
            info = stringResource(id = R.string.currency_label, totalPaid.toFormattedString())
        )
        LabeledInfo(
            label = stringResource(id = R.string.total_paid_interests_label),
            info = stringResource(
                id = R.string.currency_label,
                totalPaidInInterests.toFormattedString()
            )
        )

        totalMonetaryUpdate?.let {
            LabeledInfo(
                label = stringResource(id = R.string.total_monetary_update_label),
                info = stringResource(id = R.string.currency_label, it.toFormattedString())
            )
        }
    }
}

@Composable
private fun MonthlyInstallmentItem(monthlyInstallment: MonthlyInstallment) {
    with(monthlyInstallment) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.installment_Label, month),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        id = R.string.currency_label,
                        installment.toFormattedString()
                    ), style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.amortization_currency_value, amortization.toFormattedString()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = stringResource(id = R.string.interests_currency_value, interests.toFormattedString()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            monetaryUpdate?.let {
                Text(
                    text = stringResource(id = R.string.monetary_update_currency_value, it.toFormattedString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            insurance?.let {
                Text(
                    text = stringResource(id = R.string.insurance_currency_value, it.toFormattedString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            administrationTax?.let {
                Text(
                    text = stringResource(id = R.string.administration_tax_currency_value, it.toFormattedString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSimulationScreen() {
    val simulationScreenState = SimulationScreenState(
        isLoading = false,
        financingType = FinancingTypes.SAC,
        termInMonths = BigDecimal(360),
        totalPaid = BigDecimal(99000),
        totalPaidInInterests = BigDecimal(600000),
        totalMonetaryUpdate = BigDecimal(90000),
        monthlyInstallmentCollection = MonthlyInstallmentCollection(
            listOf(
                MonthlyInstallment(
                    month = 1,
                    interests = BigDecimal.TEN,
                    amortization = BigDecimal.ONE,
                    monetaryUpdate = BigDecimal.ONE,
                    administrationTax = BigDecimal.ONE,
                    insurance = BigDecimal.ONE,
                    installment = BigDecimal.ONE,
                    remainingBalance = BigDecimal.ONE
                ),
                MonthlyInstallment(
                    month = 1,
                    interests = BigDecimal.TEN,
                    amortization = BigDecimal.ONE,
                    monetaryUpdate = BigDecimal.ONE,
                    administrationTax = BigDecimal.ONE,
                    insurance = BigDecimal.ONE,
                    installment = BigDecimal.ONE,
                    remainingBalance = BigDecimal.ONE
                )
            )
        )
    )

    SimulationScreen(simulationScreenState = simulationScreenState)
}