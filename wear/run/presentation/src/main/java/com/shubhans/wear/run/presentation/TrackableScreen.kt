package com.shubhans.wear.run.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.shubhans.core.presentation.designsystem.ExclamationMarkIcon
import com.shubhans.core.presentation.designsystem.FinishIcon
import com.shubhans.core.presentation.designsystem.PauseIcon
import com.shubhans.core.presentation.designsystem.StartIcon
import com.shubhans.core.presentation.designsystem_wear.RuniqueTheme
import com.shubhans.core.presentation.ui.formatted
import com.shubhans.core.presentation.ui.formattedHeartRate
import com.shubhans.core.presentation.ui.toFormattedKm
import com.shubhans.wear.run.presentation.components.TrackerRunCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackableScreenRoot(
    viewModel: TrackableViewModel = koinViewModel(),
) {
    TrackableScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun TrackableScreen(
    state: TrackerState,
    onAction: (TrackerAction) -> Unit,
) {
    if (state.hasConnectedPhoneNearby) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                TrackerRunCard(
                    title = stringResource(id = R.string.Heart_rate),
                    value = if (state.conTrackableHeartRate) {
                        state.heartRate.formattedHeartRate()
                    } else {
                        stringResource(id = R.string.unsupported)
                    },
                    onValueTextColor = if (state.conTrackableHeartRate) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                TrackerRunCard(
                    title = stringResource(id = R.string.distance),
                    value = (state.distanceMeters / 1000.0).toFormattedKm(),
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.elatedTime.formatted(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.trackable) {
                    ToggleRunButton(
                        isRunActive = state.isRunActive,
                        onClick = {
                            onAction(TrackerAction.onToggledClick)
                        },
                    )
                    if (!state.isRunActive && state.isRunningStarted) {
                        FilledTonalIconButton(
                            onClick = {
                                onAction(TrackerAction.onFinished)
                            }, colors = IconButtonDefaults.filledTonalIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                            )
                        ) {
                            Icon(
                                imageVector = FinishIcon,
                                contentDescription = stringResource(id = R.string.start)
                            )
                        }
                    }
                }else {
                    Text(
                        text = stringResource(id = R.string.onen_runique_screen),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = ExclamationMarkIcon,
                contentDescription = stringResource(id = R.string.ExclamationMarkIcon),
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.no_phone_nearby), textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ToggleRunButton(
    isRunActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        onClick = onClick, modifier = modifier
    ) {
        if (isRunActive) {
            Icon(
                imageVector = PauseIcon,
                contentDescription = stringResource(id = R.string.pause),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        } else {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(id = R.string.start),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@WearPreviewDevices
@Preview
@Composable
fun TrackableScreenPreview() {
    RuniqueTheme {
        TrackableScreen(
            state = TrackerState(
                conTrackableHeartRate = true,
                hasConnectedPhoneNearby = true,
                trackable = true,
                isRunActive = false,
                isRunningStarted = true,
            ),
            onAction = {},
        )
    }
}