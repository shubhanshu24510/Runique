@file:OptIn(ExperimentalCoroutinesApi::class)

package com.shubhans.run.data.connectivity

import com.shubhans.connectivity.domain.DeviceNode
import com.shubhans.connectivity.domain.DeviceType
import com.shubhans.connectivity.domain.WearNodeDiscovery
import com.shubhans.connectivity.domain.messageing.MessageClient
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.connectivity.domain.messageing.MessagingError
import com.shubhans.connectivity.domain.messaging.WearMessageClient
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class PhoneToWatchConnector(
    nodeDiscovery: WearNodeDiscovery,
    applicationScope: CoroutineScope,
    private val messageClient: MessageClient
) : WatchConnector {
    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)

    override val connectedDevice = _connectedNode.asStateFlow()

    private val isTrackable = MutableStateFlow(false)

    override val messagingAction: Flow<MessagingAction> =
        nodeDiscovery.observeConnectedDevices(DeviceType.PHONE)
            .flatMapLatest { connectedDevices ->
                val node = connectedDevices.firstOrNull()
                if (node != null && node.isNearBy) {
                    _connectedNode.value = node
                    messageClient.connectToNode(node.id)
                } else flowOf()
            }.onEach { action ->
                if (action == MessagingAction.connectionRequest) {
                    if (isTrackable.value) {
                        sendActionToWatch(MessagingAction.Trackable)
                    } else {
                        sendActionToWatch(MessagingAction.Untraceable)
                    }
                }
            }.shareIn(
                applicationScope,
                replay = 1,
                started = SharingStarted.Eagerly
            )

    init {
        _connectedNode
            .filterNotNull()
            .flatMapLatest { isTrackable }
            .onEach { isTrackable ->
                sendActionToWatch(MessagingAction.connectionRequest)
                val action = if (isTrackable) {
                    MessagingAction.Trackable
                } else MessagingAction.Untraceable

            }.launchIn(applicationScope)
    }
    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
       return messageClient.sendOrQueryAction(action)
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        this.isTrackable.value = isTrackable
    }
}