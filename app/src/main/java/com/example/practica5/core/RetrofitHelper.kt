package com.example.practica5.core

import com.example.practica5.data.network.FlagsApiService
import com.example.practica5.utils.MonumentsConstant.BASE_URL_FLAGS_API
import com.example.practica5.utils.MonumentsConstant.BASE_URL_RETROFIT
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {

    val retrofitInstance: Retrofit by lazy { createRetrofit() }
    val flagsApiServiceInstance: FlagsApiService by lazy { createFlagsApiService() }

    private fun createRetrofit(): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_RETROFIT )
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun createFlagsApiService(): FlagsApiService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_FLAGS_API)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(FlagsApiService::class.java)
    }
}