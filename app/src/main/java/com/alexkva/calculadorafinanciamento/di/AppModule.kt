package com.alexkva.calculadorafinanciamento.di

import android.app.Application
import com.alexkva.calculadorafinanciamento.business.interfaces.GetLastSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.GetSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.InsertSimulationParametersUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.SimulationParametersRepository
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateDecimalInputUseCase
import com.alexkva.calculadorafinanciamento.business.interfaces.ValidateTermUseCase
import com.alexkva.calculadorafinanciamento.business.use_cases.GetLastSimulationParametersUseCaseImpl
import com.alexkva.calculadorafinanciamento.business.use_cases.GetSimulationParametersUseCaseImpl
import com.alexkva.calculadorafinanciamento.business.use_cases.InsertSimulationParameterUseCaseImpl
import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateDecimalInputUseCaseImpl
import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateTermUseCaseImpl
import com.alexkva.calculadorafinanciamento.data.local.AppDatabase
import com.alexkva.calculadorafinanciamento.data.local.dao.SimulationParametersDao
import com.alexkva.calculadorafinanciamento.data.repositories.SimulationParametersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDefaultIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
    @Provides
    fun provideValidateDecimalInput(): ValidateDecimalInputUseCase {
        return ValidateDecimalInputUseCaseImpl()
    }

    @Provides
    fun provideValidateTerm(): ValidateTermUseCase {
        return ValidateTermUseCaseImpl()
    }

    @Provides
    fun provideGetSimulationParameters(repository: SimulationParametersRepository): GetSimulationParametersUseCase {
        return GetSimulationParametersUseCaseImpl(repository)
    }

    @Provides
    fun provideGetLastSimulationParameters(repository: SimulationParametersRepository): GetLastSimulationParametersUseCase {
        return GetLastSimulationParametersUseCaseImpl(repository)
    }

    @Provides
    fun provideInsertSimulationParameters(repository: SimulationParametersRepository): InsertSimulationParametersUseCase {
        return InsertSimulationParameterUseCaseImpl(repository)
    }

    @Provides
    fun provideSimulationParametersRepository(dao: SimulationParametersDao): SimulationParametersRepository {
        return SimulationParametersRepositoryImpl(dao)
    }

    @Provides
    fun provideSimulationParametersDao(appDatabase: AppDatabase): SimulationParametersDao {
        return appDatabase.simulationParametersDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }
}