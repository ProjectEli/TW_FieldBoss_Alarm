package com.example.tw_fieldboss_alarm

interface AlarmInterface {
//    fun printAlarmList()

    fun setAlarm(HOUR_OF_DAY:Int, MINUTE:Int=0, SECOND:Int=0)
}