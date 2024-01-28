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

        private fun simulatePrice(simulationParameters: SimulationParameters): SimulationResult {
            val monthlyInstallmentList = mutableListOf<MonthlyInstallment>()

            with(simulationParameters) {
                val monthlyInterestRate = annualInterest.annualToMonthlyInterest()
                val monthlyInterestRatePlus = monthlyInterestRate + BigDecimal.ONE
                val monthlyInterestRatePlusByTerms =
                    monthlyInterestRatePlus.pow(termInMonths.toInt(), defaultMathContext)
                val dividend = monthlyInterestRatePlusByTerms.multiply(monthlyInterestRate, defaultMathContext)
                val divisor = monthlyInterestRatePlusByTerms -  BigDecimal.ONE

                val multiplier = dividend.divide(divisor, defaultMathContext)

                var baseInstallment = amountFinanced.multiply(multiplier, defaultMathContext)

                var remainingBalance = amountFinanced

                for (currentMonth in 1..termInMonths.toInt()) {
                    var monetaryCorrection = BigDecimal.ZERO

                    referenceRate?.let {
                        monetaryCorrection = remainingBalance.multiply(it, defaultMathContext)
                        baseInstallment += baseInstallment.multiply(
                            it,
                            defaultMathContext
                        )
                        remainingBalance += monetaryCorrection
                    }

                    val currentInterest =
                        remainingBalance.multiply(monthlyInterestRate, defaultMathContext)
                    val amortizationValue = baseInstallment - currentInterest

                    remainingBalance -= amortizationValue

                    monthlyInstallmentList.add(
                        MonthlyInstallment(
                            currentMonth,
                            interests = currentInterest.setScale(2, RoundingMode.HALF_UP),
                            amortization = amortizationValue.setScale(2, RoundingMode.HALF_UP),
                            monetaryCorrection = monetaryCorrection.setScale(
                                2,
                                RoundingMode.HALF_UP
                            ),
                            administrationTax = administrationTax ?: BigDecimal.ZERO,
                            insurance = insurance ?: BigDecimal.ZERO,
                            remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP)
                        )
                    )
                }
            }

            return SimulationResult(MonthlyInstallmentCollection(monthlyInstallmentList))
        }

        private fun simulateSac(simulationParameters: SimulationParameters): SimulationResult {
            val monthlyInstallmentList = mutableListOf<MonthlyInstallment>()

            with(simulationParameters) {
                var remainingBalance = amountFinanced
                val monthlyInterestRate = annualInterest.annualToMonthlyInterest()
                var amortizationValue = getBaseAmortizationValueSac(this)

                for (currentMonth in 1..termInMonths.toInt()) {
                    var monetaryCorrection = BigDecimal.ZERO

                    referenceRate?.let {
                        monetaryCorrection = remainingBalance.multiply(it, defaultMathContext)
                        amortizationValue += amortizationValue.multiply(it, defaultMathContext)
                        remainingBalance += monetaryCorrection
                    }

                    val currentInterest =
                        remainingBalance.multiply(monthlyInterestRate, defaultMathContext)

                    remainingBalance -= amortizationValue

                    monthlyInstallmentList.add(
                        MonthlyInstallment(
                            currentMonth,
                            interests = currentInterest.setScale(2, RoundingMode.HALF_UP),
                            amortization = amortizationValue.setScale(2, RoundingMode.HALF_UP),
                            monetaryCorrection = monetaryCorrection.setScale(
                                2,
                                RoundingMode.HALF_UP
                            ),
                            administrationTax = administrationTax ?: BigDecimal.ZERO,
                            insurance = insurance ?: BigDecimal.ZERO,
                            remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP)
                        )
                    )
                }
            }

            return SimulationResult(MonthlyInstallmentCollection(monthlyInstallmentList))
        }

        private fun getBaseAmortizationValueSac(simulationParameters: SimulationParameters): BigDecimal {
            with(simulationParameters) {
                return amountFinanced.divide(termInMonths, defaultMathContext)
            }
        }
    }
}