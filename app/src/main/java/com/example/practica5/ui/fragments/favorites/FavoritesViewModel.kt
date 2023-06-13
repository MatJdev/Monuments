package com.example.practica5.ui.fragments.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositoryFactory
import com.example.practica5.datasource.Resource
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetFavoriteMonumentsUseCase
import com.example.practica5.utils.MonumentsConstant.ERROR_FAVORITES_FOUND
import com.example.practica5.utils.MonumentsConstant.ERROR_LOADING_FAVORITES
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val favMonumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getFavMonumentsList(): LiveData<List<MonumentVO>?> = favMonumentsMutableLiveData

    private val resourceLiveData = MutableLiveData<Resource<List<MonumentVO>, String>>()
    fun getResourceState(): LiveData<Resource<List<MonumentVO>, String>> = resourceLiveData

    private val getFavoriteMonumentsUseCase: GetFavoriteMonumentsUseCase = GetFavoriteMonumentsUseCase(
        MonumentRepositoryFactory.monumentRepository)

    init {
        getFavMonuments()
    }

    fun getFavMonuments() {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading

            try {
                val result = getFavoriteMonumentsUseCase()
                val monumentListVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }

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