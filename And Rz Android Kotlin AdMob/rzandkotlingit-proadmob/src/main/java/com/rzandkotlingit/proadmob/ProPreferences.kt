package com.rzandkotlingit.proadmob

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils


internal class ProPreferences(private val builder: Builder) {
    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var prefsKey: String
    private var prefsMode = 0

    companion object {
        @JvmStatic
        private val DEFAULT_SUFFIX: String = "_preferences"
        private const val LENGTH = "#LENGTH"

        /*@JvmStatic
        fun initPrefs(context: Context, prefsName: String, prefsMode: Int) {
        }*/
        /*@JvmStatic
        fun getPreferences(): SharedPreferences {
            return sharedPrefs
        }*/
    }

    init {
        context = builder.context
        prefsKey = builder.prefsKey
        prefsMode = builder.prefsMode
        initPrefs(context, prefsKey, prefsMode)
    }

    private fun initPrefs(context: Context, prefsName: String, prefsMode: Int) {
        sharedPrefs = context.getSharedPreferences(prefsName, prefsMode)
    }

    private fun getPreferences(): SharedPreferences {
        return sharedPrefs
    }

    public fun getInt(key: String, defValue: Int): Int {
        return getPreferences().getInt(key, defValue)
    }

    public fun getInt(key: String): Int {
        return getPreferences().getInt(key, 0)
    }

    public fun putInt(key: String, value: Int) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putInt(key, value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getPreferences().getBoolean(key, defValue)
    }

    public fun getBoolean(key: String?): Boolean {
        return getPreferences().getBoolean(key, false)
    }

    public fun putBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putBoolean(key, value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun getLong(key: String, defValue: Long): Long {
        return getPreferences().getLong(key, defValue)
    }

    public fun getLong(key: String): Long {
        return getPreferences().getLong(key, 0L)
    }

    public fun putLong(key: String, value: Long) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putLong(key, value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun getDouble(key: String, defValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            getPreferences()
                .getLong(key, java.lang.Double.doubleToLongBits(defValue))
        )
    }

    public fun getDouble(key: String): Double {
        return java.lang.Double.longBitsToDouble(
            getPreferences().getLong(key, java.lang.Double.doubleToLongBits(0.0))
        )
    }

    public fun putDouble(key: String, value: Double) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun getFloat(key: String, defValue: Float): Float {
        return getPreferences().getFloat(key, defValue)
    }

    public fun getFloat(key: String): Float {
        return getPreferences().getFloat(key, 0.0f)
    }

    public fun putFloat(key: String, value: Float) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putFloat(key, value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun getString(key: String, defValue: String?): String? {
        return getPreferences().getString(key, defValue)
    }

    public fun getString(key: String): String? {
        return getPreferences().getString(key, null)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public fun getStringSet(key: String, defValue: Set<String>): Set<String?>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getPreferences().getStringSet(key, defValue)
        } else {
            // Workaround for pre-HC's missing getStringSet
            getOrderedStringSet(key, defValue)
        }
    }

    public fun putString(key: String, value: String) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        editor.putString(key, value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public fun putStringSet(key: String, value: Set<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val editor: SharedPreferences.Editor = getPreferences().edit()
            editor.putStringSet(key, value)
            editor.apply()
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value)
        }
    }

