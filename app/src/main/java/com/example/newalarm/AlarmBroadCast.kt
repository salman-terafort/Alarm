package com.example.newalarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.newalarm.data.local.Constants
import com.example.newalarm.model.Alarm
import com.example.newalarm.receiver.AlarmReceiver
import java.util.*

class AlarmBroadCast {
    private var calendar: Calendar? = null
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    fun setAlarm(
        context: Context,
        hour: Int,
        minutes: Int,
        alarm: Alarm
    ) {
        Log.d("SSS", "setAlarm1: ")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("object", alarm)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                alarm.alarmId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_IMMUTABLE
            )
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)

        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentTime = System.currentTimeMillis()
        val alarmTime = calendar.timeInMillis
        val sharedPreferences =
            context.getSharedPreferences(alarm.alarmId.toString(), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(alarm.alarmId.toString(), (alarm.numberOfDays) - 1)
        editor.putInt(Constants.ALARM_ID, alarm.alarmId)
        editor.putString(Constants.MEDICINE_NAME, alarm.name)
        editor.apply()
        // Check if the alarm time has already passed for today
        if (alarmTime != null) {
            if (alarmTime < currentTime) {
                // Add one day to the alarm time
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        /*  pendingIntent =
              PendingIntent.getBroadcast(context, 111, intent, PendingIntent.FLAG_IMMUTABLE)*/
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        alarm.cancel(pendingIntent)
        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show()
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Alarm"
            val description = "Alarm description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel", name, importance)
            channel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}