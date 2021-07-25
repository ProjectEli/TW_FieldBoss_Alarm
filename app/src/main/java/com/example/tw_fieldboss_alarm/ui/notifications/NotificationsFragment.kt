package com.example.tw_fieldboss_alarm.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tw_fieldboss_alarm.*
import com.example.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.example.tw_fieldboss_alarm.databinding.FragmentNotificationsBinding
import com.example.tw_fieldboss_alarm.ui.dashboard.DashboardFragment
import java.lang.ClassCastException
import java.util.*

class NotificationsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var callback: AlarmInterface
    private val alarmViewModel: AlarmViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.alarm_preferences,rootKey)

        _binding = FragmentNotificationsBinding.inflate(layoutInflater)
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val pref: Preference = findPreference(getString(R.string.version_info)) ?: return
        if (pref != null) {
            pref.title = BuildConfig.VERSION_NAME
        }

        if (activity != null) {
            val prefs = Prefs.with(requireActivity())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as AlarmInterface
        } catch (castException: ClassCastException) {
            Log.d("캐스트 에러","클래스 캐스트 실패")
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val value = sharedPreferences?.getBoolean(key,false)
        val valueString = if (value!!) "켜짐" else "꺼짐"

        Toast.makeText(context,"$key 값이 ${valueString}으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
        if (key in alarmViewModel.bossList) {
            alarmViewModel.alarmTimeMap[key]!!.forEach {
                callback.setAlarm(it.hours,it.minutes) // second is automatically 0 by def in interface
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

