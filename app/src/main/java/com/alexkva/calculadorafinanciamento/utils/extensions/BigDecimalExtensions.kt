package com.alexkva.calculadorafinanciamento.utils.extensions

import com.alexkva.calculadorafinanciamento.utils.constants.DECIMAL_FORMAT_PATTERN
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.pow

fun BigDecimal.toFormattedString(): String {
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN)
    return formatter.format(this)
}

fun BigDecimal.fromPercentToString(): String {
    val multipliedValue = this.multiply(BigDecimal(10000))
    return multipliedValue.toString()
}

fun BigDecimal.fromDecimalToString(): String {
    val multipliedValue = this.multiply(BigDecimal(100))
    return multipliedValue.toString()
}

fun BigDecimal.annualToMonthlyInterest(): BigDecimal {
        return BigDecimal((1 + this.toDouble()).pow(1.0/12) -1)
}