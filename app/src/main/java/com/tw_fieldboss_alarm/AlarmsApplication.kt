package com.tw_fieldboss_alarm

import android.app.Application

class AlarmsApplication: Application() {

    companion object {
        private var _instance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }

    fun get(): Application? {
        return _instance
    }





}