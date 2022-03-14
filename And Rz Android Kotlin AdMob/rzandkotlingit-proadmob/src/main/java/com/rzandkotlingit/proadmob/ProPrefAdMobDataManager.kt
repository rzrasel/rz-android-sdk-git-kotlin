package com.rzandkotlingit.proadmob

import com.google.gson.Gson

internal class ProPrefAdMobDataManager(private val builder: Builder) {
    private val gson = Gson()
    private var proConfigData: ProConfigData

    //
    init {
        this.proConfigData = builder.proConfigData
    }

    public fun onSetupPrefData(): ProPrefAdMobData {
        val isInitialized: Boolean = true
        val lastTimeMillis: Long = System.currentTimeMillis()
        val lastTimeSeconds: Long = System.currentTimeMillis() / 1000
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

    inner class AdViewDataManager() {
        private fun nextTimeDiffInMillis(proPrefAdMobData: ProPrefAdMobData): Long {
            val currentTimeMillis: Long = System.currentTimeMillis()
            return proPrefAdMobData.nextTimeMillis - currentTimeMillis
        }

        private fun nextTimeDiffInSeconds(proPrefAdMobData: ProPrefAdMobData): Long {
            val currentTimeMillis: Long = System.currentTimeMillis() / 1000
            proPrefAdMobData.nextRemainTimeSeconds =
                proPrefAdMobData.nextTimeSeconds - currentTimeMillis
            return proPrefAdMobData.nextTimeSeconds - currentTimeMillis
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
                (proPrefAdMobData.lastTimeSeconds + proPrefAdMobData.totalTimeFactorOffset) - (System.currentTimeMillis() / 1000)
            val totalEventFactor =
                proPrefAdMobData.totalEventOffset - proPrefAdMobData.totalEventCount
            if (totalTimeFactor < 0 || totalEventFactor < 0) {
                return true
            }
            return false
        }

        fun canShowAdView(proPrefAdMobData: ProPrefAdMobData, isForced: Boolean): Boolean {
            //var retVal = false
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