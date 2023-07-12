package com.example.practica5.data.remote.di

import com.example.practica5.data.remote.RemoteMonumentDataSourceImpl
import com.example.practica5.data.remote.network.FlagsApiService
import com.example.practica5.data.remote.network.MonumentService
import com.example.practica5.data.remote.network.MonumentsApiClient
import com.example.practica5.data.remote.util.RemoteConstant.BASE_URL_FLAGS_API
import com.example.practica5.data.remote.util.RemoteConstant.BASE_URL_RETROFIT
import com.example.practica5.datasource.monuments.RemoteMonumentDataSource
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
object RemoteModule {

    @Singleton
    @Provides
    fun provideRemoteMonumentDataSource(monumentService: MonumentService): RemoteMonumentDataSource {
        return RemoteMonumentDataSourceImpl(monumentService)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_RETROFIT)
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
            .baseUrl(BASE_URL_FLAGS_API)
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