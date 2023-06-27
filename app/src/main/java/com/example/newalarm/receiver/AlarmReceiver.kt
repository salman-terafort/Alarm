package com.example.newalarm.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.newalarm.AlarmUtils.createNotificationBuilder
import com.example.newalarm.AlarmUtils.createPendingIntent
import com.example.newalarm.data.local.Constants
import com.example.newalarm.model.Alarm
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Recieved", "alarm is recieved")
        val alarm = intent.getSerializableExtra("object") as Alarm
        val sharedPreferences =
            context.getSharedPreferences(alarm.alarmId.toString(), Context.MODE_PRIVATE)
        val nameOfMedicine = sharedPreferences.getString(Constants.MEDICINE_NAME, "unknown")
        val alarmdId = alarm.alarmId
        val pendingIntent = createPendingIntent(context, 99)
        val builder = createNotificationBuilder(
            context,
            "Hey, it's time take your medicine",
            "it's your medication reminder",
            pendingIntent
        )
        val managerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context)
        managerCompat.notify(0, builder.build())

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        setRepeatingAlarm(
            context,
            hour,
            minutes,
            alarm
        )
    }

    private fun setRepeatingAlarm(context: Context, hour: Int, minutes: Int, alarm: Alarm) {
        val sharedPreferences =
            context.getSharedPreferences(alarm.alarmId.toString(), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var remainigDays = sharedPreferences.getInt(alarm.alarmId.toString(), -1)
        Log.d("Remaining", remainigDays.toString())
        var calendar = Calendar.getInstance()
        var currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        var nextDay = (currentDay % 7) + 1
        calendar.set(Calendar.DAY_OF_WEEK, nextDay)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("object",alarm)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.alarmId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        if (remainigDays != -1 && remainigDays > 0) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
        remainigDays -= 1
        editor.putInt(alarm.alarmId.toString(), remainigDays)
        editor.apply()
    }
}