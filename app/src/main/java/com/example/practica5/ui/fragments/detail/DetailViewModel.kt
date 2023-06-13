package com.example.practica5.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositoryFactory
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.DeleteMonumentUseCase
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val monumentMutableLiveData = MutableLiveData<MonumentVO>()
    fun getMonument(): LiveData<MonumentVO> = monumentMutableLiveData

    private val removeCompleteLiveData = MutableLiveData<Boolean>()
    fun getRemoveComplete(): LiveData<Boolean> = removeCompleteLiveData

    fun loadData(monument: MonumentVO) {
        monumentMutableLiveData.value = monument
    }

    fun deleteMonument(monument: MonumentVO) {
        viewModelScope.launch {
            val deleteMonumentUseCase = DeleteMonumentUseCase(MonumentRepositoryFactory.monumentRepository, MonumentMapper.mapMonumentVoToBo(monument))
            deleteMonumentUseCase()
            removeCompleteLiveData.value = true
        }
    }

    fun setDeleteToFalse() {
        removeCompleteLiveData.value = false
    }
}