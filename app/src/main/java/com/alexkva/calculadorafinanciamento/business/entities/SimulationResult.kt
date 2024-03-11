package com.alexkva.calculadorafinanciamento.business.entities

import java.math.BigDecimal

data class SimulationResult(
    val financingTypes: FinancingTypes = FinancingTypes.SAC,
    val termInMonths: Int = 0,
    val monthlyInstallmentCollection: MonthlyInstallmentCollection = MonthlyInstallmentCollection(
        emptyList()
    )
) {

    fun getTotalPaid(): BigDecimal {
        return monthlyInstallmentCollection.monthlyInstallments.sumOf { it.installment }
    }

    fun getTotalPaidInInterests(): BigDecimal {
        return monthlyInstallmentCollection.monthlyInstallments.sumOf { it.interests }
    }

    fun getTotalMonetaryUpdate(): BigDecimal? {
        return monthlyInstallmentCollection.monthlyInstallments.sumOf {
            it.monetaryUpdate ?: return null
        }
    }

}
