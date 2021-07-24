package com.example.tw_fieldboss_alarm.alarms

import androidx.lifecycle.*
import kotlinx.coroutines.launch


class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {
    // 여기서 Flow를 LioveData로 바꾼다.
    val allAlarms : LiveData<List<Alarm>> = repository.allAlarms.asLiveData()
    val adapter = AlarmListAdapter()

    // 다국어 지원을 하고싶은 경우
    // https://www.charlezz.com/?p=1187
    // 기본적으로 viewmodel은 activity 재생성에도 안사라지는 특성이 있다.
    val alarmKeyList = listOf<String>("골론 알람","골모답 알람","아칸 알람","스페르첸드 알람","프라바 방어전 알람")
    val alarmDetail = mapOf(
        alarmKeyList[0] to listOf<String>("00시 00분, 골론","06시 00분, 골론","12시 00분, 골론","18시 00분, 골론"),
        alarmKeyList[1] to listOf<String>("05시 00분, 골모답", "13시 00분, 골모답", "21시 00분, 골모답"),
        alarmKeyList[2] to listOf<String>("14시 30분, 아칸", "21시 30분, 아칸"),
        alarmKeyList[3] to listOf<String>("01시 00분, 스페르첸드", "04시 00분, 스페르첸드","08시 00분, 스페르첸드",
            "16시 00분, 스페르첸드", "19시 00분, 스페르첸드", "23시 00분, 스페르첸드"),
        alarmKeyList[4] to listOf<String>("00시 00분, 프라바 방어전", "02시 00분, 프라바 방어전",
            "04시 00분, 프라바 방어전", "06시 00분, 프라바 방어전", "08시 00분, 프라바 방어전",
            "10시 00분, 프라바 방어전","12시 00분, 프라바 방어전","14시 00분, 프라바 방어전",
            "16시 00분, 프라바 방어전","18시 00분, 프라바 방어전","20시 00분, 프라바 방어전",
            "22시 00분, 프라바 방어전")
    )

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
