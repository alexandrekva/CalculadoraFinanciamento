package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

sealed class SimulationScreenUserEvent {
    data object BackButtonClicked : SimulationScreenUserEvent()
    data object CompareButtonClicked : SimulationScreenUserEvent()
}