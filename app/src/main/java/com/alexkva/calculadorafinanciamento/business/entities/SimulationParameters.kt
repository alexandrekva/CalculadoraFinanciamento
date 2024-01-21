package com.alexkva.calculadorafinanciamento.business.entities

import java.math.BigDecimal

data class SimulationParameters(
    val financingType: FinancingTypes,
    val amountFinanced: BigDecimal,
    val annualInterest: BigDecimal,
    val termInMonths: Int,
    val insurance: BigDecimal? = null,
    val administrationTax: BigDecimal? = null,
    val referenceRate: BigDecimal? = null
)
