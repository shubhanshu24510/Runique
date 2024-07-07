package com.shubhans.connectivity.domain.messageing

import com.shubhans.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface MessageClient {
    fun connectToNode(nodeId: String):Flow<MessagingAction>
    suspend fun sendOrQueryAction(action: MessagingAction):EmptyResult<MessagingError>
}