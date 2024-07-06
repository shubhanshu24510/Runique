package com.shubhans.run.data.connectivity

import com.shubhans.connectivity.data.DeviceNode
import com.shubhans.connectivity.data.DeviceType
import com.shubhans.connectivity.data.WearNodeDiscovery
import com.shubhans.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhoneToWatchConnector(
    nodeDiscovery: WearNodeDiscovery,
    applicationScope: CoroutineScope,
) : WatchConnector {
    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)

    override val connectedDevice = _connectedNode.asStateFlow()

    private val isTrackable = MutableStateFlow(false)

    val messagingActions =
        nodeDiscovery.observeConnectedDevices(DeviceType.PHONE).onEach { connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if (node != null && node.isNearBy) {
                _connectedNode.value = node
            }
        }.launchIn(applicationScope)

    override fun setIsTrackable(isTrackable: Boolean) {
        this.isTrackable.value = isTrackable
    }
}