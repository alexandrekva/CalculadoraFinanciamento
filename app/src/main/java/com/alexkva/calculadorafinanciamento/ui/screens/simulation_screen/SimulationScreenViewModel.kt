package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingSimulation
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParameterId
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import com.alexkva.calculadorafinanciamento.utils.constants.DECIMAL_FORMAT_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class SimulationScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
        val simulation = FinancingSimulation.simulate(simulationParameters)
        val decimalFormat = DecimalFormat(DECIMAL_FORMAT_PATTERN)

        _simulationState.update { state ->
            with(simulation) {
                state.copy(
                    isLoading = false,
                    financingType = simulationParameters.financingType,
                    termInMonths = simulationParameters.termInMonths,
                    totalPaid = getTotalPaid(),
                    totalPaidInInterests = getTotalPaidInInterests(),
                    totalValueAdjustedByReferenceRate = getTotalValueAdjustedByReferenceRate(),
                    monthlyInstallmentCollection = simulation.monthlyInstallmentCollection
                )
            }
        }
    }
}