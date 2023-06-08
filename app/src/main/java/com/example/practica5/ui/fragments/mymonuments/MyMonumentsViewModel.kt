package com.example.practica5.ui.fragments.mymonuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositorySingleton
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetMyMonumentsUseCase
import kotlinx.coroutines.launch

class MyMonumentsViewModel : ViewModel() {
    private val myMonumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getMyMonumentsList(): LiveData<List<MonumentVO>?> = myMonumentsMutableLiveData

    private val getMyMonumentsUseCase: GetMyMonumentsUseCase = GetMyMonumentsUseCase(MonumentRepositorySingleton.monumentRepository)

    init {
        getMyMonuments()
    }

    fun getMyMonuments() {
        viewModelScope.launch {
            val result = getMyMonumentsUseCase()
            val monumentListVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }
            if (!result.isNullOrEmpty()) {
                myMonumentsMutableLiveData.postValue(monumentListVO)
            } else {
                myMonumentsMutableLiveData.postValue(emptyList())
            }
        }
    }
}