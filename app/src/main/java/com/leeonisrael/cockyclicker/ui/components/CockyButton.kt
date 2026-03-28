package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CockyButton(onTap: () -> Unit) {
    // TODO: Replace with Cocky mascot image and animation
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onTap() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "COCKY",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
