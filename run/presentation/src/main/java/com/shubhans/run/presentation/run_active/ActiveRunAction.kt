package com.shubhans.run.presentation.run_active

sealed interface ActiveRunAction {
    object onToggleRunClicked : ActiveRunAction
    object onRusumeRunClicked : ActiveRunAction
    object onFinishRunClicked : ActiveRunAction
    object onBackClicked : ActiveRunAction
}