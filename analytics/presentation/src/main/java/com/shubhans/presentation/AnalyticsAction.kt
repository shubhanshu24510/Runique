package com.shubhans.presentation

sealed interface AnalyticsAction {
   data object onBackClicked: AnalyticsAction
}