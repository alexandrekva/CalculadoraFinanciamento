package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions

sealed class InputScreenUserEvents {
    data object DropdownMenuButtonClicked : InputScreenUserEvents()
    data object DropdownMenuClosed : InputScreenUserEvents()
    data object LogButtonClicked : InputScreenUserEvents()
    data class SegmentedButtonChanged(val selectedButtonIndex: Int) : InputScreenUserEvents()
    data class AmountFinancedChanged(val amountFinanced: String) : InputScreenUserEvents()
    data class AnnualInterestChanged(val annualInterest: String) : InputScreenUserEvents()
    data class TermChanged(val term: String) : InputScreenUserEvents()
    data class TermOptionChanged(val termOption: TermOptions) : InputScreenUserEvents()
    data class HasInsuranceChanged(val hasInsurance: Boolean) : InputScreenUserEvents()
    data class InsuranceChanged(val insurance: String) : InputScreenUserEvents()
    data class HasAdministrationTaxChanged(val hasAdministrationTax: Boolean) :
        InputScreenUserEvents()

    data class AdministrationTaxChanged(val administrationTax: String) : InputScreenUserEvents()
    data class HasReferenceRateChanged(val hasReferenceRate: Boolean) : InputScreenUserEvents()
    data class ReferenceRateChanged(val referenceRate: String) : InputScreenUserEvents()
    data object SimulateButtonClicked : InputScreenUserEvents()
    data class LastSimulationClicked(val simulationParameters: SimulationParameters) :
        InputScreenUserEvents()
}