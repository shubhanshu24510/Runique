@file:OptIn(ExperimentalMaterial3Api::class)

package com.shubhans.run.presentation.run_active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.core.presentation.designsystem.StartIcon
import com.shubhans.core.presentation.designsystem.StopIcon
import com.shubhans.core.presentation.designsystem.components.ReuniqueToolbar
import com.shubhans.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.shubhans.core.presentation.designsystem.components.RuniqueScafflod
import com.shubhans.run.domain.RunData
import com.shubhans.run.presentation.R
import com.shubhans.run.presentation.run_active.components.ActiveRunDataCard
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.minutes

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    RuniqueScafflod(
        withGradient = false,
        topAppBarToolbar = {
            ReuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClicked = { onAction(ActiveRunAction.onBackClicked) }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                onClick = {
                    onAction(ActiveRunAction.onToggleRunClicked)
                },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.stop_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            ActiveRunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewActiveRunScreen() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(
                elapsedTime = 10.minutes,
                runData = RunData(
                    distanceMeters = 1000,
                    pace = 3.minutes
                ),
                hasStartedRunning = false,
                shouldTrack = false,
                currentLocation = null,
                isRunFinished = false,
                isSavingRun = false
            ),
            onAction = {}
        )
    }
}