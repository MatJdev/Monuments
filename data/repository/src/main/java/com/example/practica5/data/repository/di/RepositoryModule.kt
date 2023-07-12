package com.example.practica5.data.repository.di

import com.example.practica5.data.remote.network.FlagsApiService
import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.data.repository.monuments.MonumentRepositoryImpl
import com.example.practica5.datasource.monuments.LocalMonumentDataSource
import com.example.practica5.datasource.monuments.RemoteMonumentDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMonumentRepository(
        localDataSource: LocalMonumentDataSource,
        remoteMonumentDataSource: RemoteMonumentDataSource,
        flagsApiService: FlagsApiService
    ): MonumentRepository {
        return MonumentRepositoryImpl(
            localDataSource,
            remoteMonumentDataSource,
            flagsApiService
        )
    }
}