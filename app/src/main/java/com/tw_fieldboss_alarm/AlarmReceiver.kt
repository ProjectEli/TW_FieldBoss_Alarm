package com.tw_fieldboss_alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tw_fieldboss_alarm.ui.fullscreenalarm.FullscreenAlarm
import java.util.*

class AlarmReceiver : BroadcastReceiver(){

    // https://spriggan4.tistory.com/2
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            context!!.resources.getResourceName(R.id.high_priority_fullscreen_channel_id) -> {
                intent?.extras?.let { extras ->
                    val alarmBossName: String? = extras.getString("alarmBossName")
                    val alarmRemainingTime: String? = extras.getString("alarmRemainingTime")
                    fullscreenNotification(context,alarmBossName,alarmRemainingTime)
                }

            }

            Intent.ACTION_BOOT_COMPLETED -> {

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fullscreenNotification(context: Context?, alarmBossName: String?, alarmRemainingTime: String?) {
        Log.d("알람","알람 실행됨")
        // 자료 굿
        // http://batmask.net/index.php/2021/03/12/786/
        val fullscreenIntent = Intent(context, FullscreenAlarm::class.java).apply {
            action = "fullscreen_activity"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title",alarmBossName)
            putExtra("text",alarmRemainingTime)
        }

        val fullscreenPendingIntent = PendingIntent.getActivity(
            context,0,fullscreenIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder =
            context?.let {
                NotificationCompat.Builder(it,it.resources.getResourceName(R.id.high_priority_fullscreen_channel_id))
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                    .setContentTitle(alarmBossName)
                    .setContentText("$alarmBossName $alarmRemainingTime")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE) // 이거 안써도 될수도있음
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setLocalOnly(true) // 이거도 안써도 될수도 있음
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // max 에서 high 로 바꿈
                    .setContentIntent(fullscreenPendingIntent) // 클릭시 실행할 것
                    .setFullScreenIntent(fullscreenPendingIntent,true) // 그냥 나오는 것
                    //.setTimeoutAfter(600000) // 10 min
            }

        with (context?.let { NotificationManagerCompat.from(it) }) {
            if (builder != null) {
                if (context.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                this?.notify(R.id.high_priority_fullscreen_notification_id,builder.build())
            }
        }

    }

}