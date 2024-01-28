package com.alexkva.calculadorafinanciamento.ui.screens.input_screen

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.business.entities.InputStates
import com.alexkva.calculadorafinanciamento.business.entities.SimulationParameters
import com.alexkva.calculadorafinanciamento.business.entities.TermOptions
import com.alexkva.calculadorafinanciamento.utils.extensions.toBigDecimalFromInput
import java.math.BigDecimal

data class InputScreenState(
    val financingType: FinancingTypes = FinancingTypes.SAC,

    val amountFinanced: String = "30000000",
    val amountFinancedState: InputStates = InputStates.VALID,

    val annualInterest: String = "990",
    val annualInterestState: InputStates = InputStates.VALID,

    val term: String = "30",
    val termOption: TermOptions = TermOptions.Years,
    val termState: InputStates = InputStates.VALID,

    val hasInsurance: Boolean = false,
    val insurance: String = "",
    val insuranceState: InputStates = InputStates.VALID,

    val hasAdministrationTax: Boolean = false,
    val administrationTax: String = "",
    val administrationTaxState: InputStates = InputStates.VALID,

    val hasReferenceRate: Boolean = false,
    val referenceRate: String = "",
    val referenceRateState: InputStates = InputStates.VALID
) {
    fun isValidInput(): Boolean {
        val mandatoryFieldsValid = amountFinancedState == InputStates.VALID &&
                annualInterestState == InputStates.VALID &&
                termState == InputStates.VALID

        val insuranceValid = !hasInsurance || (hasInsurance && insuranceState == InputStates.VALID)
        val administrationTaxValid =
            !hasAdministrationTax || (hasAdministrationTax && administrationTaxState == InputStates.VALID)
        val referenceRateValid =
            !hasReferenceRate || (hasReferenceRate && referenceRateState == InputStates.VALID)

        return mandatoryFieldsValid && insuranceValid && administrationTaxValid && referenceRateValid
    }

    fun hasOptionalInput(): Boolean {
        return hasInsurance || hasAdministrationTax || hasReferenceRate
    }

    private fun getTermInMonths(): BigDecimal {
        return when (termOption) {
            is TermOptions.Months -> term.toBigDecimal()
            is TermOptions.Years -> term.toBigDecimal().multiply(BigDecimal(12))
        }
    }

    private fun getInsurance(): BigDecimal? {
        return if (hasInsurance) insurance.toBigDecimalFromInput() else null
    }

    private fun getAdministrationTax(): BigDecimal? {
        return if (hasAdministrationTax) administrationTax.toBigDecimalFromInput() else null
    }

    private fun getReferenceRate(): BigDecimal? {
        return if (hasReferenceRate) referenceRate.toBigDecimalFromInput()
            ?.divide(BigDecimal(100)) else null
    }

    fun toSimulationParameters(): SimulationParameters {
        return SimulationParameters(
            financingType = financingType,
            amountFinanced = amountFinanced.toBigDecimalFromInput() ?: BigDecimal.ZERO,
            annualInterest = annualInterest.toBigDecimalFromInput()?.divide(BigDecimal(100))
                ?: BigDecimal.ZERO,
            termInMonths = getTermInMonths(),
            insurance = getInsurance(),
            administrationTax = getAdministrationTax(),
            referenceRate = getReferenceRate()
        )
    }
}
