package com.rzandroidgit.appusagesexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //
        startActivity(Intent(this, ActivityProAdMob::class.java))
        finish()
    }
}