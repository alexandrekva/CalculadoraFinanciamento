package com.alexkva.calculadorafinanciamento.business.entities

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

data class MonthlyInstallment(
    val month: Int = 0,
    val interests: BigDecimal = BigDecimal.ZERO,
    val amortization: BigDecimal = BigDecimal.ZERO,
    val monetaryCorrection: BigDecimal = BigDecimal.ZERO,
    val administrationTax: BigDecimal = BigDecimal.ZERO,
    val insurance: BigDecimal = BigDecimal.ZERO,
    val installment: BigDecimal = interests + amortization + administrationTax + insurance,
    val remainingBalance: BigDecimal = BigDecimal.ZERO
)

@Immutable
data class MonthlyInstallmentCollection(val monthlyInstallments: List<MonthlyInstallment>) {

}
