package com.rzandkotlingit.proadmob

import android.app.Activity
import android.content.Context
import com.google.gson.Gson

internal class ProPrefAdMobDataManager(private val builder: Builder) {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private val gson = Gson()
    private lateinit var proPrefAdMobData: ProPrefAdMobData
    private var proConfigData: ProConfigData
    private lateinit var proPreferences: ProPreferences

    //
    init {
        this.activity = builder.activity
        this.context = builder.context
        this.proConfigData = builder.proConfigData
        onSetupProPreferences()
        //
        val jsonString: String? =
            proPreferences.getString(
                ProAdMobManager.PrefKey.ADMOB_JSON_MODEL_CLASS_DATA.label,
                null
            )
        if (jsonString == null) {
            proPrefAdMobData = onSetupPrefData()
            onSavePreference()
        } else {
            proPrefAdMobData = fromJson(jsonString)
        }
        //
        //onLogPrint(proPrefAdMobData)
        //
        //proPreferences.clear()
        //onProPrefInitialize(false)
        //proPreferences.debugPrint()
    }

    private fun onSetupProPreferences() {
        proPreferences = ProPreferences.Builder()
            .withContext(context)
            .withPrefsName(context.packageName)
            .withMode(ProPreferences.Mode.PRIVATE)
            .withDefaultPrefs(false)
            .build()
    }

    public fun onSetupPrefData(): ProPrefAdMobData {
        val isInitialized: Boolean = true
        val lastTimeMillis: Long = System.currentTimeMillis()
        val lastTimeSeconds: Long = lastTimeMillis / 1000
        //
        val nextRandSeconds: Int =
            getRandomId(proConfigData.minTimeInSecond, proConfigData.maxTimeInSecond)
        val totalEventForNext: Int = getRandomId(proConfigData.minEvent, proConfigData.maxEvent)
        //
        val nextTimeSeconds: Long = lastTimeSeconds + nextRandSeconds
        val nextTimeMillis: Long = nextTimeSeconds * 1000
        val nextRemainTimeSeconds: Long = nextTimeSeconds - lastTimeSeconds
        val totalButtonClickEvent: Int = 0
        val totalViewResumeEvent: Int = 0
        val totalEventCounter: Int = 0
        //
        val randTimeFactorOffset = getDecimalFormat(
            getRandomDouble(
                proConfigData.minEventOffset,
                proConfigData.maxEventOffset
            )
        )
        val totalTimeFactorOffset = getDecimalFormat(nextRandSeconds * randTimeFactorOffset).toInt()
        val totalTimeFactorSeconds = lastTimeSeconds + totalTimeFactorOffset
        val randEventOffset = getDecimalFormat(
            getRandomDouble(
                proConfigData.minEventOffset,
                proConfigData.maxEventOffset
            )
        )
        val totalEventOffset =
            getDecimalFormat((totalEventForNext / randEventOffset) * randEventOffset.toInt()).toInt()
        val isRandomizeAdId = proConfigData.isRandomizeAdId
        //
        return ProPrefAdMobData(
            isInitialized,
            lastTimeMillis,
            lastTimeSeconds,
            nextRandSeconds,
            nextTimeMillis,
            nextTimeSeconds,
            nextRemainTimeSeconds,
            totalEventForNext,
            totalButtonClickEvent,
            totalViewResumeEvent,
            totalEventCounter,
            randTimeFactorOffset,
            totalTimeFactorOffset,
            totalTimeFactorSeconds,
            randEventOffset,
            totalEventOffset,
            isRandomizeAdId,
        )
    }

    public fun getJson(proPrefAdMobData: ProPrefAdMobData): String {
        return gson.toJson(proPrefAdMobData)
    }

    public fun getJson(json: String): String {
        return getJson(fromJson(json))
    }

    public fun fromJson(proPrefAdMobData: ProPrefAdMobData): ProPrefAdMobData {
        return fromJson(getJson(proPrefAdMobData))
    }

    public fun fromJson(json: String): ProPrefAdMobData {
        return gson.fromJson(json, ProPrefAdMobData::class.java)
    }

    public fun getString(proPrefAdMobData: ProPrefAdMobData): String {
        return getJson(proPrefAdMobData)
    }

    fun getCurrentSeconds(): Long {
        return System.currentTimeMillis() / 1000
    }

    public class Builder {
        lateinit var activity: Activity
        lateinit var context: Context
        lateinit var proConfigData: ProConfigData

