package com.example.newalarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicLong

object AlarmUtils {
    fun createPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        val day = intent.getIntExtra("currentDay", -1)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_RECEIVER_FOREGROUND
        return PendingIntent.getActivity(context, requestCode, intent, 0)
    }

    fun createNotificationBuilder(
        context: Context,
        title: String,
        message: String,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(R.drawable.ic_round_access_alarm_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_countdown_timer_24, "Snooze", pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
        return builder
    }
    fun generateUniqueId(): Int {
        val currentDateTime = Calendar.getInstance()
        val hour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentDateTime.get(Calendar.MINUTE)
        val seconds = currentDateTime.get(Calendar.SECOND)
        val year = currentDateTime.get(Calendar.YEAR)
        val day = currentDateTime.get(Calendar.DAY_OF_YEAR)
        val uniqueId = (year) + (hour * 100) + minute + day + (seconds * generateRandomNumber())
        return uniqueId
    }

    fun generateRandomNumber(): Int {
        // Generate a random number using your preferred method or library
        // For simplicity, this example generates a random number between 1 and 100
        return (1..100).random()
    }

}