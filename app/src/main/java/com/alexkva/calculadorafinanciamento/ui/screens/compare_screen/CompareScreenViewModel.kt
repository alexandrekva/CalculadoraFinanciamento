package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.navigation.NavArg
import com.alexkva.calculadorafinanciamento.navigation.Screens
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompareScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSimulationParametersUseCase: GetSimulationParametersUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _compareState = MutableStateFlow(CompareScreenState())
    val compareState = _compareState.asStateFlow()

    private val _uiEventsState = MutableStateFlow<UiEvent?>(null)
    val uiEventState = _uiEventsState.asStateFlow()

    private var simulationParametersId: SimulationParametersId = Long.MIN_VALUE

    init {
        handleNavArgs(
            savedStateHandle = savedStateHandle, navArgs = Screens.CompareScreen.navArgs
        )
    }

    private fun handleNavArgs(savedStateHandle: SavedStateHandle, navArgs: Array<out NavArg>) {
        navArgs.forEach { navArg ->
            when (navArg) {
                is NavArg.SimulationParametersId -> {
                    savedStateHandle.get<Long>(navArg.key)?.let {
                        simulationParametersId = it
                        getSimulationParameters(simulationParametersId)
                    }
                }
            }
        }
    }

    private fun getSimulationParameters(simulationParametersId: SimulationParametersId) {
        viewModelScope.launch(dispatcher) {
            getSimulationParametersUseCase(simulationParametersId).collect { result ->
                when (result) {
                    is Resource.Loading -> println(result.message)
                    is Resource.Success -> doSimulation(result.data)
                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun doSimulation(simulationParameters: SimulationParameters) {

    }

}