package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface SimulationParametersRepository {
    fun getAllSimulationParameters(): Flow<Resource<List<SimulationParameters>>>
    fun getSimulationParametersById(targetUid: SimulationParametersId): Flow<Resource<SimulationParameters>>
    fun insertSimulationParameters(simulationParameter: SimulationParameters): Flow<Resource<SimulationParametersId>>
    fun getLastSimulationParameters(): Flow<Resource<SimulationParameters>>
}