package com.example.practica5.common.di

import android.content.Context
import com.example.practica5.common.LocationHelperImpl
import com.example.practica5.common.domain.LocationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationHelperModule {

    @Singleton
    @Provides
    fun provideLocationHelper(@ApplicationContext context: Context): LocationHelper {
        return LocationHelperImpl(context)
    }
}