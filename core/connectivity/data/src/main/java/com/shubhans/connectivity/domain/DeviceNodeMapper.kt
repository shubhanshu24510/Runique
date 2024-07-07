package com.shubhans.connectivity.domain

import com.google.android.gms.wearable.Node

fun Node.toDevicesNode(): DeviceNode {
    return DeviceNode(
        id = id,
        displayName = displayName,
        isNearBy = isNearby
    )
}