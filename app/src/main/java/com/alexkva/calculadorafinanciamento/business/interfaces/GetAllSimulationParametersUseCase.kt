package com.alexkva.calculadorafinanciamento.business.interfaces

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow

interface GetAllSimulationParametersUseCase {
    operator fun invoke(): Flow<Resource<List<SimulationParameters>>>
}