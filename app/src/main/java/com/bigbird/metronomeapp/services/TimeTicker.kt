import java.util.*
import kotlin.concurrent.timerTask

interface TimeTickerListener {
    fun onSecondsTick(secondsPassed: Int)
}

class TimeTicker(private val listener: TimeTickerListener) {

    private var timer: Timer? = null
    private var secondsPassed = 0

    fun start() {
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            secondsPassed++
            listener.onSecondsTick(secondsPassed)
        }, 0, 1000)
    }

    fun stop() {
        timer?.cancel()
        secondsPassed = 0
    }
}
