package com.tw_fieldboss_alarm

import android.app.Application
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.tw_fieldboss_alarm.alarms.AlarmTime
import com.tw_fieldboss_alarm.alarms.AlarmViewModel

class AlarmService : Service() {

    private lateinit var callback: AlarmInterface

    private val bossList = listOf<String>("골론","골모답","아칸","스페르첸드","프라바 방어전")
    private val bossMapWithLocationMap = mapOf(
        bossList[0] to R.string.golon,
        bossList[1] to R.string.golmodap,
        bossList[2] to R.string.arkan,
        bossList[3] to R.string.sperchend,
        bossList[4] to R.string.defense
    )
    // val alarmKeyList = bossList.map { "$it 알람" }
    private val alarmTimeMap = mapOf(
        bossList[0] to (0..18 step 6).map { AlarmTime(it) }, // 골론
        bossList[1] to listOf(5,13,21).map { AlarmTime(it) }, // 골모답
        bossList[2] to listOf(14,21).map { AlarmTime(it,30) }, // 아칸
        bossList[3] to listOf(1,4,8,16,19,23).map { AlarmTime(it) }, // 스페르첸드
        bossList[4] to (0..22 step 2).map { AlarmTime(it) }
    )

    private val alarmTimeDifferenceList = listOf(10,5,3,1)
    private val alarmTimeDifferenceStringList: List<String> = alarmTimeDifferenceList.map {
        "${it}분 전"
    }
    private val alarmTimeDifferenceMap = alarmTimeDifferenceList.map{
        "${it}분 전" to it
    }.toMap()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}