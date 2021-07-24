package com.example.tw_fieldboss_alarm.alarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tw_fieldboss_alarm.R

class AlarmListAdapter: ListAdapter<Alarm, AlarmListAdapter.AlarmViewHolder>(AlarmsComparator()) {
    // private lateinit var binding: RecyclerviewAlarmItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AlarmViewHolder {
        return AlarmViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.alarmType)
    }


    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val alarmItemView: TextView = itemView.findViewById(R.id.TextView)

        fun bind(text: String?) {
            alarmItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): AlarmViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_alarm_item,parent,false)
                return AlarmViewHolder(view)
            }
        }
    }

    class AlarmsComparator : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.alarmType == newItem.alarmType
        }
    }
}