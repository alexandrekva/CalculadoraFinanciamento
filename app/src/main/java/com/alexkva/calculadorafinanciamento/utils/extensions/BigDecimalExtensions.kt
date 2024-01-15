package com.alexkva.calculadorafinanciamento.utils.extensions

import com.alexkva.calculadorafinanciamento.utils.constants.DECIMAL_FORMAT_PATTERN
import java.math.BigDecimal
import java.text.DecimalFormat

fun BigDecimal.toInputString(): String {
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN)
    return formatter.format(this)
}