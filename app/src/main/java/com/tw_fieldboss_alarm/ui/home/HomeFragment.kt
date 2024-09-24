package com.tw_fieldboss_alarm.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tw_fieldboss_alarm.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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

        // 먼저 inflate 를 하고 나중에 LiveData 로 모니터링 한다.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // UI components 목록
        binding.textHome

        // Button click listener binding
        val setAlarmButton = binding.setAlarmButton
        setAlarmButton.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.putExtra(AlarmClock.EXTRA_HOUR, 9)
            intent.putExtra(AlarmClock.EXTRA_MINUTES, 37)
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            startActivity(intent)
        }
        val removeAlarmButton = binding.removeAlarmButton
        removeAlarmButton.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM)
            intent.putExtra(AlarmClock.EXTRA_HOUR, 9)
            intent.putExtra(AlarmClock.EXTRA_MINUTES, 37)
            startActivity(intent)
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
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