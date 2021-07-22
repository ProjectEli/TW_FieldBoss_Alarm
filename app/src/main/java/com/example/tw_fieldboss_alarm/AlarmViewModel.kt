package com.example.tw_fieldboss_alarm

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {
    // 여기서 Flow를 LioveData로 바꾼다.
    val allAlarms : LiveData<List<Alarm>> = repository.allAlarms.asLiveData()

    // Non-blocking 방법으로 insert를 위해 새 코루틴을 실행
    fun insert(alarm: Alarm) = viewModelScope.launch {
        repository.insert(alarm)
    }

    fun delete(alarm: Alarm) = viewModelScope.launch {
        repository.delete(alarm)
    }
}

class AlarmViewModelFactory(private val repository: AlarmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}