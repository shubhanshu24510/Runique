package com.shubhans.run.presentation.run_overView

import com.shubhans.run.presentation.run_overView.model.RunUi

sealed interface RunOverviewAction {
    data object onRunClicked : RunOverviewAction
    data object onLogOutClicked : RunOverviewAction
    data object onAnalyticsClicked : RunOverviewAction
    data class onDeleteRun(val runUi: RunUi) : RunOverviewAction
}