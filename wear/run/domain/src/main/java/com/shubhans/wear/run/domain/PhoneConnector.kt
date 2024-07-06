package com.shubhans.wear.run.domain

import com.shubhans.connectivity.data.DeviceNode
import kotlinx.coroutines.flow.StateFlow

interface PhoneConnector {
    val connectedNode: StateFlow<DeviceNode?>
}