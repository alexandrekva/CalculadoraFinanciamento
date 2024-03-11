package com.alexkva.calculadorafinanciamento.ui.screens.compare_screen


sealed class CompareScreenUserEvent {
    data object BackButtonClicked : CompareScreenUserEvent()
}