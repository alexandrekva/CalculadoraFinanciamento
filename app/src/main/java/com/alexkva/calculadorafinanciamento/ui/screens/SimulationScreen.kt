package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SimulationScreenRoute(simulationId: String?) {
    SimulationScreen(simulationId)
}

@Composable
private fun SimulationScreen(string: String?) {
    Text(text = string ?: "")

}