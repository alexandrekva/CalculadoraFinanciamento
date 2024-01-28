package com.alexkva.calculadorafinanciamento.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParameterEntity

typealias SimulationParameterId = Long

@Dao
interface SimulationParametersDao {
    @Query("SELECT * FROM simulation_parameters WHERE id = :simulationParameterId LIMIT 1")
    fun findSimulationParameterById(simulationParameterId: SimulationParameterId): SimulationParameterEntity?

    @Insert
    fun insertSimulationParameter(simulationParameter: SimulationParameterEntity): SimulationParameterId
}