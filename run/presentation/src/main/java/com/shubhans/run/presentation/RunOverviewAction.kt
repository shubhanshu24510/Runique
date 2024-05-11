package com.shubhans.run.presentation

sealed interface RunOverviewAction {
    object onRunClicked : RunOverviewAction
    object onLogOutClicked : RunOverviewAction
    object onAnalyticsClicked : RunOverviewAction
}