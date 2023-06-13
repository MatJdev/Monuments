package com.example.practica5.data.repository

import android.content.Context
import com.example.practica5.MonumentsApp
import com.example.practica5.data.database.dao.MonumentDAO
import com.example.practica5.data.network.MonumentService
import com.example.practica5.datasource.LocalMonumentDataSourceImpl
import com.example.practica5.datasource.RemoteMonumentDataSourceImpl
import com.example.practica5.domain.repository.LocalMonumentDataSource
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.repository.RemoteMonumentDataSource

object MonumentRepositoryFactory {
    private lateinit var monumentDAO: MonumentDAO
    private lateinit var applicationContext: MonumentsApp
    private lateinit var localDataSource: LocalMonumentDataSource
    private lateinit var remoteDataSource: RemoteMonumentDataSource

    val monumentRepository: MonumentRepository get() = MonumentRepositoryImpl(localDataSource, remoteDataSource)

    fun initialize(context: Context) {
        applicationContext = context.applicationContext as MonumentsApp
        val monumentDataBase = applicationContext.getDataBase()
        monumentDAO = monumentDataBase.getMonumentDAO()
        localDataSource = LocalMonumentDataSourceImpl(monumentDAO)
        remoteDataSource = RemoteMonumentDataSourceImpl(MonumentService())
    }
}