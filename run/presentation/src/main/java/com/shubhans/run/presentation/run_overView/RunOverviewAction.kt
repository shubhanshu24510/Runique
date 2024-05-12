package com.shubhans.run.presentation.run_overView

sealed interface RunOverviewAction {
    object onRunClicked : RunOverviewAction
    object onLogOutClicked : RunOverviewAction
    object onAnalyticsClicked : RunOverviewAction
}