package com.alexkva.calculadorafinanciamento.navigation

sealed class Screens(val route: String) {
    object InputScreen: Screens("input_screen")

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