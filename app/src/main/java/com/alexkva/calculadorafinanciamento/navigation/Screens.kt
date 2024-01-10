package com.alexkva.calculadorafinanciamento.navigation

sealed class Screens(val route: String) {
    object Home: Screens("home")
}