package com.bigbird.metronomeapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentHomeBinding


class HomeFragment : AbstractMetronomeFragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tempoValue = "1"



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
            if (tempoValue < 200) {
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
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) / 5
            if (tempoValue > 0) {
                binding.tempoValue = tempoValue.toString()


            }
        }
        binding.multiplyTwo.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) * 2
            if (tempoValue < 200) {
                binding.tempoValue = tempoValue.toString()


            }
        }
        binding.addFive.setOnClickListener {

            val tempoValue: Int = Integer.parseInt(binding.tempoValue) + 5
            if (tempoValue < 200) {
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
                    GlobalCommon.print("stopped")
                } else {
                    binding.beatsView.resetBeats(true)
                    binding.btnPlay.setBackgroundResource(R.drawable.stop_arrow)
                    metronomeService?.play()
                    GlobalCommon.print("playing")
                }

            }

        }


        binding.tvTempoValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                val bpm = Integer.parseInt(charSequence.toString())
                metronomeService?.setBpm(bpm)

            }

            override fun afterTextChanged(editable: Editable) {}
        })
        return binding.root

    }

    override fun onTick(interval: Int) {
        if (this.isVisible && metronomeService?.isPlaying!!)
            activity?.runOnUiThread { binding.beatsView.nextBeat() }
    }


}