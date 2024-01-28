package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParameterId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface SimulationParametersRepository {
    fun getSimulationParameterById(targetUid: SimulationParameterId): Flow<Resource<SimulationParameters>>
    fun insertSimulationParameter(simulationParameter: SimulationParameters): Flow<Resource<SimulationParameterId>>
}