package com.rzandkotlingit.core

class RzAndroidSdk {
    //    private static volatile boolean isDebugEnabled = BuildConfig.DEBUG;
    //    private static volatile boolean isDebugEnabled = BuildConfig.DEBUG;
    /**
     * Returns the current version of the RzAndroid SDK for Android as a string.
     *
     * @return the current version of the SDK
     */
    fun getSdkVersion(): String? {
        return RzAndroidSdkVersion.BUILD
    }
}