package com.bigbird.metronomeapp

import android.util.Log

class GlobalCommon {

    companion object {

        fun print(value: String) {
            Log.d("****MYTAG***", value)
        }

        fun formatTime(seconds: Int): String {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60

            return if (hours > 0) {
                String.format("%dH:%01d:%02d", hours, minutes, remainingSeconds)
            } else {
                String.format("%01d:%02d", minutes, remainingSeconds)
            }
        }
    }
}