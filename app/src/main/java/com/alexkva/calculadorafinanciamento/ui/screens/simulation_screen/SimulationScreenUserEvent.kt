package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId

sealed class SimulationScreenUserEvent {
    data object BackButtonClicked : SimulationScreenUserEvent()
}