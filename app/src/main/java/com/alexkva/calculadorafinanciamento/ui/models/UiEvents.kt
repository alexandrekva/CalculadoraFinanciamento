package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull


open class UiEvent(val onConsumed: () -> Unit) {
    data class NavigateToRoute(val route: String, val onConsumedAction: () -> Unit) :
        UiEvent(onConsumed = onConsumedAction)

    data class NavigateBack(val onConsumedAction: () -> Unit) :
        UiEvent(onConsumed = onConsumedAction)

    class NavigateBackWithArgs(
        onConsumedAction: () -> Unit,
        vararg val navArgs: Pair<String, Any>
    ) :
        UiEvent(onConsumed = onConsumedAction)

    data class ShowSnackbar(
        val snackbarVisuals: SnackbarVisuals,
        val onActionPerformed: () -> Unit = {},
        val onDismissed: () -> Unit = {},
        val onConsumedAction: () -> Unit
    ) :
        UiEvent(onConsumed = onConsumedAction)
}

@Composable
fun ObserveUiEvents(uiEventsFlow: StateFlow<UiEvent?>, onEvent: (UiEvent) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEventsFlow.filterNotNull().collect {
                onEvent(it)
                it.onConsumed()
            }
        }

        lifecycleOwner.withStarted {  }
    }
}