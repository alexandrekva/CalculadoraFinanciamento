package com.alexkva.calculadorafinanciamento.ui.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

data class SegmentedButtonModel(
    val label: String,
    @DrawableRes val icon: Int? = null
)

@Immutable
data class SegmentedButtonCollection(val segmentedButtons: List<SegmentedButtonModel> = emptyList())