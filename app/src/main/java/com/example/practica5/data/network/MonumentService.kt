package com.example.practica5.data.network

import com.example.practica5.core.RetrofitHelper
import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.data.model.MonumentsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MonumentService {
    private val retrofit = RetrofitHelper.retrofitInstance

    suspend fun getMonuments(): List<MonumentDTO> {
        return withContext(Dispatchers.IO) {
            val response: Response<MonumentsResponse> = retrofit.create(MonumentsApiClient::class.java).getAllMonuments()
            response.body()?.monuments ?: emptyList()
        }
    }
}