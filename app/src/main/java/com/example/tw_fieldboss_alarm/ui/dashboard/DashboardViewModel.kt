package com.example.tw_fieldboss_alarm.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tw_fieldboss_alarm.R

// Viewmodel에 context를 안주는게 좋다. 즉 AndroidViewModel을 쓰지 말란얘기
// https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
// 그렇게 하는 방법에 관한 글
// https://oozou.com/blog/accessing-resource-string-in-viewmodel-without-leaking-context-146
class DashboardViewModel : ViewModel() {

}