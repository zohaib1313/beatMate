package com.bigbird.metronomeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentHomeBinding

const val DEFAULT_BPM = 100

class HomeFragment : AbstractMetronomeFragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tempoValue = "1"

        binding.radioBtn1.setOnClickListener {
            changeSlectedButton(id = 1)
        }
        binding.radioBtn2.setOnClickListener {
            changeSlectedButton(id = 2)
        }
        binding.radioBtn3.setOnClickListener {
            changeSlectedButton(id = 3)
        }
        binding.radioBtn4.setOnClickListener {
            changeSlectedButton(id = 4)
        }


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.tempoValue = progress.toString()
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
            if (tempoValue > 0)
                binding.tempoValue = (tempoValue - 1).toString()
        }
        binding.ivAddTempoValue.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue)
            if (tempoValue < 200)
                binding.tempoValue = (tempoValue + 1).toString()
        }
        ///tempo tvS
        binding.removeFive.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) - 5
            if (tempoValue > 0)
                binding.tempoValue = tempoValue.toString()
        }
        binding.divideTwo.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) / 5
            if (tempoValue > 0)
                binding.tempoValue = tempoValue.toString()

        }
        binding.multiplyTwo.setOnClickListener {
            val tempoValue: Int = Integer.parseInt(binding.tempoValue) * 2
            if (tempoValue < 200)
                binding.tempoValue = tempoValue.toString()

        }
        binding.addFive.setOnClickListener {

            val tempoValue: Int = Integer.parseInt(binding.tempoValue) + 5
            if (tempoValue < 200)
                binding.tempoValue = tempoValue.toString()
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            )
        }
        binding.btnPlay.setOnClickListener {
            metronomeService?.play()
            GlobalCommon.print("playing")
        }
        return binding.root

    }

    override fun onTick(interval: Int) {
        if (this.isVisible && metronomeService?.isPlaying!!)
            GlobalCommon.print("interval $interval");
        //  activity?.runOnUiThread {beatsView.nextBeat()}
    }


    private fun changeSlectedButton(id: Int) {
        when (id) {
            1 -> {
                binding.radioBtn1.setBackgroundResource(R.drawable.rounded_button_selected)
                binding.radioBtn2.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn3.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn4.setBackgroundResource(R.drawable.rounded_holo)
            }
            2 -> {
                binding.radioBtn1.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn2.setBackgroundResource(R.drawable.rounded_button_selected)
                binding.radioBtn3.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn4.setBackgroundResource(R.drawable.rounded_holo)
            }
            3 -> {
                binding.radioBtn1.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn2.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn3.setBackgroundResource(R.drawable.rounded_button_selected)
                binding.radioBtn4.setBackgroundResource(R.drawable.rounded_holo)
            }
            4 -> {
                binding.radioBtn1.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn2.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn3.setBackgroundResource(R.drawable.rounded_holo)
                binding.radioBtn4.setBackgroundResource(R.drawable.rounded_button_selected)
            }

        }
    }


}