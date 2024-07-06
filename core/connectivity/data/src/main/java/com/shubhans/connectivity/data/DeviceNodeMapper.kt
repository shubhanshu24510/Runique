package com.shubhans.connectivity.data

import com.google.android.gms.wearable.Node

fun Node.toDevicesNode(): DeviceNode {
    return DeviceNode(
        id = id,
        displayName = displayName,
        isNearBy = isNearby
    )
}