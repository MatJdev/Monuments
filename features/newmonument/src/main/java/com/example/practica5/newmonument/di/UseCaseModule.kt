package com.example.practica5.newmonument.di

import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.commonfeatures.domain.GetCountryFlagUseCase
import com.example.practica5.commonfeatures.domain.InsertMonumentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideInsertMonumentUseCase(
        monumentRepository: MonumentRepository
    ): InsertMonumentUseCase {
        return InsertMonumentUseCase(monumentRepository)
    }

    @Singleton
    @Provides
    fun provideGetCountryFlagUseCase(
        monumentRepository: MonumentRepository
    ): GetCountryFlagUseCase {
        return GetCountryFlagUseCase(monumentRepository)
    }
}