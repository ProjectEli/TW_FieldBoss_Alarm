package com.example.tw_fieldboss_alarm.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tw_fieldboss_alarm.R

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.DashboardViewString)
    }
    val text: LiveData<String> = _text
}