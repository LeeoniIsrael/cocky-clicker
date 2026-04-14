package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.util.NumberFormatter

@Composable
fun HypeCounter(totalHype: Long, hypePerTap: Long,hypePerSecond: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = NumberFormatter.format(totalHype),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        if (hypePerTap > 0) {
            Text(
                text = "${NumberFormatter.format(hypePerTap)} hype / tap",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (hypePerSecond > 0) {
            Text(
                text = "${NumberFormatter.format(hypePerSecond)} hype / sec",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
