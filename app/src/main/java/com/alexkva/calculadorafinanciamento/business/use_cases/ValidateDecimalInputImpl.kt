package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.InputStates

class ValidateDecimalInputImpl: ValidateDecimalInput {
    override fun invoke(input: String): InputStates {
        return when {
            input.any { !it.isDigit() } -> InputStates.INVALID_CHARACTERS
            input.isEmpty() -> InputStates.EMPTY
            else -> InputStates.VALID
        }
    }
}