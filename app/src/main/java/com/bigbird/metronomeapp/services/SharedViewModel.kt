package com.bigbird.metronomeapp.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    var metronomeService: MutableLiveData<MetronomeService> = MutableLiveData()

}