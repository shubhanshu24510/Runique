package com.shubhans.connectivity.data

import kotlinx.coroutines.flow.Flow

interface NodeDiscovery {
    fun observeConnectedDevices(localDevicesType: DeviceType): Flow<Set<DeviceNode>>
}