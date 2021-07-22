package com.example.tw_fieldboss_alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tw_fieldboss_alarm.databinding.ActivityMainBinding
import com.example.tw_fieldboss_alarm.Prefs
import com.example.tw_fieldboss_alarm.databinding.FragmentDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    val fullscreenNotificationChannelId = "fullScreenNotificationChannelId"
//    private val alarmViewModel: AlarmViewModel by viewModels {
//        AlarmViewModelFactory((this?.application as AlarmsApplication).repository)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        createNotificationChannel()
        createAlarmManager()


//        val binding2 = FragmentDashboardBinding.inflate(layoutInflater)
//        val recyclerView = binding2.recyclerview
//        val adapter = AlarmListAdapter()
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // observer한다.
//        alarmViewModel.allAlarms.observe(this, Observer { alarms ->
//            alarms?.let { adapter.submitList(it) }
//        })
//
//        setResultListener("requestKey") { key,bundle ->
//            listOf<String>("00시 0분, 골론","06시 0분, 골론","12시 0분, 골론","18시 0분, 골론",
//                "05시 0분, 골모답", "13시 0분, 골모답", "21시 0분, 골모답").forEach {
//                bundle.getString(it).let { result ->
//                    if (result == "켜짐") {
//                        alarmViewModel.insert(Alarm(it))
//                    } else {
//                        alarmViewModel.delete(Alarm(it))
//                    }
//                }
//            }
//        }
    }


    private fun createAlarmManager() {
        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this,AlarmReceiver::class.java).apply {
            action = Intent.ACTION_CREATE_REMINDER
        }
            .let { intent ->
            PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        }
//
//        val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
//        val calendar: Calendar = Calendar.getInstance(timeZone).apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY,2)
//            set(Calendar.MINUTE,44)
//            set(Calendar.SECOND,0)
//        }
//
//        // 알람시간관련 정확하게 하려면 setRepeating으로는 안된다. setExact로 하고 알람 표시하자마자 다음꺼 생성해야됨
//        // https://superwony.tistory.com/99
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 도즈모드 대응
//            alarmMgr?.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                alarmIntent
//            )
//        }
//        else {
//            alarmMgr?.set( // 킷캣이하버전 대응
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                alarmIntent
//            )
//        }
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
                getString(R.string.high_priority_fullscreen_channel_id),
                fullScreenName,importanceFullscreenAlarm).apply {
                    description = fullScreenChannelDescription
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