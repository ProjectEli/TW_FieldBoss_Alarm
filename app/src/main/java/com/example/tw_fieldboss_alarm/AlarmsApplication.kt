package com.example.tw_fieldboss_alarm

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class AlarmsApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AlarmRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { AlarmRepository(database.alarmDao()) }



    companion object {
        private var _instance: Application? = null
        private var _preferences: SharedPreferences? = null

        fun getSharedPreferences(): SharedPreferences {
            if (_preferences == null) _preferences =
                PreferenceManager.getDefaultSharedPreferences(_instance)
            return _preferences!!
        }
        //set methods
        fun setPreferences(key: String?, value: String?) {
            getSharedPreferences().edit().putString(key, value).commit()
        }

        fun setPreferences(key: String?, value: Long) {
            getSharedPreferences().edit().putLong(key, value).commit()
        }

        fun setPreferences(key: String?, value: Int) {
            getSharedPreferences().edit().putInt(key, value).commit()
        }

        fun setPreferencesBoolean(key: String?, value: Boolean) {
            getSharedPreferences().edit().putBoolean(key, value).commit()
        }

        //get methods
        fun getPrefranceData(key: String?): String? {
            return getSharedPreferences().getString(key, "")
        }

        fun getPrefranceDataInt(key: String?): Int {
            return getSharedPreferences().getInt(key, 0)
        }

        fun getPrefranceDataBoolean(key: String?): Boolean {
            return getSharedPreferences().getBoolean(key, false)
        }

        fun getPrefranceDataLong(interval: String?): Long {
            return getSharedPreferences().getLong(interval, 0)
        }
    }



    override fun onCreate() {
        super.onCreate()
        _instance = this
    }

    fun get(): Application? {
        return _instance
    }





}