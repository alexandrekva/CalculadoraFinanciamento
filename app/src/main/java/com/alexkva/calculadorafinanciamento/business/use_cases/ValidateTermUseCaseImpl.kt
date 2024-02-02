package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateTermUseCase

class ValidateTermUseCaseImpl: ValidateTermUseCase {
    override fun invoke(input: String): InputStates {
        return when {
            input.any { !it.isDigit() } -> InputStates.INVALID_CHARACTERS
            input.isEmpty() -> InputStates.EMPTY
            input.toInt() <= 0 ->InputStates.INVALID_INPUT
            else -> InputStates.VALID
        }
    }
}