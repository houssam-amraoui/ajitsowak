package ma.pam.ajitsowak.utils

import android.content.Context
import android.content.SharedPreferences
import ma.pam.ajitsowak.MyApp.getAppInstance
import ma.pam.ajitsowak.utils.Constants.myPreferences


class SharedPrefUtils {
    private val mSharedPreferences: SharedPreferences = getAppInstance().getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
    private var mSharedPreferencesEditor: SharedPreferences.Editor

    init {
        mSharedPreferencesEditor = mSharedPreferences.edit()
        mSharedPreferencesEditor.apply()
    }

    fun setValue(key: String, value: Any?) {
        when (value) {
            is Int? -> {
                mSharedPreferencesEditor.putInt(key, value!!).apply()
            }
            is String? -> {
                mSharedPreferencesEditor.putString(key, value!!).apply()
            }
            is Double? -> {
                mSharedPreferencesEditor.putString(key, value.toString()).apply()
            }
            is Long? -> {
                mSharedPreferencesEditor.putLong(key, value!!).apply()
            }
            is Boolean? -> {
                mSharedPreferencesEditor.putBoolean(key, value!!).apply()
            }
        }
    }

    fun getStringValue(key: String, defaultValue: String = ""): String {
        return mSharedPreferences.getString(key, defaultValue)!!
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun getLongValue(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun getBooleanValue(keyFlag: String, defaultValue: Boolean = false): Boolean {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue)
    }

    fun removeKey(key: String) {
        mSharedPreferencesEditor.remove(key).apply()
    }

    fun clear() {
        mSharedPreferencesEditor.clear().apply()
    }
}