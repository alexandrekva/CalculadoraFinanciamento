package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.runtime.Immutable
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters

data class LogItemModel(
    val simulationParameters: SimulationParameters,
    val isVisible: Boolean = true
)

@Immutable
data class LogItemCollection(val logItems: List<LogItemModel> = emptyList()) {
    fun hasVisibleItem(): Boolean {
        return logItems.any { it.isVisible }
    }

    companion object {
        fun fromSimulationParametersList(simulationParametersList: List<SimulationParameters>): LogItemCollection {
            return LogItemCollection(simulationParametersList.map {
                LogItemModel(
                    simulationParameters = it
                )
            })
        }
    }
}