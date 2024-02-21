package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSimulationParametersUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
): GetSimulationParametersUseCase{
    override operator fun invoke(simulationParametersId: SimulationParametersId): Flow<Resource<SimulationParameters>> {
        return repository.getSimulationParametersById(simulationParametersId)
    }
}