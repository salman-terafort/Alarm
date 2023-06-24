package com.example.newalarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newalarm.data.local.AlarmDao
import com.example.newalarm.model.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDataBase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}