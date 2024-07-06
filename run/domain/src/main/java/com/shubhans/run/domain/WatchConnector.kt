package com.shubhans.run.domain

import com.shubhans.connectivity.data.DeviceNode
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevice: StateFlow<DeviceNode?>
    fun setIsTrackable(isTrackable: Boolean)
}