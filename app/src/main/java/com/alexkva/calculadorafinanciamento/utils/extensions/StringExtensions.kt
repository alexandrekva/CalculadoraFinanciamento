package com.alexkva.calculadorafinanciamento.utils.extensions

import com.alexkva.calculadorafinanciamento.utils.constants.CLEAN_ZEROS_PATTERN
import com.alexkva.calculadorafinanciamento.utils.constants.NUMERIC_CHARS_PATTERN
import java.math.BigDecimal

fun String.cleanNumericString() = replace(Regex(NUMERIC_CHARS_PATTERN), "")

fun String.formatToNumericString(): String {
    return cleanNumericString().replace(Regex(CLEAN_ZEROS_PATTERN), "")
}

fun String.toBigDecimalFromInput(): BigDecimal? {
    return toBigDecimalOrNull()?.divide(100.toBigDecimal())
}

fun String.toDoubleFromInput(): Double? {
    return toDoubleOrNull()?.div(100)
}

fun String.limitedCharacters(maxLength: Int): String {
    return if (length > maxLength) substring(0, maxLength) else this
}
