package com.tw_fieldboss_alarm.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tw_fieldboss_alarm.AlarmsApplication
import com.tw_fieldboss_alarm.R
import com.tw_fieldboss_alarm.alarms.Alarm
import com.tw_fieldboss_alarm.alarms.AlarmViewModel
import com.tw_fieldboss_alarm.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val alarmViewModel: AlarmViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("EliOnCreate","Fragment 생성됨")

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("EliOnCreateView","View 생성됨")
        _binding = FragmentDashboardBinding.inflate(layoutInflater) // 뒤보다 이게 먼저와야됨
        val recyclerView = binding.recyclerview
        recyclerView.adapter = alarmViewModel.adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        populateAlarmList()



//        setFragmentResultListener("requestKey") { key,bundle ->
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
        return binding.root
    }

    private fun populateAlarmList() {
        when (AlarmsApplication.getPrefranceDataBoolean(getString(R.string.enableAllAlarmSwitch)) ) {
            true -> alarmViewModel.bossList.forEach { alarmKey ->
                AlarmsApplication.getPrefranceDataBoolean(alarmKey).let {
                    if (it) { // true 이면
                        alarmViewModel.alarmDetail[alarmKey]?.forEach { element-> alarmViewModel.insert(
                            Alarm(element)
                        )}
                    } else {
                        alarmViewModel.alarmDetail[alarmKey]?.forEach { element-> alarmViewModel.delete(
                            Alarm(element)
                        )}
                    }
                }
            }
            false -> alarmViewModel.deleteAll()
            // else -> alarmViewModel.deleteAll()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}