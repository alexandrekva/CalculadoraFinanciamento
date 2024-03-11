package com.alexkva.calculadorafinanciamento.utils.classes

import com.alexkva.calculadorafinanciamento.business.entities.SimulationResult
import com.alexkva.calculadorafinanciamento.ui.models.CompareItem
import com.alexkva.calculadorafinanciamento.utils.extensions.toFormattedString

class SimulationComparator {
    companion object {
        fun buildCompareItemCollection(
            currencySymbol: String, vararg simulationResults: SimulationResult
        ): List<CompareItem> {
            return simulationResults.map { simulationResult ->
                val values = mutableListOf<String>().apply {
                    add("$currencySymbol ${simulationResult.getTotalPaid().toFormattedString()}")
                    add(
                        "$currencySymbol ${
                            simulationResult.getTotalPaidInInterests().toFormattedString()
                        }"
                    )
                    simulationResult.getTotalMonetaryUpdate()?.let { monetaryUpdate ->
                        add("$currencySymbol ${monetaryUpdate.toFormattedString()}")
                    }

                    simulationResult.monthlyInstallmentCollection.monthlyInstallments.firstOrNull()
                        ?.let {
                            add("$currencySymbol ${it.installment.toFormattedString()}")
                        }

                    simulationResult.monthlyInstallmentCollection.monthlyInstallments.lastOrNull()
                        ?.let {
                            add("$currencySymbol ${it.installment.toFormattedString()}")
                        }
                }

                CompareItem(
                    label = "${simulationResult.financingTypes.label} ${simulationResult.termInMonths}",
                    values = values
                )
            }
        }
    }
}