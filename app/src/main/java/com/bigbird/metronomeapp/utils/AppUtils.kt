package com.bigbird.metronomeapp.utils

class AppUtils {

    companion object {

        fun getTempoDescription(tempo: Int): String {
            return when (tempo) {
                in Int.MIN_VALUE..24 -> "Larghissimo"
                in 25..40 -> "Adagissimo"
                in 41..66 -> "Grave"
                in 67..68 -> "Adagio"
                in 69..80 -> "Adagietto"
                in 81..108 -> "Lento"
                in 109..112 -> "Andante moderato"
                in 113..120 -> "Allegretto"
                in 121..156 -> "Allegro"
                in 157..160 -> "Molto Allegro"
                in 161..184 -> "Vivacissimo"
                in 185..200 -> "Presto"
                in 201..Int.MAX_VALUE -> "Prestissimo"
                else -> "Invalid tempo"
            }
        }


    }
}

enum class Colors {
    White, Purple, Silver, Green, Pink

}