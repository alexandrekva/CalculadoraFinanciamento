package com.alexkva.calculadorafinanciamento.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParameterEntity

@Dao
interface SimulationParametersDao {
    @Query("SELECT * FROM simulation_parameters WHERE id = :targetUid LIMIT 1")
    fun findSimulationParameterById(targetUid: Int): SimulationParameterEntity?

    @Insert
    fun insertSimulationParameter(simulationParameter: SimulationParameterEntity): Long
}