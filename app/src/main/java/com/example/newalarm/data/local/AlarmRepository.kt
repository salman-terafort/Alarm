package com.example.newalarm.data.local

import com.example.newalarm.model.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AlarmRepository @Inject constructor(private val alarmDao: AlarmDao) {
    val allAlarms: Flow<List<Alarm>> = alarmDao.getAlarms()


    suspend fun insert(alarm: Alarm) {
        alarmDao.inserAlarm(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

}