package com.example.practica5.datasource

import com.example.practica5.data.database.dao.MonumentDAO
import com.example.practica5.data.database.entities.MonumentDBO
import com.example.practica5.domain.repository.LocalMonumentDataSource

class LocalMonumentDataSourceImpl(private val monumentDAO: MonumentDAO) : LocalMonumentDataSource {
    override suspend fun getAllMonuments(): List<MonumentDBO> {
        return monumentDAO.getAllMonuments()
    }

    override suspend fun insertMonument(monument: List<MonumentDBO>) {
        monumentDAO.insertMonument(monument)
    }

    override suspend fun updateFavoriteMonument(id: Long, favorite: Boolean) {
        monumentDAO.updateFavoriteMonument(id, favorite)
    }

    override suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentDBO> {
        return monumentDAO.getMonumentsOrderByLocationNtoS()
    }

    override suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentDBO> {
        return monumentDAO.getMonumentsOrderByLocationEtoW()
    }

    override suspend fun getSortedMonuments(sortMode: String): List<MonumentDBO> {
        return monumentDAO.getSortedMonuments(sortMode)
    }

    override suspend fun getUniqueCountries(): List<String> {
        return monumentDAO.getUniqueCountries()
    }

    override suspend fun getFilteredMonuments(country: String): List<MonumentDBO> {
        return monumentDAO.getFilteredMonuments(country)
    }
}