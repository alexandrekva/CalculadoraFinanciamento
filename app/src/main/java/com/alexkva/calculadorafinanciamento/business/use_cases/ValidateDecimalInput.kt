package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.InputStates

interface ValidateDecimalInput {
    operator fun invoke(input: String): InputStates
}