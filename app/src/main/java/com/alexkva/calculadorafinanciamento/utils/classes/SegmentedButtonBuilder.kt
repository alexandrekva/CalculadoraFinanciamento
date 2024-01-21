package com.alexkva.calculadorafinanciamento.utils.classes

import com.alexkva.calculadorafinanciamento.business.entities.FinancingTypes
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonCollection
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonModel
import kotlin.enums.EnumEntries

class SegmentedButtonBuilder {
    companion object {
        fun buildByFinancingTypes(financingTypes: EnumEntries<FinancingTypes>): SegmentedButtonCollection {
            return SegmentedButtonCollection(financingTypes.map {
                SegmentedButtonModel(
                    label = it.label,
                    icon = ResourceUtil.getDrawableResByString(it.label)
                )
            })
        }
    }
}