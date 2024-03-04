package com.alexkva.calculadorafinanciamento.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavController

data class NavigationCommands(val navController: NavController) {

    fun execute(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is NavigationCommand.NavigateTo -> navigateTo(navigationCommand)
            is NavigationCommand.NavigateBack -> navigateBack()
        }
    }

    private fun navigateTo(navigationCommand: NavigationCommand.NavigateTo) {
        navController.navigate(navigationCommand.route) {
            launchSingleTop = navigationCommand.launchSingleTop
            restoreState = navigationCommand.restoreState

            navigationCommand.popUpToId?.let {
                popUpTo(it) {
                    inclusive = navigationCommand.inclusive
                }
            }

            navigationCommand.popUpToRoute?.let {
                popUpTo(it) {
                    inclusive = navigationCommand.inclusive
                }
            }
        }
    }

    private fun navigateBack(vararg navArgs: Pair<String, Any>) {
        navController.previousBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
            navArgs.forEach { arg ->
                savedStateHandle.set(key = arg.first, value = arg.second)
            }
        }
        navController.popBackStack()
    }
}

sealed class NavigationCommand {
    data class NavigateTo(
        val route: String,
        val launchSingleTop: Boolean = false,
        val restoreState: Boolean = false,
        @IdRes val popUpToId: Int? = null,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false,
        val saveState: Boolean = false
    ) : NavigationCommand()

    data object NavigateBack : NavigationCommand()
}