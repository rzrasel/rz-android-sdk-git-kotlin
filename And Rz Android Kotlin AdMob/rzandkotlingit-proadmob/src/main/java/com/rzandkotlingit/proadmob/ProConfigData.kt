package com.rzandkotlingit.proadmob

import com.google.gson.annotations.SerializedName

data class ProConfigData(
    @SerializedName("max_time_in_second_for_next_ad") val maxTimeInSecond: Int,
    @SerializedName("min_time_in_second_for_next_ad") val minTimeInSecond: Int,
    @SerializedName("max_ui_event_for_next_ad") val maxEvent: Int,
    @SerializedName("min_ui_event_for_next_ad") val minEvent: Int,
    @SerializedName("max_time_offset_for_next_ad") val maxTimeOffset: Double,
    @SerializedName("min_time_offset_for_next_ad") val minTimeOffset: Double,
    @SerializedName("max_ui_event_offset_for_next_ad") val maxEventOffset: Double,
    @SerializedName("min_ui_event_offset_for_next_ad") val minEventOffset: Double,
    @SerializedName("is_randomize_ad_id") val isRandomizeAdId: Boolean,
    @SerializedName("is_debug_mode") val isDebug: Boolean,
)
