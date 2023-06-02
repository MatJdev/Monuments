package com.example.practica5.datasource

import com.example.practica5.data.network.MonumentService
import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.domain.repository.RemoteMonumentDataSource

class RemoteMonumentDataSourceImpl(private val monumentService: MonumentService) : RemoteMonumentDataSource {
    override suspend fun getMonuments(): List<MonumentDTO> {
        return monumentService.getMonuments()
    }
}