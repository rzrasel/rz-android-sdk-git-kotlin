package com.rzandroidgit.appusagesexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //ProPreferences
        startActivity(Intent(this, ActivityProPreferences::class.java))
        finish()
    }
}