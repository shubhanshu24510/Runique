package com.shubhans.wear.run.presentation

sealed interface TrackerAction {
    data object onToggledClick : TrackerAction
    data object onFinished : TrackerAction
}