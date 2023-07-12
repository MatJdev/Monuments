package com.example.practica5.home.di

import com.example.practica5.common.domain.LocationHelper
import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.commonfeatures.domain.GetFilteredMonumentsByCountryUseCase
import com.example.practica5.commonfeatures.domain.GetMonumentsOrderedUseCase
import com.example.practica5.commonfeatures.domain.GetMonumentsUseCase
import com.example.practica5.commonfeatures.domain.GetUniqueCountriesUseCase
import com.example.practica5.commonfeatures.domain.UpdateFavoriteMonumentUseCase
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
}