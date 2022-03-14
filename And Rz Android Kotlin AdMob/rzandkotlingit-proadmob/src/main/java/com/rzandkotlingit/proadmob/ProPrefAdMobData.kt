package com.rzandkotlingit.proadmob

import com.google.gson.annotations.SerializedName

internal data class ProPrefAdMobData(
    @SerializedName("admob_data_is_initialized") var isInitialized: Boolean,
    @SerializedName("admob_last_showing_time_millis") var lastTimeMills: Long,
    @SerializedName("admob_last_showing_time_seconds") var lastTimeSeconds: Long,
    @SerializedName("admob_next_showing_rand_seconds") var nextRandSeconds: Int,
    @SerializedName("admob_next_viewing_time_millis") var nextTimeMills: Long,
    @SerializedName("admob_next_viewing_time_seconds") var nextTimeSeconds: Long,
    @SerializedName("admob_next_view_remain_time_seconds") var nextRemainTimeSeconds: Long,
    @SerializedName("admob_total_event_for_next_viewing") var totalEventForNextViewing: Int,
    @SerializedName("admob_total_button_click_event") var totalButtonClickEvent: Int,
    @SerializedName("admob_total_view_resume_event") var totalViewResumeEvent: Int,
    @SerializedName("admob_total_event_counter") var totalEventCount: Int,
    @SerializedName("admob_rand_time_factor_offset") var randTimeFactorOffset: Double,
    @SerializedName("admob_total_time_factor_offset") var totalTimeFactorOffset: Int,
    @SerializedName("admob_rand_event_offset") var randEventOffset: Double,
    @SerializedName("admob_total_event_offset") var totalEventOffset: Int,
    //@SerializedName("admob_next_event_need") var totalEventCounter: Int,
)
