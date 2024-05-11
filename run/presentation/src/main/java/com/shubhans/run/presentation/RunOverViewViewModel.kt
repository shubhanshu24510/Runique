package com.shubhans.run.presentation

import androidx.lifecycle.ViewModel

class RunOverViewViewModel : ViewModel() {
    fun onAction(action: RunOverviewAction) {
        when (action) {
            is RunOverviewAction.onRunClicked -> {
                // Handle run click
            }

            is RunOverviewAction.onLogOutClicked -> {
                // Handle logout click
            }

            is RunOverviewAction.onAnalyticsClicked -> {
                // Handle analytics click
            }
        }
    }


}