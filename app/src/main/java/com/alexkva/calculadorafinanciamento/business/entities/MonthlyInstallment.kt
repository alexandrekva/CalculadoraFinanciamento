package com.alexkva.calculadorafinanciamento.business.entities

import androidx.compose.runtime.Immutable
import java.math.BigDecimal
import java.math.MathContext

data class MonthlyInstallment(
    val month: Int = 0,
    val interests: BigDecimal = BigDecimal.ZERO,
    val amortization: BigDecimal = BigDecimal.ZERO,
    val monetaryUpdate: BigDecimal = BigDecimal.ZERO,
    val administrationTax: BigDecimal = BigDecimal.ZERO,
    val insurance: BigDecimal = BigDecimal.ZERO,
    val installment: BigDecimal = interests + amortization + administrationTax + insurance,
    val remainingBalance: BigDecimal = BigDecimal.ZERO
) {
    fun getAmortizationInterestsRelation(): BigDecimal {
        return amortization.divide(interests, MathContext.DECIMAL128)
    }

    fun isBalanceIncreasing(): Boolean {
        return amortization < monetaryUpdate
    }
}

@Immutable
data class MonthlyInstallmentCollection(val monthlyInstallments: List<MonthlyInstallment> = emptyList())
