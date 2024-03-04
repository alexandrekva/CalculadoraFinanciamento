package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull

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
    }
}