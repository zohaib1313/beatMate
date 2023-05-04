package com.bigbird.metronomeapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.bigbird.metronomeapp.GlobalCommon
import com.bigbird.metronomeapp.services.MetronomeService
import com.bigbird.metronomeapp.services.MetronomeService.Companion.metronomeService
import com.bigbird.metronomeapp.utils.Keys
import com.bigbird.metronomeapp.utils.MySharedPreferences

abstract class AbstractMetronomeFragment : Fragment(), MetronomeService.TickListener,
    MetronomeService.TimeTickerListener {
    protected var isLocalBounded = false
    private var isBackGroundPlayOn: String = "false"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isBackGroundPlayOn =
            MySharedPreferences(requireContext()).getValue(key = Keys.keyBackgroundPlay, "false")
                .toString()

        GlobalCommon.print("bindinnngnggg")
        if (isBackGroundPlayOn == "true") {
            unbindLocalService()
            bindGlobalService()
            GlobalCommon.print("binded global service")
        } else {
            GlobalCommon.print("binded local service")
            bindService()
        }

    }

    private fun bindGlobalService() {
        val intent = Intent(requireContext(), MetronomeService::class.java)
       activity?.applicationContext?.bindService(
            intent,
            mConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun bindService() {
        activity?.bindService(
            Intent(
                activity,
                MetronomeService::class.java
            ), mConnection, Context.BIND_AUTO_CREATE
        )
        isLocalBounded = true
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            GlobalCommon.print("service**** connected***")
            metronomeService = (service as MetronomeService.MetronomeBinder).getService()
            metronomeService?.addTickListener(this@AbstractMetronomeFragment)

        }

        override fun onServiceDisconnected(className: ComponentName) {
            metronomeService = null
            isLocalBounded = false
            GlobalCommon.print("service**** disconnected***")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalCommon.print("onnnn ddessstroyy")
       unbindLocalService()
    }

    private fun unbindLocalService() {
        if (isLocalBounded) {
            metronomeService?.removeTickListener(this)
            metronomeService?.pause()
            // Detach our existing connection.
            requireActivity().unbindService(mConnection)
            isLocalBounded = false
        }
    }
}