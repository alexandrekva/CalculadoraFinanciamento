package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull


open class UiEvent(val onConsumed: () -> Unit) {
    data class NavigationEvent(val destination: String, val onConsumedAction: () -> Unit) :
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
    }
}