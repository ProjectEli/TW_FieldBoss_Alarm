package com.tw_fieldboss_alarm

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class ResourcesProvider(val context: Context) {
    companion object{
        lateinit var context: Context
        fun getString(stringId: Int): String {
            return context.getString(stringId)
        }
    }
}