package com.example.practica5.datasource.monuments

import com.example.practica5.model.bo.monument.MonumentBO

interface RemoteMonumentDataSource {
    suspend fun getMonuments(): List<MonumentBO>
}