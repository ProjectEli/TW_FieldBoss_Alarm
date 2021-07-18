package com.example.tw_fieldboss_alarm.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tw_fieldboss_alarm.R

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.HomeViewString)
    }
    val text: LiveData<String> = _text
}