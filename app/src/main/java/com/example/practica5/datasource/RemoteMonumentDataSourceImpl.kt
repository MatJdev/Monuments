package com.example.practica5.datasource

import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.network.MonumentService
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.RemoteMonumentDataSource

class RemoteMonumentDataSourceImpl(private val monumentService: MonumentService) : RemoteMonumentDataSource {
    override suspend fun getMonuments(): List<MonumentBO> {
        return monumentService.getMonuments().map { MonumentMapper.mapMonumentDtoToBo(it) }
    }
}