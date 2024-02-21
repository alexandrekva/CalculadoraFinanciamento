package com.alexkva.calculadorafinanciamento.business.entities

enum class FinancingTypes(val label: String) {
    SAC("SAC"), PRICE("Price");

    fun inverse(): FinancingTypes {
        return when (this) {
            SAC -> PRICE
            PRICE -> SAC
        }
    }
}
