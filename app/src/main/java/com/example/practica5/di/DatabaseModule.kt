package com.example.practica5.di

import android.app.Application
import androidx.room.Room
import com.example.practica5.data.database.MonumentDataBase
import com.example.practica5.data.database.dao.MonumentDAO
import com.example.practica5.data.network.MonumentService
import com.example.practica5.datasource.LocalMonumentDataSourceImpl
import com.example.practica5.datasource.RemoteMonumentDataSourceImpl
import com.example.practica5.domain.repository.LocalMonumentDataSource
import com.example.practica5.domain.repository.RemoteMonumentDataSource
import com.example.practica5.utils.MonumentsConstant
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

    @Singleton
    @Provides
    fun provideRemoteMonumentDataSource(monumentService: MonumentService): RemoteMonumentDataSource {
        return RemoteMonumentDataSourceImpl(monumentService)
    }

    @Provides
    fun provideMonumentDAO(database: MonumentDataBase): MonumentDAO {
        return database.getMonumentDAO()
    }

    @Provides
    @Singleton
    fun provideMonumentDataBase(application: Application): MonumentDataBase {
        return Room.databaseBuilder(application, MonumentDataBase::class.java, MonumentsConstant.DATABASE_NAME)
            .build()
    }
}