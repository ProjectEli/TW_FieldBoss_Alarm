package com.tw_fieldboss_alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.tw_fieldboss_alarm.alarms.AlarmViewModelFactory
import com.tw_fieldboss_alarm.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),AlarmInterface {

    private lateinit var binding: ActivityMainBinding
    private val alarmViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((application as AlarmsApplication).repository)
    }
    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavBar()
        createNotificationChannel()
        createAlarmManager()
        observeAlarmList()
        setAllAlarm()
    }

    override fun setAllAlarm() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
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

//    private fun startAlarmService() {
//        val alarmServiceIntent = Intent(this,AlarmService::class.java)
//        startService(alarmServiceIntent)
//    }

    private fun setupNavBar() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home , R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun observeAlarmList() {
        // observe 한다.
        alarmViewModel.allAlarms.observe(this) { alarms ->
            alarms.let { alarmViewModel.adapter.submitList(it) }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
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


    private fun createAlarmManager() {
        alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // normal notification
            val channel = NotificationChannel( //앞쪽 name 은 하위호환용. null 넣어도 됨
                resources.getResourceName(R.id.normal_notification_channel_id),
                getString(R.string.normal_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            .apply {
                description = getString(R.string.normal_notification_channel_description)
                setSound(null,null) // remove sound
            }
            // Register the channel with the system
//            notificationManager.createNotificationChannel(channel)

            // full screen notification
            val channelFullscreenAlarm = NotificationChannel(
                resources.getResourceName(R.id.high_priority_fullscreen_channel_id),
                "fullScreenNotification",
                NotificationManager.IMPORTANCE_HIGH)
            .apply {
                description = "Full screen channel"
                setSound(null,null) // remove sound for importance high
            }
//            notificationManager.createNotificationChannel(channelFullscreenAlarm)

            notificationManager.createNotificationChannels(mutableListOf(
                channel,channelFullscreenAlarm
            ))
        }
    }

    override fun onDestroy() {
        Log.d("종료","앱 종료됨")
        super.onDestroy()
    }

//    private fun turnScreenOnAndKeyguardOff(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            setShowWhenLocked(true)
//            setTurnScreenOn(true)
//            window.addFlags(
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
//        } else {
//            window.addFlags(
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED    // deprecated api 27
//                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD     // deprecated api 26
//                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON   // deprecated api 27
//                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
//        }
//        val keyguardMgr = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            keyguardMgr.requestDismissKeyguard(this, null)
//        }
//    }

}