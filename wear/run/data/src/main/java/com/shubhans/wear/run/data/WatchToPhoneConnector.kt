@file:OptIn(ExperimentalCoroutinesApi::class)

package com.shubhans.wear.run.data

import com.shubhans.connectivity.domain.DeviceNode
import com.shubhans.connectivity.domain.DeviceType
import com.shubhans.connectivity.domain.WearNodeDiscovery
import com.shubhans.connectivity.domain.messageing.MessageClient
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.connectivity.domain.messageing.MessagingError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class WatchToPhoneConnector(
    nodeDiscovery: WearNodeDiscovery,
    applicationScope: CoroutineScope,
    private val messageClient: MessageClient
) : PhoneConnector {

    val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedNode = _connectedNode.asStateFlow()

    override val messingAction: Flow<MessagingAction> =
        nodeDiscovery.observeConnectedDevices(DeviceType.WATCH)
            .flatMapLatest { connectedNodes ->
            val node = connectedNodes.firstOrNull()
            if (node != null && node.isNearBy) {
                _connectedNode.value = node
                messageClient.connectToNode(node.id)
            }else flowOf()
        }
            .shareIn(
                applicationScope,
                started = SharingStarted.Eagerly
            )

    override suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError> {
       return messageClient.sendOrQueryAction(action)
    }
}