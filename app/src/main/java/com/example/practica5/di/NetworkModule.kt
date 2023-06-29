package com.example.practica5.di

import com.example.practica5.data.network.FlagsApiService
import com.example.practica5.data.network.MonumentsApiClient
import com.example.practica5.utils.MonumentsConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        return Retrofit.Builder()
            .baseUrl(MonumentsConstant.BASE_URL_RETROFIT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideFlagsApi(): FlagsApiService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(MonumentsConstant.BASE_URL_FLAGS_API)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(FlagsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMonumentApiClient(retrofit: Retrofit): MonumentsApiClient {
        return retrofit.create(MonumentsApiClient::class.java)
    }
}