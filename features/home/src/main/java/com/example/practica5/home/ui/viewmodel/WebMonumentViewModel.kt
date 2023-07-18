package com.example.practica5.home.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.common.util.MonumentsConstant.EMPTY_INFO
import com.example.practica5.home.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebMonumentViewModel : ViewModel() {
    private val webMonumentLiveData = MutableStateFlow<MonumentVO?>(null)
    fun getWebMonument(): StateFlow<MonumentVO?> = webMonumentLiveData.asStateFlow()

    fun loadMonument(monument: MonumentVO) {
        webMonumentLiveData.value = monument
    }

    fun getWebUrl(context: Context): String {
        val monument = getWebMonument().value
        return if (monument?.urlExtraInformation.isNullOrEmpty()) {
            context.getString(R.string.webMonument__empty_url_info, monument?.name)
        } else {
            monument?.urlExtraInformation ?: EMPTY_INFO
        }
    }

    fun getEmailMessage(context: Context): String {
        val monument = getWebMonument().value
        return context.getString(
            R.string.webMonument__message_email,
            monument?.name,
            monument?.city,
            monument?.urlExtraInformation
        )
    }

    fun clearLiveData() {
        webMonumentLiveData.value = null
    }
}