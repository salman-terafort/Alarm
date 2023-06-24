package com.example.newalarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

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

}