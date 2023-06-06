package com.example.practica5.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practica5.domain.model.vo.MonumentVO

class DetailViewModel : ViewModel() {
    private val monumentMutableLiveData = MutableLiveData<MonumentVO>()
    val monumentLiveData: LiveData<MonumentVO> = monumentMutableLiveData

    fun loadData(monument: MonumentVO) {
        monumentMutableLiveData.value = monument
    }
}