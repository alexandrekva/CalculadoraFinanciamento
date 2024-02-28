package com.alexkva.calculadorafinanciamento.navigation

import androidx.navigation.NavType

sealed class NavArg(
    open val isOptional: Boolean,
    open val defaultValue: Any? = null,
    open val nullable: Boolean? = null
) {
    abstract val key: String
    abstract val navType: NavType<*>

    data class SimulationParametersId(
        override val isOptional: Boolean,
        override val defaultValue: Any? = null,
        override val nullable: Boolean? = null
    ) : NavArg(isOptional, defaultValue, nullable) {
        override val key: String = SIMULATION_PARAMETERS_ID_KEY
        override val navType: NavType<*> = NavType.LongType
    }

    data class Test(
        override val isOptional: Boolean,
        override val defaultValue: Any? = null,
        override val nullable: Boolean? = null
    ) : NavArg(isOptional, defaultValue, nullable) {
        override val key: String = TEST_KEY
        override val navType: NavType<*> = NavType.StringType
    }

    data class Test2(
        override val isOptional: Boolean,
        override val defaultValue: Any? = null,
        override val nullable: Boolean? = null
    ) : NavArg(isOptional, defaultValue, nullable) {
        override val key: String = TEST_KEY2
        override val navType: NavType<*> = NavType.StringType
    }

    companion object {
        const val SIMULATION_PARAMETERS_ID_KEY = "simulationParametersId"
        const val TEST_KEY = "test"
        const val TEST_KEY2 = "test2"
    }
}



