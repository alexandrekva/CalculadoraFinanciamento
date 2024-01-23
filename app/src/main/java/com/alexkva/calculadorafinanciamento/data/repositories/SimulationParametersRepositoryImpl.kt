package com.alexkva.calculadorafinanciamento.data.repositories

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersDao
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SimulationParametersRepositoryImpl @Inject constructor(
    private val dao: SimulationParametersDao
) : SimulationParametersRepository {

    override fun getSimulationParameterById(targetUid: Int): Flow<Resource<SimulationParameters>> =
        flow {
            emit(Resource.Loading())

            val result = dao.findSimulationParameterById(targetUid)
            result?.let {
                emit(Resource.Success(it.toDomain()))
            } ?: emit(Resource.Error("Nenhum item encontrado"))
        }.catch { e ->
            emit(Resource.Error("Erro ao obter o parâmetro de simulação: ${e.message}"))
        }

    override fun insertSimulationParameter(simulationParameter: SimulationParameters): Flow<Resource<Long>> =
        flow<Resource<Long>> {
            val result = dao.insertSimulationParameter(simulationParameter.toEntity())
            emit(Resource.Success(result))
        }.catch { e ->
            emit(Resource.Error("Erro ao inserir item no BD: ${e.message}"))
        }
}