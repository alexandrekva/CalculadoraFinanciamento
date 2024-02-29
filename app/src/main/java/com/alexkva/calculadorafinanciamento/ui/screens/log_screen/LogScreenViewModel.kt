package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.DeleteAllSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.DeleteSimulationParametersByIdUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.GetAllSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.navigation.NavArg
import com.alexkva.calculadorafinanciamento.navigation.Screens
import com.alexkva.calculadorafinanciamento.ui.models.LogItemCollection
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
class LogScreenViewModel @Inject constructor(
    private val getAllSimulationParametersUseCase: GetAllSimulationParametersUseCase,
    private val deleteSimulationParametersByIdUseCase: DeleteSimulationParametersByIdUseCase,
    private val deleteAllSimulationParametersUseCase: DeleteAllSimulationParametersUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _logState = MutableStateFlow(
        LogScreenState()
    )
    val logState = _logState.asStateFlow()

    private val _uiEventsState = MutableStateFlow<UiEvent?>(null)
    val uiEventState = _uiEventsState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            getAllSimulationParametersUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        updateLogState(result.data)
                    }

                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    internal fun onUserEvent(userEvent: LogScreenUserEvent) {
        when (userEvent) {
            is LogScreenUserEvent.BackButtonClicked -> {
                onBackButtonClicked()
            }

            is LogScreenUserEvent.DeleteAllLogsButtonClicked -> {
                onDeleteAllLogsButtonClicked()
            }

            is LogScreenUserEvent.DeleteLogButtonClicked -> {
                onDeleteLogButtonClicked(userEvent.simulationParametersId)
            }

            is LogScreenUserEvent.EditButtonClicked -> {
                onEditButtonClicked(userEvent.simulationParametersId)
            }

            is LogScreenUserEvent.SimulateButtonClicked -> {
                onSimulateButtonClicked(userEvent.simulationParametersId)
            }
        }
    }

    private fun onBackButtonClicked() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(UiEvent.NavigateBack(::onUiEventConsumed))
        }
    }

    private fun onDeleteAllLogsButtonClicked() {
        viewModelScope.launch(dispatcher) {
            deleteAllSimulationParametersUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> makeAllItemLogNotVisible()

                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun onDeleteLogButtonClicked(simulationParametersId: SimulationParametersId) {
        viewModelScope.launch(dispatcher) {
            deleteSimulationParametersByIdUseCase(simulationParametersId).collect { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> makeItemLogNotVisible(simulationParametersId)
                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun onEditButtonClicked(simulationParametersId: SimulationParametersId) {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(
                UiEvent.NavigateToRoute(
                    route = Screens.InputScreen.getNavigationRoute(simulationParametersId),
                    ::onUiEventConsumed
                )
            )
        }
    }

    private fun onSimulateButtonClicked(simulationParametersId: SimulationParametersId) {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(
                UiEvent.NavigateToRoute(
                    route = Screens.SimulationScreen.getNavigationRoute(simulationParametersId),
                    ::onUiEventConsumed
                )
            )
        }
    }

    private fun makeItemLogNotVisible(simulationParametersId: SimulationParametersId) {
        _logState.update { logScreenState ->
            logScreenState.copy(logItemCollection = LogItemCollection(
                logScreenState.logItemCollection.logItems.map { logItem ->
                    if (logItem.simulationParameters.simulationParametersId == simulationParametersId) {
                        logItem.copy(isVisible = false)
                    } else {
                        logItem
                    }
                }
            )
            )
        }
    }

    private fun makeAllItemLogNotVisible() {
        _logState.update { logScreenState ->
            logScreenState.copy(logItemCollection = LogItemCollection(
                logScreenState.logItemCollection.logItems.map { logItem ->
                    logItem.copy(isVisible = false)
                }
            )
            )
        }
    }


    private fun updateLogState(simulationParametersList: List<SimulationParameters>) {
        _logState.update {
            it.copy(
                isLoading = false,
                logItemCollection = LogItemCollection.fromSimulationParametersList(
                    simulationParametersList.reversed()
                )
            )
        }
    }

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }


}