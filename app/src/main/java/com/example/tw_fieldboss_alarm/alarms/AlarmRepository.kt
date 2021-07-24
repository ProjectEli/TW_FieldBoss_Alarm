package com.example.tw_fieldboss_alarm.alarms

import androidx.annotation.WorkerThread
import com.example.tw_fieldboss_alarm.alarms.Alarm
import com.example.tw_fieldboss_alarm.alarms.AlarmDao
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {

    // Observed Flow가 observer에 데이터 변했는지 알려준다.
    val allAlarms: Flow<List<Alarm>> = alarmDao.getAlphabetizedAlarms()


    // Room은 기본으로 메인스레드 아닌곳에서 suspended query를 넣기 때문에 long running 작업을 구현할 필요 없음음
   @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    @WorkerThread
    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    @WorkerThread
    suspend fun deleteAll() {
        alarmDao.deleteAll()
    }
}