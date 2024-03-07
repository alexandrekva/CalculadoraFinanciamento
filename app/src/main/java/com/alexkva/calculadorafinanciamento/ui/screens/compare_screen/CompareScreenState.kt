package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import com.alexkva.calculadorafinanciamento.business.entities.MonthlyInstallmentCollection
import java.math.BigDecimal

data class CompareScreenState(
    val isLoading: Boolean = true,
    val termInMonths: Int = 0,
    val amountFinanced: BigDecimal = BigDecimal.ZERO,

    val totalPaidCurrentSimulation: BigDecimal = BigDecimal.ZERO,
    val totalPaidInInterestsCurrentSimulation: BigDecimal = BigDecimal.ZERO,
    val totalMonetaryUpdateCurrentSimulation: BigDecimal? = null,
    val monthlyInstallmentCollectionCurrentSimulation: MonthlyInstallmentCollection = MonthlyInstallmentCollection(),

    val totalPaidCompareSimulation: BigDecimal = BigDecimal.ZERO,
    val totalPaidInInterestsCompareSimulation: BigDecimal = BigDecimal.ZERO,
    val totalMonetaryUpdateCompareSimulation: BigDecimal? = null,
    val monthlyInstallmentCollectionCompareSimulation: MonthlyInstallmentCollection = MonthlyInstallmentCollection()
)