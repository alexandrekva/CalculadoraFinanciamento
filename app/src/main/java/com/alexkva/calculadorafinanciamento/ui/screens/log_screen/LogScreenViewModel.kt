package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.interfaces.GetAllSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
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
                        updateLogState(result.data.reversed())
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
        }
    }

    private fun onBackButtonClicked() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(UiEvent.NavigateBack(::onUiEventConsumed))
        }
    }

    private fun onDeleteAllLogsButtonClicked() {
        //TODO delete log from local data source

        _logState.update {
            it.copy(simulationParametersList = emptyList())
        }
    }

    private fun onDeleteLogButtonClicked(simulationParametersId: SimulationParametersId) {
        //TODO delete log from local data source

        _logState.update {
            it.copy(simulationParametersList = it.simulationParametersList.filterNot { it.simulationParametersId == simulationParametersId })
        }
    }

    private fun updateLogState(simulationParametersList: List<SimulationParameters>) {
        _logState.update {
            it.copy(isLoading = false, simulationParametersList = simulationParametersList)
        }
    }

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }
}