package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface GetSimulationParametersUseCase {
    operator fun invoke(simulationParametersId: SimulationParametersId): Flow<Resource<SimulationParameters>>
}
