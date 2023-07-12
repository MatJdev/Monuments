package com.example.practica5.local.monuments

import com.example.practica5.model.bo.monument.MonumentBO
import com.example.practica5.datasource.monuments.LocalMonumentDataSource
import com.example.practica5.local.MonumentDAO
import javax.inject.Inject

class LocalMonumentDataSourceImpl @Inject constructor(private val monumentDAO: MonumentDAO) :
    LocalMonumentDataSource {
    override suspend fun getAllMonuments(): List<MonumentBO> {
        return monumentDAO.getAllMonuments().map { it.toBO() }
    }

    override suspend fun insertMonument(monument: List<MonumentBO>) {
        monumentDAO.insertMonument(monument.map { it.toDBO() })
    }

    override suspend fun updateFavoriteMonument(id: Long, favorite: Boolean) {
        monumentDAO.updateFavoriteMonument(id, favorite)
    }

    override suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentBO> {
        return monumentDAO.getMonumentsOrderByLocationNtoS().map { it.toBO() }
    }

    override suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentBO> {
        return monumentDAO.getMonumentsOrderByLocationEtoW().map { it.toBO() }
    }

    override suspend fun getSortedMonuments(sortMode: String): List<MonumentBO> {
        return monumentDAO.getSortedMonuments(sortMode).map { it.toBO() }
    }

    override suspend fun getUniqueCountries(): List<String> {
        return monumentDAO.getUniqueCountries()
    }

    override suspend fun getFilteredMonuments(country: String): List<MonumentBO> {
        return monumentDAO.getFilteredMonuments(country).map { it.toBO() }
    }

    override suspend fun insertOneMonument(monument: MonumentBO) {
        return monumentDAO.insertOneMonument(monument.toDBO())
    }

    override suspend fun getMyMonuments(): List<MonumentBO> {
        return monumentDAO.getMyMonuments().map { it.toBO() }
    }

    override suspend fun deleteMonument(monument: MonumentBO) {
        return monumentDAO.deleteMonument(monument.toDBO())
    }

    override suspend fun getFavoriteMonuments(): List<MonumentBO> {
        return monumentDAO.getFavoriteMonuments().map { it.toBO() }
    }
}