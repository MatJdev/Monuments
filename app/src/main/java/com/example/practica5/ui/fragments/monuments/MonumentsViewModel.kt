package com.example.practica5.ui.fragments.monuments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositorySingleton
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.usecase.GetFilteredMonumentsByCountryUseCase
import com.example.practica5.domain.usecase.GetMonumentsOrderedUseCase
import com.example.practica5.domain.usecase.GetMonumentsUseCase
import com.example.practica5.domain.usecase.GetUniqueCountriesUseCase
import com.example.practica5.domain.usecase.OrderBy
import com.example.practica5.domain.usecase.UpdateFavoriteMonumentUseCase
import com.example.practica5.utils.MonumentsConstant.ALL_COUNTRIES
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ID
import com.example.practica5.utils.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.utils.MonumentsConstant.SORTED_EAST_WEST
import com.example.practica5.utils.MonumentsConstant.SORTED_NORTH_SOUTH
import kotlinx.coroutines.launch

class MonumentsViewModel(application: Application) : AndroidViewModel(application) {

    private val monumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    val monumentsListLiveData: MutableLiveData<List<MonumentVO>?> get() = monumentsMutableLiveData

    private val getMonumentsUseCase: GetMonumentsUseCase

    private val monumentRepository: MonumentRepository


    init {
        MonumentRepositorySingleton.initialize(application.applicationContext)
        monumentRepository = MonumentRepositorySingleton.monumentRepository
        getMonumentsUseCase = GetMonumentsUseCase(monumentRepository)
        getAllMonuments()
    }

    private fun getAllMonuments() {
        viewModelScope.launch {
            val result = getMonumentsUseCase()
            val resultVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }
            if (!result.isNullOrEmpty()) {
                monumentsMutableLiveData.postValue(resultVO)
            }
        }
    }

    fun updateFavoriteMonument(monumentId: Long, favorite: Boolean) {
        UpdateFavoriteMonumentUseCase(monumentRepository, monumentId, favorite)
    }

    private val getMonumentsOrderedUseCase = GetMonumentsOrderedUseCase(monumentRepository)

    fun getSortedMonuments(sortMode: String) {
        viewModelScope.launch {
            val monumentsOrdered = when (sortMode) {
                MONUMENT_ID -> getMonumentsOrderedUseCase(OrderBy.ID)
                MONUMENT_NAME -> getMonumentsOrderedUseCase(OrderBy.NAME)
                SORTED_NORTH_SOUTH -> getMonumentsOrderedUseCase(OrderBy.LATITUDE)
                SORTED_EAST_WEST -> getMonumentsOrderedUseCase(OrderBy.LONGITUDE)
                else -> getMonumentsOrderedUseCase(OrderBy.ID)
            }
            monumentsMutableLiveData.value = monumentsOrdered.map { MonumentMapper.mapMonumentBoToVo(it) }
        }
    }

    suspend fun getUniqueCountries(): List<String> {
        val uniqueCountries = GetUniqueCountriesUseCase(monumentRepository)
        val list = uniqueCountries().toMutableList()
        list.add(ALL_COUNTRIES)
        return list
    }


    fun getFilteredMonumentsByCountry(country: String) {
        viewModelScope.launch {
            if (country == ALL_COUNTRIES) {
                getAllMonuments()
            } else {
                val getFilteredMonumentsByCountryUseCase =
                    GetFilteredMonumentsByCountryUseCase(monumentRepository, country)
                monumentsMutableLiveData.value =
                    getFilteredMonumentsByCountryUseCase().map { MonumentMapper.mapMonumentBoToVo(it) }
            }
        }
    }

    fun getMonumentByTitle(title: String): MonumentVO? {
        return monumentsListLiveData.value?.find { monument -> monument.name == title }
    }
}