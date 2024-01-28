package com.alexkva.calculadorafinanciamento.business.entities

import java.math.BigDecimal

data class SimulationResult(
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

    fun getTotalValueAdjustedByReferenceRate(): BigDecimal {
        return monthlyInstallmentCollection.monthlyInstallments.sumOf { it.monetaryCorrection}
    }

}
