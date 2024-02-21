package com.alexkva.calculadorafinanciamento.data.repositories

import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersDao
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SimulationParametersRepositoryImpl @Inject constructor(
    private val dao: SimulationParametersDao
) : SimulationParametersRepository {

    override fun getSimulationParametersById(targetUid: SimulationParametersId): Flow<Resource<SimulationParameters>> =
        flow {
            emit(Resource.Loading())

            val result = dao.findSimulationParametersById(targetUid)
            result?.let {
                emit(Resource.Success(it.toDomain()))
            } ?: emit(Resource.Error("Nenhum item encontrado"))
        }.catch { e ->
            emit(Resource.Error("Erro ao obter o parâmetro de simulação: ${e.message}"))
        }

    override fun insertSimulationParameters(simulationParameter: SimulationParameters): Flow<Resource<SimulationParametersId>> =
        flow {
            emit(Resource.Loading())

            val result = dao.insertSimulationParameters(simulationParameter.toEntity())
            emit(Resource.Success(result))
        }.catch { e ->
            emit(Resource.Error("Erro ao inserir item no BD: ${e.message}"))
        }

    override fun getLastSimulationParameters(): Flow<Resource<SimulationParameters>> =
        flow {
            emit(Resource.Loading())

            val result = dao.getLastInsertedSimulationParameters()
            result?.let {
                emit(Resource.Success(it.toDomain()))
            } ?: emit(Resource.Error("Nenhum item encontrado"))
        }.catch { e ->
            emit(Resource.Error("Erro o recuperar item do BD: ${e.message}"))
        }
}