package com.alexkva.calculadorafinanciamento.utils.classes

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonModel

class SegmentedButtonBuilder {
    companion object {
        fun buildByFinancingTypes(financingTypes: Array<FinancingTypes>): List<SegmentedButtonModel> {
            return financingTypes.map {
                SegmentedButtonModel(
                    label = it.label,
                    icon = ResourceUtil.getDrawableResByString(it.label)
                )
            }
        }
    }
}