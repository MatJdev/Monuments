package com.example.practica5.launcher.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LauncherViewModel : ViewModel() {
    private val navigateToDestinationMutableLiveData = MutableLiveData<Int>()
    val navigateToDestinationLiveData: LiveData<Int>
        get() = navigateToDestinationMutableLiveData

    fun navigateWithDelay(delayMillis: Long, destinationId: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToDestinationMutableLiveData.value = destinationId
        }, delayMillis)
    }
}