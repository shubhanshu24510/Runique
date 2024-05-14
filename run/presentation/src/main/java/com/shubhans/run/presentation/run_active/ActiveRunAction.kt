package com.shubhans.run.presentation.run_active

sealed interface ActiveRunAction {
    data object onToggleRunClicked : ActiveRunAction
    data object onRusumeRunClicked : ActiveRunAction
    data object onFinishRunClicked : ActiveRunAction
    data object onBackClicked : ActiveRunAction
    data class submitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationPermissionRationale: Boolean
    ) : ActiveRunAction

    data class submitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ) : ActiveRunAction
    data object dismissRationalDialog : ActiveRunAction
}