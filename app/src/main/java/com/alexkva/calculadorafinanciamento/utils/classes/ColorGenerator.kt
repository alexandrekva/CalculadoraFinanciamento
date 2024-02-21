package com.alexkva.calculadorafinanciamento.utils.classes

import androidx.compose.ui.graphics.Color

class ColorGenerator {
    companion object {
        fun generateByAmortizationRelation(relation: Float): Color {
            val formattedValue = relation.coerceIn(0f, 1f)
            val scaledValue = (formattedValue * 100).toInt()
            val red = (180 * (100 - scaledValue) / 100).coerceIn(0, 180)
            val green = (180 * scaledValue / 100).coerceIn(0, 180)
            val blue = 0

            return Color(red = red, green = green, blue = blue)
        }
    }
}