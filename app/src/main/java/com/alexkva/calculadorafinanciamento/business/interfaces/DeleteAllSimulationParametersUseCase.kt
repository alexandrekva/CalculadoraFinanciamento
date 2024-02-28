package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface DeleteAllSimulationParametersUseCase {
    operator fun invoke(): Flow<Resource<Unit>>
}
