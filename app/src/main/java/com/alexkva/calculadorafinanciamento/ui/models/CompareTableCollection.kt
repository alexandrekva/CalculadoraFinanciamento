package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class CompareTableCollection(
    val rowLabelList: List<String> = emptyList(),
    val compareItemCollection: List<CompareItem> = emptyList()
)

data class CompareItem(
    val label: String,
    val values: List<String>
)
