package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import com.alexkva.calculadorafinanciamento.business.entities.SimulationResult

data class SimulationScreenState(
    val isLoading: Boolean = true,
    val simulationResult: SimulationResult = SimulationResult()
)