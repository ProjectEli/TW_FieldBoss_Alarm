package com.tw_fieldboss_alarm.ui.fullscreenalarm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tw_fieldboss_alarm.MainActivity
import com.tw_fieldboss_alarm.databinding.ActivityFullscreenAlarmBinding


class FullscreenAlarm : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenAlarmBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            android.os.Build.VERSION.SDK_INT >= 27 -> {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }
        }
        // setContentView(R.layout.activity_fullscreen_alarm)

        binding = ActivityFullscreenAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val snoozeButton = binding.snoozeButton as Button
        snoozeButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("터치","터치됨")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("터치업","터치업됨")
                }
            }
            false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}