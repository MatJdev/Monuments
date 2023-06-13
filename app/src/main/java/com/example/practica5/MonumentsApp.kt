package com.example.practica5

import android.app.Application
import androidx.room.Room
import com.example.practica5.data.database.MonumentDataBase
import com.example.practica5.data.repository.MonumentRepositoryFactory
import com.example.practica5.domain.LocationHelper
import com.example.practica5.utils.MonumentsConstant.DATABASE_NAME

class MonumentsApp : Application() {
    private lateinit var monumentDataBase: MonumentDataBase
    private lateinit var locationHelper: LocationHelper

    companion object {
        private lateinit var instance: MonumentsApp

        fun getInstance(): MonumentsApp {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        monumentDataBase =  Room.databaseBuilder(this, MonumentDataBase::class.java, DATABASE_NAME).build()
        MonumentRepositoryFactory.initialize(applicationContext)
        initializeLocationHelper()
    }

    fun getDataBase(): MonumentDataBase {
        return monumentDataBase
    }

    private fun initializeLocationHelper() {
        locationHelper = LocationHelperImpl(applicationContext)
    }

    fun getLocationHelper(): LocationHelper {
        return locationHelper
    }
}