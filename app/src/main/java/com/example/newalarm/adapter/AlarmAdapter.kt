package com.example.newalarm.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newalarm.databinding.NewAlarmItemsBinding
import com.example.newalarm.model.Alarm
import java.util.*

class AlarmAdapter(private var alarms: List<Alarm>, val deleteClickListner: DeleteClickListner) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var alarmsList: MutableList<Alarm> = alarms.toMutableList()

    inner class AlarmViewHolder(val binding: NewAlarmItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Alarm) {
            val calender = Calendar.getInstance()
            calender.set(Calendar.HOUR_OF_DAY, item.alarmHour)
            calender.set(Calendar.MINUTE, item.minute)
            binding.textView7.text =
                DateUtils.formatDateTime(binding.textView7.context, calender.timeInMillis, 1)
            binding.currentActivity.text = "medicine name : ${item.name}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewAlarmItemsBinding.inflate(inflater, parent, false)
        return AlarmViewHolder(binding)
    }

    fun updateData(newData: List<Alarm>) {
        alarmsList = newData as MutableList<Alarm>
        notifyDataSetChanged()
    }

    override fun getItemCount() = alarmsList.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = alarmsList[position]
        holder.bind(item)
        holder.binding.wbDeleteImageView.setOnClickListener {
            deleteClickListner.deleteAlarm(item)
        }
    }
}