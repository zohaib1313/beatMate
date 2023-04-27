package com.bigbird.metronomeapp.fragments


import TimeTicker
import TimeTickerListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentHomeBinding

import com.bigbird.metronomeapp.utils.AppUtils
import com.bigbird.metronomeapp.utils.Colors
import com.bigbird.metronomeapp.utils.Keys
import com.bigbird.metronomeapp.utils.MySharedPreferences


class HomeFragment : AbstractMetronomeFragment(), TimeTickerListener {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var isFlashOn: String = "true"
    private var activeTheme: String = Colors.Green.name
    private var toggleColor: Boolean = false

    var totalPlayedForSeconds: Int = 0
    val timeTicker: TimeTicker = TimeTicker(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        ///setting defaults
        binding.tempoValue = Keys.defaultBpm.toString()
        binding.activeTempo = AppUtils.getTempoDescription(Keys.defaultBpm)
        binding.seekBar.max = Keys.maxProgress


        isFlashOn =
            MySharedPreferences(requireContext()).getValue(key = Keys.keyColorFlashOn, "false")
                .toString()

        activeTheme = MySharedPreferences(requireContext()).getValue(
            key = Keys.keyActiveThemeColor, Colors.White.name
        ).toString()



        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.tempoValue = progress.toString()
                metronomeService?.setBpm(progress)


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        ////tempo ivs
        binding.ivRemoveTempoValue.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue)
            if (tempoValue > 0) {
                val value = (tempoValue - 1)
                binding.tempoValue = value.toString()
            }
        }
        binding.ivAddTempoValue.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue)
            if (tempoValue < Keys.maxProgress) {
                val value = (tempoValue + 1)
                binding.tempoValue = value.toString()

            }

        }
        ///tempo tvS
        binding.removeFive.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) - 5
            if (tempoValue > 0) {
                binding.tempoValue = tempoValue.toString()
            }
        }


        binding.divideTwo.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) / 2
            if (tempoValue > 0) {
                binding.tempoValue = tempoValue.toString()

            }
        }


        binding.multiplyTwo.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) * 2
            if (tempoValue < Keys.maxProgress) {
                binding.tempoValue = tempoValue.toString()

            }
        }


        binding.addFive.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) + 5
            if (tempoValue < Keys.maxProgress) {
                binding.tempoValue = tempoValue.toString()
            }
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            )
        }

        binding.btnPlay.setOnClickListener {

            metronomeService?.isPlaying?.let {
                if (it) {
                    binding.beatsView.resetBeats(true)
                    binding.btnPlay.setBackgroundResource(R.drawable.play_arrow)
                    metronomeService?.pause()
                    timeTicker.stop()

                    val practiceTime: String =
                        MySharedPreferences(requireContext()).getValue(key = Keys.keyPracticeTime, "0")
                            .toString()
                    GlobalCommon.print("lastSaved time = $practiceTime")
                    GlobalCommon.print("sesssion time = $totalPlayedForSeconds")
                    val timeee = totalPlayedForSeconds + Integer.parseInt(practiceTime)

                    GlobalCommon.print("total session time = ${(timeee)}")
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyPracticeTime,
                        timeee.toString()
                    )

                } else {
                    binding.beatsView.resetBeats(true)
                    binding.btnPlay.setBackgroundResource(R.drawable.stop_arrow)
                    timeTicker.start()
                    metronomeService?.play()


                }

            }

        }


        binding.tvTempoValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                val bpm = Integer.parseInt(charSequence.toString())
                metronomeService?.setBpm(bpm)
                binding.activeTempo = AppUtils.getTempoDescription(bpm)

            }

            override fun afterTextChanged(editable: Editable) {}
        })





        return binding.root

    }


    override fun onTick(interval: Int) {
        if (this.isVisible && metronomeService?.isPlaying!!) {
            activity?.runOnUiThread { binding.beatsView.nextBeat() }
            activity?.runOnUiThread {
                if ((activeTheme != Colors.White.name) && (isFlashOn == "true")) {
                    changeTheme(activeTheme)
                }
            }

        }
    }


    private fun changeTheme(activeColor: String) {
        when (activeColor) {

            Colors.White.name -> {

            }
            Colors.Purple.name -> {
                if (toggleColor) {

                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.purple_a
                        )
                    )

                } else {
                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.purple_b
                        )
                    )
                }
            }
            Colors.Silver.name -> {
                if (toggleColor) {

                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver_a
                        )
                    )

                } else {
                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver_b
                        )
                    )
                }
            }
            Colors.Green.name -> {
                if (toggleColor) {

                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.green_a
                        )
                    )

                } else {
                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.green_b
                        )
                    )
                }
            }
            Colors.Pink.name -> {
                if (toggleColor) {

                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.pink_a
                        )
                    )

                } else {
                    binding.constraintLayoutMain.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.pink_b
                        )
                    )
                }
            }
        }
        toggleColor = !toggleColor
    }

    override fun onSecondsTick(secondsPassed: Int) {

        activity?.runOnUiThread {
            binding.tvTimer.text = GlobalCommon.formatTime(secondsPassed)
        }
        totalPlayedForSeconds = secondsPassed

    }


}