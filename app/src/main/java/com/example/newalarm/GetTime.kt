package com.example.newalarm

interface GetTime {
    fun getDataOfTime(
        requestCode: Int,
        hour: String,
        minutes: String,
        days: Int = 0,
        name: String = ""
    )
}