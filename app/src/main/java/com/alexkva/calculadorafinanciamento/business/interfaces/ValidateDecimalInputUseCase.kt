package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.InputStates

interface ValidateDecimalInputUseCase {
    operator fun invoke(input: String): InputStates
}