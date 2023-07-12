package com.example.practica5.favorites.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val getFavoriteMonumentsUseCase: GetFavoriteMonumentsUseCase) :
    ViewModel() {
    private val favMonumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getFavMonumentsList(): LiveData<List<MonumentVO>?> = favMonumentsMutableLiveData

    private val resourceLiveData = MutableLiveData<Resource<List<MonumentVO>, String>>()
    fun getResourceState(): LiveData<Resource<List<MonumentVO>, String>> = resourceLiveData

    init {
        getFavMonuments()
    }

    fun getFavMonuments() {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading

            try {
                val result = getFavoriteMonumentsUseCase()
                val monumentListVO = result?.map { monument: MonumentBO -> MonumentMapper.mapMonumentBoToVo(monument) }

                if (!result.isNullOrEmpty()) {
                    favMonumentsMutableLiveData.postValue(monumentListVO)
                    resourceLiveData.postValue(Resource.Success(monumentListVO as List<MonumentVO>))
                } else {
                    favMonumentsMutableLiveData.postValue(emptyList())
                    resourceLiveData.postValue(Resource.Error(ERROR_FAVORITES_FOUND))
                }
            } catch (e: Exception) {
                resourceLiveData.postValue(Resource.Error(ERROR_LOADING_FAVORITES))
            }
        }
    }
}