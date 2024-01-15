package com.alexkva.calculadorafinanciamento.navigation

sealed class Screens(val route: String) {
    object InputScreen: Screens("input_screen")
}