package com.alexkva.calculadorafinanciamento.business.use_cases

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetAllSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSimulationParametersUseCaseImpl @Inject constructor(
    private val repository: SimulationParametersRepository
) : GetAllSimulationParametersUseCase {
    override operator fun invoke(): Flow<Resource<List<SimulationParameters>>> {
        return repository.getAllSimulationParameters()
    }
}