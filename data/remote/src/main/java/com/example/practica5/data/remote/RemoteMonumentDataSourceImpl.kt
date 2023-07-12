package com.example.practica5.data.remote

import com.example.practica5.data.remote.monuments.toBO
import com.example.practica5.data.remote.network.MonumentService
import com.example.practica5.datasource.monuments.RemoteMonumentDataSource
import com.example.practica5.model.bo.monument.MonumentBO
import javax.inject.Inject

class RemoteMonumentDataSourceImpl @Inject constructor(private val monumentService: MonumentService) :
    RemoteMonumentDataSource {
    override suspend fun getMonuments(): List<MonumentBO> {
        return monumentService.getMonuments().map { it.toBO() }
    }
}