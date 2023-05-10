package com.bigbird.metronomeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.databinding.FragmentSettingsBinding
import com.bigbird.metronomeapp.services.SharedViewModel
import com.bigbird.metronomeapp.utils.Colors
import com.bigbird.metronomeapp.utils.Keys
import com.bigbird.metronomeapp.utils.MySharedPreferences
import com.google.android.play.core.review.ReviewManagerFactory

class SettingsFragment : Fragment() {

    private lateinit var audioManager: AudioManager

    private lateinit var viewModel: SharedViewModel

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!
    private var isFlashOn: String = "false"
    private var isBackGroundPlayOn: String = "false"

    private var activeTheme: String = Colors.White.name
    private var stereoProgress: Float = 50.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)



        return binding.root

    }


    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* metronomeService?.setVolume(0.0f, 0.0f)
         metronomeService?.pause()*/

        ///back button
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        ///flash setup
        setupFlash()

        //
        setupBackgroundPlay()

        ///theme setup
        setupTheme()


        //practice time
        readPracticeTime()

        //volume
        setupVolume()

        //steroPaning
        setupStereoPanning()


        val reviewManager = ReviewManagerFactory.create(requireContext())

        binding.btnRateUs.setOnClickListener {
            val request = reviewManager.requestReviewFlow()
            Log.d("loooogg", "On Click");

            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    // Use the ReviewInfo object to display the review dialog
                    reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                        .addOnFailureListener { e ->

                            Log.d("loooogg", "review not enabled");
                            // Handle error
                        }
                } else {
                    Log.d("loooogg", "review not enabled2");

                    // Handle error
                }
            }.addOnFailureListener {
                Log.d("loooogg", "review not started ${it.toString()}");

            }
        }

        binding.button.setOnClickListener {

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@beatmate.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Feedback"))
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupStereoPanning() {


        MySharedPreferences(requireContext()).getValue(key = Keys.keyStereoPanning, "50")?.let {
            stereoProgress = it.toFloat()
            val leftVolume = (100 - stereoProgress) / 100f
            val rightVolume = stereoProgress / 100f
            GlobalCommon.print("left=$leftVolume right=$rightVolume")
            binding.seekBarStereoPanning.progress = stereoProgress.toInt()
            viewModel.metronomeService.value?.setVolume(leftVolume, rightVolume)

        }

        binding.seekBarStereoPanning.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                GlobalCommon.print(progress.toString())
                // adjust left and right volumes based on seek bar progress
                val leftVolume = (100 - progress) / 100f
                val rightVolume = progress / 100f

                GlobalCommon.print("left=$leftVolume right=$rightVolume")
                viewModel.metronomeService.value?.setVolume(leftVolume, rightVolume)
                MySharedPreferences(requireContext()).setValue(
                    key = Keys.keyStereoPanning,
                    progress.toString()
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun setupVolume() {


        binding.seekBarVolume.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    progress,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

        })
    }

    @SuppressLint("SetTextI18n")
    private fun readPracticeTime() {
        val practiceTime: String =
            MySharedPreferences(requireContext()).getValue(key = Keys.keyPracticeTime, "0")
                .toString()
        binding.tvTimePicker.text = GlobalCommon.formatTime(Integer.parseInt(practiceTime))

    }


    private fun setupFlash() {
        isFlashOn =
            MySharedPreferences(requireContext()).getValue(key = Keys.keyColorFlashOn, "false")
                .toString()
        binding.switchColorFlash.isChecked = isFlashOn == "true"
        binding.switchColorFlash.setOnCheckedChangeListener { _, isChecked ->
            MySharedPreferences(requireContext()).setValue(
                key = Keys.keyColorFlashOn,
                isChecked.toString()
            )
        }
    }


    private fun setupBackgroundPlay() {
        isBackGroundPlayOn =
            MySharedPreferences(requireContext()).getValue(key = Keys.keyBackgroundPlay, "false")
                .toString()
        binding.switchBgPlay.isChecked = isBackGroundPlayOn == "true"
        binding.switchBgPlay.setOnCheckedChangeListener { _, isChecked ->
            MySharedPreferences(requireContext()).setValue(
                key = Keys.keyBackgroundPlay,
                isChecked.toString()
            )
        }
    }

    private fun setupTheme() {

        activeTheme =
            MySharedPreferences(requireContext()).getValue(
                key = Keys.keyActiveThemeColor,
                Colors.White.name
            ).toString()


        GlobalCommon.print("active theme = $activeTheme")
        when (activeTheme) {

            Colors.White.name -> {
                binding.radioGroup1.check(R.id.radioWhite)

            }
            Colors.Purple.name -> {
                binding.radioGroup1.check(R.id.radioPurple)

            }
            Colors.Silver.name -> {
                binding.radioGroup1.check(R.id.radioSilver)

            }
            Colors.Green.name -> {
                binding.radioGroup1.check(R.id.radioGreen)

            }
            Colors.Pink.name -> {
                binding.radioGroup1.check(R.id.radioPink)

            }
        }


        binding.radioGroup1.setOnCheckedChangeListener { _, checkedId ->

            GlobalCommon.print(checkedId.toString())
            when (checkedId) {
                R.id.radioWhite -> {
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyActiveThemeColor,
                        Colors.White.name
                    )
                }
                R.id.radioPurple -> {
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyActiveThemeColor,
                        Colors.Purple.name
                    )
                }
                R.id.radioSilver -> {
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyActiveThemeColor,
                        Colors.Silver.name
                    )
                }
                R.id.radioGreen -> {
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyActiveThemeColor,
                        Colors.Green.name
                    )
                }
                R.id.radioPink -> {
                    MySharedPreferences(requireContext()).setValue(
                        Keys.keyActiveThemeColor,
                        Colors.Pink.name
                    )
                }
            }
        }

    }


}