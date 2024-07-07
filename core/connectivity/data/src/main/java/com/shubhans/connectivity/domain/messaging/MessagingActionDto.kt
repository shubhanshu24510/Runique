package com.shubhans.connectivity.domain.messaging

import kotlinx.serialization.Serializable
@Serializable
sealed interface MessagingActionDto {
    @Serializable
    data object StartOrResume : MessagingActionDto
    @Serializable
    data object Pause : MessagingActionDto
    @Serializable
    data object Finish : MessagingActionDto
    @Serializable
    data object Trackable : MessagingActionDto
    @Serializable
    data object Untraceable : MessagingActionDto
    @Serializable
    data object connectionRequest : MessagingActionDto
    @Serializable
    data class HeartRateUpdate(val heartRate: Int) : MessagingActionDto
    @Serializable
    data class DistanceUpdate(val distanceMeters: Double) : MessagingActionDto
    @Serializable
    data class ElapsedTimeUpdate(val elapsedTime: Int) : MessagingActionDto

}