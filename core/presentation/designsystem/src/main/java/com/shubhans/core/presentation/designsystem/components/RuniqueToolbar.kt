@file:OptIn(ExperimentalMaterial3Api::class)

package com.shubhans.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shubhans.core.presentation.designsystem.AnalyticsIcon
import com.shubhans.core.presentation.designsystem.ArrowLeftIcon
import com.shubhans.core.presentation.designsystem.LogoutIcon
import com.shubhans.core.presentation.designsystem.Poppins
import com.shubhans.core.presentation.designsystem.R
import com.shubhans.core.presentation.designsystem.RuniqueTheme

@Composable
fun ReuniqueToolbar(
    showBackButton: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    menuItems: List<DropDownMenuItem> = emptyList(),
    onMenuItemClicked: (Int) -> Unit = {},
    onBackClicked: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null,
) {
    var isDropDownMenuOpen by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(id = R.string.arrow_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                Box {
                    DropdownMenu(
                        expanded = isDropDownMenuOpen,
                        onDismissRequest = { isDropDownMenuOpen = false },
                    ) {
                        menuItems.forEachIndexed { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        onMenuItemClicked(index)
                                    }) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.title
                                )
                            }
                        }
                    }
                    IconButton(onClick = { isDropDownMenuOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewReuniqueTopTool() {
    RuniqueTheme {
        ReuniqueToolbar(
            showBackButton = true,
            title = "Runique",
            menuItems = listOf(
                DropDownMenuItem(
                    title = "Analitics",
                    icon = AnalyticsIcon
                ),
                DropDownMenuItem(
                    title = "Logout",
                    icon = LogoutIcon
                ),
            ),
            onMenuItemClicked = { /* Handle menu item click */ },
            onBackClicked = { /* Handle back click */ }
        )
    }
}