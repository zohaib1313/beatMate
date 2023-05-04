package com.bigbird.metronomeapp.services

import java.util.*

class TickGenerator {

    private var tickValue = 0
    private var timer: Timer? = null
    private var listener: MetronomeService.TimeTickerListener? = null
    fun startTicking() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                tickValue++
                listener?.onSecondsTick(tickValue)

            }
        }, 0, 1000)
    }

    fun setTickListener(tickListener: MetronomeService.TimeTickerListener) {
        listener = tickListener
    }

    fun stopTicking() {
        timer?.cancel()
        timer = null
        tickValue = 0
    }


}
