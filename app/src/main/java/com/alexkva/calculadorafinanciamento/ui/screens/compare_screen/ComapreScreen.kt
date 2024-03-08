package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.navigation.NavigationCommand
import com.alexkva.calculadorafinanciamento.ui.components.CustomTopBar
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
    with(compareScreenState) {
        Scaffold(topBar = {
            CustomTopBar(title = { Text(text = stringResource(id = R.string.compare_label)) },
                leadingIcon = {
                    IconButton(onClick = { onUserEvent(CompareScreenUserEvent.BackButtonClicked) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = null
                        )
                    }
                })
        }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(paddingValues), contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                } else {
                    Text(text = compareScreenState.toString())
                }
            }
        }
    }
}