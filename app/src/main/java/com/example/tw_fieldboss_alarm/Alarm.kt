package com.example.tw_fieldboss_alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class Alarm(@PrimaryKey @ColumnInfo(name="alarmType") val alarmType: String)
