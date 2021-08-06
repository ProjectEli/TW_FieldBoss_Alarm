package com.tw_fieldboss_alarm.ui.home

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tw_fieldboss_alarm.AlarmInterface
import com.tw_fieldboss_alarm.MainActivity
import com.tw_fieldboss_alarm.R
import com.tw_fieldboss_alarm.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var callback: AlarmInterface

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // 먼저 inflate 를 하고 나중에 LiveData 로 모니터링한다.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // UI components 목록
        val textView: TextView = binding.textHome
//        val notificationButton = binding.buttonNotificationHome
//        val fullScreenNotificationButton = binding.buttonFullscreenActivityHome
//        val showNotificationListButton = binding.buttonShowNotificationList

//        // 텍스트 채우기
//        homeViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = getString(R.string.HomeViewString)
//        })

//        // onClickListener definitions
//        notificationButton.setOnClickListener {
//            val snackBar = Snackbar.make(binding.HomeLayout,"상단바 알림을 보냈습니다.",Snackbar.LENGTH_SHORT)
//            snackBar.setAction(R.string.undo_string, SnackBarUndoListener())
//            snackBar.show()
//            sendNotification()
//        }
//
//        fullScreenNotificationButton.setOnClickListener {
////            val intent = Intent(context, FullscreenAlarm::class.java)
////            context?.startActivity(intent)
//            val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
//            val currentTime = System.currentTimeMillis()
//            val calendar: Calendar = Calendar.getInstance(timeZone).apply {
//                timeInMillis = currentTime
//            }
//            callback.setAlarm(calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                calendar.get(Calendar.SECOND)+5,
//                getString(R.string.arkan),
//                    53)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            showNotificationListButton.setOnClickListener {
////                callback.printAlarmList()
//            }
//        } else {
//            showNotificationListButton.setOnClickListener {
//                val snackBar = Snackbar.make(binding.HomeLayout,"롤리팝 이상에서만 지원되는 기능입니다.",Snackbar.LENGTH_SHORT)
//                snackBar.setAction(R.string.undo_string, SnackBarUndoListener())
//                snackBar.show()
//                sendNotification()
//            }
//        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as AlarmInterface
            Log.d("HomeFragment.kt","callback 셋업됨")
        } catch (castException: ClassCastException) {
            Log.d("캐스트 에러","클래스 캐스트 실패")
        }
    }

    private fun sendNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder =
            context?.let { context ->
                NotificationCompat.Builder(context, context.resources.getResourceName(R.id.normal_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                    .setContentTitle("[베리넨 루미] 골론")
                    .setContentText("1분 전!!! 베리넨 루미로 가세요! 어서!") // lower API level or 확장전 한줄알림
                    .setStyle(NotificationCompat.BigTextStyle() // higher API level
                        .bigText("1분 전!!! 베리넨 루미로 가세요! 1분 전!!! 베리넨 루미로 가세요! 1분 전!!! 베리넨 루미로 가세요!"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
//                        .setFullScreenIntent(fullScreenPendingIntent, true)
            }
        with (context?.let { context -> NotificationManagerCompat.from(context) }) {
            if (builder != null) {
                this?.notify(R.id.normal_notification_id, // 일회성임. 이걸 저장해야 나중에 알람 추적 가능
                    builder.build())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}