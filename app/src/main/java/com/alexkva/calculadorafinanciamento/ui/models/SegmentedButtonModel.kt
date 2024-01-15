package com.alexkva.calculadorafinanciamento.ui.models

import androidx.annotation.DrawableRes

data class SegmentedButtonModel(
    val label: String,
    @DrawableRes val icon: Int? = null
)