@file:OptIn(ExperimentalMaterial3Api::class)

package com.shubhans.run.presentation.run_active

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.core.presentation.designsystem.StartIcon
import com.shubhans.core.presentation.designsystem.StopIcon
import com.shubhans.core.presentation.designsystem.components.ReuniqueToolbar
import com.shubhans.core.presentation.designsystem.components.RuniqueDialog
import com.shubhans.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.shubhans.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.shubhans.core.presentation.designsystem.components.RuniqueScafflod
import com.shubhans.run.domain.RunData
import com.shubhans.run.presentation.R
import com.shubhans.run.presentation.run_active.components.ActiveRunDataCard
import com.shubhans.run.presentation.utils.hasLocationPermission
import com.shubhans.run.presentation.utils.hasNotificationPermission
import com.shubhans.run.presentation.utils.shouldShowLocationPermissionRationale
import com.shubhans.run.presentation.utils.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.minutes

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state, onAction = viewModel::onAction
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState, onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCourseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.submitLocationPermissionInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.submitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationPermissionRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationPermissionRationale =
            activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.submitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = showLocationPermissionRationale
            )
        )
        onAction(
            ActiveRunAction.submitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationPermissionRationale
            )
        )
        if (!showLocationPermissionRationale && !showNotificationPermissionRationale) {
            permissionLauncher.requestRuniquePermission(context)
        }
    }
    RuniqueScafflod(withGradient = false, topAppBarToolbar = {
        ReuniqueToolbar(showBackButton = true,
            title = stringResource(id = R.string.active_run),
            onBackClicked = { onAction(ActiveRunAction.onBackClicked) })
    }, floatingActionButton = {
        RuniqueFloatingActionButton(
            icon = if (state.shouldTrack) {
                StopIcon
            } else {
                StartIcon
            }, onClick = {
                onAction(ActiveRunAction.onToggleRunClicked)
            }, iconSize = 20.dp, contentDescription = if (state.shouldTrack) {
                stringResource(id = R.string.stop_run)
            } else {
                stringResource(id = R.string.start_run)
            }
        )
    }) { paddingValues ->
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
    if (state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            onDismiss = { /*TODO*/ },
            primaryButtonAction = {
                RuniqueOutlinedActionButton(text = stringResource(id =R.string.okay),
                    isLoading =false,
                    onClick = {
                        onAction(ActiveRunAction.dismissRationalDialog)
                        permissionLauncher.requestRuniquePermission(context)
                    }
                )
            },
            secondaryButtonAction = { /*TODO*/ })
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermission(context: Context){
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()
    when {
        (!hasLocationPermission && !hasNotificationPermission) -> {
            launch(locationPermission + notificationPermission)
        }

        !hasLocationPermission -> {
            launch(locationPermission)
        }

        !hasNotificationPermission -> {
            launch(notificationPermission)
        }
    }
}

@Preview
@Composable
private fun PreviewActiveRunScreen() {
    RuniqueTheme {
        ActiveRunScreen(state = ActiveRunState(
            elapsedTime = 10.minutes,
            runData = RunData(
                distanceMeters = 1000, pace = 3.minutes
            ),
            hasStartedRunning = false,
            shouldTrack = false,
            currentLocation = null,
            isRunFinished = false,
            isSavingRun = false
        ), onAction = {})
    }
}