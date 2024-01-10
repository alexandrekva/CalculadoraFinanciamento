package com.alexkva.calculadorafinanciamento.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.alexkva.calculadorafinanciamento.navigation.SetNavigation
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            CalculadoraFinanciamentoTheme {
                SetNavigation(navController = navController)
            }
        }
    }
}