package com.tw_fieldboss_alarm.alarms

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class Alarm(@PrimaryKey @ColumnInfo(name="alarmType") val alarmType: String)

data class AlarmTime(val hours: Int, val minutes: Int = 0)