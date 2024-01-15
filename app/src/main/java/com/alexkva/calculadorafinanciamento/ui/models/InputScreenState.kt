package com.alexkva.calculadorafinanciamento.ui.models

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions

data class InputScreenState(
    val financingType: FinancingTypes = FinancingTypes.SAC,
    val amountFinanced: String = "",
    val amountFinancedState: InputStates = InputStates.VALID,
    val annualInterest: String = "",
    val annualInterestState: InputStates = InputStates.VALID,
    val term: String = "",
    val termOption: TermOptions = TermOptions.Years,
    val termState: InputStates = InputStates.VALID
)
