package com.shubhans.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shubhans.core.presentation.designsystem.RunIcon
import com.shubhans.core.presentation.designsystem.RuniqueGreen
import com.shubhans.core.presentation.designsystem.RuniqueGreen30

@Composable
fun RuniqueFloatingActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(CircleShape)
            .background(RuniqueGreen30),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .padding(12.dp)
                .background(RuniqueGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(iconSize)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRuniqueFloatingActionButton() {
    RuniqueFloatingActionButton(
        icon = RunIcon,
        onClick = {},
        contentDescription = "Add",
        modifier = Modifier.padding(16.dp)
    )
}