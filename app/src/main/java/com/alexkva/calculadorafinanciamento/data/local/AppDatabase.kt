package com.alexkva.calculadorafinanciamento.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersDao
import com.alexkva.calculadorafinanciamento.data.local.entities.SimulationParameterEntity
import com.alexkva.calculadorafinanciamento.data.local.utils.Converters

@Database(entities = [SimulationParameterEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun simulationParametersDao(): SimulationParametersDao

    companion object {
        const val DATABASE_NAME = "app_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).addTypeConverter(Converters()).build()
        }
    }
}
