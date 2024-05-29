package com.shubhans.run.presentation.run_overView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.run.presentation.run_overView.mapper.toRunUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverViewViewModel(
    val repository: RunRepository
) : ViewModel() {
    var state by mutableStateOf(RunOverViewState())
        private set

    init {
        repository.getRuns().onEach { runs ->
            val runUi = runs.map { it.toRunUi() }
            state = state.copy(runs = runUi)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            repository.syncPendingRuns()
            repository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when (action) {
            RunOverviewAction.onRunClicked -> Unit
            RunOverviewAction.onAnalyticsClicked -> Unit
            RunOverviewAction.onLogOutClicked -> Unit
            is RunOverviewAction.onDeleteRun -> {
                viewModelScope.launch {
                    repository.deleteRun(action.runUi.id)
                }
            }
        }
    }
}