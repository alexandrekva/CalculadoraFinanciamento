package com.alexkva.calculadorafinanciamento.business.entities

sealed class TermOptions(val label: String, val charLimit: Int) {
    object Months: TermOptions(label = "meses", charLimit = 3)
    object Years: TermOptions(label = "anos", charLimit = 2)

    fun inverse(): TermOptions {
        return when (this) {
            is Months -> Years
            is Years -> Months
        }
    }
}