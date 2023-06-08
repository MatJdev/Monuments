package com.example.practica5.domain.repository

import com.example.practica5.data.database.entities.MonumentDBO
import com.example.practica5.domain.model.bo.MonumentBO

interface LocalMonumentDataSource {
    suspend fun getAllMonuments(): List<MonumentBO>
    suspend fun insertMonument(monument: List<MonumentDBO>)
    suspend fun updateFavoriteMonument(id: Long, favorite: Boolean)
    suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentBO>
    suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentBO>
    suspend fun getSortedMonuments(sortMode: String): List<MonumentBO>
    suspend fun getUniqueCountries(): List<String>
    suspend fun getFilteredMonuments(country: String): List<MonumentBO>
    suspend fun insertOneMonument(monument: MonumentDBO)
    suspend fun getMyMonuments(): List<MonumentBO>
    suspend fun deleteMonument(monument: MonumentDBO)
    suspend fun getFavoriteMonuments(): List<MonumentBO>
}