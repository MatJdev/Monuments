package com.example.practica5.commonfeatures.viewmodel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val resourceStateFlow = MutableStateFlow<Resource<List<MonumentVO>, String>>(Resource.Loading)
    fun getResourceState(): StateFlow<Resource<List<MonumentVO>, String>> = resourceStateFlow.asStateFlow()

    init {
        getAllMonuments()
    }

    fun getAllMonuments() {
        viewModelScope.launch {
            resourceStateFlow.value = Resource.Loading
            try {
                val result = getMonumentsUseCase.getMonuments()
                val resultVO: List<MonumentVO> = result.map { MonumentMapper.mapMonumentBoToVo(it) }
                if (result.isNotEmpty()) {
                    resourceStateFlow.value = Resource.Success(resultVO)
                }
            } catch (e: Exception) {
                resourceStateFlow.value = Resource.Error(ERROR_LOADING_MONUMENTS)
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
            resourceStateFlow.value = Resource.Loading
            try {
                val monumentsOrdered = when (sortMode) {
                    MONUMENT_ID -> getMonumentsOrderedUseCase(OrderBy.ID)
                    MONUMENT_NAME -> getMonumentsOrderedUseCase(OrderBy.NAME)
                    SORTED_NORTH_SOUTH -> getMonumentsOrderedUseCase(OrderBy.LATITUDE)
                    SORTED_EAST_WEST -> getMonumentsOrderedUseCase(OrderBy.LONGITUDE)
                    else -> getMonumentsOrderedUseCase(OrderBy.ID)
                }
                resourceStateFlow.value = Resource.Success(monumentsOrdered.map { MonumentMapper.mapMonumentBoToVo(it) })
            } catch (e: Exception) {
                resourceStateFlow.value = Resource.Error(ERROR_LOADING_MONUMENTS)
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
            resourceStateFlow.value = Resource.Loading
            try {
                if (country == ALL_COUNTRIES) {
                    getAllMonuments()
                } else {
                    val filteredMonuments = getFilteredMonumentsByCountryUseCase.invoke()
                    resourceStateFlow.value = Resource.Success(filteredMonuments.map { monument: MonumentBO ->
                        MonumentMapper.mapMonumentBoToVo(monument)
                    })
                }
            } catch (e: Exception) {
                resourceStateFlow.value = Resource.Error(ERROR_LOADING_MONUMENTS)
            }
        }
    }

    fun getMonumentByTitle(title: String): MonumentVO? {
        return getResourceState().value
            .takeIf { it is Resource.Success }
            .let { (it as? Resource.Success)?.data }
            ?.find { monument -> monument.name == title }
    }

    fun updateCountrySelected(newCountry: String) {
        getFilteredMonumentsByCountryUseCase.setCountry(newCountry)
    }
}