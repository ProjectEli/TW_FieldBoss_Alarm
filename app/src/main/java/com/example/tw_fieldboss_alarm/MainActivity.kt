package com.example.tw_fieldboss_alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tw_fieldboss_alarm.databinding.ActivityMainBinding
import com.example.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.example.tw_fieldboss_alarm.alarms.AlarmViewModelFactory
import com.example.tw_fieldboss_alarm.ui.home.SnackBarUndoListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), AlarmInterface {

    private lateinit var binding: ActivityMainBinding
    val fullscreenNotificationChannelId = "fullScreenNotificationChannelId"
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
    }

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
        // observer한다.
        alarmViewModel.allAlarms.observe(this) { alarms ->
            alarms.let { alarmViewModel.adapter.submitList(it) }
        }
    }

    override fun setAlarm(HOUR_OF_DAY: Int, MINUTE: Int, SECOND: Int) {
        //        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            //action = Intent.ACTION_CREATE_REMINDER
            action = resources.getResourceName(R.id.high_priority_fullscreen_channel_id)
            putExtra("title","골론 알람")
            putExtra("text","골론 10분 전")
        }.let { intent ->
            PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
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

        // 알람시간관련 정확하게 하려면 setRepeating으로는 안된다. setExact로 하고 알람 표시하자마자 다음꺼 생성해야됨
        // https://superwony.tistory.com/99
        // setExact에서는 nextAlarmClock을 쓸 수가 없다.
        // https://stackoverflow.com/questions/31257252/alarmmanager-alarmclockinfo-getnextalarmclock-causes-nullpointerexception
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 도즈모드 대응
            Log.d("알람","${alarmTimeString}으로 알람 셋업됨")
            alarmMgr?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
        else {
            alarmMgr?.set( // 킷캣이하버전 대응
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }


    private fun createAlarmManager() {
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // normal notification
            val name = getString(R.string.normal_notification_channel_name)
            val descriptionText = getString(R.string.normal_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel( //앞쪽 name은 하위호환용. null 넣어도 됨
                getString(R.string.normal_notification_channel_id),
                name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
//            notificationManager.createNotificationChannel(channel)

            // full screen notification
            val fullScreenName = "fullScreenNotification"
            val fullScreenChannelDescription = "Full screen channel"
            val importanceFullscreenAlarm = NotificationManager.IMPORTANCE_HIGH
            val channelFullscreenAlarm = NotificationChannel(
                this.resources.getResourceName(R.id.high_priority_fullscreen_channel_id),
                fullScreenName,importanceFullscreenAlarm).apply {
                    description = fullScreenChannelDescription
                    setSound(null,null) // remove sound for importance high
            }
//            notificationManager.createNotificationChannel(channelFullscreenAlarm)

            notificationManager.createNotificationChannels(mutableListOf(
                channel,channelFullscreenAlarm
            ))
        }
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