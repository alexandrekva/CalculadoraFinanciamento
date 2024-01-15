package com.alexkva.calculadorafinanciamento.di

import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateDecimalInput
import com.alexkva.calculadorafinanciamento.business.use_cases.ValidateDecimalInputImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideValidateDecimalInput(): ValidateDecimalInput {
        return ValidateDecimalInputImpl()
    }
}