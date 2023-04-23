package com.bigbird.metronomeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {


    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {

            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment(),NavOptions.Builder()
                .setPopUpTo(
                    R.id.welcomeFragment,
                    true
                ) // remove the previous fragment from the back stack
                .build())
        }
    }

}