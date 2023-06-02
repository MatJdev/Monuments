package com.example.practica5.domain.repository

import com.example.practica5.data.database.entities.MonumentDBO

interface LocalMonumentDataSource {
    suspend fun getAllMonuments(): List<MonumentDBO>
    suspend fun insertMonument(monument: List<MonumentDBO>)
    suspend fun updateFavoriteMonument(id: Long, favorite: Boolean)
    suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentDBO>
    suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentDBO>
    suspend fun getSortedMonuments(sortMode: String): List<MonumentDBO>
    suspend fun getUniqueCountries(): List<String>
    suspend fun getFilteredMonuments(country: String): List<MonumentDBO>
}