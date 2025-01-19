package com.shubhans.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shubhans.domain.AnalyticsRepository
import com.shubhans.presentation.mappers.toAnalyticsState
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    var state by mutableStateOf<AnalyticsState?>(null)
        private set

    init {
        viewModelScope.launch {
            state = analyticsRepository.getAnalyticsValues().toAnalyticsState()
        }
    }
}