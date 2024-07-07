package com.shubhans.connectivity.domain.messaging

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.shubhans.connectivity.domain.messageing.MessageClient
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.connectivity.domain.messageing.MessagingError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WearMessageClient(
    context: Context,
) : MessageClient {

    private val client = Wearable.getMessageClient(context)
    private var connectedNodeId: String? = null
    private val messageQueue = mutableListOf<MessagingAction>()

    override fun connectToNode(nodeId: String): Flow<MessagingAction> {
        connectedNodeId = nodeId
        return callbackFlow {
            val listener: (MessageEvent) -> Unit = { event ->
                if (event.path.startsWith(BASE_PATH_MESSAGING_ACTION)) {
                    val json = event.data.decodeToString()
                    val action = Json.decodeFromString<MessagingActionDto>(json)
                    trySend(action.toMessagingAction())
                }
            }
            client.addListener(listener)

            messageQueue.forEach {
                sendOrQueryAction(it)
            }
            messageQueue.clear()

            awaitClose {
                client.removeListener(listener)
            }
        }
    }

    override suspend fun sendOrQueryAction(action: MessagingAction): EmptyResult<MessagingError> {
        return connectedNodeId?.let { id ->
            try {
                val json = Json.encodeToString(action.toMessageActionDto())
                client.sendMessage(id, BASE_PATH_MESSAGING_ACTION, json.encodeToByteArray()).await()
                Result.Success(Unit)
            } catch (e: ApiException) {
                Result.Error(
                    if (e.status.isInterrupted) {
                        MessagingError.CONNECTION_INTERRUPTED
                    } else MessagingError.UNKNOWN
                )
            }
        } ?: run {
            messageQueue.add(action)
            Result.Error(MessagingError.DISCONNECTED)
        }
    }

    companion object {
        private const val BASE_PATH_MESSAGING_ACTION = "runique/messaging_action"
    }

}