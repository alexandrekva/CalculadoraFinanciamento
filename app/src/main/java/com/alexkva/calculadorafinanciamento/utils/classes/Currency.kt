package com.alexkva.calculadorafinanciamento.utils.classes

enum class Currency(val symbol: String, val isBrazilianDecimalFormat: Boolean) {
    BRAZILIAN_REAL("R$", true),
    USD("$", false)
}