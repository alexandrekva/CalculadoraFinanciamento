package com.alexkva.calculadorafinanciamento.business.entities

data class SimulationResult(
    val monthlyInstallmentCollection: MonthlyInstallmentCollection = MonthlyInstallmentCollection(
        emptyList()
    )
)
