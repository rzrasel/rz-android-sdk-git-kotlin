package com.rzandkotlingit.proadmob

import android.app.Activity
import android.content.Context
import android.os.Debug
import com.google.android.gms.ads.AdRequest

public class ProAdMobManager(private val builder: Builder) {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var adEventListener: OnAdEventListener
    private lateinit var proPreferences: ProPreferences
    private lateinit var proAdMobHelper: ProAdMobHelper
    private var isDebug: Boolean

    init {
        this.activity = builder.activity
        this.context = builder.context
        this.isDebug = builder.isDebug
        adEventListener = builder.eventListener
        proAdMobHelper = ProAdMobHelper(activity, context)
            .setEventListener(SetAdEventListener())
            .setIsDebug(isDebug)
        onSetupProPreferences()
        //
        //proPreferences.clear()
        //onProPrefInitialize(false)
        //proPreferences.debugPrint()
    }

    private fun onSetupProPreferences() {
        //ProPreferences
        proPreferences = ProPreferences.Builder()
            .withContext(context)
            .withPrefsName(context.packageName)
            .withMode(ProPreferences.Mode.PRIVATE)
            .withDefaultPrefs(false)
            .build()
    }

    private fun lastAdViewTimeDifference(isInSecond: Boolean): Int {
        val currentTimeMillis: Long = System.currentTimeMillis()
        val lastAdViewTimeMillis =
            proPreferences.getLong(PrefKey.ADMOB_LAST_VIEW_TIME_MILLIS.label, 0)
        if (isInSecond) {
            return ((currentTimeMillis - lastAdViewTimeMillis) / 1000).toInt()
        }
        return (currentTimeMillis - lastAdViewTimeMillis).toInt()
    }

    private fun isLastAdViewEventCountPassed(): Boolean {
        val totalAdEvent =
            proPreferences.getInt(PrefKey.ADMOB_TOTAL_EVENT_OCCURRED.label, 0)
        val nextAdEventNeed =
            proPreferences.getInt(PrefKey.ADMOB_NEXT_EVENT_NEED.label, 0)
        if (totalAdEvent >= nextAdEventNeed) {
            return true
        }
        return false
    }

    public fun canShowAdView(isForced: Boolean): Boolean {
        var retVal = false
        val nextAdViewTimeSeconds =
            proPreferences.getInt(PrefKey.ADMOB_NEXT_VIEW_TIME_SECONDS.label, 0)
        val timeDiffSeconds = lastAdViewTimeDifference(true)
        if (timeDiffSeconds > nextAdViewTimeSeconds) {
            retVal = true
            if (isForced) {
                return retVal
            }
        }
        if (isLastAdViewEventCountPassed() && retVal) {
            return true
        }
        return false
    }

