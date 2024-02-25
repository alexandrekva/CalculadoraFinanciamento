package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters

data class LogScreenState(
    val isLoading: Boolean = true,
    val simulationParametersList: List<SimulationParameters> = emptyList()
)