package com.example.practica5.data.network

import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.data.model.MonumentsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MonumentService @Inject constructor(private val api: MonumentsApiClient){

    suspend fun getMonuments(): List<MonumentDTO> {
        return withContext(Dispatchers.IO) {
            val response: Response<MonumentsResponse> = api.getAllMonuments()
            response.body()?.monuments ?: emptyList()
        }
    }
}