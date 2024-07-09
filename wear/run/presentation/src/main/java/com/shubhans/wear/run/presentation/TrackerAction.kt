package com.shubhans.wear.run.presentation

sealed interface TrackerAction {
    data object onToggledClick : TrackerAction
    data object onFinishedClick : TrackerAction
    data class OnBodySensorPermissionResult(val isGranted: Boolean) : TrackerAction

}