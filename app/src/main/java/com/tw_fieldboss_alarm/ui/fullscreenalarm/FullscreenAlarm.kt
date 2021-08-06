package com.tw_fieldboss_alarm.ui.fullscreenalarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.tw_fieldboss_alarm.*
import com.tw_fieldboss_alarm.AlarmsApplication.Companion.getSharedPreferences
import com.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.tw_fieldboss_alarm.alarms.AlarmViewModelFactory
import com.tw_fieldboss_alarm.databinding.ActivityFullscreenAlarmBinding
import com.tw_fieldboss_alarm.ui.home.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*


class FullscreenAlarm : AppCompatActivity(), AlarmInterface{

    private lateinit var binding: ActivityFullscreenAlarmBinding
    private val alarmViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((application as AlarmsApplication).repository)
    }
    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            android.os.Build.VERSION.SDK_INT >= 27 -> {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }
        }
        alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
        // setContentView(R.layout.activity_fullscreen_alarm)

        binding = ActivityFullscreenAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let { extras ->
            val title = extras.getString("title")
            val text = extras.getString("text")
            Log.d("FullscreenAlarm.kt","제목: $title")
            Log.d("FullscreenAlarm.kt","내용: $text")
            binding.textView.text = title
            binding.textViewAlarmTime.text = text
        }

//        val snoozeButton = binding.snoozeButton as Button
//        snoozeButton.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Log.d("터치","터치됨")
//                }
//                MotionEvent.ACTION_UP -> {
//                    Log.d("터치업","터치업됨")
//                }
//            }
//            false
//        }
        setAllAlarm()
    }

    // https://stackoverflow.com/questions/28922521/how-to-cancel-alarm-from-alarmmanager/28922621
    // Cancel 할 경우에도 requestcode 를 포함하여 완전히 같은걸로 만들어야 한다.
    override fun cancelAlarm(HOUR_OF_DAY: Int, MINUTE: Int, SECOND: Int) {
        val requestCode: Int = HOUR_OF_DAY.times(10000) + MINUTE.times(100) + SECOND
        //        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            //action = Intent.ACTION_CREATE_REMINDER
            action = resources.getResourceName(R.id.high_priority_fullscreen_channel_id)
//            putExtra("title","골론 알람")
//            putExtra("text","골론 10분 전")
        }.let { intent ->
            PendingIntent.getBroadcast(this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
        val period = 1000 * 60 * 60 * 24 // 하루 반복
        val currentTime = System.currentTimeMillis()
        val calendar: Calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY,HOUR_OF_DAY)
            set(Calendar.MINUTE,MINUTE)
            set(Calendar.SECOND,SECOND)
        }

        // 시간 비교 후 알람시간이 미래가 될 때까지 계속 더함
        // https://link2me.tistory.com/1719
        while (currentTime > calendar.timeInMillis) {
            calendar.timeInMillis += period
        }

        val simpleDateFormat = SimpleDateFormat("MM월 dd일 EEEE HH시 mm분", Locale.KOREA)
        val alarmTimeString = simpleDateFormat.format(calendar.timeInMillis)

        alarmMgr.cancel(alarmIntent)
        Log.d("알람","$alarmTimeString 알람 취소됨")
    }

    override fun setAlarm(HOUR_OF_DAY: Int, MINUTE: Int, SECOND: Int, bossNameWithLocation: String, timeDifference: Int) {
        //        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode: Int = HOUR_OF_DAY.times(10000) + MINUTE.times(100) + SECOND
        alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            //action = Intent.ACTION_CREATE_REMINDER
            action = resources.getResourceName(R.id.high_priority_fullscreen_channel_id)
            putExtra("alarmBossName","${bossNameWithLocation}")
            putExtra("alarmRemainingTime","${timeDifference}분 전")
        }.let { intent ->
            PendingIntent.getBroadcast(this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
        val period = 1000 * 60 * 60 * 24 // 하루 반복
        val currentTime = System.currentTimeMillis()
        val calendar: Calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY,HOUR_OF_DAY)
            set(Calendar.MINUTE,MINUTE)
            set(Calendar.SECOND,SECOND)
        }

        // 시간 비교 후 알람시간이 미래가 될 때까지 계속 더함
        // https://link2me.tistory.com/1719
        while (currentTime > calendar.timeInMillis) {
            calendar.timeInMillis += period
        }

        val simpleDateFormat = SimpleDateFormat("MM월 dd일 EEEE HH시 mm분", Locale.KOREA)
        val alarmTimeString = simpleDateFormat.format(calendar.timeInMillis)

        // 알람시간관련 정확하게 하려면 setRepeating 으로는 안된다. setExact 로 하고 알람 표시하자마자 다음꺼 생성해야됨
        // https://superwony.tistory.com/99
        // setExact 에서는 nextAlarmClock 을 쓸 수가 없다.
        // https://stackoverflow.com/questions/31257252/alarmmanager-alarmclockinfo-getnextalarmclock-causes-nullpointerexception
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 도즈모드 대응
            Log.d("알람","$bossNameWithLocation ${timeDifference}분 전, ${alarmTimeString}으로 알람 셋업됨")
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
        else {
            alarmMgr.set( // 킷캣이하버전 대응
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }

    override fun setAllAlarm() {
        val sharedPreferences = AlarmsApplication.getSharedPreferences()
        alarmViewModel.bossList.forEach{ bossName ->
            alarmViewModel.alarmTimeMap[bossName]!!.forEach { alarmTime ->
                alarmViewModel.alarmTimeDifferenceStringList.forEach { alarmTimeDifferenceString ->
                    val alarmMinuteDifference: Int = alarmViewModel.alarmTimeDifferenceMap[alarmTimeDifferenceString]!!

                    sharedPreferences.getBoolean(bossName,false).let { bossOn ->
                        sharedPreferences.getBoolean(alarmTimeDifferenceString,false).let { alarmTimeDifferenceOn ->
                            if (bossOn && alarmTimeDifferenceOn) {
                                setAlarm(alarmTime.hours,alarmTime.minutes-alarmMinuteDifference,
                                    bossNameWithLocation = getString(alarmViewModel.bossMapWithLocationMap[bossName]!!),
                                    timeDifference = alarmMinuteDifference)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}