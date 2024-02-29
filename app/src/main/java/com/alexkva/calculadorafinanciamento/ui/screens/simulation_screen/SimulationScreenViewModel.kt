package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingSimulation
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.navigation.NavArg
import com.alexkva.calculadorafinanciamento.navigation.Screens
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
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
    savedStateHandle: SavedStateHandle,
    private val getSimulationParametersUseCase: GetSimulationParametersUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _simulationState = MutableStateFlow(SimulationScreenState())
    val simulationState = _simulationState.asStateFlow()

    private val _uiEventsState = MutableStateFlow<UiEvent?>(null)
    val uiEventState = _uiEventsState.asStateFlow()

    init {
        handleNavArgs(
            savedStateHandle = savedStateHandle,
            navArgs = Screens.SimulationScreen.navArgs
        )
    }

    private fun handleNavArgs(savedStateHandle: SavedStateHandle, navArgs: Array<out NavArg>) {
        navArgs.forEach { navArg ->
            when (navArg) {
                is NavArg.SimulationParametersId -> {
                    savedStateHandle.get<Long>(navArg.key)?.let {
                        getSimulationParameters(it)
                    }
                }

                else -> Unit
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
        val simulation = FinancingSimulation.simulate(simulationParameters)

        _simulationState.update { state ->
            with(simulation) {
                state.copy(
                    isLoading = false,
                    financingType = simulationParameters.financingType,
                    amountFinanced = simulationParameters.amountFinanced,
                    termInMonths = simulationParameters.termInMonths,
                    totalPaid = getTotalPaid(),
                    totalPaidInInterests = getTotalPaidInInterests(),
                    totalMonetaryUpdate = getTotalMonetaryUpdate(),
                    monthlyInstallmentCollection = simulation.monthlyInstallmentCollection
                )
            }
        }
    }

    internal fun onUserEvent(userEvent: SimulationScreenUserEvent) {
        when (userEvent) {
            is SimulationScreenUserEvent.BackButtonClicked -> {
                onBackButtonClicked()
            }
        }
    }

    private fun onBackButtonClicked() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(UiEvent.NavigateBack(::onUiEventConsumed))
        }
    }

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }
}