package com.tw_fieldboss_alarm

interface AlarmInterface {
//    fun printAlarmList()

    fun setAlarm(hourOfDay:Int, minutes:Int=0, seconds:Int=0, bossNameWithLocation: String="보스 이름 설정 안됨(장소 설정 안됨)", timeDifference:Int=0)

    fun setAllAlarm()

    fun cancelAlarm(hourOfDay:Int, minutes:Int=0, seconds:Int=0)
}