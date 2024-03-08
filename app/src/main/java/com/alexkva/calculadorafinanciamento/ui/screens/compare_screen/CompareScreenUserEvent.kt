package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen.SimulationScreenUserEvent

sealed class CompareScreenUserEvent {
    data object BackButtonClicked : CompareScreenUserEvent()
}