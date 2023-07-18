package com.example.practica5.favorites.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.common.Resource
import com.example.practica5.common.util.MonumentMapper
import com.example.practica5.favorites.domain.GetFavoriteMonumentsUseCase
import com.example.practica5.common.util.MonumentsConstant.ERROR_FAVORITES_FOUND
import com.example.practica5.common.util.MonumentsConstant.ERROR_LOADING_FAVORITES
import com.example.practica5.model.bo.monument.MonumentBO
import com.example.practica5.model.vo.MonumentVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val getFavoriteMonumentsUseCase: GetFavoriteMonumentsUseCase) :
    ViewModel() {

    private val resourceStateFlow = MutableStateFlow<Resource<List<MonumentVO>, String>>(Resource.Loading)
    fun getResourceState(): StateFlow<Resource<List<MonumentVO>, String>> = resourceStateFlow.asStateFlow()

    init {
        getFavMonuments()
    }

    fun getFavMonuments() {
        viewModelScope.launch {
            resourceStateFlow.value = Resource.Loading

            try {
                val result = getFavoriteMonumentsUseCase()
                val monumentListVO = result?.map { monument: MonumentBO -> MonumentMapper.mapMonumentBoToVo(monument) }

                if (!result.isNullOrEmpty()) {
                    resourceStateFlow.value = Resource.Success(monumentListVO as List<MonumentVO>)
                } else {
                    resourceStateFlow.value = Resource.Error(ERROR_FAVORITES_FOUND)
                }
            } catch (e: Exception) {
                resourceStateFlow.value = Resource.Error(ERROR_LOADING_FAVORITES)
            }
        }
    }
}