package com.example.tw_fieldboss_alarm.ui.dashboard

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tw_fieldboss_alarm.*
import com.example.tw_fieldboss_alarm.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    // private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val alarmViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((activity?.application as AlarmsApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater) // 뒤보다 이게 먼저와야됨
        val recyclerView = binding.recyclerview
        val adapter = AlarmListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        // observer한다.
        alarmViewModel.allAlarms.observe(viewLifecycleOwner, Observer { alarms ->
            alarms?.let { adapter.submitList(it) }
        })

        setFragmentResultListener("requestKey") { key,bundle ->
            listOf<String>("00시 0분, 골론","06시 0분, 골론","12시 0분, 골론","18시 0분, 골론",
                "05시 0분, 골모답", "13시 0분, 골모답", "21시 0분, 골모답").forEach {
                bundle.getString(it).let { result ->
                    if (result == "켜짐") {
                        alarmViewModel.insert(Alarm(it))
                    } else {
                        alarmViewModel.delete(Alarm(it))
                    }
                }
            }
        }



//        // view를 inflate하고 binding class의 instance를 가져옴
//        _binding = FragmentDashboardBinding.inflate(layoutInflater)
//        val textView: TextView = binding.textDashboard
//
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}