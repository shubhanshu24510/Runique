package com.shubhans.run.domain

import com.shubhans.connectivity.domain.DeviceNode
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.connectivity.domain.messageing.MessagingError
import com.shubhans.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevice: StateFlow<DeviceNode?>
    val messagingAction: Flow<MessagingAction>
    suspend fun sendActionToWatch(action: MessagingAction):EmptyResult<MessagingError>
    fun setIsTrackable(isTrackable: Boolean)
}