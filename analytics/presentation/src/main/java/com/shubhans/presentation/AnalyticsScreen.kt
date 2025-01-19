@file:OptIn(ExperimentalMaterial3Api::class)

package com.shubhans.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhans.analytics.presentation.R
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.core.presentation.designsystem.components.GradientBackground
import com.shubhans.core.presentation.designsystem.components.RuniqueScafflod
import com.shubhans.core.presentation.designsystem.components.RuniqueToolbar
import com.shubhans.presentation.components.AnalyticsCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = koinViewModel(),
) {
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                AnalyticsAction.onBackClicked -> onBackClick()
            }
        }
    )
}

@Composable
private fun AnalyticsDashboardScreen(
    state: AnalyticsState?,
    onAction: (AnalyticsAction) -> Unit
) {
    RuniqueScafflod(
        topAppBarToolbar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.analytics_title),
                onBackClicked = {
                    onAction(AnalyticsAction.onBackClicked)
                }
            )
        }, floatingActionButton = {}
    ) { padding ->
        if(state == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_distance_run),
                        value = state.totalDistanceRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_time_run),
                        value = state.totalTimeRun,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.fastest_ever_run),
                        value = state.fastestEverRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.avg_distance),
                        value = state.avgDistance,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.avg_pace),
                        value = state.avgPace,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnalyticsDashboardScreenPreview() {
    RuniqueTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsState(
                totalDistanceRun = "0.2 km",
                totalTimeRun = "0d 0h 0m",
                fastestEverRun = "143.9 km/h",
                avgDistance = "0.1 km",
                avgPace = "07:10"
            ),
            onAction = {}
        )
    }
}