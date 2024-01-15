package com.alexkva.calculadorafinanciamento.ui.models

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions

sealed class InputScreenUserEvents {
    data class FinancingTypeChanged(val financingType: String) : InputScreenUserEvents()
    data class AmountFinancedChanged(val amountFinanced: String) : InputScreenUserEvents()
    data class AnnualInterestChanged(val annualInterest: String) : InputScreenUserEvents()
    data class TermChanged(val term: String) : InputScreenUserEvents()
    object TermOptionChanged : InputScreenUserEvents()
}