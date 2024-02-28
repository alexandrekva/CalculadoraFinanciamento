package com.alexkva.calculadorafinanciamento.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersId

sealed class Screens(val route: String, vararg val navArgs: NavArg) {
    open fun buildNavigationRoute(vararg args: Any?): String {
        return buildString {
            append(route)
            args.filterNotNull().forEach {
                append(
                    "/$it"
                )
            }
        }
    }

    data object InputScreen : Screens(
        route = INPUT_SCREEN_ROUTE,
        NavArg.SimulationParametersId(isOptional = true)
    ) {
        fun getNavigationRoute(simulationParametersId: SimulationParametersId? = null): String {
            return buildNavigationRoute(simulationParametersId)
        }
    }

    data object SimulationScreen : Screens(
        route = SIMULATION_SCREEN_ROUTE,
        NavArg.SimulationParametersId(isOptional = false)
    ) {
        fun getNavigationRoute(simulationParametersId: SimulationParametersId): String {
            return buildNavigationRoute(simulationParametersId)
        }
    }

    data object LogScreen : Screens(
        LOG_SCREEN_ROUTE,
        NavArg.Test(isOptional = false),
        NavArg.Test2(isOptional = true),
    ) {
        fun getNavigationRoute(arg1: String, arg2: String): String {
            return buildNavigationRoute(arg1, arg2)
        }
    }

    fun getFullRoute(): String {
        val a=  buildString {
            append(route)
            navArgs.forEach {
                when (it.isOptional) {
                    true -> append("?${it.key}={${it.key}}")
                    false -> append("/{${it.key}}")
                }
            }
        }

        return a
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

    companion object {
        const val INPUT_SCREEN_ROUTE = "input"
        const val LOG_SCREEN_ROUTE = "log"
        const val SIMULATION_SCREEN_ROUTE = "simulation"

    }
}