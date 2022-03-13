package com.rzandkotlingit.proadmob

import com.google.gson.annotations.SerializedName

internal data class ProPrefAdMobData(
    @SerializedName("admob_data_is_initialized") var isInitialized: Boolean,
    @SerializedName("admob_last_showing_time_millis") var lastTimeMills: Long,
    @SerializedName("admob_last_showing_time_seconds") var lastTimeSeconds: Long,
    @SerializedName("admob_next_viewing_time_millis") var nextTimeMills: Long,
    @SerializedName("admob_next_viewing_time_seconds") var nextTimeSeconds: Long,
    @SerializedName("admob_next_view_remain_time_seconds") var nextRemainTimeSeconds: Long,
    @SerializedName("admob_total_event_arise_for_next_viewing") var totalEventForNextViewing: Int,
    @SerializedName("admob_total_button_click_event") var totalButtonClickEvent: Int,
    @SerializedName("admob_total_view_resume_event") var totalViewResumeEvent: Int,
    @SerializedName("admob_total_event_counter") var totalEventCounter: Int,
    //@SerializedName("admob_next_event_need") var totalEventCounter: Int,
)
