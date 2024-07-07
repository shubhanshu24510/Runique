package com.shubhans.connectivity.domain

import kotlinx.coroutines.flow.Flow

interface NodeDiscovery {
    fun observeConnectedDevices(localDevicesType: DeviceType): Flow<Set<DeviceNode>>
}