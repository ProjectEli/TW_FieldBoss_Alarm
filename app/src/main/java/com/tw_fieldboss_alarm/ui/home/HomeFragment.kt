package com.tw_fieldboss_alarm.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tw_fieldboss_alarm.AlarmInterface
import com.tw_fieldboss_alarm.databinding.FragmentHomeBinding

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

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // 먼저 inflate 를 하고 나중에 LiveData 로 모니터링한다.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // UI components 목록
        binding.textHome
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}