package com.example.practica5.data.network

import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.data.model.MonumentsResponse
import retrofit2.Response
import retrofit2.http.GET

interface MonumentsApiClient {
    @GET("ff109567f93e9cbcae18b291ce6e4e664b578177/monuments_data.json")
    suspend fun getAllMonuments(): Response<MonumentsResponse>
}