package com.shubhans.wear.run.data

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.shubhans.connectivity.data.DeviceNode
import com.shubhans.connectivity.data.DeviceType
import com.shubhans.connectivity.data.WearNodeDiscovery
import com.shubhans.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WatchToPhoneConnector(
    nodeDiscovery: WearNodeDiscovery, applicationScope: CoroutineScope
) : PhoneConnector {

    val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedNode = _connectedNode.asStateFlow()

    val messagingAction =
        nodeDiscovery.observeConnectedDevices(DeviceType.WATCH).onEach { connectedNodes ->
            val node = connectedNodes.firstOrNull()
            if (node != null && node.isNearBy) {
                _connectedNode.value = node
            }
        }.launchIn(applicationScope)
}