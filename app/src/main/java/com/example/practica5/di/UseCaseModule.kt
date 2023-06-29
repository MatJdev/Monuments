package com.example.practica5.di

import com.example.practica5.domain.LocationHelper
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.usecase.GetCountryFlagUseCase
import com.example.practica5.domain.usecase.GetFilteredMonumentsByCountryUseCase
import com.example.practica5.domain.usecase.GetMonumentsOrderedUseCase
import com.example.practica5.domain.usecase.GetMonumentsUseCase
import com.example.practica5.domain.usecase.GetUniqueCountriesUseCase
import com.example.practica5.domain.usecase.InsertMonumentUseCase
import com.example.practica5.domain.usecase.UpdateFavoriteMonumentUseCase
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
    fun provideGetMonumentsUseCase(
        monumentRepository: MonumentRepository,
        locationHelper: LocationHelper
    ): GetMonumentsUseCase {
        return GetMonumentsUseCase(monumentRepository, locationHelper)
    }

    @Singleton
    @Provides
    fun provideGetMonumentsOrderedUseCase(monumentRepository: MonumentRepository): GetMonumentsOrderedUseCase {
        return GetMonumentsOrderedUseCase(monumentRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateFavoriteMonumentUseCase(monumentRepository: MonumentRepository): UpdateFavoriteMonumentUseCase {
        return UpdateFavoriteMonumentUseCase(monumentRepository)
    }

    @Singleton
    @Provides
    fun provideGetUniqueCountriesUseCase(monumentRepository: MonumentRepository): GetUniqueCountriesUseCase {
        return GetUniqueCountriesUseCase(monumentRepository)
    }


    @Singleton
    @Provides
    fun provideGetFilteredMonumentsByCountryUseCase(
        monumentRepository: MonumentRepository
    ): GetFilteredMonumentsByCountryUseCase {
        return GetFilteredMonumentsByCountryUseCase(monumentRepository)
    }

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