    public fun onButtonClick() {
        var totalEvent = proPreferences.getInt(PrefKey.ADMOB_TOTAL_BUTTON_CLICK_EVENT.label, 0)
        totalEvent += 1
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_BUTTON_CLICK_EVENT.label, totalEvent)
        onEventArise()
    }

    public fun onResume() {
        var totalEvent = proPreferences.getInt(PrefKey.ADMOB_TOTAL_VIEW_RESUME_EVENT.label, 0)
        totalEvent += 1
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_VIEW_RESUME_EVENT.label, totalEvent)
        onEventArise()
    }

    private fun onEventArise() {
        var totalEvent = proPreferences.getInt(PrefKey.ADMOB_TOTAL_EVENT_OCCURRED.label, 0)
        totalEvent += 1
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_EVENT_OCCURRED.label, totalEvent)
        val nextRemainTime =
            proPreferences.getInt(PrefKey.ADMOB_NEXT_VIEW_REMAIN_TIME_SECONDS.label, 0)
        val timeDiffSeconds = lastAdViewTimeDifference(true)
        val timeRemain = nextRemainTime - timeDiffSeconds
        proPreferences.putInt(PrefKey.ADMOB_NEXT_VIEW_REMAIN_TIME_SECONDS.label, timeRemain)
    }

    public fun onClear() {
        proPreferences.clear()
    }

    public fun onDebugPrint() {
        proPreferences.debugPrint()
    }

    private fun onProPrefInitialize(isForced: Boolean) {
        val isInitialized =
            proPreferences.getBoolean(PrefKey.ADMOB_IS_VIEW_PRE_PREF_INITIALIZED.label, false)
        if (isInitialized) {
            if (!isForced) {
                return
            }
        }
        val lastAdViewMillis: Long = System.currentTimeMillis()
        val nextTimeSeconds: Int = getRandomId(30, 70)
        val nextEventNeed: Int = getRandomId(12, 22)
        proPreferences.putBoolean(PrefKey.ADMOB_IS_VIEW_PRE_PREF_INITIALIZED.label, true)
        proPreferences.putLong(PrefKey.ADMOB_LAST_VIEW_TIME_MILLIS.label, lastAdViewMillis)
        proPreferences.putInt(PrefKey.ADMOB_NEXT_VIEW_TIME_SECONDS.label, nextTimeSeconds)
        proPreferences.putInt(PrefKey.ADMOB_NEXT_VIEW_REMAIN_TIME_SECONDS.label, nextTimeSeconds)
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_EVENT_OCCURRED.label, 0)
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_BUTTON_CLICK_EVENT.label, 0)
        proPreferences.putInt(PrefKey.ADMOB_TOTAL_VIEW_RESUME_EVENT.label, 0)
        proPreferences.putInt(PrefKey.ADMOB_NEXT_EVENT_NEED.label, nextEventNeed)
    }

    public fun getAdRequest(): AdRequest {
        return proAdMobHelper.getAdRequest()
    }

    public fun onLoadAd(admobAdUnitId: String) {
        if (isDebug) {
            return
        }
        proAdMobHelper.onLoadAd(admobAdUnitId)
    }

    public fun onPrepareAd(adRequest: AdRequest, admobAdUnitId: String) {
        if (isDebug) {
            return
        }
        proAdMobHelper.onPrepareAd(adRequest, admobAdUnitId)
    }

    public fun show() {
        proAdMobHelper.show()
        onProPrefInitialize(true)
    }

    inner class SetAdEventListener : ProAdMobHelper.OnAdEventListener {
        override fun onAdLoaded() {
            adEventListener.onAdLoaded()
        }

        override fun onAdFailedToLoad(adError: String) {
            adEventListener.onAdFailedToLoad(adError)
        }

        override fun onAdShowedFullScreenContent() {
            adEventListener.onAdShowedFullScreenContent()
        }

        override fun onAdDismissedFullScreenContent() {
            adEventListener.onAdDismissedFullScreenContent()
        }

        override fun onAdFailedToShowFullScreenContent(adError: String) {
            adEventListener.onAdFailedToShowFullScreenContent(adError)
        }
    }

    public class Builder {
        lateinit var activity: Activity
        lateinit var context: Context
        lateinit var eventListener: OnAdEventListener
        var isDebug = false

        fun setEventListener(adEventListener: OnAdEventListener): Builder {
            this.eventListener = adEventListener
            return this
        }

        fun setIsDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun build(activity: Activity, context: Context): ProAdMobManager {
            this.activity = activity
            this.context = context
            return ProAdMobManager(this)
        }
    }

    interface OnAdEventListener {
        fun onAdLoaded()
        fun onAdFailedToLoad(adError: String)
        fun onAdShowedFullScreenContent()
        fun onAdDismissedFullScreenContent()
        fun onAdFailedToShowFullScreenContent(adError: String)
    }

    public enum class PrefKey(val label: String) {
        ADMOB_IS_VIEW_PRE_PREF_INITIALIZED("admob_is_view_pre_pref_initialized"),
        ADMOB_LAST_VIEW_TIME_MILLIS("admob_last_view_time_millis"),
        ADMOB_NEXT_VIEW_TIME_SECONDS("admob_next_view_time_seconds"),
        ADMOB_NEXT_VIEW_REMAIN_TIME_SECONDS("admob_next_view_remain_time_seconds"),
        ADMOB_TOTAL_EVENT_OCCURRED("admob_total_event_arise"),
        ADMOB_TOTAL_BUTTON_CLICK_EVENT("admob_total_button_click_event"),
        ADMOB_TOTAL_VIEW_RESUME_EVENT("admob_total_view_resume_event"),
        ADMOB_NEXT_EVENT_NEED("admob_next_event_need"),
        NONE("none");

        companion object {
            @JvmStatic
            fun getItem(label: String): PrefKey {
                for (item in PrefKey.values()) {
                    if (item.label == label) {
                        return item
                    }
                }
                return NONE
            }
        }
    }
}