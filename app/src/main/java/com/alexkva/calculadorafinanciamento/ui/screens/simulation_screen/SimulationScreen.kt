package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallment
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallmentCollection
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
                        .padding(paddingValues)
                        .clip(RoundedCornerShape(14.dp, 14.dp, 0.dp, 0.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(24.dp)
                ) {
                    items(monthlyInstallmentCollection.monthlyInstallments) { monthlyInstallment ->
                        with(monthlyInstallment) {
                            Text(text = "Mês ${this.month} / Parcela ${this.installment} / Juros ${this.interests} / Amortização ${this.amortization} /  Saldo ${this.remainingBalance}")
                        }
                    }
                }
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
        totalValueAdjustedByReferenceRate = BigDecimal(90000),
        monthlyInstallmentCollection = MonthlyInstallmentCollection(
            listOf(
                MonthlyInstallment(
                    month = 1,
                    interests = BigDecimal.TEN,
                    amortization = BigDecimal.ONE,
                    monetaryCorrection = BigDecimal.ONE,
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