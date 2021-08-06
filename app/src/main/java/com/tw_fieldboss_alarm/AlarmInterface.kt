package com.tw_fieldboss_alarm

import android.content.Context

interface AlarmInterface {
//    fun printAlarmList()

    fun setAlarm(HOUR_OF_DAY:Int, MINUTE:Int=0, SECOND:Int=0,bossNameWithLocation: String="보스이름설정안됨(장소설정안됨)",timeDifference:Int=0)

    fun setAllAlarm()

    fun cancelAlarm(HOUR_OF_DAY:Int, MINUTE:Int=0, SECOND:Int=0)
}