package com.bigbird.metronomeapp.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class MySharedPreferences(context: Context) {


    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Keys.keySharedPref, Context.MODE_PRIVATE
    )

    open fun setValue(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    open fun getValue(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    open fun removeValue(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    open fun clearAllValues() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    suspend fun saveAsyncValue(key: String, value: String) {
        withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }
}



