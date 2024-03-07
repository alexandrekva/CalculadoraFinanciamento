package com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingSimulation
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.navigation.NavArg
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
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

    private var simulationParametersId: SimulationParametersId = Long.MIN_VALUE


    init {
        handleNavArgs(
            savedStateHandle = savedStateHandle, navArgs = Screens.SimulationScreen.navArgs
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
                    is Resource.Loading -> setLoading()
                    is Resource.Success -> doSimulation(result.data)
                    is Resource.Error -> Log.e(
                        "Error", "getSimulationParameters: ${result.message}"
                    )
                }
            }
        }
    }

    private fun setLoading() {
        _simulationState.update { state ->
            state.copy(isLoading = true)
        }
    }

    private fun doSimulation(simulationParameters: SimulationParameters) {
        setLoading()

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

            is SimulationScreenUserEvent.CompareButtonClicked -> {
                onCompareButtonClicked()
            }
        }
    }

    private fun onBackButtonClicked() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(
                UiEvent.Navigate(NavigationCommand.NavigateBack, ::onUiEventConsumed)
            )
        }
    }

    private fun onCompareButtonClicked() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(
                UiEvent.Navigate(
                    NavigationCommand.NavigateTo(
                        route = Screens.CompareScreen.getNavigationRoute(
                            simulationParametersId
                        )
                    ), ::onUiEventConsumed
                )
            )
        }
    }

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }
}