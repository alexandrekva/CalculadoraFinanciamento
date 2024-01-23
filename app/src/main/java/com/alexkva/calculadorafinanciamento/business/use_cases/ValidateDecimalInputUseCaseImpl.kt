package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateDecimalInputUseCase

class ValidateDecimalInputUseCaseImpl: ValidateDecimalInputUseCase {
    override fun invoke(input: String): InputStates {
        return when {
            input.any { !it.isDigit() } -> InputStates.INVALID_CHARACTERS
            input.isEmpty() -> InputStates.EMPTY
            else -> InputStates.VALID
        }
    }
}