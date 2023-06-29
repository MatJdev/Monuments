package com.example.practica5.datasource

import com.example.practica5.data.database.dao.MonumentDAO
import com.example.practica5.data.database.entities.MonumentDBO
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.LocalMonumentDataSource
import javax.inject.Inject

class LocalMonumentDataSourceImpl @Inject constructor(private val monumentDAO: MonumentDAO) : LocalMonumentDataSource {
    override suspend fun getAllMonuments(): List<MonumentBO> {
        return monumentDAO.getAllMonuments().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun insertMonument(monument: List<MonumentBO>) {
        monumentDAO.insertMonument(monument.map { MonumentMapper.mapMonumentBoToDbo(it) })
    }

    override suspend fun updateFavoriteMonument(id: Long, favorite: Boolean) {
        monumentDAO.updateFavoriteMonument(id, favorite)
    }

    override suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentBO> {
        return monumentDAO.getMonumentsOrderByLocationNtoS().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentBO> {
        return monumentDAO.getMonumentsOrderByLocationEtoW().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getSortedMonuments(sortMode: String): List<MonumentBO> {
        return monumentDAO.getSortedMonuments(sortMode).map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getUniqueCountries(): List<String> {
        return monumentDAO.getUniqueCountries()
    }

    override suspend fun getFilteredMonuments(country: String): List<MonumentBO> {
        return monumentDAO.getFilteredMonuments(country).map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun insertOneMonument(monument: MonumentDBO) {
        return monumentDAO.insertOneMonument(monument)
    }

    override suspend fun getMyMonuments(): List<MonumentBO> {
        return monumentDAO.getMyMonuments().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun deleteMonument(monument: MonumentDBO) {
        return monumentDAO.deleteMonument(monument)
    }

    override suspend fun getFavoriteMonuments(): List<MonumentBO> {
        return monumentDAO.getFavoriteMonuments().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }
}