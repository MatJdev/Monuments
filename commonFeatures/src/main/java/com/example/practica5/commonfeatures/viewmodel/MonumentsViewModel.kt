package com.example.practica5.commonfeatures.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.common.Resource
import com.example.practica5.common.util.MonumentMapper
import com.example.practica5.common.util.MonumentsConstant.ALL_COUNTRIES
import com.example.practica5.common.util.MonumentsConstant.ERROR_LOADING_MONUMENTS
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_ID
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.common.util.MonumentsConstant.SORTED_EAST_WEST
import com.example.practica5.common.util.MonumentsConstant.SORTED_NORTH_SOUTH
import com.example.practica5.commonfeatures.domain.GetFilteredMonumentsByCountryUseCase
import com.example.practica5.commonfeatures.domain.GetMonumentsOrderedUseCase
import com.example.practica5.commonfeatures.domain.GetMonumentsUseCase
import com.example.practica5.commonfeatures.domain.GetUniqueCountriesUseCase
import com.example.practica5.commonfeatures.domain.OrderBy
import com.example.practica5.commonfeatures.domain.UpdateFavoriteMonumentUseCase
import com.example.practica5.model.bo.monument.MonumentBO
import com.example.practica5.model.vo.MonumentVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonumentsViewModel @Inject constructor(
    private val getMonumentsUseCase: GetMonumentsUseCase,
    private val getMonumentsOrderedUseCase: GetMonumentsOrderedUseCase,
    private val updateFavoriteMonumentUseCase: UpdateFavoriteMonumentUseCase,
    private val getUniqueCountriesUseCase: GetUniqueCountriesUseCase,
    private val getFilteredMonumentsByCountryUseCase: GetFilteredMonumentsByCountryUseCase
) : ViewModel() {

    private val monumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getMonumentsList(): LiveData<List<MonumentVO>?> = monumentsMutableLiveData

    private val resourceLiveData = MutableLiveData<Resource<List<MonumentVO>, String>>()
    fun getResourceState(): LiveData<Resource<List<MonumentVO>, String>> = resourceLiveData

    init {
        getAllMonuments()
    }

    fun getAllMonuments() {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading
            try {
                val result = getMonumentsUseCase.getMonuments()
                val resultVO: List<MonumentVO> = result.map { MonumentMapper.mapMonumentBoToVo(it) }
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
        viewModelScope.launch {
            updateFavoriteMonumentUseCase.invoke(monumentId, setFavorite)
        }
    }

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
        val list = getUniqueCountriesUseCase.invoke().toMutableList()
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
                    val filteredMonuments = getFilteredMonumentsByCountryUseCase.invoke()
                    resourceLiveData.value = Resource.Success(filteredMonuments.map { monument: MonumentBO ->
                        MonumentMapper.mapMonumentBoToVo(monument)
                    })
                    monumentsMutableLiveData.value =
                        filteredMonuments.map { monument: MonumentBO -> MonumentMapper.mapMonumentBoToVo(monument) }
                }
            } catch (e: Exception) {
                resourceLiveData.value = Resource.Error(ERROR_LOADING_MONUMENTS)
            }
        }
    }

    fun getMonumentByTitle(title: String): MonumentVO? {
        return getMonumentsList().value?.find { monument -> monument.name == title }
    }

    fun updateCountrySelected(newCountry: String) {
        getFilteredMonumentsByCountryUseCase.setCountry(newCountry)
    }
}