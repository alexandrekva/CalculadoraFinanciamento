package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.InsertSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParameterId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertSimulationParameterUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
): InsertSimulationParametersUseCase {
    override operator fun invoke(simulationParameters: SimulationParameters): Flow<Resource<SimulationParameterId>> {
        return repository.insertSimulationParameter(simulationParameters)
    }
}