package com.rzandroidgit.appusagesexample

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.rzandkotlingit.proadmob.ProAdMobManager

class ActivityProAdMob : AppCompatActivity() {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var proAdMobManager: ProAdMobManager

    //
    private lateinit var sysBtnProAdMob: Button
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_ad_mob)
        //
        activity = this
        context = this
        //
        sysBtnProAdMob = findViewById(R.id.sysBtnProAdMob)
        //
        proAdMobManager = ProAdMobManager.Builder()
            .setEventListener(SetAdEventListener())
            .setIsDebug(false)
            .build(activity, context)
        //proAdMobManager.onClear()
        /*println("DEBUG_LOG_PRINT: ${ProAdMobManager.PrefKey.getItem("none")}")
        println("DEBUG_LOG_PRINT: ${ProAdMobManager.PrefKey.ADMOB_LAST_VIEW_TIME_MILLIS.name}")
        if (proAdMobManager.canShowAdView(false)) {
            println("DEBUG_LOG_PRINT: you can show")
        } else {
            println("DEBUG_LOG_PRINT: you can not show")
        }*/
        sysBtnProAdMob.setOnClickListener(View.OnClickListener {
            proAdMobManager.onButtonClick()
            onLoadAd()
            proAdMobManager.onDebugPrint()
        })
    }

    override fun onResume() {
        super.onResume()
        proAdMobManager.onResume()
        onLoadAd()
        proAdMobManager.onDebugPrint()
    }

    private fun onLoadAd() {
        if (proAdMobManager.canShowAdView(false)) {
            println("DEBUG_LOG_PRINT: you can show")
            val admobAdUnitId = resources.getString(R.string.admob_inters_ad_unit_id)
            proAdMobManager.onLoadAd(admobAdUnitId)
        } else {
            println("DEBUG_LOG_PRINT: you can not show")
        }
    }

    inner class SetAdEventListener : ProAdMobManager.OnAdEventListener {
        override fun onAdLoaded() {
            println("DEBUG_LOG_PRINT: onAdLoaded()")
            proAdMobManager.show()
        }

        override fun onAdFailedToLoad(adError: String) {
            println("DEBUG_LOG_PRINT: onAdFailedToLoad(adError: String)")
        }

        override fun onAdShowedFullScreenContent() {
            println("DEBUG_LOG_PRINT: onAdShowedFullScreenContent()")
        }

        override fun onAdDismissedFullScreenContent() {
            println("DEBUG_LOG_PRINT: onAdDismissedFullScreenContent()")
        }

        override fun onAdFailedToShowFullScreenContent(adError: String) {
            println("DEBUG_LOG_PRINT: onAdFailedToShowFullScreenContent(adError: String)")
        }
    }
}