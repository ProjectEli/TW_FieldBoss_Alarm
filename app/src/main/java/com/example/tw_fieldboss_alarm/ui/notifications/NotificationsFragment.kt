package com.example.tw_fieldboss_alarm.ui.notifications

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tw_fieldboss_alarm.AlarmsApplication
import com.example.tw_fieldboss_alarm.BuildConfig
import com.example.tw_fieldboss_alarm.Prefs
import com.example.tw_fieldboss_alarm.R
import com.example.tw_fieldboss_alarm.databinding.FragmentNotificationsBinding
import com.example.tw_fieldboss_alarm.ui.dashboard.DashboardFragment
import java.util.*

class NotificationsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.alarm_preferences,rootKey)

        _binding = FragmentNotificationsBinding.inflate(layoutInflater)
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val pref: Preference = findPreference(R.id.versionName.toString()) ?: return
        if (pref != null) {
            pref.title = BuildConfig.VERSION_NAME
        }

        if (activity != null) {
            val prefs = Prefs.with(requireActivity())
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
        when (key) {
            "골론 알람" -> {
                setFragmentResult("requestKey",
                    bundleOf("00시 0분, 골론" to valueString, "06시 0분, 골론" to valueString,
                        "12시 0분, 골론" to valueString, "18시 0분, 골론" to valueString
                    ))


//                val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
//                val calendar: Calendar = Calendar.getInstance(timeZone).apply {
//                    timeInMillis = System.currentTimeMillis()
//                    set(Calendar.HOUR_OF_DAY,2)
//                    set(Calendar.MINUTE,44)
//                    set(Calendar.SECOND,0)
//                }
//
//                // 알람시간관련 정확하게 하려면 setRepeating으로는 안된다. setExact로 하고 알람 표시하자마자 다음꺼 생성해야됨
//                // https://superwony.tistory.com/99
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 도즈모드 대응
//                    alarmMgr?.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.timeInMillis,
//                        alarmIntent
//                    )
//                }
//                else {
//                    alarmMgr?.set( // 킷캣이하버전 대응
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.timeInMillis,
//                        alarmIntent
//                    )
//                }
            }

            "골모답 알람" -> {
                setFragmentResult("requestKey",
                    bundleOf("05시 0분, 골모답" to valueString, "13시 0분, 골모답" to valueString,
                        "21시 0분, 골모답" to valueString
                    ))
            }
            else -> {

            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

