package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId

sealed class LogScreenUserEvent {
    data object BackButtonClicked : LogScreenUserEvent()
    data class DeleteLogButtonClicked(val simulationParametersId: SimulationParametersId) :
        LogScreenUserEvent()

    data object DeleteAllLogsButtonClicked : LogScreenUserEvent()
    data class SimulateButtonClicked(val simulationParametersId: SimulationParametersId) : LogScreenUserEvent()
    data class EditButtonClicked(val simulationParametersId: SimulationParametersId) : LogScreenUserEvent()
}