package com.example.tw_fieldboss_alarm.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tw_fieldboss_alarm.*
import com.example.tw_fieldboss_alarm.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true) // 왜넣는지는 모르겠지만 넣음
//        val layoutManager = LinearLayoutManager(context)

        val optionList: List<SwitchData> = mutableListOf(
            SwitchData(getString(R.string.golon),getString(R.string.golon_time),true,R.id.golonSwitch),
            SwitchData(getString(R.string.golmodap),getString(R.string.golmodap_time),true,R.id.golmodapSwitch),
            SwitchData(getString(R.string.arkan),getString(R.string.arkan_time),true,R.id.arkanSwitch),
            SwitchData(getString(R.string.sperchend),getString(R.string.sperchend_time),true,R.id.sperchendSwitch),
            SwitchData(getString(R.string.defense),getString(R.string.defense_time),true,R.id.defenseSwitch)
        )

        val optionViewAdapter = OptionViewAdapter(optionList)
        binding.recyclerView.adapter = optionViewAdapter


        val recyclerView2 = binding.recyclerView2
        recyclerView2.setHasFixedSize(true)
        val optionList2: List<SwitchDataWithoutSubstring> = mutableListOf(
            SwitchDataWithoutSubstring(getString(R.string.alarm_10min),true,R.id.alarm_10minSwitch),
            SwitchDataWithoutSubstring(getString(R.string.alarm_5min),true,R.id.alarm_5minSwitch),
            SwitchDataWithoutSubstring(getString(R.string.alarm_3min),true,R.id.alarm_5minSwitch),
            SwitchDataWithoutSubstring(getString(R.string.alarm_1min),true,R.id.alarm_1minSwitch)
        )
        val optionViewAdapter2 = OptionViewAdapter2(optionList2)
        binding.recyclerView2.adapter = optionViewAdapter2

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

