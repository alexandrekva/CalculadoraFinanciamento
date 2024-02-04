package com.alexkva.calculadorafinanciamento.business.entities

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

data class MonthlyInstallment(
    val month: Int = 0,
    val interests: BigDecimal = BigDecimal.ZERO,
    val amortization: BigDecimal = BigDecimal.ZERO,
    val monetaryUpdate: BigDecimal? = null,
    val administrationTax: BigDecimal? = null,
    val insurance: BigDecimal? = null,
    val installment: BigDecimal = interests + amortization + (administrationTax
        ?: BigDecimal.ZERO) + (insurance ?: BigDecimal.ZERO),
    val remainingBalance: BigDecimal = BigDecimal.ZERO
) {
    fun isBalanceIncreasing(): Boolean {
        return amortization < monetaryUpdate
    }
}

@Immutable
data class MonthlyInstallmentCollection(val monthlyInstallments: List<MonthlyInstallment> = emptyList())
