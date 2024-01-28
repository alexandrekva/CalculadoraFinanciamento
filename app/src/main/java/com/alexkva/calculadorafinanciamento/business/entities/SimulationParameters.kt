package com.alexkva.calculadorafinanciamento.business.entities

import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParameterEntity
import java.math.BigDecimal

data class SimulationParameters(
    val financingType: FinancingTypes,
    val amountFinanced: BigDecimal,
    val annualInterest: BigDecimal,
    val termInMonths: BigDecimal,
    val insurance: BigDecimal? = null,
    val administrationTax: BigDecimal? = null,
    val referenceRate: BigDecimal? = null
) {
    fun toEntity(): SimulationParameterEntity {
        return SimulationParameterEntity(
            financingType = financingType,
            amountFinanced = amountFinanced,
            annualInterest = annualInterest,
            termInMonths = termInMonths,
            insurance = insurance,
            administrationTax = administrationTax,
            referenceRate = referenceRate
        )
    }
}
