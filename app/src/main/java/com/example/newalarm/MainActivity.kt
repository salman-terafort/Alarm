package com.example.newalarm

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newalarm.adapter.AlarmAdapter
import com.example.newalarm.adapter.DeleteClickListner
import com.example.newalarm.databinding.ActivityMainBinding
import com.example.newalarm.dialog.AlarmSetDialoge
import com.example.newalarm.model.Alarm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DeleteClickListner {
    val viewModel: AlarmViewModel by viewModels()
    private var alarmBroadcast: AlarmBroadCast? = null
    private lateinit var binding: ActivityMainBinding
    private var adapter: AlarmAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Create the adapter instance
        adapter = AlarmAdapter(emptyList(), this)
        alarmBroadcast = AlarmBroadCast()
        alarmBroadcast?.createNotificationChannel(this)

        viewModel.allAlarms.observe(this, Observer { list ->
            adapter?.updateData(list)
        })
        Log.i("Adapter", adapter.toString())
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerAlarm.layoutManager = layoutManager
        binding.recyclerAlarm.setHasFixedSize(true)
        binding.recyclerAlarm.adapter = adapter
        binding.btnAdd.setOnClickListener {
            openDialogue()
        }
    }

    private fun openDialogue() {
        AlarmSetDialoge(this).apply {
            setTitle("Select time")
            setCancelable(false)
            setOnCheckVibrateAndLabel(object : GetTime {
                override fun getDataOfTime(
                    requestCode: Int,
                    hour: String,
                    minutes: String,
                    days: Int,
                    name: String
                ) {
                    val alarm = Alarm(
                        requestCode,
                        name,
                        Integer.parseInt(hour),
                        Integer.parseInt(minutes), 2
                    )
                    alarmBroadcast?.setAlarm(
                        this@MainActivity, Integer.parseInt(hour),
                        Integer.parseInt(minutes), 999, alarm
                    )
                    viewModel.inserAlarm(alarm)
                }
            })
            show()
        }
    }

    override fun deleteAlarm(alarm: Alarm) {
        viewModel.deleteAlarm(alarm)
        alarmBroadcast?.cancelAlarm(this, alarm.hashCode())
        Log.i("AlarmId", "at time of cancelling ${alarm.hashCode()}")
    }
}