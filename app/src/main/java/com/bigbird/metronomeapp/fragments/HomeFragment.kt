package com.bigbird.metronomeapp.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentHomeBinding
import com.bigbird.metronomeapp.services.MetronomeService
import com.bigbird.metronomeapp.services.MetronomeService.Companion.metronomeService
import com.bigbird.metronomeapp.services.MetronomeService.Companion.tickGenerator
import com.bigbird.metronomeapp.services.SharedViewModel
import com.bigbird.metronomeapp.services.TempoTap
import com.bigbird.metronomeapp.utils.AppUtils
import com.bigbird.metronomeapp.utils.Colors
import com.bigbird.metronomeapp.utils.Keys
import com.bigbird.metronomeapp.utils.MySharedPreferences


class HomeFragment : AbstractMetronomeFragment(), MetronomeService.TimeTickerListener {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: SharedViewModel

    private val binding get() = _binding!!

    ///taps
    private var tapCount = 0
    private var totalDuration: Double = 0.0
    private var lastTapTime: Double = 0.0


    private var isFlashOn: String = "true"
    private var activeTheme: String = Colors.White.name
    private var toggleColor: Boolean = false

    private var totalPlayedForSeconds: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        setUpStereo()
        setupTempo()
        tickGenerator.setTickListener(this)
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
            metronomeService?.let {
                viewModel.metronomeService.value = it

                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                )
            }

        }


        setPlayButtonDrawable(activeTheme, true)
        metronomeService?.isPlaying?.let {

            GlobalCommon.print("playing $it  ${activeTheme}")
            if (it) {
                setPlayButtonDrawable(activeTheme, false)
            } else {
                setPlayButtonDrawable(activeTheme, true)
            }
        }


        binding.btnPlay.setOnClickListener {

            metronomeService?.isPlaying?.let {
                if (it) {
                    binding.beatsView.resetBeats(true)
                    setPlayButtonDrawable(activeTheme, true)

                    metronomeService?.pause()

                    val practiceTime: String =
                        MySharedPreferences(requireContext()).getValue(
                            key = Keys.keyPracticeTime,
                            "0"
                        )
                            .toString()
                    GlobalCommon.print("lastSaved time = $practiceTime")
                    GlobalCommon.print("sesssion time = $totalPlayedForSeconds")
                    val timeee = totalPlayedForSeconds + Integer.parseInt(practiceTime)

                    GlobalCommon.print("total session time = ${(timeee)}")
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyPracticeTime,
                        timeee.toString()
                    )
                    totalPlayedForSeconds=0

                } else {

                    setUpStereo()
                    setupTempo()
                    binding.beatsView.resetBeats(true)
                    setPlayButtonDrawable(activeTheme, false)
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
                MySharedPreferences(requireContext()).setValue(
                    key = Keys.keyActiveTempo,
                    bpm.toString()
                )
            }

            override fun afterTextChanged(editable: Editable) {}
        })


        setInitialTheme(activeTheme)

        val tempoTap = TempoTap()
        binding.roundedButton.setOnClickListener {
            tempoTap.tap()
            val bpm = tempoTap.calculateBPM()
            if (bpm != -1) {
                binding.tempoValue = bpm.toString()
                Log.d("Tap Timing", "BPM: $bpm")
                tempoTap.reset(hard = false)
            }
            //   tapCount++
//            val currentTime = System.currentTimeMillis()
//            if (tapCount == 1) {
//                lastTapTime = currentTime.toDouble()
//            } else {
//                val duration = currentTime - lastTapTime
//                totalDuration += duration
//                lastTapTime = currentTime.toDouble()
//                val averageDuration = totalDuration / tapCount
//                val averageSeconds = (averageDuration / 1000)
//                var bpm = ((60.0) / averageSeconds).toInt()
//                Log.d("Tap Timing", "Average duration: $averageSeconds s")
//                Log.d("Tap Timing", "BPM: $bpm")
//                if (bpm > Keys.maxProgress) {
//                    bpm = 300
//                    binding.tempoValue = bpm.toString()
//                } else {
//                    binding.tempoValue = bpm.toString()
//                }
//
//                if (tapCount == 4) {
//                    tapCount = 0
//                    totalDuration = 0.0
//                }
            ///          }

        }

        if(totalPlayedForSeconds>0)
        binding.tvTimer.text = GlobalCommon.formatTime(totalPlayedForSeconds)

        return binding.root

    }


    private fun setUpStereo() {
        MySharedPreferences(requireContext()).getValue(key = Keys.keyStereoPanning, "50")?.let {
            val stereoProgress = it.toFloat()
            val leftVolume = (100 - stereoProgress) / 100f
            val rightVolume = stereoProgress / 100f
            GlobalCommon.print("left=$leftVolume right=$rightVolume")
            metronomeService?.setVolume(leftVolume, rightVolume)
        }
    }

    private fun setupTempo() {

        MySharedPreferences(requireContext()).getValue(
            key = Keys.keyActiveTempo,
            Keys.defaultBpm.toString()
        )?.let {
            binding.tempoValue = it
            binding.activeTempo = AppUtils.getTempoDescription(it.toInt())
            binding.seekBar.max = Keys.maxProgress
            metronomeService?.setBpm(it.toInt())
        }
    }


    private fun setInitialTheme(activeColor: String) {
        when (activeColor) {

            Colors.White.name -> {
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            Colors.Purple.name -> {
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_b
                    )
                )


            }
            Colors.Silver.name -> {
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.silver_b
                    )
                )

            }
            Colors.Green.name -> {
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green_b
                    )
                )


            }
            Colors.Pink.name -> {

                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pink_b
                    )
                )

            }
        }


    }

    private fun setPlayButtonDrawable(activeColor: String, play: Boolean) {
        when (activeColor) {

            Colors.White.name -> {

                if (play) {
                    binding.btnPlay.setImageResource(R.drawable.play_arrow_white)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.stop_arrow_white)

                }
            }
            Colors.Purple.name -> {
                if (play) {
                    binding.btnPlay.setImageResource(R.drawable.play_arrow_purple)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.stop_arrow_purple)

                }


            }
            Colors.Silver.name -> {
                if (play) {
                    binding.btnPlay.setImageResource(R.drawable.play_arrow_silver)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.stop_arrow_silver)

                }

            }
            Colors.Green.name -> {
                if (play) {
                    binding.btnPlay.setImageResource(R.drawable.play_arrow_green)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.stop_arrow_green)

                }


            }
            Colors.Pink.name -> {

                if (play) {
                    binding.btnPlay.setImageResource(R.drawable.play_arrow_pink)
                } else {
                    binding.btnPlay.setImageResource(R.drawable.stop_arrow_pink)

                }

            }
        }
    }

    override fun onTick(interval: Int) {
        GlobalCommon.print("ticckkk==${interval.toString()}")
        if (this.isVisible && isAdded && metronomeService?.isPlaying == true) {
            activity?.runOnUiThread { binding.beatsView.nextBeat(tick = interval) }
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
                    ContextCompat.getColor(
                        requireContext(), R.color.purple_a
                    ).let { it ->
                        binding.constraintLayoutMain.setBackgroundColor(it)

                    }


                } else {
                    ContextCompat.getColor(
                        requireContext(), R.color.purple_b
                    ).let {
                        binding.constraintLayoutMain.setBackgroundColor(it)


                    }

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
        if (this.isVisible && isAdded) {
            activity?.runOnUiThread {
                binding.tvTimer.text = GlobalCommon.formatTime(secondsPassed)
            }
        }
        totalPlayedForSeconds = secondsPassed

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}