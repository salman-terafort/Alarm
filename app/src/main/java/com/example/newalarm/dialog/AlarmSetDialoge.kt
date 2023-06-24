package com.example.newalarm.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.newalarm.GetTime
import com.example.newalarm.databinding.AlarmSetDialogeBinding
import java.util.*

class AlarmSetDialoge(context: Context) : AlertDialog(context) {
    private var dialogBinding: AlarmSetDialogeBinding = AlarmSetDialogeBinding.inflate(
        LayoutInflater.from(context)
    )
    private var getCheckVibrateAndLabel: GetTime? = null
    fun setOnCheckVibrateAndLabel(getCheckVibrateAndLabel: GetTime) {
        this.getCheckVibrateAndLabel = getCheckVibrateAndLabel
    }

    init {
        dialogBinding.alarmTimePickerHour.value = Calendar.getInstance().time.hours
        dialogBinding.alarmTimePickerMinute.value = Calendar.getInstance().time.minutes
        dialogBinding.okText.setOnClickListener {
            getCheckVibrateAndLabel!!.getDataOfTime(
                0,
                String.format("%02d", dialogBinding.alarmTimePickerHour.value),
                String.format("%02d", dialogBinding.alarmTimePickerMinute.value),
                0,
                dialogBinding.etName.text.toString()
            )
            dismiss()
            cancel()
        }
        dialogBinding.cancelText.setOnClickListener {
            dismiss()
            cancel()
        }
        setView(dialogBinding.root)
    }


}