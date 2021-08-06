package com.tw_fieldboss_alarm.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tw_fieldboss_alarm.AlarmInterface
import com.tw_fieldboss_alarm.BuildConfig
import com.tw_fieldboss_alarm.Prefs
import com.tw_fieldboss_alarm.R
import com.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.tw_fieldboss_alarm.databinding.FragmentNotificationsBinding

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
        pref.title = BuildConfig.VERSION_NAME

        if (activity != null) {
            val prefs = Prefs.with(requireActivity())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as AlarmInterface
            Log.d("NotificationFragment.kt","callback 셋업됨")
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
        val timeDifference = 9

        if (key == "전체 알람 활성화") {
            alarmViewModel.bossList.forEach{ bossName ->
                alarmViewModel.alarmTimeMap[bossName]!!.forEach { alarmTime ->
                    alarmViewModel.alarmTimeDifferenceStringList.forEach { alarmTimeDifferenceString ->
                        val alarmMinuteDifference: Int = alarmViewModel.alarmTimeDifferenceMap[alarmTimeDifferenceString]!!
                        if (value) {
                            sharedPreferences.getBoolean(bossName,false).let { bossOn ->
                                sharedPreferences.getBoolean(alarmTimeDifferenceString,false).let { alarmTimeDifferenceOn ->
                                    if (bossOn && alarmTimeDifferenceOn) {
                                        callback.setAlarm(alarmTime.hours,alarmTime.minutes-alarmMinuteDifference-timeDifference,
                                            bossNameWithLocation = getString(alarmViewModel.bossMapWithLocationMap[bossName]!!),
                                            timeDifference = alarmMinuteDifference+timeDifference)
                                    }
                                }
                            }
                        }
                        else {
                            callback.cancelAlarm(alarmTime.hours,alarmTime.minutes-alarmMinuteDifference-timeDifference)
                        }
                    }
                }
            }
        }

        // 필드보스에 있는거 켜고 끌 때
        if (key in alarmViewModel.bossList) {
            alarmViewModel.alarmTimeMap[key]!!.forEach {
                alarmViewModel.alarmTimeDifferenceStringList.forEach { alarmTimeDifferenceString ->
                    sharedPreferences.getBoolean(alarmTimeDifferenceString,true).let{ alarmTimeDifferenceOn ->
                        if (alarmTimeDifferenceOn) {
                            val alarmMinuteDifference: Int = alarmViewModel.alarmTimeDifferenceMap[alarmTimeDifferenceString]!!
                            if (value) {
                                callback.setAlarm(it.hours,it.minutes-alarmMinuteDifference-timeDifference,
                                    bossNameWithLocation = getString(alarmViewModel.bossMapWithLocationMap[key]!!),
                                    timeDifference = alarmMinuteDifference+timeDifference) // second is automatically 0 by def in interface
                            }
                            else {
                                callback.cancelAlarm(it.hours,it.minutes-alarmMinuteDifference-timeDifference) // second is automatically 0 by def in interface
                            }
                        }
                    }
                }
            }
        }
        else if (key in alarmViewModel.alarmTimeDifferenceStringList) {
            val alarmMinuteDifference: Int = alarmViewModel.alarmTimeDifferenceMap[key]!!
            alarmViewModel.bossList.forEach{ bossName ->
                // boss on 되었는지 확인(루프 들어가기 전에 계산절약용)
                sharedPreferences.getBoolean(bossName,true).let { bossOn ->
                    if (bossOn) {
                        alarmViewModel.alarmTimeMap[bossName]!!.forEach{
                            if (value) {
                                callback.setAlarm(it.hours,it.minutes-alarmMinuteDifference-timeDifference,
                                    bossNameWithLocation = getString(alarmViewModel.bossMapWithLocationMap[bossName]!!),
                                    timeDifference = alarmMinuteDifference+timeDifference) // second is automatically 0 by def in interface
                            }
                            else {
                                callback.cancelAlarm(it.hours,it.minutes-alarmMinuteDifference-timeDifference) // second is automatically 0 by def in interface
                            }
                        }
                    }
                }
            }
        }
//
//
//        if (value) {
//            if (key in alarmViewModel.bossList) {
//                alarmViewModel.alarmTimeMap[key]!!.forEach {
//                    callback.setAlarm(it.hours,it.minutes-timeDifference) // second is automatically 0 by def in interface
//                }
//            }
//        }
//        else {
//            if (key in alarmViewModel.bossList) {
//                alarmViewModel.alarmTimeMap[key]!!.forEach {
//
//                }
//            }
//        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

