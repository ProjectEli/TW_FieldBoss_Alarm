package com.tw_fieldboss_alarm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tw_fieldboss_alarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        Log.d("종료","앱 종료됨")
        super.onDestroy()
    }
}