    public fun getOrderedStringSet(key: String, defValue: Set<String?>?): Set<String?>? {
        val prefs: SharedPreferences = getPreferences()
        if (prefs.contains(key + LENGTH)) {
            val set = LinkedHashSet<String?>()
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                for (i in 0 until stringSetLength) {
                    set.add(prefs.getString("$key[$i]", null))
                }
            }
            return set
        }
        return defValue
    }

    public fun putOrderedStringSet(key: String, value: Set<String>) {
        val editor: SharedPreferences.Editor = getPreferences().edit()
        var stringSetLength = 0
        if (sharedPrefs.contains(key + LENGTH)) {
            // First read what the value was
            stringSetLength = sharedPrefs.getInt(key + LENGTH, -1)
        }
        editor.putInt(key + LENGTH, value.size)
        var i = 0
        for (aValue in value) {
            editor.putString("$key[$i]", aValue)
            i++
        }
        while (i < stringSetLength) {

            // Remove any remaining values
            editor.remove("$key[$i]")
            i++
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun remove(key: String) {
        val prefs: SharedPreferences = getPreferences()
        val editor: SharedPreferences.Editor = prefs.edit()
        if (prefs.contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            val stringSetLength = prefs.getInt(key + LENGTH, -1)
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH)
                for (i in 0 until stringSetLength) {
                    editor.remove("$key[$i]")
                }
            }
        }
        editor.remove(key)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    public fun remove() {
        val prefs: SharedPreferences = getPreferences()
        prefs.edit().clear().commit()
    }

    public fun contains(key: String): Boolean {
        return getPreferences().contains(key)
    }

    public fun clear(): SharedPreferences.Editor {
        val editor: SharedPreferences.Editor = getPreferences().edit().clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
        return editor
    }

    public fun edit(): SharedPreferences.Editor {
        return getPreferences().edit()
    }

    public fun print() {
        logPrint()
    }

    public fun debugPrint() {
        logPrint()
    }

    public fun onDebugPrint() {
        logPrint()
    }

    public fun logPrint() {
        val prefs: SharedPreferences = getPreferences()
        val allEntries = prefs.all
        println("\n\nDEBUG_LOG_PRINT_SHARED_PREFERENCE: ProPreferencesManager>")
        for ((key, value) in allEntries) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            println(
                ""
                        /*+ "DEBUG_LOG_PRINT_SHARED_PREFERENCE:"
                        + " ProPreferencesManager: "*/
                        + "PREFERENCES>>"
                        + " Key: " + key
                        + " Value: " + value
                        + ""
            )
        }
        println("<DEBUG_LOG_PRINT_SHARED_PREFERENCE: ProPreferencesManager\n\n")
    }

    class Builder {
        lateinit var context: Context
        lateinit var prefsKey: String
        var prefsMode = -1
        private var prefsUseDefault = false
        fun withPrefsName(argPrefsName: String): Builder {
            prefsKey = argPrefsName
            return this
        }

        fun withContext(argContext: Context): Builder {
            context = argContext
            return this
        }

        fun withMode(argMode: Mode): Builder {
            prefsMode =
                if (argMode.label === ContextWrapper.MODE_PRIVATE || argMode.label === ContextWrapper.MODE_WORLD_READABLE || argMode.label === ContextWrapper.MODE_WORLD_WRITEABLE || argMode.label === ContextWrapper.MODE_MULTI_PROCESS) {
                    argMode.label
                } else {
                    throw RuntimeException("The mode in the SharedPreference can only be set too ContextWrapper.MODE_PRIVATE, ContextWrapper.MODE_WORLD_READABLE, ContextWrapper.MODE_WORLD_WRITEABLE or ContextWrapper.MODE_MULTI_PROCESS")
                }
            return this
        }

        fun withDefaultPrefs(defaultPrefs: Boolean): Builder {
            prefsUseDefault = defaultPrefs
            return this
        }

        fun build(): ProPreferences {
            if (context == null) {
                throw RuntimeException("Context not set, please set context before building the Prefs instance.")
            }
            if (TextUtils.isEmpty(prefsKey)) {
                prefsKey = context.packageName
            }
            if (prefsUseDefault) {
                prefsKey += DEFAULT_SUFFIX
            }
            if (prefsMode == -1) {
                prefsMode = Mode.PRIVATE.label
            }

            //ProPreferencesManager.initPrefs(context, prefsKey, prefsMode);
            return ProPreferences(this)
        }
    }

    public enum class Mode(val label: Int) {
        PRIVATE(ContextWrapper.MODE_PRIVATE),

        @SuppressLint("WorldReadableFiles")
        WORLD_READABLE(ContextWrapper.MODE_WORLD_READABLE),

        @SuppressLint("WorldWriteableFiles")
        WORLD_WRITEABLE(ContextWrapper.MODE_WORLD_WRITEABLE),
        MULTI_PROCESS(ContextWrapper.MODE_MULTI_PROCESS),
        DEFAULT(ContextWrapper.MODE_PRIVATE);

        companion object {
            @JvmStatic
            fun getItem(label: Int): Mode {
                for (item in values()) {
                    if (item.label == label) {
                        return item
                    }
                }
                return DEFAULT
            }
        }
    }
}