        fun build(
            activity: Activity,
            context: Context,
            proConfigData: ProConfigData
        ): ProPrefAdMobDataManager {
            this.activity = activity
            this.context = context
            this.proConfigData = proConfigData
            return ProPrefAdMobDataManager(this)
        }
    }

    inner class AdViewDataManager() {

        private fun nextTimeDiffInMillis(proPrefAdMobData: ProPrefAdMobData): Long {
            return proPrefAdMobData.nextTimeMillis - System.currentTimeMillis()
        }

        private fun nextTimeDiffInSeconds(proPrefAdMobData: ProPrefAdMobData): Long {
            return proPrefAdMobData.nextTimeSeconds - getCurrentSeconds()
        }

        private fun canShowByForced(proPrefAdMobData: ProPrefAdMobData): Boolean {
            val eventRemain =
                proPrefAdMobData.totalEventForNextViewing - proPrefAdMobData.totalEventCount
            if (nextTimeDiffInSeconds(proPrefAdMobData) < 0 || eventRemain < 0) {
                return true
            }
            return false
        }

        private fun canPassByRegular(proPrefAdMobData: ProPrefAdMobData): Boolean {
            val eventRemain =
                proPrefAdMobData.totalEventForNextViewing - proPrefAdMobData.totalEventCount
            if (nextTimeDiffInSeconds(proPrefAdMobData) < 0 && eventRemain < 0) {
                return true
            }
            return false
        }

        private fun isMaxTimeOver(proPrefAdMobData: ProPrefAdMobData): Boolean {
            val totalTimeFactor =
                proPrefAdMobData.totalTimeFactorSeconds - getCurrentSeconds()
            val totalEventFactor =
                proPrefAdMobData.totalEventOffset - proPrefAdMobData.totalEventCount
            if (totalTimeFactor < 0 || totalEventFactor < 0) {
                return true
            }
            return false
        }

        fun canShowAdView(isForced: Boolean): Boolean {
            //var retVal = false
            onSavePreference()
            //onLogPrint(proPrefAdMobData)
            if (canShowByForced(proPrefAdMobData) && isForced) {
                return true
            }
            if (canPassByRegular(proPrefAdMobData)) {
                return true
            }
            if (isMaxTimeOver(proPrefAdMobData)) {
                return true
            }
            return false
        }

        public fun onButtonClick() {
            proPrefAdMobData.totalButtonClickEvent = proPrefAdMobData.totalButtonClickEvent + 1
            onEventArise()
        }

        public fun onResume() {
            proPrefAdMobData.totalViewResumeEvent = proPrefAdMobData.totalViewResumeEvent + 1
            onEventArise()
        }

        private fun onEventArise() {
            proPrefAdMobData.totalEventCount = proPrefAdMobData.totalEventCount + 1
            onSavePreference()
        }
    }

    public fun onRestartPreference() {
        proPrefAdMobData = onSetupPrefData()
        onSavePreference()
    }

    private fun setRemainTimeSeconds() {
        proPrefAdMobData.nextRemainTimeSeconds =
            proPrefAdMobData.nextTimeSeconds - getCurrentSeconds()
    }

    private fun onSavePreference() {
        setRemainTimeSeconds()
        val jsonString: String = getJson(proPrefAdMobData)
        proPreferences.putString(
            ProAdMobManager.PrefKey.ADMOB_JSON_MODEL_CLASS_DATA.label,
            jsonString
        )
    }

    public fun onLogPrint() {
        onLogPrint(proPrefAdMobData)
    }

    public fun onLogPrint(proPrefAdMobData: ProPrefAdMobData) {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPrint(proPrefAdMobData: ProPrefAdMobData)")
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobData -> ${getString(proPrefAdMobData)}")
    }

    private fun getString(proConfigData: ProConfigData): String {
        return gson.toJson(proConfigData)
    }

    public fun onConfigLogPrint() {
        onLogPrint(proConfigData)
    }

    public fun onLogPrint(proConfigData: ProConfigData) {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPrint(proConfigData: ProConfigData)")
        println("DEBUG_LOG_PRINT_ADMOB: ProConfigData -> ${getString(proConfigData)}")
    }

    public fun onLogPreferences() {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPreferences()")
        println("DEBUG_LOG_PRINT_ADMOB: proPreferences ->")
        proPreferences.debugPrint()
    }

    private enum class PrefKey(val label: String) {
        ADMOB_JSON_MODEL_CLASS_DATA("admob_json_model_class_data"),
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