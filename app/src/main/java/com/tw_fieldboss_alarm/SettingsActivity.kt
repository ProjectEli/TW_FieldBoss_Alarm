package com.tw_fieldboss_alarm

import NsecTimerChecker
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import java.util.*
import kotlin.concurrent.timer

class SettingsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


//        var CHANNEL_ID = "FieldBoss_notification"
//        var textTitle = "필드보스 알람"
//        var textContent = "필드보스 알람임"
//        var notificationId = 307;
//        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(textTitle)
//            .setContentText(textContent)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        with(NotificationManagerCompat.from(this)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(notificationId, builder.build())
//        }
        when (Build.VERSION.SDK_INT) {
            in 1..18 -> {
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1000) // 200ms
            }
            else -> {
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                val effect = VibrationEffect.createOneShot(
                    1000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            }
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private var timerChecker10min: NsecTimerChecker = NsecTimerChecker(600)
        private var timerChecker5min: NsecTimerChecker = NsecTimerChecker(300)
        private var timerChecker3min: NsecTimerChecker = NsecTimerChecker(180)
        private var timerChecker1min: NsecTimerChecker = NsecTimerChecker(60)
        private var _timerInterval:Long = 5000 // [ms]
        private var packageName = activity?.packageName;

        var alarm10minEnabled = false;
        var alarm5minEnabled = false;
        var alarm3minEnabled = false;
        var alarm1minEnabled = false;



        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            startAlarmSequence()
        }

        private fun startAlarmSequence() {
            timer(period = _timerInterval){
                onTimedEvent()
            }
        }

        private fun onTimedEvent(){
//            val intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
            updateBossEnabled(timerChecker10min)
            updateBossEnabled(timerChecker5min)
            updateBossEnabled(timerChecker3min)
            updateBossEnabled(timerChecker1min)

            var currentTime =Date(Calendar.getInstance().timeInMillis)
            val fullAlarmStringBuilder = java.lang.StringBuilder()

            val alarm10minPreference: SwitchPreference? = findPreference("alarm_10min")
            val alarm5minPreference: SwitchPreference? = findPreference("alarm_5min")
            val alarm3minPreference: SwitchPreference? = findPreference("alarm_3min")
            val alarm1minPreference: SwitchPreference? = findPreference("alarm_1min")

            if (alarm10minPreference != null) {
                alarm10minEnabled = alarm10minPreference.isChecked
            }
            if (alarm5minPreference != null) {
                alarm10minEnabled = alarm5minPreference.isChecked
            }
            if (alarm3minPreference != null) {
                alarm10minEnabled = alarm3minPreference.isChecked
            }
            if (alarm1minPreference != null) {
                alarm10minEnabled = alarm1minPreference.isChecked
            }

            if (alarm1minEnabled) {
                fullAlarmStringBuilder.append(timerChecker1min.checkAlarmText(currentTime,_timerInterval))
            }
            if (alarm3minEnabled) {
                fullAlarmStringBuilder.append(timerChecker3min.checkAlarmText(currentTime,_timerInterval))
            }
            if (alarm5minEnabled) {
                fullAlarmStringBuilder.append(timerChecker5min.checkAlarmText(currentTime,_timerInterval))
            }
            if (alarm10minEnabled) {
                fullAlarmStringBuilder.append(timerChecker10min.checkAlarmText(currentTime,_timerInterval))
            }

            // 장기적으로 다음과 같이 가야할 것으로 예상함
            // https://developer.android.com/training/scheduling/alarms
            if (! fullAlarmStringBuilder.toString()?.isNullOrEmpty())
            {
//                Log.d("fullstring",fullAlarmStringBuilder.toString())
//                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // https://codechacha.com/ko/android-vibration/

            }
            else
            {
                Log.d("fullstring2","no built string")
            }

//            Log.d("Eli",currentTime.time.toString())
//            currentTime.add(Calendar.SECOND,-10)
//            Log.d("Eli",currentTime.time.toString())
        }

        private fun updateBossEnabled(timerChecker: NsecTimerChecker){
//        activity?.getSharedPreferences(getString(R.string.golon), Context.MODE_PRIVATE)
            val golonPreference: SwitchPreference? = findPreference("golon")
            val golmodapPreference: SwitchPreference? = findPreference("golmodap")
            val arkanPreference: SwitchPreference? = findPreference("arkan")
            val sperchendPreference: SwitchPreference? = findPreference("sperchend")
            val defensePreference: SwitchPreference? = findPreference("defense")


            if (golonPreference != null) {
                timerChecker.golonAlarmEnabled = golonPreference.isChecked
            }
            if (golmodapPreference != null) {
                timerChecker.golmodapAlarmEnabled = golmodapPreference.isChecked
            }
            if (arkanPreference != null) {
                timerChecker.arkanAlarmEnabled = arkanPreference.isChecked
            }
            if (sperchendPreference != null) {
                timerChecker.sperchendAlarmEnabled = sperchendPreference.isChecked
            }
            if (defensePreference != null) {
                timerChecker.defenseAlarmEnabled = defensePreference.isChecked
            }
        }
    }
}