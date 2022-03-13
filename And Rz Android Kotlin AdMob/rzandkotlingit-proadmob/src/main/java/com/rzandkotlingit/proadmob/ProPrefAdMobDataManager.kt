package com.rzandkotlingit.proadmob

import com.google.gson.Gson

internal class ProPrefAdMobDataManager {
    private val gson = Gson()

    //
    public fun onPrefDataSetup(): ProPrefAdMobData {
        return ProPrefAdMobData(
            false, //isInitialized
            System.currentTimeMillis(), //lastTimeMills
            (System.currentTimeMillis() / 1000), //lastTimeSeconds
            System.currentTimeMillis(), //nextTimeMills
            (System.currentTimeMillis() / 1000), //nextTimeSeconds
            (System.currentTimeMillis() / 1000), //nextRemainTimeSeconds
            0, //totalEventForNextViewing
            0, //totalButtonClickEvent
            0, //totalViewResumeEvent
            0, //totalEventCounter
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

    public fun logPrint(proPrefAdMobData: ProPrefAdMobData) {
        onLogPrint(proPrefAdMobData)
    }
    public fun onLogPrint(proPrefAdMobData: ProPrefAdMobData) {
        println("DEBUG_LOG_PRINT_ADMOB: ProPrefAdMobDataManager->onLogPrint(proPrefAdMobData: ProPrefAdMobData)")
        println("DEBUG_LOG_PRINT_ADMOB: ${getString(proPrefAdMobData)}")
    }
}