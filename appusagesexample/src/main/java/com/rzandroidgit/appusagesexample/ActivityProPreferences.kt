package com.rzandroidgit.appusagesexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.rzandjavagit.propreferences.ProPreferences

class ActivityProPreferences : AppCompatActivity() {
    private lateinit var proPreferences: ProPreferences
    private lateinit var sysButtonProPref: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_preferences)
        //
        sysButtonProPref = findViewById<Button>(R.id.sysButtonProPref)
        //ProPreferences
        proPreferences = ProPreferences.Builder()
            .withContext(this)
            .withPrefsName(packageName)
            .withMode(ProPreferences.Mode.PRIVATE)
            .withDefaultPrefs(false)
            .build()
        proPreferences.putLong("long_value", 1000L)
        sysButtonProPref.setOnClickListener(View.OnClickListener {
            println("DEBUG_LOG_PRINT: ${proPreferences.getLong("long_value")}")
        })
        proPreferences.logPrint()
    }
}