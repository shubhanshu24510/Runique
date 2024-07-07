package com.shubhans.connectivity.domain.messaging

import com.shubhans.connectivity.domain.messageing.MessagingAction

fun MessagingAction.toMessageActionDto(): MessagingActionDto {
    return when (this) {
        MessagingAction.StartOrResume -> MessagingActionDto.StartOrResume
        MessagingAction.Pause -> MessagingActionDto.Pause
        MessagingAction.Finish -> MessagingActionDto.Finish
        MessagingAction.Trackable -> MessagingActionDto.Trackable
        MessagingAction.Untraceable -> MessagingActionDto.Untraceable
        MessagingAction.connectionRequest -> MessagingActionDto.connectionRequest
        is MessagingAction.HeartRateUpdate -> MessagingActionDto.HeartRateUpdate(heartRate)
        is MessagingAction.DistanceUpdate -> MessagingActionDto.DistanceUpdate(distanceMeters)
        is MessagingAction.ElapsedTimeUpdate -> MessagingActionDto.ElapsedTimeUpdate(elapsedTime)
    }
}

fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when (this) {
        MessagingActionDto.StartOrResume -> MessagingAction.StartOrResume
        MessagingActionDto.Pause -> MessagingAction.Pause
        MessagingActionDto.Finish -> MessagingAction.Finish
        MessagingActionDto.Trackable -> MessagingAction.Trackable
        MessagingActionDto.Untraceable -> MessagingAction.Untraceable
        MessagingActionDto.connectionRequest -> MessagingAction.connectionRequest
        is MessagingActionDto.HeartRateUpdate -> MessagingAction.HeartRateUpdate(heartRate)
        is MessagingActionDto.DistanceUpdate -> MessagingAction.DistanceUpdate(distanceMeters)
        is MessagingActionDto.ElapsedTimeUpdate -> MessagingAction.ElapsedTimeUpdate(elapsedTime)
    }
}