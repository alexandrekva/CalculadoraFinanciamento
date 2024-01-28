package com.alexkva.calculadorafinanciamento.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import java.math.BigDecimal
import javax.annotation.Nonnull

@Entity(tableName = "simulation_parameters")
data class SimulationParameterEntity(
    @PrimaryKey(autoGenerate = true)
    @Nonnull
    @ColumnInfo(name = "id") val uid: Long = 0,
    @ColumnInfo(name = "financing_type") val financingType: FinancingTypes,
    @ColumnInfo(name = "amount_financed") val amountFinanced: BigDecimal,
    @ColumnInfo(name = "annual_interest") val annualInterest: BigDecimal,
    @ColumnInfo(name = "term_in_months") val termInMonths: BigDecimal,
    @ColumnInfo(name = "insurance") val insurance: BigDecimal?,
    @ColumnInfo(name = "administration_tax") val administrationTax: BigDecimal?,
    @ColumnInfo(name = "reference_rate") val referenceRate: BigDecimal?
) {
    fun toDomain(): SimulationParameters {
        return SimulationParameters(
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


