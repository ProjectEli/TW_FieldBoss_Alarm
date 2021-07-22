package com.example.tw_fieldboss_alarm

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class AlarmsApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AlarmRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { AlarmRepository(database.alarmDao()) }
}