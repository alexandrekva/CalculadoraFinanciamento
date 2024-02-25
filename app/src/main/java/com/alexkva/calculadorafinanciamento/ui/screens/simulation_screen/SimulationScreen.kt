package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallment
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallmentCollection
import com.alexkva.calculadorafinanciamento.ui.components.CustomTopBar
import com.alexkva.calculadorafinanciamento.ui.components.LabeledInfo
import com.alexkva.calculadorafinanciamento.ui.models.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme
import com.alexkva.calculadorafinanciamento.utils.classes.ColorGenerator
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedString
import java.math.BigDecimal

@Composable
fun SimulationScreenRoute(
    viewModel: SimulationScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val simulationState: SimulationScreenState by viewModel.simulationState.collectAsStateWithLifecycle()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.NavigateBack -> navigateBack()
        }
    }

    SimulationScreen(simulationScreenState = simulationState, onUserEvent = viewModel::onUserEvent)
}

@Composable
private fun SimulationScreen(simulationScreenState: SimulationScreenState, onUserEvent: (SimulationScreenUserEvent) -> Unit) {

    if (simulationScreenState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
    } else {
        with(simulationScreenState) {
            Scaffold(
                topBar = {
                    CustomTopBar(
                        title = { Text(text = stringResource(id = R.string.log_label)) },
                        leadingIcon = {
                            IconButton(onClick = { onUserEvent(SimulationScreenUserEvent.BackButtonClicked) }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = null
                                )
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
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                SimulationHeader(
                                    financingType = financingType,
                                    termInMonths = termInMonths,
                                    amountFinanced = amountFinanced,
                                    totalPaid = totalPaid,
                                    totalPaidInInterests = totalPaidInInterests,
                                    totalMonetaryUpdate = totalMonetaryUpdate
                                )
                            }

                            item {
                                FirstAndLastInstallment(
                                    firstInstallment = monthlyInstallmentCollection.monthlyInstallments.first(),
                                    lastInstallment = monthlyInstallmentCollection.monthlyInstallments.last(),
                                    compareLabel = stringResource(
                                        id = R.string.compare_label,
                                        simulationScreenState.financingType.inverse().label
                                    )
                                )
                            }

                            item {
                                InfoSection()
                            }

                            itemsIndexed(monthlyInstallmentCollection.monthlyInstallments) { index, monthlyInstallment ->
                                MonthlyInstallmentItem(monthlyInstallment)
                                if (index < monthlyInstallmentCollection.monthlyInstallments.lastIndex) {
                                    Divider(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
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
    termInMonths: Int,
    amountFinanced: BigDecimal,
    totalPaid: BigDecimal,
    totalPaidInInterests: BigDecimal,
    totalMonetaryUpdate: BigDecimal?
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${financingType.label} $termInMonths",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        LabeledInfo(
            label = stringResource(id = R.string.financed_value_label),
            info = stringResource(id = R.string.currency_value, amountFinanced.toFormattedString()),
            labelColor = MaterialTheme.colorScheme.secondary,
            infoColor = MaterialTheme.colorScheme.primary
        )
        LabeledInfo(
            label = stringResource(id = R.string.total_paid_label),
            info = stringResource(id = R.string.currency_value, totalPaid.toFormattedString()),
            labelColor = MaterialTheme.colorScheme.secondary,
            infoColor = MaterialTheme.colorScheme.primary
        )
        LabeledInfo(
            label = stringResource(id = R.string.total_paid_interests_label),
            info = stringResource(
                id = R.string.currency_value,
                totalPaidInInterests.toFormattedString()
            ),
            labelColor = MaterialTheme.colorScheme.secondary,
            infoColor = MaterialTheme.colorScheme.primary
        )

        totalMonetaryUpdate?.let {
            LabeledInfo(
                label = stringResource(id = R.string.total_monetary_update_label),
                info = stringResource(id = R.string.currency_value, it.toFormattedString()),
                labelColor = MaterialTheme.colorScheme.secondary,
                infoColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FirstAndLastInstallment(
    firstInstallment: MonthlyInstallment,
    lastInstallment: MonthlyInstallment,
    compareLabel: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            LabeledInfo(
                label = stringResource(id = R.string.first_installment_label),
                info = stringResource(
                    id = R.string.currency_value, firstInstallment.installment.toFormattedString()
                )
            )

            LabeledInfo(
                label = stringResource(id = R.string.last_installment_label),
                info = stringResource(
                    id = R.string.currency_value, lastInstallment.installment.toFormattedString()
                )
            )

            TextButton(modifier = Modifier.align(Alignment.End), onClick = { /*TODO*/ }) {
                Text(text = compareLabel)
            }
        }
    }
}

@Composable
private fun InfoSection() {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.amortization_relation_legend),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(
            text = stringResource(id = R.string.balance_increasing_legend),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}


@Composable
private fun MonthlyInstallmentItem(monthlyInstallment: MonthlyInstallment) {
    with(monthlyInstallment) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.installment_Label, month),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (monthlyInstallment.isBalanceIncreasing()) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(id = R.string.exclamation_point),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    ColorGenerator.generateByAmortizationRelation(
                                        monthlyInstallment.amortizationRelation()
                                    )
                                )
                        )
                    }
                }

                Text(
                    text = stringResource(
                        id = R.string.currency_value,
                        installment.toFormattedString()
                    ), style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = stringResource(
                    id = R.string.amortization_currency_value,
                    amortization.toFormattedString()
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = stringResource(
                    id = R.string.interests_currency_value,
                    interests.toFormattedString()
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            monetaryUpdate?.let {
                Text(
                    text = stringResource(
                        id = R.string.monetary_update_currency_value,
                        it.toFormattedString()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            insurance?.let {
                Text(
                    text = stringResource(
                        id = R.string.insurance_currency_value,
                        it.toFormattedString()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            administrationTax?.let {
                Text(
                    text = stringResource(
                        id = R.string.administration_tax_currency_value,
                        it.toFormattedString()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    id = R.string.remaining_balance_currency_value,
                    remainingBalance.toFormattedString()
                ),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(showBackground = true, wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSimulationScreen() {
    CalculadoraFinanciamentoTheme {
        val simulationScreenState = SimulationScreenState(
            isLoading = false,
            financingType = FinancingTypes.SAC,
            termInMonths = 360,
            totalPaid = BigDecimal(99000),
            totalPaidInInterests = BigDecimal(600000),
            totalMonetaryUpdate = BigDecimal(90000),
            monthlyInstallmentCollection = MonthlyInstallmentCollection(
                listOf(
                    MonthlyInstallment(
                        month = 1,
                        interests = BigDecimal.TEN,
                        amortization = BigDecimal.ONE,
                        monetaryUpdate = BigDecimal(10),
                        administrationTax = BigDecimal.ONE,
                        insurance = BigDecimal.ONE,
                        installment = BigDecimal(100000.50),
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

        SimulationScreen(simulationScreenState = simulationScreenState, onUserEvent = {})
    }
}