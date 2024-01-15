package com.alexkva.calculadorafinanciamento.utils.classes

import androidx.annotation.DrawableRes
import com.alexkva.calculadorafinanciamento.R

class ResourceUtil {
    companion object {
        @DrawableRes
        fun getDrawableResByString(string: String): Int? {
            return when {
                string.equals("sac", true) -> R.drawable.ic_sac
                string.equals("price", true) -> R.drawable.ic_price
                else -> null
            }
        }
    }
}