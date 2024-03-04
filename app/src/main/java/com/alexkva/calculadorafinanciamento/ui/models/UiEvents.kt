package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.material3.SnackbarVisuals
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand


open class UiEvent(val onConsumed: () -> Unit) {
    data class Navigate(
        val navigationCommand: NavigationCommand, val onConsumedAction: () -> Unit
    ) : UiEvent(onConsumed = onConsumedAction)

    data class ShowSnackbar(
        val snackbarVisuals: SnackbarVisuals,
        val onActionPerformed: () -> Unit = {},
        val onDismissed: () -> Unit = {},
        val onConsumedAction: () -> Unit
    ) : UiEvent(onConsumed = onConsumedAction)
}