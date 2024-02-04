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

                for (currentMonth in 1..termInMonths) {
                    val monetaryUpdate = referenceRate?.let {
                        updatedBalance.multiply(it, defaultMathContext)
                    }

                    val amortizationUpdate = referenceRate?.let {
                        updatedAmortization.multiply(it, defaultMathContext)
                    }

                    monetaryUpdate?.let { updatedBalance += it }
                    amortizationUpdate?.let { updatedAmortization += it }

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

                for (currentMonth in 1..termInMonths) {
                    val monetaryUpdate = referenceRate?.let {
                        updatedBalance.multiply(it, defaultMathContext)
                    }

                    val installmentUpdate = referenceRate?.let {
                        updatedInstallment.multiply(it, defaultMathContext)
                    }

                    monetaryUpdate?.let { updatedBalance += it }
                    installmentUpdate?.let { updatedInstallment += it }

                    val currentInterest =
                        updatedBalance.multiply(monthlyInterestRate, defaultMathContext)
                    val currentAmortization = updatedInstallment - currentInterest

                    updatedBalance -= currentAmortization

                    monthlyInstallmentList.add(
                        constructRoundedMonthlyInstallmentPrice(
                            month = currentMonth,
                            interests = currentInterest,
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
            termInMonths: Int
        ): BigDecimal {

            val numerator = monthlyInterestRate.multiply(amountFinanced, defaultMathContext)

            val denominator =
                BigDecimal.ONE.add(monthlyInterestRate).pow(-termInMonths, defaultMathContext)
                    .subtract(
                        BigDecimal.ONE
                    )

            return -numerator.divide(denominator, defaultMathContext)
        }

        private fun constructRoundedMonthlyInstallmentPrice(
            month: Int,
            interests: BigDecimal,
            amortization: BigDecimal,
            remainingBalance: BigDecimal,
            monetaryUpdate: BigDecimal?,
            insurance: BigDecimal?,
            administrationTax: BigDecimal?
        ): MonthlyInstallment {
            return MonthlyInstallment(
                month = month,
                interests = interests.setScale(2, RoundingMode.HALF_UP),
                amortization = amortization.setScale(2, RoundingMode.HALF_UP),
                remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP),
                monetaryUpdate = monetaryUpdate?.setScale(2, RoundingMode.HALF_UP),
                insurance = insurance?.setScale(2, RoundingMode.HALF_UP),
                administrationTax = administrationTax?.setScale(2, RoundingMode.HALF_UP)
            )
        }

        private fun constructRoundedMonthlyInstallmentSac(
            month: Int,
            interests: BigDecimal,
            amortization: BigDecimal,
            remainingBalance: BigDecimal,
            monetaryUpdate: BigDecimal?,
            insurance: BigDecimal?,
            administrationTax: BigDecimal?
        ): MonthlyInstallment {
            return MonthlyInstallment(
                month = month,
                interests = interests.setScale(2, RoundingMode.HALF_UP),
                amortization = amortization.setScale(2, RoundingMode.HALF_UP),
                remainingBalance = remainingBalance.setScale(2, RoundingMode.HALF_UP),
                monetaryUpdate = monetaryUpdate?.setScale(2, RoundingMode.HALF_UP),
                insurance = insurance?.setScale(2, RoundingMode.HALF_UP),
                administrationTax = administrationTax?.setScale(2, RoundingMode.HALF_UP)
            )
        }

        private fun getBaseAmortizationValueSac(simulationParameters: SimulationParameters): BigDecimal {
            with(simulationParameters) {
                return amountFinanced.divide(BigDecimal(termInMonths), defaultMathContext)
            }
        }
    }
}