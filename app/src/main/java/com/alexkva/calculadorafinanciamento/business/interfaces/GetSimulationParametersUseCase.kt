package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParameterId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface GetSimulationParametersUseCase {
    operator fun invoke(simulationParameterId: SimulationParameterId): Flow<Resource<SimulationParameters>>
}
