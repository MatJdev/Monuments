package com.example.practica5.ui.fragments.monuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.MonumentsApp
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositoryFactory
import com.example.practica5.datasource.Resource
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetFilteredMonumentsByCountryUseCase
import com.example.practica5.domain.usecase.GetMonumentsOrderedUseCase
import com.example.practica5.domain.usecase.GetMonumentsUseCase
import com.example.practica5.domain.usecase.GetUniqueCountriesUseCase
import com.example.practica5.domain.usecase.OrderBy
import com.example.practica5.domain.usecase.UpdateFavoriteMonumentUseCase
import com.example.practica5.utils.MonumentsConstant.ALL_COUNTRIES
import com.example.practica5.utils.MonumentsConstant.ERROR_LOADING_MONUMENTS
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ID
import com.example.practica5.utils.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.utils.MonumentsConstant.SORTED_EAST_WEST
import com.example.practica5.utils.MonumentsConstant.SORTED_NORTH_SOUTH
import kotlinx.coroutines.launch

class MonumentsViewModel : ViewModel() {

    private val monumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getMonumentsList(): LiveData<List<MonumentVO>?> = monumentsMutableLiveData

    private val resourceLiveData = MutableLiveData<Resource<List<MonumentVO>, String>>()
    fun getResourceState(): LiveData<Resource<List<MonumentVO>, String>> = resourceLiveData

    private val getMonumentsUseCase by lazy {
        GetMonumentsUseCase(
            MonumentRepositoryFactory.monumentRepository,
            MonumentsApp.getInstance().getLocationHelper()
        )
    }

    init {
        getAllMonuments()
    }

    fun getAllMonuments() {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading
            try {
                val result = getMonumentsUseCase.getMonuments()
                val resultVO = result.map { MonumentMapper.mapMonumentBoToVo(it) }
                if (result.isNotEmpty()) {
                    monumentsMutableLiveData.postValue(resultVO)
                    resourceLiveData.value = Resource.Success(resultVO)
                }
            } catch (e: Exception) {
                resourceLiveData.value = Resource.Error(ERROR_LOADING_MONUMENTS)
            }
        }
    }

    fun updateFavoriteMonument(monumentId: Long, favorite: Boolean) {
        val setFavorite = !favorite
        val updateFavoriteMonument =
            UpdateFavoriteMonumentUseCase(MonumentRepositoryFactory.monumentRepository, monumentId, setFavorite)
        viewModelScope.launch {
            updateFavoriteMonument()
        }
    }

    private val getMonumentsOrderedUseCase = GetMonumentsOrderedUseCase(MonumentRepositoryFactory.monumentRepository)

    fun getSortedMonuments(sortMode: String) {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading
            try {
                val monumentsOrdered = when (sortMode) {
                    MONUMENT_ID -> getMonumentsOrderedUseCase(OrderBy.ID)
                    MONUMENT_NAME -> getMonumentsOrderedUseCase(OrderBy.NAME)
                    SORTED_NORTH_SOUTH -> getMonumentsOrderedUseCase(OrderBy.LATITUDE)
                    SORTED_EAST_WEST -> getMonumentsOrderedUseCase(OrderBy.LONGITUDE)
                    else -> getMonumentsOrderedUseCase(OrderBy.ID)
                }
                resourceLiveData.value = Resource.Success(monumentsOrdered.map { MonumentMapper.mapMonumentBoToVo(it) })
                monumentsMutableLiveData.value = monumentsOrdered.map { MonumentMapper.mapMonumentBoToVo(it) }
            } catch (e: Exception) {
                resourceLiveData.value = Resource.Error(ERROR_LOADING_MONUMENTS)
            }
        }
    }

    suspend fun getUniqueCountries(): List<String> {
        val uniqueCountries = GetUniqueCountriesUseCase(MonumentRepositoryFactory.monumentRepository)
        val list = uniqueCountries().toMutableList()
        list.add(ALL_COUNTRIES)
        return list
    }


    fun getFilteredMonumentsByCountry(country: String) {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading
            try {
                if (country == ALL_COUNTRIES) {
                    getAllMonuments()
                } else {
                    val getFilteredMonumentsByCountryUseCase =
                        GetFilteredMonumentsByCountryUseCase(MonumentRepositoryFactory.monumentRepository, country)
                    resourceLiveData.value = Resource.Success(getFilteredMonumentsByCountryUseCase().map { MonumentMapper.mapMonumentBoToVo(it) })
                    monumentsMutableLiveData.value =
                        getFilteredMonumentsByCountryUseCase().map { MonumentMapper.mapMonumentBoToVo(it) }
                }
            } catch (e: Exception) {
                resourceLiveData.value = Resource.Error(ERROR_LOADING_MONUMENTS)
            }
        }
    }

    fun getMonumentByTitle(title: String): MonumentVO? {
        return getMonumentsList().value?.find { monument -> monument.name == title }
    }
}