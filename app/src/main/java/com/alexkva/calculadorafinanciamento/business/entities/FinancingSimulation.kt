package com.alexkva.calculadorafinanciamento.business.entities

import com.alexkva.calculadorafinanciamento.utils.extensions.annualToMonthlyInterest
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class FinancingSimulation {

    companion object {
        private val defaultMathContext = MathContext.DECIMAL128

        fun simulate(simulationParameters: SimulationParameters): SimulationResult {
            return when (simulationParameters.financingType) {
                FinancingTypes.SAC -> simulateSac(simulationParameters)
                FinancingTypes.PRICE -> simulatePrice(simulationParameters)
            }
        }

        private fun simulateSac(simulationParameters: SimulationParameters): SimulationResult {
            val monthlyInstallmentList = mutableListOf<MonthlyInstallment>()

            with(simulationParameters) {
                val monthlyInterestRate = annualInterest.annualToMonthlyInterest()
                val baseAmortization = getBaseAmortizationValueSac(this)

                var updatedBalance = amountFinanced
                var updatedAmortization = baseAmortization



                for (currentMonth in 1..termInMonths.toInt()) {
                    val monetaryUpdate = getReferenceRateUpdate(
                        value = updatedBalance,
                        referenceRate = referenceRate
                    )
                    val amortizationUpdate = getReferenceRateUpdate(
                        value = updatedAmortization,
                        referenceRate = referenceRate
                    )

                    updatedBalance += monetaryUpdate
                    updatedAmortization += amortizationUpdate

                    val currentInterest =
                        updatedBalance.multiply(monthlyInterestRate, defaultMathContext)

                    updatedBalance -= updatedAmortization

                    monthlyInstallmentList.add(
                        constructRoundedMonthlyInstallmentSac(
                            month = currentMonth,
                            interests = currentInterest,
                            amortization = updatedAmortization,
                            remainingBalance = updatedBalance,
                            monetaryUpdate = monetaryUpdate,
                            insurance = insurance,
                            administrationTax = administrationTax
                        )
                    )
                }
            }

            return SimulationResult(MonthlyInstallmentCollection(monthlyInstallmentList))
        }

        private fun simulatePrice(simulationParameters: SimulationParameters): SimulationResult {
            val monthlyInstallmentList = mutableListOf<MonthlyInstallment>()

            with(simulationParameters) {
                val monthlyInterestRate = annualInterest.annualToMonthlyInterest()

                val baseInstallment = calculatePriceMonthlyInstallment(
                    amountFinanced = amountFinanced,
                    monthlyInterestRate = monthlyInterestRate,
                    termInMonths = termInMonths
                )

                var updatedInstallment = baseInstallment

                var updatedBalance = amountFinanced

                for (currentMonth in 1..termInMonths.toInt()) {
                    val monetaryUpdate = getReferenceRateUpdate(
                        value = updatedBalance,
                        referenceRate = referenceRate
                    )
                    val installmentUpdate = getReferenceRateUpdate(
                        value = updatedInstallment,
                        referenceRate = referenceRate
                    )

                    updatedBalance += monetaryUpdate
                    updatedInstallment += installmentUpdate

                    val currentInterest = updatedBalance.multiply(monthlyInterestRate, defaultMathContext)
                    val currentAmortization = baseInstallment - currentInterest

                    updatedBalance -= currentAmortization

                    monthlyInstallmentList.add(
                        constructRoundedMonthlyInstallmentPrice(
                            month = currentMonth,
                            interests = currentInterest,
                            installment = updatedInstallment,
                            amortization = currentAmortization,
                            remainingBalance = updatedBalance,
                            monetaryUpdate = monetaryUpdate,
                            insurance = insurance,
                            administrationTax = administrationTax
                        )
                    )
                }
            }
            return SimulationResult(MonthlyInstallmentCollection(monthlyInstallmentList))
        }

        private fun calculatePriceMonthlyInstallment(
            amountFinanced: BigDecimal,
            monthlyInterestRate: BigDecimal,
            termInMonths: BigDecimal
        ): BigDecimal {
            val monthlyInterestRatePlus = monthlyInterestRate + BigDecimal.ONE
            val monthlyInterestRatePlusByTerms =
                monthlyInterestRatePlus.pow(termInMonths.toInt(), defaultMathContext)
            val dividend =
                monthlyInterestRatePlusByTerms.multiply(monthlyInterestRate, defaultMathContext)
            val divisor = monthlyInterestRatePlusByTerms - BigDecimal.ONE
            val multiplicand = dividend.divide(divisor, defaultMathContext)

            return amountFinanced.multiply(multiplicand)
        }

        private fun getReferenceRateUpdate(
            value: BigDecimal,
            referenceRate: BigDecimal?
        ): BigDecimal {
            return referenceRate?.let { value.multiply(it, defaultMathContext) } ?: BigDecimal.ZERO
        }

        private fun constructRoundedMonthlyInstallmentPrice(
            month: Int,
            interests: BigDecimal,
            installment: BigDecimal,
            amortization: BigDecimal,
            remainingBalance: BigDecimal,
            monetaryUpdate: BigDecimal,
            insurance: BigDecimal?,
            administrationTax: BigDecimal?
        ): MonthlyInstallment {
            return MonthlyInstallment(
                month = month,
                interests = interests.setScale(2, RoundingMode.HALF_UP),
                installment = installment.setScale(2, RoundingMode.HALF_UP),
                amortization = amortization.setScale(2, RoundingMode.HALF_UP),
                remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP),
                monetaryUpdate = monetaryUpdate.setScale(2, RoundingMode.HALF_UP)
                    ?: BigDecimal.ZERO,
                insurance = insurance?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO,
                administrationTax = administrationTax?.setScale(2, RoundingMode.HALF_UP)
                    ?: BigDecimal.ZERO
            )
        }

        private fun constructRoundedMonthlyInstallmentSac(
            month: Int,
            interests: BigDecimal,
            amortization: BigDecimal,
            remainingBalance: BigDecimal,
            monetaryUpdate: BigDecimal,
            insurance: BigDecimal?,
            administrationTax: BigDecimal?
        ): MonthlyInstallment {
            return MonthlyInstallment(
                month = month,
                interests = interests.setScale(2, RoundingMode.HALF_UP),
                amortization = amortization.setScale(2, RoundingMode.HALF_UP),
                remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP),
                monetaryUpdate = monetaryUpdate.setScale(2, RoundingMode.HALF_UP)
                    ?: BigDecimal.ZERO,
                insurance = insurance?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO,
                administrationTax = administrationTax?.setScale(2, RoundingMode.HALF_UP)
                    ?: BigDecimal.ZERO
            )
        }

        private fun getBaseAmortizationValueSac(simulationParameters: SimulationParameters): BigDecimal {
            with(simulationParameters) {
                return amountFinanced.divide(termInMonths, defaultMathContext)
            }
        }
    }
}