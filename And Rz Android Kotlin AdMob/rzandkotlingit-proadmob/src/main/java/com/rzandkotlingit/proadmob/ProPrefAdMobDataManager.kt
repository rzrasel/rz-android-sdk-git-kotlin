package com.rzandkotlingit.proadmob

import com.google.gson.Gson

internal class ProPrefAdMobDataManager(private val builder: Builder) {
    private val gson = Gson()
    private lateinit var proConfigData: ProConfigData

    //
    init {
        this.proConfigData = builder.proConfigData
    }

    public fun onPrefDataSetup(): ProPrefAdMobData {
        val isInitialized: Boolean = true
        val lastTimeMills: Long = System.currentTimeMillis()
        val lastTimeSeconds: Long = System.currentTimeMillis() / 1000
        //
        val nextRandSeconds: Int =
            getRandomId(proConfigData.minTimeInSecond, proConfigData.maxTimeInSecond)
        val totalEventForNext: Int = getRandomId(proConfigData.minEvent, proConfigData.maxEvent)
        //
        val nextTimeSeconds: Long = lastTimeSeconds + nextRandSeconds
        val nextTimeMills: Long = nextTimeSeconds * 1000
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
        val randEventOffset = getDecimalFormat(
            getRandomDouble(
                proConfigData.minEventOffset,
                proConfigData.maxEventOffset
            )
        )
        val totalEventOffset =
            getDecimalFormat((totalEventForNext / randEventOffset) * randEventOffset.toInt()).toInt()
        //
        return ProPrefAdMobData(
            isInitialized,
            lastTimeMills,
            lastTimeSeconds,
            nextRandSeconds,
            nextTimeMills,
            nextTimeSeconds,
            nextRemainTimeSeconds,
            totalEventForNext,
            totalButtonClickEvent,
            totalViewResumeEvent,
            totalEventCounter,
            randTimeFactorOffset,
            totalTimeFactorOffset,
            randEventOffset,
            totalEventOffset,
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

    public class Builder {
        lateinit var proConfigData: ProConfigData
        fun build(proConfigData: ProConfigData): ProPrefAdMobDataManager {
            this.proConfigData = proConfigData
            return ProPrefAdMobDataManager(this)
        }
    }

    public fun logPrint(proPrefAdMobData: ProPrefAdMobData) {
        onLogPrint(proPrefAdMobData)
    }

    public fun onLogPrint(proPrefAdMobData: ProPrefAdMobData) {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPrint(proPrefAdMobData: ProPrefAdMobData)")
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobData -> ${getString(proPrefAdMobData)}")
    }

    public fun getString(proConfigData: ProConfigData): String {
        return gson.toJson(proConfigData)
    }

    public fun logPrint() {
        onLogPrint(proConfigData)
    }

    public fun logPrint(proConfigData: ProConfigData) {
        onLogPrint(proConfigData)
    }

    public fun onLogPrint() {
        onLogPrint(proConfigData)
    }

    public fun onLogPrint(proConfigData: ProConfigData) {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPrint(proConfigData: ProConfigData)")
        println("DEBUG_LOG_PRINT_ADMOB: ProConfigData -> ${getString(proConfigData)}")
    }
}