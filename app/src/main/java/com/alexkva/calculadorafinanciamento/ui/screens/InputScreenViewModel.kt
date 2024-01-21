package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.lifecycle.ViewModel
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
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
    val validateDecimalInput: ValidateDecimalInput
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

            is InputScreenUserEvents.SimulateButtonClicked -> {
                validateInputFields()
                if (inputState.value.isValidInput()) {
                    val simParams = inputState.value.toSimulationParameters()
                    println(simParams)
                } else {
                    println("Not Valid Input")
                }
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
        _inputState.update {
            it.copy(amountFinanced = amountFinanced, amountFinancedState = InputStates.VALID)
        }
    }

    private fun updateTerm(term: String) {
        _inputState.update { it.copy(term = term, termState = InputStates.VALID) }
    }

    private fun updateAnnualInterest(annualInterest: String) {
        _inputState.update {
            it.copy(
                annualInterest = annualInterest,
                annualInterestState = InputStates.VALID
            )
        }
    }

    private fun updateHasInsurance(hasInsurance: Boolean) {
        _inputState.update {
            it.copy(hasInsurance = hasInsurance)
        }
    }

    private fun updateInsurance(insurance: String) {
        _inputState.update { it.copy(insurance = insurance, insuranceState = InputStates.VALID) }
    }

    private fun updateHasAdministrationTax(hasAdministrationTax: Boolean) {
        _inputState.update {
            it.copy(hasAdministrationTax = hasAdministrationTax)
        }
    }

    private fun updateAdministrationTax(administrationTax: String) {
        _inputState.update {
            it.copy(
                administrationTax = administrationTax,
                administrationTaxState = InputStates.VALID
            )
        }
    }

    private fun updateHasReferenceRate(hasReferenceRate: Boolean) {
        _inputState.update {
            it.copy(hasReferenceRate = hasReferenceRate)
        }
    }

    private fun updateReferenceRate(referenceRate: String) {
        _inputState.update {
            it.copy(
                referenceRate = referenceRate,
                referenceRateState = InputStates.VALID
            )
        }
    }

    private fun validateInputFields() {
        _inputState.update {
            it.run {
                copy(
                    amountFinancedState = validateDecimalInput(amountFinanced),
                    annualInterestState = validateDecimalInput(annualInterest),
                    termState = validateDecimalInput(term),
                    insuranceState = if (hasInsurance) validateDecimalInput(insurance) else insuranceState,
                    administrationTaxState = if (hasAdministrationTax) validateDecimalInput(
                        administrationTax
                    ) else administrationTaxState,
                    referenceRateState = if (hasReferenceRate) validateDecimalInput(referenceRate) else referenceRateState
                )
            }
        }
    }
}