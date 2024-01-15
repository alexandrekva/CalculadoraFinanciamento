package com.alexkva.calculadorafinanciamento.ui.screens

import androidx.lifecycle.ViewModel
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions
import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateDecimalInput
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenState
import com.alexkva.calculadorafinanciamento.ui.models.InputScreenUserEvents
import com.alexkva.calculadorafinanciamento.utils.extensions.formatToNumericString
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
                updateTermOption(_inputState.value.termOption.inverse())
            }

            is InputScreenUserEvents.AmountFinancedChanged -> {
                updateAmountFinanced(userEvent.amountFinanced)
            }

            is InputScreenUserEvents.TermChanged -> {
                val term = userEvent.term.formatToNumericString()
                    .limitedCharacters(inputState.value.termOption.charLimit)
                updateTerm(term)
            }

            is InputScreenUserEvents.AnnualInterestChanged -> {
                updateAnnualInterest(userEvent.annualInterest)
            }

            else -> {}
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

    //it.formatToNumericString()
    //                    .limitedCharacters(inputState.value.termOption.charLimit)
}