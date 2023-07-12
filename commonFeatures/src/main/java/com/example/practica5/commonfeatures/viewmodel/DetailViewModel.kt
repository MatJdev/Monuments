package com.example.practica5.commonfeatures.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica5.common.util.MonumentMapper
import com.example.practica5.commonfeatures.domain.DeleteMonumentUseCase
import com.example.practica5.model.vo.MonumentVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private var deleteMonumentUseCase: DeleteMonumentUseCase): ViewModel() {
    private val monumentMutableLiveData = MutableLiveData<MonumentVO>()
    fun getMonument(): LiveData<MonumentVO> = monumentMutableLiveData

    private val removeCompleteLiveData = MutableLiveData<Boolean>()
    fun getRemoveComplete(): LiveData<Boolean> = removeCompleteLiveData

    fun loadData(monument: MonumentVO) {
        monumentMutableLiveData.value = monument
    }

    fun deleteMonument(monument: MonumentVO) {
        viewModelScope.launch {
            deleteMonumentUseCase.setMonument(MonumentMapper.mapMonumentVoToBo(monument))
            deleteMonumentUseCase.invoke()
            removeCompleteLiveData.value = true
        }
    }

    fun setDeleteToFalse() {
        removeCompleteLiveData.value = false
    }
}