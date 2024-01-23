package com.alexkva.calculadorafinanciamento.data.local.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.math.BigDecimal

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromBigDecimalToString(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun fromStringToBigDecimal(value: String?): BigDecimal? {
        return value?.toBigDecimalOrNull()
    }
}