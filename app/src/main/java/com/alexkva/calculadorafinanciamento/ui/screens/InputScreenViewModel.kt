package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.lifecycle.ViewModel
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions
import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateDecimalInput
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenState
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenUserEvents
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InputScreenViewModel @Inject constructor(
    validateDecimalInput: ValidateDecimalInput
) : ViewModel() {

    private val _inputState = MutableStateFlow(InputScreenState())
    val inputState = _inputState.asStateFlow()

    internal fun onUserEvent(userEvent: InputScreenUserEvents) {
        when (userEvent) {
            is InputScreenUserEvents.FinancingTypeChanged -> {
                updateFinancingType(FinancingTypes.valueOf(userEvent.financingType.uppercase()))
            }

            is InputScreenUserEvents.TermOptionChanged -> {
                updateTermOption(userEvent.termOption)
            }

            is InputScreenUserEvents.AmountFinancedChanged -> {
                updateAmountFinanced(userEvent.amountFinanced)
            }

            is InputScreenUserEvents.TermChanged -> {
                updateTerm(userEvent.term)
            }

            is InputScreenUserEvents.AnnualInterestChanged -> {
                updateAnnualInterest(userEvent.annualInterest)
            }

            is InputScreenUserEvents.HasInsuranceChanged -> {
                updateHasInsurance(userEvent.hasInsurance)
            }

            is InputScreenUserEvents.InsuranceChanged -> {
                updateInsurance(userEvent.insurance)
            }

            is InputScreenUserEvents.HasAdministrationTaxChanged -> {
                updateHasAdministrationTax(userEvent.hasAdministrationTax)
            }

            is InputScreenUserEvents.AdministrationTaxChanged -> {
                updateAdministrationTax(userEvent.administrationTax)
            }

            is InputScreenUserEvents.HasReferenceRateChanged -> {
                updateHasReferenceRate(userEvent.hasReferenceRate)
            }

            is InputScreenUserEvents.ReferenceRateChanged -> {
                updateReferenceRate(userEvent.referenceRate)
            }
        }
    }

    private fun updateFinancingType(financingType: FinancingTypes) {
        _inputState.update { it.copy(financingType = financingType) }
    }

    private fun updateTermOption(termOption: TermOptions) {
        _inputState.update {
            it.copy(
                termOption = termOption,
                term = it.term.limitedCharacters(termOption.charLimit)
            )
        }
    }

    private fun updateAmountFinanced(amountFinanced: String) {
        _inputState.update { it.copy(amountFinanced = amountFinanced) }
    }

    private fun updateTerm(term: String) {
        _inputState.update { it.copy(term = term) }
    }

    private fun updateAnnualInterest(annualInterest: String) {
        _inputState.update { it.copy(annualInterest = annualInterest) }
    }

    private fun updateHasInsurance(hasInsurance: Boolean) {
        _inputState.update {
            if (hasInsurance && it.insurance.isEmpty()) {
                it.copy(hasInsurance = hasInsurance, insurance = "")
            } else {
                it.copy(hasInsurance = hasInsurance)
            }
        }
    }

    private fun updateInsurance(insurance: String) {
        _inputState.update { it.copy(insurance = insurance) }
    }

    private fun updateHasAdministrationTax(hasAdministrationTax: Boolean) {
        _inputState.update {
            if (hasAdministrationTax && it.administrationTax.isEmpty()) {
                it.copy(hasAdministrationTax = hasAdministrationTax, administrationTax = "")
            } else {
                it.copy(hasAdministrationTax = hasAdministrationTax)
            }
        }
    }

    private fun updateAdministrationTax(administrationTax: String) {
        _inputState.update { it.copy(administrationTax = administrationTax) }
    }

    private fun updateHasReferenceRate(hasReferenceRate: Boolean) {
        _inputState.update {
            if (hasReferenceRate && it.referenceRate.isEmpty()) {
                it.copy(hasReferenceRate = hasReferenceRate, referenceRate = "")
            } else {
                it.copy(hasReferenceRate = hasReferenceRate)
            }
        }
    }

    private fun updateReferenceRate(referenceRate: String) {
        _inputState.update { it.copy(referenceRate = referenceRate) }
    }
}