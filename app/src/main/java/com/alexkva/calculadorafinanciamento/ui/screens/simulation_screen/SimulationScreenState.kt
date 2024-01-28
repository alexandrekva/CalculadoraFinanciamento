package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallmentCollection
import java.math.BigDecimal

data class SimulationScreenState(
    val isLoading: Boolean = true,
    val termInMonths: BigDecimal = BigDecimal.ZERO,
    val financingType: FinancingTypes = FinancingTypes.SAC,
    val totalPaid: BigDecimal = BigDecimal.ZERO,
    val totalPaidInInterests: BigDecimal = BigDecimal.ZERO,
    val totalValueAdjustedByReferenceRate: BigDecimal = BigDecimal.ZERO,
    val monthlyInstallmentCollection: MonthlyInstallmentCollection = MonthlyInstallmentCollection()
)