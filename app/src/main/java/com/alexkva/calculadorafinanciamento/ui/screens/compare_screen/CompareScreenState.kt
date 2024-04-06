package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import com.alexkva.calculadorafinanciamento.business.entities.SimulationResult
import java.math.BigDecimal

data class CompareScreenState(
    val isLoading: Boolean = true,
    val termInMonths: Int = 0,
    val amountFinanced: BigDecimal = BigDecimal.ZERO,
    val currentSimulationResult: SimulationResult = SimulationResult(),
    val compareSimulationResult: SimulationResult = SimulationResult()
)