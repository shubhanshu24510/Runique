package com.shubhans.connectivity.domain.messageing

sealed interface MessagingAction {
    data object StartOrResume : MessagingAction
    data object Pause : MessagingAction
    data object Finish : MessagingAction
    data object Trackable : MessagingAction
    data object Untraceable : MessagingAction
    data object connectionRequest : MessagingAction
    data class HeartRateUpdate(val heartRate: Int) : MessagingAction
    data class DistanceUpdate(val distanceMeters: Double) : MessagingAction
    data class ElapsedTimeUpdate(val elapsedTime: Int) : MessagingAction

}