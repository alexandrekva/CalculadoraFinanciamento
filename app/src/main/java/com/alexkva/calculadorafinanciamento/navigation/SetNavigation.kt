package com.alexkva.calculadorafinanciamento.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexkva.calculadorafinanciamento.ui.screens.input_screen.InputScreenRoute
import com.alexkva.calculadorafinanciamento.ui.screens.log_screen.LogScreenRoute
import com.alexkva.calculadorafinanciamento.ui.screens.simulation_screen.SimulationScreenRoute

@Composable
fun SetNavigation(
    navController: NavHostController,
    startDestination: String = Screens.InputScreen.route,
    navigationCommands: NavigationCommands = NavigationCommands(navController)
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(
            route = Screens.InputScreen.getFullRoute(),
            arguments = Screens.InputScreen.getArguments()
        ) {
            InputScreenRoute(onNavigationCommand = navigationCommands::execute)
        }
        composable(
            route = Screens.SimulationScreen.getFullRoute(),
            arguments = Screens.SimulationScreen.getArguments()
        ) {
            SimulationScreenRoute(onNavigationCommand = navigationCommands::execute)
        }

        composable(
            route = Screens.LogScreen.getFullRoute(), arguments = Screens.LogScreen.getArguments()
        ) {
            LogScreenRoute(
                onNavigationCommand = navigationCommands::execute
            )
        }
    }
}