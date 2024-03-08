package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

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
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> doSimulation(result.data)
                    is Resource.Error -> Log.e(
                        "Error",
                        "getSimulationParameters: ${result.message}",
                    )
                }
            }
        }
    }

    fun onUserEvent(userEvent: CompareScreenUserEvent) {
        when (userEvent) {
            is CompareScreenUserEvent.BackButtonClicked -> onBackButtonClicked()
        }
    }

    private fun onLoading() {
        _compareState.update { it.copy(isLoading = true) }
    }

    private fun onBackButtonClicked() {
        _uiEventsState.update {
            UiEvent.Navigate(
                NavigationCommand.NavigateBack, onConsumedAction = ::onConsumedAction
            )
        }
    }

    private fun doSimulation(simulationParameters: SimulationParameters) {
        viewModelScope.launch(dispatcher) {
            val compareSimulationParameters =
                simulationParameters.toFinancingType(simulationParameters.financingType.inverse())

            val currentSimulation = FinancingSimulation.simulate(simulationParameters)
            val compareSimulation = FinancingSimulation.simulate(compareSimulationParameters)

            _compareState.update {
                it.copy(
                    isLoading = false,
                    termInMonths = simulationParameters.termInMonths,
                    amountFinanced = simulationParameters.amountFinanced,
                    totalPaidCurrentSimulation = currentSimulation.getTotalPaid(),
                    totalPaidCompareSimulation = compareSimulation.getTotalPaid(),
                    totalPaidInInterestsCurrentSimulation = currentSimulation.getTotalPaidInInterests(),
                    totalPaidInInterestsCompareSimulation = compareSimulation.getTotalPaidInInterests(),
                    totalMonetaryUpdateCompareSimulation = currentSimulation.getTotalMonetaryUpdate(),
                    totalMonetaryUpdateCurrentSimulation = compareSimulation.getTotalMonetaryUpdate(),
                    monthlyInstallmentCollectionCurrentSimulation = currentSimulation.monthlyInstallmentCollection,
                    monthlyInstallmentCollectionCompareSimulation = compareSimulation.monthlyInstallmentCollection
                )
            }
        }
    }

    private fun onConsumedAction() {
        _uiEventsState.update { null }
    }

}