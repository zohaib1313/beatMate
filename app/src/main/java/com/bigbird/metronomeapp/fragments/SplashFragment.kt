package com.bigbird.metronomeapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.R
import com.bigbird.metronomeapp.databinding.FragmentSplashBinding
import java.util.*
import kotlin.concurrent.schedule


class SplashFragment : Fragment() {


    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        Handler(Looper.getMainLooper()).postDelayed({

            val navOptions = NavOptions.Builder()
                .setPopUpTo(
                    R.id.splashFragment,
                    true
                ) // remove the previous fragment from the back stack
                .build()
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToWelcomeFragment2(),
                navOptions = navOptions
            )

        }, 2000)
    }


}