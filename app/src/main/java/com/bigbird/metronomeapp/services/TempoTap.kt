package com.bigbird.metronomeapp.services

import android.os.Handler
import android.os.Looper

class TempoTap {
    private val tapTimes = mutableListOf<Long>()
    private val handler = Handler(Looper.getMainLooper())
    private val clearTapsRunnable = Runnable {
        tapTimes.clear()
    }

    fun tap() {
        val currentTime = System.currentTimeMillis()
        tapTimes.add(currentTime)
    }

    fun calculateBPM(): Int {
        if (tapTimes.size < 2) {
            return -1 // Not enough taps
        }

        val tapIntervals = mutableListOf<Long>()
        for (i in 1 until tapTimes.size) {
            val interval = tapTimes[i] - tapTimes[i - 1]
            tapIntervals.add(interval)
        }

        val averageInterval = tapIntervals.average()
        val bpm = (60 / (averageInterval / 1000)).toInt()

        return bpm
    }

    fun reset(hard: Boolean) {
        if (hard) {
            tapTimes.clear()
        }else{
            handler.removeCallbacks(clearTapsRunnable)
            handler.postDelayed(clearTapsRunnable, 2000L)
        }
    }
}
