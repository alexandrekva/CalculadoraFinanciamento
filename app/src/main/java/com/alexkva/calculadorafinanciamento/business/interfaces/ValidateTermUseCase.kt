package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.InputStates

interface ValidateTermUseCase {
    operator fun invoke(input: String): InputStates
}