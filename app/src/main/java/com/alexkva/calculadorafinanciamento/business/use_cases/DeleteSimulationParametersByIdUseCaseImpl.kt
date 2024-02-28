package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.interfaces.DeleteSimulationParametersByIdUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteSimulationParametersByIdUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
): DeleteSimulationParametersByIdUseCase{
    override operator fun invoke(simulationParametersId: SimulationParametersId): Flow<Resource<Unit>> {
        return repository.deleteSimulationParametersById(simulationParametersId)
    }
}