@file:OptIn(ExperimentalMaterial3Api::class)

package com.shubhans.run.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhans.core.presentation.designsystem.AnalyticsIcon
import com.shubhans.core.presentation.designsystem.LogoIcon
import com.shubhans.core.presentation.designsystem.RunIcon
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.core.presentation.designsystem.components.DropDownMenuItem
import com.shubhans.core.presentation.designsystem.components.ReuniqueToolbar
import com.shubhans.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.shubhans.core.presentation.designsystem.components.RuniqueScafflod
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverViewScreenRoot(
    viewViewModel: RunOverViewViewModel = koinViewModel()
) {
    RunOverViewScreen(onAction = viewViewModel::onAction)
}

@Composable
fun RunOverViewScreen(
    onAction: (RunOverviewAction) -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
    )
    RuniqueScafflod(topAppBarToolbar = {
        ReuniqueToolbar(showBackButton = false,
            title = stringResource(R.string.runique),
            scrollBehavior = scrollBehavior,
            menuItems = listOf(
                DropDownMenuItem(
                    title = stringResource(R.string.analytics), icon = AnalyticsIcon
                ), DropDownMenuItem(
                    title = stringResource(R.string.logout), icon = AnalyticsIcon
                )
            ),
            onMenuItemClicked = { index ->
                when (index) {
                    0 -> onAction(RunOverviewAction.onAnalyticsClicked)
                    1 -> onAction(RunOverviewAction.onLogOutClicked)
                }
            },
            startContent = {
                Icon(
                    imageVector = LogoIcon,
                    contentDescription = stringResource(id = R.string.logo_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            })
    },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.onRunClicked) })
        }
    ) { paddingValues ->
    }
}

@Preview
@Composable
private fun PreviewRunOverViewScreenRoot() {
    RuniqueTheme {
        RunOverViewScreen()
    }

}