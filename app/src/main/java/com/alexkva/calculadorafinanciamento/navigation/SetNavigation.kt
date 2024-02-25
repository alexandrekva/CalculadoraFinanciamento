package com.alexkva.calculadorafinanciamento.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alexkva.calculadorafinanciamento.ui.screens.input_screen.InputScreenRoute
import com.alexkva.calculadorafinanciamento.ui.screens.log_screen.LogScreenRoute
import com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen.SimulationScreenRoute

@Composable
fun SetNavigation(
    navController: NavHostController,
    startDestination: String = Screens.InputScreen.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screens.InputScreen.route) {
            InputScreenRoute(navigateTo = { direction -> navController.navigate(direction) })
        }
        composable(
            route = "${Screens.SimulationScreen.route}/{simulationId}", arguments = listOf(
                navArgument("simulationId") { type = NavType.StringType })
        ) {
            SimulationScreenRoute(navigateBack = { navController.popBackStack() })
        }

        composable(route = Screens.LogScreen.route) {
            LogScreenRoute(navigateBack = { navController.popBackStack() })
        }
    }
}