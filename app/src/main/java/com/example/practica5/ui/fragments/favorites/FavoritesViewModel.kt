package com.example.practica5.ui.fragments.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositorySingleton
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetFavoriteMonumentsUseCase
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val favMonumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getFavMonumentsList(): LiveData<List<MonumentVO>?> = favMonumentsMutableLiveData

    private val getFavoriteMonumentsUseCase: GetFavoriteMonumentsUseCase = GetFavoriteMonumentsUseCase(
        MonumentRepositorySingleton.monumentRepository)

    init {
        getFavMonuments()
    }

    fun getFavMonuments() {
        viewModelScope.launch {
            val result = getFavoriteMonumentsUseCase()
            val monumentListVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }
            if (!result.isNullOrEmpty()) {
                favMonumentsMutableLiveData.postValue(monumentListVO)
            } else {
                favMonumentsMutableLiveData.postValue(emptyList())
            }
        }
    }
}