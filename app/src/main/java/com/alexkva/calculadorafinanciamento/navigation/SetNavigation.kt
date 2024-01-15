package com.alexkva.calculadorafinanciamento.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexkva.calculadorafinanciamento.ui.screens.InputScreen

@Composable
fun SetNavigation(
    navController: NavHostController,
    startDestination: String = Screens.InputScreen.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screens.InputScreen.route) {
            InputScreen()
        }
    }
}