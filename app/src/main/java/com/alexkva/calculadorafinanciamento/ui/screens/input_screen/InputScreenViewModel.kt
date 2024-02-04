package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions
import com.alexkva.calculadorafinanciamento.business.interfaces.InsertSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateDecimalInputUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateTermUseCase
import com.alexkva.calculadorafinanciamento.navigation.Screens
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import com.alexkva.calculadorafinanciamento.utils.classes.Resource
import com.alexkva.calculadorafinanciamento.utils.classes.SegmentedButtonBuilder
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
    private val validateDecimalInputUseCase: ValidateDecimalInputUseCase,
    private val validateTermUseCase: ValidateTermUseCase,
    private val insertSimulationParametersUseCase: InsertSimulationParametersUseCase,
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


    internal fun onUserEvent(userEvent: InputScreenUserEvents) {
        when (userEvent) {
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
        }
    }

    private fun updateSelectedButton(selectedButtonIndex: Int) {
        _inputState.update { it.copy(selectedSegmentedButton = selectedButtonIndex) }
    }

    private fun updateTermOption(termOption: TermOptions) {
        _inputState.update {
            it.copy(
                termOption = termOption,
                term = it.term.limitedCharacters(termOption.charLimit)
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
                annualInterest = annualInterest,
                annualInterestState = InputStates.VALID
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
                administrationTax = administrationTax,
                administrationTaxState = InputStates.VALID
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
                referenceRate = referenceRate,
                referenceRateState = InputStates.VALID
            )
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
                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEventsState.emit(
                        UiEvent.NavigationEvent(
                            Screens.SimulationScreen.withArgs(result.data.toString()),
                            ::onUiEventConsumed
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

    private fun onUiEventConsumed() {
        _uiEventsState.update { null }
    }
}