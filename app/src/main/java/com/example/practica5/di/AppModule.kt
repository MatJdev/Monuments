package com.example.practica5.di

import android.app.Application
import android.content.Context
import com.example.practica5.LocationHelperImpl
import com.example.practica5.data.network.FlagsApiService
import com.example.practica5.data.repository.MonumentRepositoryImpl
import com.example.practica5.domain.LocationHelper
import com.example.practica5.domain.repository.LocalMonumentDataSource
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.repository.RemoteMonumentDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMonumentRepository(
        localDataSource: LocalMonumentDataSource,
        remoteMonumentDataSource: RemoteMonumentDataSource,
        flagsApiService: FlagsApiService
    ): MonumentRepository {
        return MonumentRepositoryImpl(localDataSource, remoteMonumentDataSource, flagsApiService)
    }

    @Singleton
    @Provides
    fun provideLocationHelper(@ApplicationContext context: Context): LocationHelper {
        return LocationHelperImpl(context)
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext app: Application): Context {
        return app.applicationContext
    }
}