package com.tw_fieldboss_alarm.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {
    // 여기서 Flow를 LioveData로 바꾼다.
    val allAlarms : LiveData<List<Alarm>> = repository.allAlarms.asLiveData()
    val adapter = AlarmListAdapter()
    val f = DecimalFormat("00") // 2자리 숫자


    // 다국어 지원을 하고싶은 경우
    // https://www.charlezz.com/?p=1187
    // 기본적으로 viewmodel은 activity 재생성에도 안사라지는 특성이 있다.
    val bossList = listOf<String>("골론","골모답","아칸","스페르첸드","프라바 방어전")
    // val alarmKeyList = bossList.map { "$it 알람" }
    val alarmTimeMap = mapOf(
        bossList[0] to (0..18 step 6).map { AlarmTime(it) }, // 골론
        bossList[1] to listOf(5,13,21).map { AlarmTime(it) }, // 골모답
        bossList[2] to listOf(14,21).map { AlarmTime(it,30) }, // 아칸
        bossList[3] to listOf(1,4,8,16,19,23).map { AlarmTime(it) }, // 스페르첸드
        bossList[4] to (0..22 step 2).map { AlarmTime(it) }
    )
    val alarmDetail = alarmTimeMap.map {
        it.key to it.value.map { alarm ->"${f.format(alarm.hours)}시 ${f.format(alarm.minutes)}분, ${it.key}" }
    }.toMap()

    // Non-blocking 방법으로 insert를 위해 새 코루틴을 실행
    fun insert(alarm: Alarm) = viewModelScope.launch {
        repository.insert(alarm)
    }

    fun delete(alarm: Alarm) = viewModelScope.launch {
        repository.delete(alarm)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
