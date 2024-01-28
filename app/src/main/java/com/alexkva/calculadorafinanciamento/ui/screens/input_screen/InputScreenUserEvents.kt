package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import com.alexkva.calculadorafinanciamento.business.entities.TermOptions

sealed class InputScreenUserEvents {
    data class FinancingTypeChanged(val financingType: String) : InputScreenUserEvents()
    data class AmountFinancedChanged(val amountFinanced: String) : InputScreenUserEvents()
    data class AnnualInterestChanged(val annualInterest: String) : InputScreenUserEvents()
    data class TermChanged(val term: String) : InputScreenUserEvents()
    data class TermOptionChanged(val termOption: TermOptions) : InputScreenUserEvents()
    data class HasInsuranceChanged(val hasInsurance: Boolean) : InputScreenUserEvents()
    data class InsuranceChanged(val insurance: String) : InputScreenUserEvents()
    data class HasAdministrationTaxChanged(val hasAdministrationTax: Boolean) : InputScreenUserEvents()
    data class AdministrationTaxChanged(val administrationTax: String) : InputScreenUserEvents()
    data class HasReferenceRateChanged(val hasReferenceRate: Boolean) : InputScreenUserEvents()
    data class ReferenceRateChanged(val referenceRate: String) : InputScreenUserEvents()
    object SimulateButtonClicked: InputScreenUserEvents()
}