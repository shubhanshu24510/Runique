package com.shubhans.run.presentation.run_overView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.core.domain.run.SyncRunScheduler
import com.shubhans.run.presentation.run_overView.mapper.toRunUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class RunOverViewViewModel(
    private val repository: RunRepository,
    private val syncRunScheduler: SyncRunScheduler,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage
) : ViewModel() {
    var state by mutableStateOf(RunOverViewState())
        private set

    init {
        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                SyncRunScheduler.SyncType.FetchRuns(
                    30.minutes
                )
            )
        }
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
            RunOverviewAction.onLogOutClicked -> logout()
            is RunOverviewAction.onDeleteRun -> {
                viewModelScope.launch {
                    repository.deleteRun(action.runUi.id)
                }
            }

            RunOverviewAction.onAnalyticsClicked -> Unit
        }
    }

    private fun logout() {
        applicationScope.launch {
            syncRunScheduler.cancelAllScheduledSync()
            repository.deleteAllRuns()
            repository.logout()
            sessionStorage.set(null)
        }
    }
}