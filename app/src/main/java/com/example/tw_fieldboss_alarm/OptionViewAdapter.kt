package com.example.tw_fieldboss_alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 내용 작성에 참고함: https://dreamaz.tistory.com/345
// 참고자료2: https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=ko
// 참고자료3: https://velog.io/@l2hyunwoo/Android-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-5

class OptionViewAdapter(private val dataSet:List<SwitchData>):
    RecyclerView.Adapter<OptionViewAdapter.ViewHolder>() {

    class ViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
        fun onBind(switchData: SwitchData) {
            val bossSummaryTextView: TextView = view.findViewById(R.id.title)
            val bossTimeDescriptionTextView : TextView = view.findViewById(R.id.boss_time_description)
            val optionSwitch: Switch = view.findViewById(R.id.option_switch)

            bossSummaryTextView.text = switchData.bossSummaryText
            bossTimeDescriptionTextView.text = switchData.bossTimeDescriptionText
            optionSwitch.isChecked = switchData.optionSwitchEnabled
            optionSwitch.id = switchData.id;
        }
//        fun onBind(titleData: TitleData) {
//            val titleTextView: TextView = view.findViewById(R.id.title_text)
//            titleTextView.text = titleData.titleText
//        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.switch_preference,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

class OptionViewAdapter2(private val dataSet:List<SwitchDataWithoutSubstring>):
    RecyclerView.Adapter<OptionViewAdapter2.ViewHolder>() {

    class ViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
        fun onBind(switchData: SwitchDataWithoutSubstring) {
            val textView: TextView = view.findViewById(R.id.title)
            val optionSwitch: Switch = view.findViewById(R.id.option_switch)

            textView.text = switchData.timeText
            optionSwitch.isChecked = switchData.optionSwitchEnabled
            optionSwitch.id = switchData.id;
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.switch_preference_without_substring,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}


data class SwitchData(
    val bossSummaryText: String, val bossTimeDescriptionText: String, val optionSwitchEnabled: Boolean, val id: Int
) {}

data class SwitchDataWithoutSubstring(
    val timeText: String, val optionSwitchEnabled: Boolean, val id: Int
) {}

data class TitleData( val titleText: String) {}