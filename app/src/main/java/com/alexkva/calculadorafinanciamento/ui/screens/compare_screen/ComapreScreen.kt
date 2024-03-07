package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.ui.components.ObserveUiEvents
import com.alexkva.calculadorafinanciamento.ui.models.UiEvent

@Composable
fun CompareScreenRoute(
    viewModel: CompareScreenViewModel = hiltViewModel(),
    onNavigationCommand: (NavigationCommand) -> Unit
) {
    val compareState: CompareScreenState by viewModel.compareState.collectAsStateWithLifecycle()

    ObserveUiEvents(uiEventsFlow = viewModel.uiEventState) { uiEvent ->
        when (uiEvent) {
            is UiEvent.Navigate -> onNavigationCommand(uiEvent.navigationCommand)
        }
    }

    CompareScreen(compareScreenState = compareState, onUserEvent = viewModel::onUserEvent)
}

@Composable
fun CompareScreen(
    compareScreenState: CompareScreenState, onUserEvent: (CompareScreenUserEvent) -> Unit
) {

}