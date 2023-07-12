package com.example.practica5.local.di

import android.app.Application
import androidx.room.Room
import com.example.practica5.local.MonumentDataBase
import com.example.practica5.local.MonumentDAO
import com.example.practica5.local.monuments.LocalMonumentDataSourceImpl
import com.example.practica5.datasource.monuments.LocalMonumentDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalMonumentDataSource(monumentDAO: MonumentDAO): LocalMonumentDataSource {
        return LocalMonumentDataSourceImpl(monumentDAO)
    }

    @Provides
    fun provideMonumentDAO(database: MonumentDataBase): MonumentDAO {
        return database.getMonumentDAO()
    }

    @Provides
    @Singleton
    fun provideMonumentDataBase(application: Application): MonumentDataBase {
        return Room.databaseBuilder(application, MonumentDataBase::class.java, DATABASE_NAME)
            .build()
    }

    private const val DATABASE_NAME = "monuments_dataBase"
}