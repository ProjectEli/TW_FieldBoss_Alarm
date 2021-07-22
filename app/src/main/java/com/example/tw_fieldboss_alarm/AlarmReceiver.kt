package com.example.tw_fieldboss_alarm

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmReceiver : BroadcastReceiver() {
    // https://spriggan4.tistory.com/2
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            context?.getString(R.string.fullscreen_alarm_action) -> {
                if (context != null) {
                    val extras = intent?.extras
                    val title: String? = extras?.getString("title")
                    val text: String? = extras?.getString("text")
                    fullscreenNotification(context,title,text)
                }
            }
        }
    }

    private fun fullscreenNotification(context: Context?,title: String?, text: String?) {
        Log.d("알람","알람 실행됨")
        // 자료 굿
        // http://batmask.net/index.php/2021/03/12/786/
        val fullscreenIntent = Intent(context,FullscreenAlarm::class.java).apply {
            action = "fullscreen_activity"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val fullscreenPendingIntent = PendingIntent.getActivity(
            context,0,fullscreenIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var builder =
            context?.let {context ->
                NotificationCompat.Builder(context,context.getString(R.string.high_priority_fullscreen_channel_id))
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE) // 이거 안써도 될수도있음
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setLocalOnly(true) // 이거도 안써도 될수도 있음
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // max에서 high로 바꿈
                    .setContentIntent(fullscreenPendingIntent) // 클릭시 실행할 것
                    .setFullScreenIntent(fullscreenPendingIntent,true) // 그냥 나오는 것
            }

        with (context?.let { context -> NotificationManagerCompat.from(context) }) {
            if (builder != null) {
                this?.notify(R.id.high_priority_fullscreen_notification_id,builder.build())
            }
        }
    }
}