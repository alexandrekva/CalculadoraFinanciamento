package com.alexkva.calculadorafinanciamento.navigation

import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId

sealed class Screens(val route: String, vararg val navArgs: NavArg) {
    open fun buildNavigationRoute(vararg args: Pair<NavArg, Any?>): String {
        val navigationRoute = buildString {
            append(route)

            val firstOptionalIndex = navArgs.indexOfFirst { it.isOptional }

            args.filter { it.first.isOptional && it.second != null || !it.first.isOptional }
                .forEachIndexed { index, navPair ->
                    val prefix = when {
                        (!navPair.first.isOptional) -> "/"
                        (index == firstOptionalIndex) -> "?"
                        else -> "&"
                    }

                    when (navPair.first.isOptional) {
                        true -> append("${prefix}${navPair.first.key}=${navPair.second}")
                        false -> append("${prefix}${navPair.second}")
                    }
                }
        }

        Log.d("SCREENS", "buildNavigationRoute: $navigationRoute")
        return navigationRoute
    }

    fun getFullRoute(): String {
        val fullRoute = buildString {
            append(route)

            val firstOptionalIndex = navArgs.indexOfFirst { it.isOptional }

            navArgs.forEachIndexed { index, navArg ->
                val prefix = when {
                    (!navArg.isOptional) -> "/"
                    (index == firstOptionalIndex) -> "?"
                    else -> "&"
                }

                when (navArg.isOptional) {
                    true -> append("${prefix}${navArg.key}={${navArg.key}}")
                    false -> append("${prefix}{${navArg.key}}")
                }
            }
        }

        Log.d("SCREENS", "getFullRoute: $fullRoute")
        return fullRoute
    }

    fun getArguments(): List<NamedNavArgument> {
        return navArgs.map { navArg ->
            navArgument(navArg.key) {
                type = navArg.navType
                navArg.nullable?.let { nullable = it }
                navArg.defaultValue?.let { defaultValue = it }
            }
        }
    }

    data object InputScreen : Screens(
        route = INPUT_SCREEN_ROUTE, NavArg.SimulationParametersId(isOptional = true, defaultValue = Long.MIN_VALUE)
    ) {
        fun getNavigationRoute(simulationParametersId: SimulationParametersId? = null): String {
            val arg1 = navArgs[0] to simulationParametersId
            return buildNavigationRoute(arg1)
        }
    }

    data object SimulationScreen : Screens(
        route = SIMULATION_SCREEN_ROUTE, NavArg.SimulationParametersId(isOptional = false)
    ) {
        fun getNavigationRoute(simulationParametersId: SimulationParametersId): String {
            val arg1 = navArgs[0] to simulationParametersId
            return buildNavigationRoute(arg1)
        }
    }

    data object LogScreen : Screens(
        LOG_SCREEN_ROUTE
    ) {
        fun getNavigationRoute(): String {
            return buildNavigationRoute()
        }
    }

    companion object {
        const val INPUT_SCREEN_ROUTE = "input"
        const val LOG_SCREEN_ROUTE = "log"
        const val SIMULATION_SCREEN_ROUTE = "simulation"

    }
}