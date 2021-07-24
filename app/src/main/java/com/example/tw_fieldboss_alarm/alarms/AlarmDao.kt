package com.example.tw_fieldboss_alarm.alarms

import androidx.room.*
import com.example.tw_fieldboss_alarm.alarms.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm_table ORDER BY alarmType ASC") // 오름차순으로 정렬된거 반환
    fun getAlphabetizedAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    suspend fun deleteAll()

    @Delete
//    suspend fun delete(alarm: Alarm)
    suspend fun delete(vararg alarms: Alarm)
}