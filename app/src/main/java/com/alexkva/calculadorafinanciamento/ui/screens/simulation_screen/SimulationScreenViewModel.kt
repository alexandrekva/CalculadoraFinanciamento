package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingSimulation
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParameterId
import com.alexkva.calculadorafinanciamento.ui.screens.input_screen.InputScreenState
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimulationScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSimulationParametersUseCase: GetSimulationParametersUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _simulationState = MutableStateFlow(SimulationScreenState())
    val simulationState = _simulationState.asStateFlow()


init {
    val simulationParameterId = savedStateHandle.get<String>("simulationId")
    simulationParameterId?.let {
        getSimulationParameters(it.toLong())
    }
}
    private fun getSimulationParameters(simulationParameterId: SimulationParameterId) {
        viewModelScope.launch(dispatcher) {
            getSimulationParametersUseCase(simulationParameterId).collect { result ->
                when (result) {
                    is Resource.Loading -> println(result.message)
                    is Resource.Success -> doSimulation(result.data)
                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun doSimulation(simulationParameters: SimulationParameters) {
        val financingSimulation = FinancingSimulation(simulationParameters)
        val simulationResult = financingSimulation.simulate()

        _simulationState.update { it.copy(isLoading = false, simulationResult = simulationResult) }
    }
}