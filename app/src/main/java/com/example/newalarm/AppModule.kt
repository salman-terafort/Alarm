package com.example.newalarm

import android.content.Context
import androidx.room.Room
import com.example.newalarm.data.AlarmDataBase
import com.example.newalarm.data.local.AlarmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
    @Singleton
    @Provides
    fun getContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAlarmDataBase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AlarmDataBase::class.java,
        "alarm_db"
    ).build()

    @Provides
    @Singleton
    fun provideAlarmDao(db: AlarmDataBase) = db.alarmDao()

}