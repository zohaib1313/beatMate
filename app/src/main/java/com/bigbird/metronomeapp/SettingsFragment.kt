package com.bigbird.metronomeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.databinding.FragmentSettingsBinding

import com.bigbird.metronomeapp.utils.Colors
import com.bigbird.metronomeapp.utils.Keys
import com.bigbird.metronomeapp.utils.MySharedPreferences

class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!
    private var isFlashOn: String = "false"
    private var activeTheme: String = Colors.White.name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ///back button
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        ///flash setup
        setupFlash()

        ///theme setup
        setupTheme()


        //practice time
        readPracticeTime()

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