package com.tw_fieldboss_alarm.ui.home

import android.content.Intent
import android.provider.AlarmClock
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tw_fieldboss_alarm.R

class HomeViewModel : ViewModel() {

    private val _textInt = MutableLiveData<Int>().apply {
        value = R.string.HomeViewString
    }
    val text: LiveData<Int> = _textInt
}