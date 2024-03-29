package com.alexkva.calculadorafinanciamento.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParametersEntity

typealias SimulationParametersId = Long

@Dao
interface SimulationParametersDao {

    @Query("SELECT * FROM simulation_parameters")
    fun getAllSimulationParameters(): List<SimulationParametersEntity>

    @Query("SELECT * FROM simulation_parameters WHERE id = :simulationParametersId LIMIT 1")
    fun findSimulationParametersById(simulationParametersId: SimulationParametersId): SimulationParametersEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSimulationParameters(simulationParameter: SimulationParametersEntity): SimulationParametersId

    @Query("SELECT * FROM simulation_parameters ORDER BY id DESC LIMIT 1")
    fun getLastInsertedSimulationParameters(): SimulationParametersEntity?

    @Query("DELETE FROM simulation_parameters WHERE id = :simulationParametersId")
    fun deleteSimulationParametersById(simulationParametersId: SimulationParametersId)

    @Query("DELETE FROM simulation_parameters")
    fun deleteAllSimulationParameters()

}