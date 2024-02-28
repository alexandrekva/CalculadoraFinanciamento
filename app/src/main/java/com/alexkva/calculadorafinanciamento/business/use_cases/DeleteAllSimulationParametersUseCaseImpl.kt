package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.interfaces.DeleteAllSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAllSimulationParametersUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
): DeleteAllSimulationParametersUseCase{
    override operator fun invoke(): Flow<Resource<Unit>> {
        return repository.deleteAllSimulationParameters()
    }
}