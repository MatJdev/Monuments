package com.example.practica5.data.remote.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FlagsApiService {
    @GET("/{countryCode}/shiny/16.png")
    suspend fun getFlagImage(
        @Path("countryCode") countryCode: String
    ): Response<ResponseBody>
}