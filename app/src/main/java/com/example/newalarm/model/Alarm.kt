package com.example.newalarm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newalarm.data.local.Constants
import java.io.Serializable

@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var alarmId: Int,
    var name : String,
    var alarmHour: Int,
    var minute: Int,
    val numberOfDays: Int
) : Serializable
