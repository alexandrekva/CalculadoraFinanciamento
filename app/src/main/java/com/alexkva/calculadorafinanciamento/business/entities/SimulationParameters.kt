package com.alexkva.calculadorafinanciamento.business.entities

import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParametersEntity
import java.math.BigDecimal

data class SimulationParameters(
    val simulationParametersId: SimulationParametersId = Long.MIN_VALUE,
    val financingType: FinancingTypes,
    val amountFinanced: BigDecimal,
    val annualInterest: BigDecimal,
    val termInMonths: Int,
    val insurance: BigDecimal? = null,
    val administrationTax: BigDecimal? = null,
    val referenceRate: BigDecimal? = null
) {
    fun toEntity(): SimulationParametersEntity {
        return SimulationParametersEntity(
            financingType = financingType,
            amountFinanced = amountFinanced,
            annualInterest = annualInterest,
            termInMonths = termInMonths,
            insurance = insurance,
            administrationTax = administrationTax,
            referenceRate = referenceRate
        )
    }

    fun toFinancingType(financingType: FinancingTypes): SimulationParameters {
        return this.copy(financingType = financingType)
    }
}
