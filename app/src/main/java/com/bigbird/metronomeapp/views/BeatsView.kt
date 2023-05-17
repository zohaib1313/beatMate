package com.bigbird.metronomeapp.views

import android.content.Context
import android.graphics.BlendMode
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bigbird.metronomeapp.R

const val MAX_BEAT = 4

/**
 * Displays the beats and active beat dynamically
 */
class BeatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var beatsPerMeasure = 4
        set(beats) {
            field = beats
            resetBeats(true)
        }
    private var isEmphasis = true
        set(isEmphasis) {
            field = isEmphasis
            resetBeats(false)
        }
    private var highlightedBeat = -1
    private val firstEmptyCircle =
        ContextCompat.getDrawable(context, R.drawable.rounded_holo)
    private val firstFullCircle =
        ContextCompat.getDrawable(context, R.drawable.active_beat)
    private val firstEmptyCircleNoEmphasis =
        ContextCompat.getDrawable(context, R.drawable.rounded_holo)
    private val firstFullCircleNoEmphasis =
        ContextCompat.getDrawable(context, R.drawable.rounded_holo)
    private val offCircle =
        ContextCompat.getDrawable(context, R.drawable.rounded_holo)
    private val emptyCircle = ContextCompat.getDrawable(context, R.drawable.rounded_holo)
    private val fullCircle = ContextCompat.getDrawable(context, R.drawable.active_beat)
    private val marginParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)


    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        marginParams.setMargins(40, 0, 40, 0)
        marginParams.width = 100
        // marginParams.height=120
        createBeats()
    }

    private fun createBeats() {
        for (i in 0 until MAX_BEAT) {
            val imageView = ImageView(context)
            val drawable = getCircleDrawable(i, false)
            imageView.setImageDrawable(drawable)
            imageView.layoutParams = marginParams
            imageView.clipToOutline=false


            addView(imageView)
        }
    }

    fun nextBeat(tick: Int) {
        for (i in 0 until MAX_BEAT) {
            val beatsViews = getChildAt(i) as ImageView
            if (i == tick) {
                animateImageChange(imageView = beatsViews, newDrawable = fullCircle)
               // beatsViews.setImageDrawable(fullCircle)
            } else {
               // beatsViews.setImageDrawable(emptyCircle)
                  animateImageChange(imageView = beatsViews, newDrawable = emptyCircle)
            }

        }
    }

    private fun animateImageChange(imageView: ImageView, newDrawable: Drawable?) {
        imageView.adjustViewBounds = true
        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
        }


        imageView.setLayerPaint(paint)
        imageView.setImageDrawable(newDrawable)
    }

    private fun getCircleDrawable(beatIndex: Int, isFull: Boolean): Drawable? {

        return when (beatIndex) {
            0 -> when (isEmphasis) {
                true -> if (isFull) firstFullCircle else firstEmptyCircle
                false -> if (isFull) firstFullCircleNoEmphasis else firstEmptyCircleNoEmphasis
            }
            in 1 until beatsPerMeasure -> if (isFull) fullCircle else emptyCircle
            else -> offCircle
        }
    }

    /**
     * Resets the beats view
     * @param resetHighlightedBeat - indicates if the highlighted beat should be reset to zero
     */
    fun resetBeats(resetHighlightedBeat: Boolean) {
        if (resetHighlightedBeat) highlightedBeat = -1
        removeAllViews()
        createBeats()
    }
}