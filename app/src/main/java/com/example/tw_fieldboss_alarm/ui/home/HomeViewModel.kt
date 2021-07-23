package com.example.tw_fieldboss_alarm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tw_fieldboss_alarm.R

class HomeViewModel : ViewModel() {

    private val _textInt = MutableLiveData<Int>().apply {
        value = R.string.HomeViewString
    }
    val text: LiveData<Int> = _textInt
}