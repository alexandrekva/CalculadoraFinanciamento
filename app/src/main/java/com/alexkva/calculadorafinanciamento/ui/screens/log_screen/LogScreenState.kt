package com.alexkva.calculadorafinanciamento.ui.screens.log_screen

import com.alexkva.calculadorafinanciamento.ui.models.LogItemCollection

data class LogScreenState(
    val isLoading: Boolean = true,
    val logItemCollection: LogItemCollection = LogItemCollection()
)