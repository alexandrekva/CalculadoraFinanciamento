package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetLastSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastSimulationParametersUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
): GetLastSimulationParametersUseCase{
    override operator fun invoke(): Flow<Resource<SimulationParameters>> {
        return repository.getLastSimulationParameters()
    }
}