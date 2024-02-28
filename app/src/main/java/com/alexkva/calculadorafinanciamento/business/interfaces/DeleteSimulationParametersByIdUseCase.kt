package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface DeleteSimulationParametersByIdUseCase {
    operator fun invoke(simulationParametersId: SimulationParametersId): Flow<Resource<Unit>>
}
