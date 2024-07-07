package com.shubhans.connectivity.domain

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WearNodeDiscovery(
    context: Context
) : NodeDiscovery {

    private val capabilityClient = Wearable.getCapabilityClient(context)
    override fun observeConnectedDevices(localDevicesType: DeviceType): Flow<Set<DeviceNode>> {
        return callbackFlow {
            val remoteCapability = when (localDevicesType) {
                DeviceType.PHONE -> "runique_wear_app"
                DeviceType.WATCH -> "runique_phone_app"
            }
            try {
                val capability = capabilityClient.getCapability(
                    remoteCapability, CapabilityClient.FILTER_REACHABLE
                ).await()
                val connectedDevices = capability.nodes.map {
                    it.toDevicesNode()
                }.toSet()
                send(connectedDevices)

            } catch (e: ApiException) {
                awaitClose()
                return@callbackFlow
            }

            val listener: (CapabilityInfo) -> Unit = {
                trySend(it.nodes.map { it.toDevicesNode() }.toSet())
            }
            capabilityClient.addListener(listener, remoteCapability)

            awaitClose {
                capabilityClient.removeListener(listener)
            }
        }
    }
}