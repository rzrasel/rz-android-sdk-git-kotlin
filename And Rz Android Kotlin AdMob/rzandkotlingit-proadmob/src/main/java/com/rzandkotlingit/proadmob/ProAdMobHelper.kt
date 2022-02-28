package com.rzandkotlingit.proadmob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

internal class ProAdMobHelper(activity: Activity, context: Context) {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private val TAG = "ProAdMobHelper"
    private lateinit var adEventListener: OnAdEventListener
    private var isDebug: Boolean = false
    private lateinit var adRequest: AdRequest
    private lateinit var admobAdUnitId: String
    private var interstitialAd: InterstitialAd? = null

    init {
        this.activity = activity
        this.context = context
        //https://stackoverflow.com/questions/57275821/tagforchilddirectedtreatmentboolean-deprecated
        val conf = RequestConfiguration.Builder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build()
        MobileAds.setRequestConfiguration(conf);
        MobileAds.initialize(this.context) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )
    }

    fun setEventListener(adEventListener: OnAdEventListener): ProAdMobHelper {
        this.adEventListener = adEventListener
        return this
    }

    fun setIsDebug(isDebug: Boolean): ProAdMobHelper {
        this.isDebug = isDebug
        return this
    }

    fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    fun onLoadAd(admobAdUnitId: String) {
        if (isDebug) {
            return
        }
        this.admobAdUnitId = admobAdUnitId
        onPrepareAd(getAdRequest(), this.admobAdUnitId)
    }

    fun onPrepareAd(adRequest: AdRequest, admobAdUnitId: String) {
        if (isDebug) {
            return
        }
        this.adRequest = adRequest
        this.admobAdUnitId = admobAdUnitId
        InterstitialAd.load(context, admobAdUnitId, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(argInterstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    interstitialAd = argInterstitialAd
                    //Log.i(TAG, "onAdLoaded");
                    interstitialAd!!.setFullScreenContentCallback(OnFullScreenContentCallback())
                    if (adEventListener != null) {
                        adEventListener.onAdLoaded()
                    }
                }

                override fun onAdFailedToLoad(argLoadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, "DEBUG_LOG_ERROR: onAdFailedToLoad " + argLoadAdError.message)
                    interstitialAd = null
                    if (adEventListener != null) {
                        adEventListener.onAdFailedToLoad(argLoadAdError.message)
                    }
                }
            })
    }

    fun show() {
        if (interstitialAd != null) {
            interstitialAd!!.show(activity)
        } else {
            println("DEBUG_LOG_PRINT_TAG: The interstitial ad wasn't ready yet.")
        }
    }

    inner class OnFullScreenContentCallback : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            // Called when fullscreen content is dismissed.
            //Log.d("TAG", "The ad was dismissed.");
            if (adEventListener != null) {
                adEventListener.onAdDismissedFullScreenContent()
            }
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // Called when fullscreen content failed to show.
            //Log.d("TAG", "The ad failed to show.");
            if (adEventListener != null) {
                adEventListener.onAdFailedToShowFullScreenContent(adError.message)
            }
        }

        override fun onAdShowedFullScreenContent() {
            // Called when fullscreen content is shown.
            // Make sure to set your reference to null so you don't
            // show it a second time.
            interstitialAd = null
            //Log.d("TAG", "The ad was shown.");
            if (adEventListener != null) {
                adEventListener.onAdShowedFullScreenContent()
            }
        }
    }

    interface OnAdEventListener {
        fun onAdLoaded()
        fun onAdFailedToLoad(adError: String)
        fun onAdShowedFullScreenContent()
        fun onAdDismissedFullScreenContent()
        fun onAdFailedToShowFullScreenContent(adError: String)
    }
}