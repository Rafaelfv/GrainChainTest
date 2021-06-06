package com.rafaelfv.grainchaintest.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.rafaelfv.grainchaintest.R
import com.rafaelfv.grainchaintest.base.BaseViewModel
import javax.inject.Inject

class FragmentMainViewModel : BaseViewModel(), LifecycleObserver {

    @Inject
    lateinit var context: Context
    var visibilityBtnRecord: MutableLiveData<Boolean> = MutableLiveData()
    var visibilityBtnIndicator: MutableLiveData<Boolean> = MutableLiveData()
    var srcImageRecording: MutableLiveData<Int> = MutableLiveData()
    private var recordingLastStatus = false

    init {
        srcImageRecording.value = R.mipmap.ic_route_map
    }

    fun updateVisibilityBtn(visible: Boolean) {
        visibilityBtnRecord.value = visible
    }

    fun onClickBtnRecord() {
        if(!recordingLastStatus){
            startRecording()
        }else{
            stopRecording()
        }
        recordingLastStatus = !recordingLastStatus
    }

    private fun stopRecording() {
        visibilityBtnIndicator.value = false
        srcImageRecording.value = R.mipmap.ic_route_map
    }

    private fun startRecording() {
        visibilityBtnIndicator.value = true
        srcImageRecording.value = R.mipmap.ic_stop_record
    }

}