package com.alexkva.calculadorafinanciamento.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexkva.calculadorafinanciamento.ui.screens.MainScreen

@Composable
fun SetNavigation(navController: NavHostController, startDestination: String = Screens.Home.route) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screens.Home.route) {
            MainScreen()
        }
    }
}