package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
        LazyColumn {
            items(simulationScreenState.simulationResult.monthlyInstallmentCollection.monthlyInstallments) { monthlyInstallment ->
                Text(text = monthlyInstallment.toString())
            }
        }
    }
}