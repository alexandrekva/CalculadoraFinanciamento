package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions
import com.alexkva.calculadorafinanciamento.business.interfaces.GetLastSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.InsertSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateDecimalInputUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateTermUseCase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId
import com.alexkva.calculadorafinanciamento.navigation.NavArg
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.navigation.Screens
import com.alexkva.calculadorafinanciamento.ui.models.CustomSnackbarVisuals
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import com.alexkva.calculadorafinanciamento.utils.classes.SegmentedButtonBuilder
import com.alexkva.calculadorafinanciamento.utils.extensions.fromDecimalToString
import com.alexkva.calculadorafinanciamento.utils.extensions.fromPercentToString
import com.alexkva.calculadorafinanciamento.utils.extensions.limitedCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.enums.EnumEntries

@HiltViewModel
class InputScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateDecimalInputUseCase: ValidateDecimalInputUseCase,
    private val validateTermUseCase: ValidateTermUseCase,
    private val insertSimulationParametersUseCase: InsertSimulationParametersUseCase,
    private val getLastSimulationParametersUseCase: GetLastSimulationParametersUseCase,
    private val getSimulationParametersUseCase: GetSimulationParametersUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val financingTypes: EnumEntries<FinancingTypes> = FinancingTypes.entries

    private val _inputState = MutableStateFlow(
        InputScreenState(
            segmentedButtons = SegmentedButtonBuilder.buildByFinancingTypes(financingTypes)
        )
    )
    val inputState = _inputState.asStateFlow()

    private val _uiEventsState = MutableStateFlow<UiEvent?>(null)
    val uiEventState = _uiEventsState.asStateFlow()

    private var simulationParametersId: SimulationParametersId? = null

    init {
        handleNavArgs(savedStateHandle = savedStateHandle, navArgs = Screens.InputScreen.navArgs)
    }

    private fun handleNavArgs(savedStateHandle: SavedStateHandle, navArgs: Array<out NavArg>) {
        navArgs.forEach { navArg ->
            when (navArg) {
                is NavArg.SimulationParametersId -> {
                    savedStateHandle.get<Long>(navArg.key)?.let {
                        if (it != navArg.defaultValue) {
                            simulationParametersId = it
                            getSimulationParameters(it)
                        } else {
                            getLastSimulationParameter()
                        }
                    }
                }
            }
        }
    }

    private fun getLastSimulationParameter() {
        viewModelScope.launch(dispatcher) {
            getLastSimulationParametersUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> displayLastSimulationSnackbar(result.data)
                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun getSimulationParameters(
        simulationParametersId: SimulationParametersId
    ) {
        viewModelScope.launch(dispatcher) {
            getSimulationParametersUseCase(simulationParametersId).collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> updateSimulationParameters(result.data)
                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private suspend fun displayLastSimulationSnackbar(simulationParameters: SimulationParameters) {
        _uiEventsState.emit(
            UiEvent.ShowSnackbar(
                snackbarVisuals = CustomSnackbarVisuals(
                    message = "Deseja continuar a última simulação?",
                    actionLabel = "Sim",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                ), onActionPerformed = {
                    onUserEvent(
                        InputScreenUserEvents.LastSimulationClicked(
                            simulationParameters
                        )
                    )
                }, onConsumedAction = ::onUiEventConsumed
            )
        )
    }

    internal fun onUserEvent(userEvent: InputScreenUserEvents) {
        when (userEvent) {
            is InputScreenUserEvents.DropdownMenuButtonClicked -> {
                updateDropdownMenu()
            }

            is InputScreenUserEvents.DropdownMenuClosed -> {
                closeDropdownMenu()
            }

            is InputScreenUserEvents.SegmentedButtonChanged -> {
                updateSelectedButton(userEvent.selectedButtonIndex)
            }

            is InputScreenUserEvents.TermOptionChanged -> {
                updateTermOption(userEvent.termOption)
            }

            is InputScreenUserEvents.AmountFinancedChanged -> {
                updateAmountFinanced(userEvent.amountFinanced)
            }

            is InputScreenUserEvents.TermChanged -> {
                updateTerm(userEvent.term)
            }

            is InputScreenUserEvents.AnnualInterestChanged -> {
                updateAnnualInterest(userEvent.annualInterest)
            }

            is InputScreenUserEvents.HasInsuranceChanged -> {
                updateHasInsurance(userEvent.hasInsurance)
            }

            is InputScreenUserEvents.InsuranceChanged -> {
                updateInsurance(userEvent.insurance)
            }

            is InputScreenUserEvents.HasAdministrationTaxChanged -> {
                updateHasAdministrationTax(userEvent.hasAdministrationTax)
            }

            is InputScreenUserEvents.AdministrationTaxChanged -> {
                updateAdministrationTax(userEvent.administrationTax)
            }

            is InputScreenUserEvents.HasReferenceRateChanged -> {
                updateHasReferenceRate(userEvent.hasReferenceRate)
            }

            is InputScreenUserEvents.ReferenceRateChanged -> {
                updateReferenceRate(userEvent.referenceRate)
            }

            is InputScreenUserEvents.SimulateButtonClicked -> {
                validateInputFields()

                if (inputState.value.isValidInput()) {
                    val simParams = inputState.value.toSimulationParameters(
                        getFinancingTypeBySelectedButton()
                    )
                    insertSimulationParameters(simParams)
                }
            }

            is InputScreenUserEvents.LastSimulationClicked -> {
                updateSimulationParameters(userEvent.simulationParameters)
            }

            is InputScreenUserEvents.LogButtonClicked -> {
                closeDropdownMenu()
                navigateToLog()
            }
        }
    }

    internal fun onLifecycleEvent(lifecycleEvent: Lifecycle.Event) {
        when (lifecycleEvent) {
            Lifecycle.Event.ON_RESUME -> verifySimulationParametersIdArg()
            else -> Unit
        }
    }

    private fun navigateToLog() {
        viewModelScope.launch(dispatcher) {
            _uiEventsState.emit(
                UiEvent.Navigate(
                    NavigationCommand.NavigateTo(
                        route = Screens.LogScreen.getNavigationRoute()
                    ), ::onUiEventConsumed
                )
            )
        }
    }

    private fun updateDropdownMenu() {
        _inputState.update { it.copy(isDropdownMenuExpanded = !it.isDropdownMenuExpanded) }
    }

    private fun closeDropdownMenu() {
        _inputState.update { it.copy(isDropdownMenuExpanded = false) }
    }

    private fun updateSelectedButton(selectedButtonIndex: Int) {
        _inputState.update { it.copy(selectedSegmentedButton = selectedButtonIndex) }
    }

    private fun updateTermOption(termOption: TermOptions) {
        _inputState.update {
            it.copy(
                termOption = termOption, term = it.term.limitedCharacters(termOption.charLimit)
            )
        }
    }

    private fun updateAmountFinanced(amountFinanced: String) {
        _inputState.update {
            it.copy(amountFinanced = amountFinanced, amountFinancedState = InputStates.VALID)
        }
    }

    private fun updateTerm(term: String) {
        _inputState.update { it.copy(term = term, termState = InputStates.VALID) }
    }

    private fun updateAnnualInterest(annualInterest: String) {
        _inputState.update {
            it.copy(
                annualInterest = annualInterest, annualInterestState = InputStates.VALID
            )
        }
    }

    private fun updateHasInsurance(hasInsurance: Boolean) {
        _inputState.update {
            it.copy(hasInsurance = hasInsurance)
        }
    }

    private fun updateInsurance(insurance: String) {
        _inputState.update { it.copy(insurance = insurance, insuranceState = InputStates.VALID) }
    }

    private fun updateHasAdministrationTax(hasAdministrationTax: Boolean) {
        _inputState.update {
            it.copy(hasAdministrationTax = hasAdministrationTax)
        }
    }

    private fun updateAdministrationTax(administrationTax: String) {
        _inputState.update {
            it.copy(
                administrationTax = administrationTax, administrationTaxState = InputStates.VALID
            )
        }
    }

    private fun updateHasReferenceRate(hasReferenceRate: Boolean) {
        _inputState.update {
            it.copy(hasReferenceRate = hasReferenceRate)
        }
    }

    private fun updateReferenceRate(referenceRate: String) {
        _inputState.update {
            it.copy(
                referenceRate = referenceRate, referenceRateState = InputStates.VALID
            )
        }
    }

    private fun updateSimulationParameters(simulationParameters: SimulationParameters) {
        _inputState.update {
            with(simulationParameters) {
                it.copy(
                    selectedSegmentedButton = financingTypes.indexOf(financingType),
                    amountFinanced = amountFinanced.fromDecimalToString(),
                    amountFinancedState = InputStates.VALID,
                    annualInterest = annualInterest.fromPercentToString(),
                    annualInterestState = InputStates.VALID,
                    termOption = TermOptions.Months,
                    term = termInMonths.toString(),
                    termState = InputStates.VALID,
                    hasInsurance = insurance != null,
                    insurance = insurance?.fromDecimalToString() ?: "",
                    hasAdministrationTax = administrationTax != null,
                    administrationTax = administrationTax?.fromDecimalToString() ?: "",
                    hasReferenceRate = referenceRate != null,
                    referenceRate = referenceRate?.fromPercentToString() ?: ""
                )
            }
        }
    }

    private fun validateInputFields() {
        _inputState.update {
            it.run {
                copy(
                    amountFinancedState = validateDecimalInputUseCase(amountFinanced),
                    annualInterestState = validateDecimalInputUseCase(annualInterest),
                    termState = validateTermUseCase(term),
                    insuranceState = if (hasInsurance) validateDecimalInputUseCase(insurance) else insuranceState,
                    administrationTaxState = if (hasAdministrationTax) validateDecimalInputUseCase(
                        administrationTax
                    ) else administrationTaxState,
                    referenceRateState = if (hasReferenceRate) validateDecimalInputUseCase(
                        referenceRate
                    ) else referenceRateState
                )
            }
        }
    }

    private fun insertSimulationParameters(simulationParameters: SimulationParameters) {
        viewModelScope.launch(dispatcher) {
            insertSimulationParametersUseCase(simulationParameters).collect { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> _uiEventsState.emit(
                        UiEvent.Navigate(
                            NavigationCommand.NavigateTo(
                                route = Screens.SimulationScreen.getNavigationRoute(result.data)
                            ), ::onUiEventConsumed
                        )
                    )

                    is Resource.Error -> println(result.message)
                }
            }
        }
    }

    private fun getFinancingTypeBySelectedButton(): FinancingTypes {
        return financingTypes[inputState.value.selectedSegmentedButton]
    }

    private fun verifySimulationParametersIdArg() {

    }

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }
}