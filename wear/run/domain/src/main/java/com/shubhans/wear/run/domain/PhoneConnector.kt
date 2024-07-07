package com.shubhans.wear.run.domain

import com.shubhans.connectivity.domain.DeviceNode
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.connectivity.domain.messageing.MessagingError
import com.shubhans.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PhoneConnector {
    val connectedNode: StateFlow<DeviceNode?>
    val messingAction: Flow<MessagingAction>
    suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError>
}