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
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Recieved", "alarm is recieved")
        val sharedPreferences =
            context.getSharedPreferences(Constants.REMAININGDAYSPREF, Context.MODE_PRIVATE)
        val nameOfMedicine = sharedPreferences.getString(Constants.MEDICINE_NAME, "unknown")
        val alarmdId = sharedPreferences.getInt(Constants.ALARM_ID, -1)
        val pendingIntent = createPendingIntent(context, 99)
        val builder = createNotificationBuilder(
            context,
            "Hey, it's time take your $nameOfMedicine",
            "it's your medication reminder",
            pendingIntent
        )
        val managerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context)
        managerCompat.notify(0, builder.build())

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
       /* setRepeatingAlarm(
            context,
            hour,
            minutes,
            alarmdId
        )*/
    }

    private fun setRepeatingAlarm(context: Context, hour: Int, minutes: Int, alarmId: Int) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.REMAININGDAYSPREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var remainigDays = sharedPreferences.getInt(Constants.REMAININGDAYS, -1)
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
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
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
        editor.putInt(Constants.REMAININGDAYS, remainigDays)
        editor.apply()
    }
}