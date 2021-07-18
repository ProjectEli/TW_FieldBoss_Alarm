package com.example.tw_fieldboss_alarm.ui.home

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tw_fieldboss_alarm.FullscreenAlarm
import com.example.tw_fieldboss_alarm.R
import com.example.tw_fieldboss_alarm.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    val notificationId = 3389 // 일회성임. 이걸 저장해야 나중에 알람 추적 가능

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // 알림 보내기 버튼
        val notificationButton = binding.buttonNotification//.findViewById<Button>(R.id.button_notification)

        notificationButton.setOnClickListener {
            val snackBar = Snackbar.make(binding.HomeLayout,"상단바 알림을 보냈습니다.",Snackbar.LENGTH_SHORT)
            snackBar.setAction(R.string.undo_string, SnackBarUndoListener())
            snackBar.show()

            val intent = Intent(context, HomeFragment::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

//            val fullScreenIntent = Intent(context, FullScreenActivity::class.java)
//            val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
//                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            var builder =
                context?.let { context ->
                    NotificationCompat.Builder(context, getString(R.string.notification_channel_name))
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
                    this?.notify(notificationId,builder.build())
                }
            }
        }

        // 전체화면 알림 띄우기 버튼
        val fullScreennotificationButton = binding.buttonFullscreenActivity
        fullScreennotificationButton.setOnClickListener {
            val intent = Intent(context,FullscreenAlarm::class.java)
            context?.startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}