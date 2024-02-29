package com.alexkva.calculadorafinanciamento.navigation

import androidx.navigation.NavController

data class NavigationCommands(val navController: NavController) {
    fun navigateTo(route: String, clearStack: Boolean = false) {
        navController.navigate(route) {
            if(clearStack) {
                popUpTo(navController.previousBackStackEntry?.id ?: "") {
                    inclusive = true
                }
            }
        }
    }


    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateBackWithArgs(navArgs: Array<out Pair<String, Any>>) {
        navController.previousBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
            navArgs.forEach { arg ->
                savedStateHandle[arg.first] = arg.second
            }

        }
        navController.popBackStack()
    }
}