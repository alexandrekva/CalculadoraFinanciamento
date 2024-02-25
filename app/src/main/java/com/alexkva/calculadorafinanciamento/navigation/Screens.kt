package com.alexkva.calculadorafinanciamento.navigation

sealed class Screens(val route: String) {
    data object InputScreen: Screens("input")
    data object SimulationScreen: Screens("simulation")

    data object LogScreen: Screens("log")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach {
                append(
                    "/$it"
                )
            }
        }
    }
}